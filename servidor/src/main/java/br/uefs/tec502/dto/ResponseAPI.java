package br.uefs.tec502.dto;

import br.uefs.tec502.enums.HttpStatus;

public class ResponseAPI {
    private String payload;
    private HttpStatus status;

    public ResponseAPI() {
        this.payload = "";
        this.status = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    public ResponseAPI(String payload, HttpStatus status) {
        this.payload = payload;
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getPayload() {
        return payload;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }
}
