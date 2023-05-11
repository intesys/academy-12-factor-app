package it.intesys.academy.twelvefactordemo;

public class Configs {

    /**
     * Test environment
     */
    public static final String CUSTOM_MESSAGE_TEST = "Hello World Test!";
    public static final int SERVER_PORT_TEST = 8080;
    public static final String DATABASE_PASSWORD_TEST = "abc1234";

    /**
     * Prod environment
     */
    public static final String CUSTOM_MESSAGE_PROD = "Hello World Prod!";
    public static final int SERVER_PORT_PROD = 9090;
    public static final String DATABASE_PASSWORD_PROD = "abc1234";




    public static String getCustomMessage() {
        String env = System.getenv("environment");
        if (env != null && env.equals("prod")) {
            return CUSTOM_MESSAGE_PROD;
        } else {
            return CUSTOM_MESSAGE_TEST;
        }
    }

    public static int getServerPort() {
        String env = System.getenv("environment");
        if (env != null && env.equals("prod")) {
            return SERVER_PORT_PROD;
        } else {
            return SERVER_PORT_TEST;
        }
    }
}
