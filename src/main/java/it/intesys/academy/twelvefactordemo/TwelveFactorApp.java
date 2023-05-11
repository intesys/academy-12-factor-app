package it.intesys.academy.twelvefactordemo;


import io.javalin.Javalin;

public class TwelveFactorApp {

	public static void main(String[] args) {

		var customMessage = Configs.getStringProperty("CUSTOM_MESSAGE");
		var serverPort = Configs.getIntegerProperty("SERVER_PORT");

		Javalin app = Javalin.create()
				.get("/", ctx -> ctx.result(customMessage))
				.start(serverPort);
	}

}
