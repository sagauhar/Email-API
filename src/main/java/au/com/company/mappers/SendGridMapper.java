package au.com.company.mappers;

import au.com.company.data.Email;
import au.com.company.data.SendGridRequest;
import com.netflix.config.DynamicPropertyFactory;
import org.apache.commons.lang.StringUtils;
import org.apache.http.entity.ContentType;

import java.util.Arrays;
import java.util.stream.Stream;

import static au.com.company.Constants.FROM_EMAIL_ADDRESS;

public class SendGridMapper {
    /**
     * It maps the Email object to SendGrid specific object
     * @param email Email object which needs to be mapped
     * @return SendGrid specific object
     */
    public static SendGridRequest map(Email email) {
        return Stream.of(new SendGridRequest())
                .map(SendGridMapper::fillFrom)
                .map(request -> fillSubject(email, request))
                .map(request -> fillContent(email, request))
                .map(request -> fillPersonalizations(email, request))
                .findAny()
                .orElse(null);
    }

    /**
     * It returns the object with configured From address
     * @param request The SendGridRequest that needs to be populated
     * @return The SendGridRequest which is populated
     */
    private static SendGridRequest fillFrom(SendGridRequest request) {
        SendGridRequest.EmailAddress emailAddress = new SendGridRequest.EmailAddress();
        emailAddress.setEmail(DynamicPropertyFactory.getInstance().getStringProperty(FROM_EMAIL_ADDRESS, StringUtils.EMPTY).getValue());
        request.setFrom(emailAddress);
        return request;
    }

    /**
     * It returns the object with subject
     * @param request The SendGridRequest that needs to be populated
     * @return The SendGridRequest which is populated
     */
    private static SendGridRequest fillSubject(Email email, SendGridRequest request) {
        request.setSubject(email.getSubject());
        return request;
    }

    /**
     * It returns the object with Email body
     * @param request The SendGridRequest that needs to be populated
     * @return The SendGridRequest which is populated
     */
    private static SendGridRequest fillContent(Email email, SendGridRequest request) {
        SendGridRequest.Content content = new SendGridRequest.Content();
        content.setType(ContentType.TEXT_PLAIN.getMimeType());
        content.setValue(email.getBody());
        request.setContent(new SendGridRequest.Content[] {content});
        return request;
    }

    /**
     * It returns the object with Email addresses
     * @param request The SendGridRequest that needs to be populated
     * @return The SendGridRequest which is populated
     */
    private static SendGridRequest fillPersonalizations(Email email, SendGridRequest request) {
        SendGridRequest.Personalization personalizations = new SendGridRequest.Personalization();
        personalizations.setTo(getPersonalizationAddresses(email.getTo()));
        personalizations.setCc(getPersonalizationAddresses(email.getCc()));
        personalizations.setBcc(getPersonalizationAddresses(email.getBcc()));
        request.setPersonalizations(new SendGridRequest.Personalization[] {personalizations});
        return request;
    }

    /**
     * A generic function to convert Email addresses to SendGrid specific object
     * @param addresses Semicolon separated Email addresses
     * @return The SendGridRequest.EmailAddress array which is populated
     */
    private static SendGridRequest.EmailAddress [] getPersonalizationAddresses(String addresses) {
        if (addresses != null) {
            return Arrays.stream(addresses.split(";"))
                    .map(String::trim)
                    .map(address -> {
                        SendGridRequest.EmailAddress emailAddress = new SendGridRequest.EmailAddress();
                        emailAddress.setEmail(address);
                        return emailAddress;
                    })
                    .toArray(SendGridRequest.EmailAddress []::new);
        } else {
            return null;
        }
    }
}
