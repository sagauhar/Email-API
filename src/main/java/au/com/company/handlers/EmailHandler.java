package au.com.company.handlers;

import au.com.company.data.Email;
import au.com.company.data.HttpResponse;
import au.com.company.logic.EmailLogic;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.reactivex.netty.protocol.http.server.HttpServerRequest;
import io.reactivex.netty.protocol.http.server.HttpServerResponse;
import rx.Observable;

import java.io.IOException;
import java.nio.charset.Charset;

public class EmailHandler implements HttpHandler {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private EmailLogic emailLogic = new EmailLogic();

    /**
     * This function is called when the request comes for sending email
     * @param request Request object which has the info about the client request
     * @param response Response object which is used to send the response back
     * @return Observable<Void> to make it compatible with RequestHandler class
     */
    @Override
    public Observable<Void> handle(HttpServerRequest<ByteBuf> request, HttpServerResponse<ByteBuf> response) {
        return request.getContent()
                .flatMap(byteBuf -> {
                    try {
                        Email email = MAPPER.readValue(byteBuf.toString(Charset.forName("UTF-8")), Email.class);
                        HttpResponse httpResponse = emailLogic.sendEmail(email);
                        response.writeString(httpResponse.getMessage());
                        response.setStatus(httpResponse.getStatusCode());
                    } catch (IOException e) {
                        response.writeString(e.getMessage());
                        response.setStatus(HttpResponseStatus.INTERNAL_SERVER_ERROR);
                    }

                    return response.close();
                });
    }
}
