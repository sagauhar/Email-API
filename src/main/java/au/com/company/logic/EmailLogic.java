package au.com.company.logic;

import au.com.company.commands.EmailCommand;
import au.com.company.data.Email;
import au.com.company.data.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;

import static au.com.company.Constants.FAILURE_MESSAGE;
import static au.com.company.Constants.SUCCESSFUL_MESSAGE;
import static au.com.company.Constants.VALIDATION_MESSAGE;

public class EmailLogic {
    /**
     * This function calls the Hystrix command when Email object is passed
     * @param email The Client Email object that needs to be used
     * @return Returns the au.com.company.data.HttpResponse object
     */
    public HttpResponse sendEmail(Email email) {
        HttpResponse httpResponse;
        if (email != null && !email.isInvalid()) {
            httpResponse = new EmailCommand(email).execute();
            httpResponse.setMessage(httpResponse.isSuccessful() ? SUCCESSFUL_MESSAGE : FAILURE_MESSAGE);
        } else {
            httpResponse = new HttpResponse(HttpResponseStatus.PRECONDITION_FAILED, VALIDATION_MESSAGE);
        }

        return httpResponse;
    }
}
