package com.jenniferliang.bookcataloguer;

public class ServerException extends Exception {

    private int _httpCode;
    private String _httpMessage;

    public ServerException(int httpCode, String httpMessage) {
        super("HTTP status " + httpCode +
                ((httpMessage != null) ? (": " + httpMessage) : ""));
        _httpMessage = (httpMessage != null) ? httpMessage : "";
        _httpCode = httpCode;
    }

    public int getHttpCode() {
        return _httpCode;
    }

    public String getHttpMessage() {
        return _httpMessage;
    }
}
