package br.uefs.tec502;

public class Request {

    public static String makeRequest(String httpMethod, String endpoint, String payload) {
        String request = String.format("%s %s HTTP/1.1 \r\nHost: localhost:8081 \r\n%s", httpMethod, endpoint, payload);
        return request;
    }
}
