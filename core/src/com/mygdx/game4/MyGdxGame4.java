package com.mygdx.game4;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.net.HttpRequestBuilder;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Timer;

public class MyGdxGame4 extends ApplicationAdapter {
	Dialog endDialog;

	Skin skin;
	Stage stage;

	@Override
	public void create() {
		skin = new Skin(Gdx.files.internal("clean-crispy-ui.json"));

		stage = new Stage();

		Gdx.input.setInputProcessor(stage);

		endDialog = new Dialog("End Game", skin) {
			protected void result(Object object) {
				if (object.equals("solicitud_http")) {
					realizarSolicitudHTTP();
				}
			}
		};

		endDialog.button("Solicitud HTTP", "solicitud_http");
	}

	private void realizarSolicitudHTTP() {
		HttpRequestBuilder requestBuilder = new HttpRequestBuilder();
		Net.HttpRequest httpRequest = requestBuilder.newRequest().method(Net.HttpMethods.GET).url("https://www.iesesteveterradas.cat/").build();
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
				System.out.println("REBUT!");
			}
		});

		System.out.println("Solicitud HTTP enviada");
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0, 1, 0, 1); // Establece el color de fondo a verde
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // Borra el buffer de color con el color de fondo especificado

		stage.act(); // Actualiza todos los actores de la escena
		stage.draw(); // Dibuja la escena

		if (!endDialog.hasParent()) {
			endDialog.show(stage);
		}
	}

	@Override
	public void dispose() {
		stage.dispose();
	}
}
