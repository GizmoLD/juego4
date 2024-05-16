package com.mygdx.game4;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.net.HttpRequestBuilder;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class MyGdxGame4 extends ApplicationAdapter {
	private static final String SKIN_FILE_PATH = "clean-crispy-ui.json";
	private static final String BUTTON_LABEL = "Send HTTP Request";
	private static final String HTTP_REQUEST_URL = "https://www.iesesteveterradas.cat/";

	private Skin skin;
	private Stage stage;
	private Dialog endDialog;

	@Override
	public void create() {
		skin = new Skin(Gdx.files.internal(SKIN_FILE_PATH));
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);

		setupDialog();
	}

	private void setupDialog() {
		BitmapFont font = new BitmapFont();
		font.getData().setScale(2);

		// Crea una nueva etiqueta con la fuente aumentada
		Label.LabelStyle labelStyle = new Label.LabelStyle();
		labelStyle.font = font;

		Label label = new Label("HTTP Request Sent", labelStyle);

		endDialog = new Dialog("", skin) {
			protected void result(Object object) {
				if (object.equals("solicitud_http")) {
					sendHttpRequest();
				}
			}
		};

		endDialog.getContentTable().add(label);
		endDialog.button(BUTTON_LABEL, "solicitud_http");
		endDialog.show(stage);
	}
//
	private void sendHttpRequest() {
		HttpRequestBuilder requestBuilder = new HttpRequestBuilder();
		Net.HttpRequest httpRequest = requestBuilder.newRequest().method(Net.HttpMethods.GET).url(HTTP_REQUEST_URL).build();

		Gdx.net.sendHttpRequest(httpRequest, new Net.HttpResponseListener() {
			@Override
			public void cancelled() {
				System.out.println("Cancelled");
			}

			@Override
			public void failed(Throwable t) {
				System.out.println("FAILED: " + t.getMessage());
			}

			@Override
			public void handleHttpResponse(Net.HttpResponse httpResponse) {
				System.out.println("HTTP RESPONSE RECEIVED");
				Gdx.app.postRunnable(new Runnable() {
					@Override
					public void run() {
						showDialog("HTTP Request Sent");
					}
				});
			}
		});

		System.out.println("HTTP Request Sent");
	}

	private void showDialog(String message) {
		Label label = new Label(message, skin.get("default", Label.LabelStyle.class));
		label.setFontScale(2);

		endDialog.getContentTable().clear();
		endDialog.getContentTable().add(label);
		endDialog.show(stage);
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0, 1, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		stage.act();
		stage.draw();
	}

	@Override
	public void dispose() {
		stage.dispose();
		skin.dispose();
	}
}
