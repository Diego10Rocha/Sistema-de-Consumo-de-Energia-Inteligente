package br.uefs.tec502.http;

public class HttpResponse {

    public static String OK (String payload) {
        return String.format("HTTP/1.1 200 OK\r\n\r\n%s", payload);
    }

    public static String BadRequest (String payload) {
        return String.format("HTTP/1.1 400 Bad Request\r\n\r\n%s", payload);
    }

    public static String NotFound (String payload) {
        return String.format("HTTP/1.1 404 Not Found\r\n\r\n%s", payload);
    }

    public static String InternalServerError (String payload) {
        return String.format("HTTP/1.1 500 Internal Server Error\r\n\r\n%s", payload);
    }

    public static String Created () {
        return "HTTP/1.1 201 Created\r\n\r\n";
    }

    public static String OK () {
        return "HTTP/1.1 200 OK\r\n\r\n";
    }

    public static String BadRequest () {
        return "HTTP/1.1 400 Bad Request\r\n\r\n";
    }

    public static String NotFound () {
        return "HTTP/1.1 404 Not Found\r\n\r\n";
    }

    public static String InternalServerError () {
        return "HTTP/1.1 500 Internal Server Error\r\n\r\n";
    }

    public static String NotAllowed () {
        return "HTTP/1.1 405 Not Allowed\r\n\r\n";
    }
}
