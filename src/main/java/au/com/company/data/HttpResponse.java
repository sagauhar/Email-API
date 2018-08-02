package au.com.company.data;

import io.netty.handler.codec.http.HttpResponseStatus;

public class HttpResponse {
    private HttpResponseStatus statusCode;
    private String message;

    public HttpResponse(HttpResponseStatus statusCode) {
        this(statusCode, null);
    }

    public HttpResponse(HttpResponseStatus statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    public HttpResponseStatus getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(HttpResponseStatus statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccessful() {
        return getStatusCode() != null && getStatusCode().code() >= 200 && getStatusCode().code() < 300;
    }

    public boolean isError() {
        return getStatusCode() == null || (getStatusCode().code() >= 300 && getStatusCode().code() < 400) ||
                (getStatusCode().code() >= 500 && getStatusCode().code() < 600);
    }
}
