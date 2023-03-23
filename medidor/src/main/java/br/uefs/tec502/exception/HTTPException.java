package br.uefs.tec502.exception;

public class HTTPException extends Exception {
    public HTTPException() {
        super("Method Not Allowed");
    }
}
