package br.uefs.tec502;

public class Request {

    /**
     * metodo que monta a requisição a ser enviada para o servidor
     * @param httpMethod
     * @param endpoint
     * @param payload
     * @return
     */
    public static String makeRequest(String httpMethod, String endpoint, String payload) {
        String request = String.format("%s %s HTTP/1.1 \r\nHost: localhost:1017 \r\n%s", httpMethod, endpoint, payload);
        return request;
    }
}
