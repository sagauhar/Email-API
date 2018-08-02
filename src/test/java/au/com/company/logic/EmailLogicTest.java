package au.com.company.logic;

import au.com.company.data.Email;
import au.com.company.data.HttpResponse;
import com.netflix.config.DynamicPropertyFactory;
import io.netty.handler.codec.http.HttpResponseStatus;
import org.apache.commons.configuration.Configuration;
import org.junit.Test;

import static au.com.company.Constants.*;
import static org.junit.Assert.*;

public class EmailLogicTest {
    @Test
    public void testNullEmail() {
        EmailLogic emailLogic = new EmailLogic();
        HttpResponse response = emailLogic.sendEmail(null);
        assertNotNull("Response should not be null", response);
        assertEquals("Status code should be 400", HttpResponseStatus.PRECONDITION_FAILED, response.getStatusCode());
        assertEquals("Message should be Failure", VALIDATION_MESSAGE, response.getMessage());
    }

    @Test
    public void testInvalidEmail() {
        EmailLogic emailLogic = new EmailLogic();
        Email email = new Email();
        HttpResponse response = emailLogic.sendEmail(null);
        assertNotNull("Response should not be null", response);
        assertEquals("Status code should be 400", HttpResponseStatus.PRECONDITION_FAILED, response.getStatusCode());
        assertEquals("Message should be Failure", VALIDATION_MESSAGE, response.getMessage());

        email.setTo("test@email.com");
        response = emailLogic.sendEmail(email);
        assertNotNull("Response should not be null", response);
        assertEquals("Status code should be 400", HttpResponseStatus.PRECONDITION_FAILED, response.getStatusCode());
        assertEquals("Message should be Failure", VALIDATION_MESSAGE, response.getMessage());

        email.setSubject("Subject");
        response = emailLogic.sendEmail(email);
        assertNotNull("Response should not be null", response);
        assertEquals("Status code should be 400", HttpResponseStatus.PRECONDITION_FAILED, response.getStatusCode());
        assertEquals("Message should be Failure", VALIDATION_MESSAGE, response.getMessage());

        email.setTo(null);
        email.setCc("test@email.com");
        response = emailLogic.sendEmail(email);
        assertNotNull("Response should not be null", response);
        assertEquals("Status code should be 400", HttpResponseStatus.PRECONDITION_FAILED, response.getStatusCode());
        assertEquals("Message should be Failure", VALIDATION_MESSAGE, response.getMessage());

        email.setCc(null);
        email.setBcc("test@email.com");
        response = emailLogic.sendEmail(email);
        assertNotNull("Response should not be null", response);
        assertEquals("Status code should be 400", HttpResponseStatus.PRECONDITION_FAILED, response.getStatusCode());
        assertEquals("Message should be Failure", VALIDATION_MESSAGE, response.getMessage());
    }

    @Test
    public void testValidEmail() {
        EmailLogic emailLogic = new EmailLogic();
        Email email = new Email();
        email.setTo("sagauhar@yahoo.com");
        email.setSubject("testValidEmail with To");
        email.setBody("Unit Test Body");
        HttpResponse response = emailLogic.sendEmail(email);
        assertNotNull("Response should not be null", response);
        assertTrue("Status code should be successful", response.isSuccessful());
        assertFalse("Status code should not be a failure", response.isError());
        assertEquals("Message should be Success", SUCCESSFUL_MESSAGE, response.getMessage());

        email.setCc("shoaib.gauhar@gmail.com");
        email.setSubject("testValidEmail with CC");
        email.setBody("Unit Test Body");
        response = emailLogic.sendEmail(email);
        assertNotNull("Response should not be null", response);
        assertTrue("Status code should be successful", response.isSuccessful());
        assertFalse("Status code should not be a failure", response.isError());
        assertEquals("Message should be Success", SUCCESSFUL_MESSAGE, response.getMessage());

        email.setCc(null);
        email.setBcc("shoaib.gauhar@gmail.com");
        email.setSubject("testValidEmail with BCC");
        email.setBody("Unit Test Body");
        response = emailLogic.sendEmail(email);
        assertNotNull("Response should not be null", response);
        assertTrue("Status code should be successful", response.isSuccessful());
        assertFalse("Status code should not be a failure", response.isError());
        assertEquals("Message should be Success", SUCCESSFUL_MESSAGE, response.getMessage());
    }

    @Test
    public void testFailover() {
        DynamicPropertyFactory.getInstance();
        Configuration config = (Configuration) DynamicPropertyFactory.getBackingConfigurationSource();
        config.clearProperty(SENDGRID_API_URL);

        EmailLogic emailLogic = new EmailLogic();
        Email email = new Email();
        email.setTo("sagauhar@yahoo.com");
        email.setSubject("testFailover");
        email.setBody("Unit Test Body");
        HttpResponse response = emailLogic.sendEmail(email);
        assertNotNull("Response should not be null", response);
        assertTrue("Status code should be successful", response.isSuccessful());
        assertFalse("Status code should not be a failure", response.isError());
        assertEquals("Message should be Success", SUCCESSFUL_MESSAGE, response.getMessage());
    }
}
