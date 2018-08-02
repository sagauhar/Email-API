package au.com.company.mappers;

import au.com.company.data.Email;
import au.com.company.data.MailGunRequest;
import com.netflix.config.DynamicPropertyFactory;
import org.apache.commons.lang.StringUtils;

import static au.com.company.Constants.FROM_EMAIL_ADDRESS;

public class MailGunMapper {
    /**
     * It maps the Email object to MailGun specific object
     * @param email Email object which needs to be mapped
     * @return MailGun specific object
     */
    public static MailGunRequest map(Email email) {
        MailGunRequest request = new MailGunRequest();
        request.setFrom(DynamicPropertyFactory.getInstance().getStringProperty(FROM_EMAIL_ADDRESS, StringUtils.EMPTY).getValue());
        request.setTo(email.getTo());
        request.setCc(email.getCc());
        request.setBcc(email.getBcc());
        request.setSubject(email.getSubject());
        request.setText(email.getBody());

        return request;
    }
}
