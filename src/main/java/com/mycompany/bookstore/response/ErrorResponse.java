package com.mycompany.bookstore.response;

public class ErrorResponse {
    private String error;
    private int status;

    public ErrorResponse() {}

    public ErrorResponse( int status, String error) {
        this.status = status;
        this.error = error;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

}
