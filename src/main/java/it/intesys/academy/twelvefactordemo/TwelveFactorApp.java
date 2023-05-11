package it.intesys.academy.twelvefactordemo;


import io.javalin.Javalin;

public class TwelveFactorApp {

	public static void main(String[] args) {

		var customMessage = Configs.getCustomMessage();
		var serverPort = Configs.getServerPort();

		Javalin app = Javalin.create()
				.get("/", ctx -> ctx.result(customMessage))
				.start(serverPort);
	}

}
