package au.com.company.commands;

import au.com.company.data.Email;
import au.com.company.data.HttpResponse;
import au.com.company.mappers.MailGunMapper;
import au.com.company.mappers.SendGridMapper;
import com.netflix.config.DynamicPropertyFactory;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;
import io.netty.handler.codec.http.HttpResponseStatus;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.util.Base64;

import static au.com.company.Constants.*;

public class EmailCommand extends HystrixCommand<HttpResponse> {
    private static final String COMMAND_NAME = EmailCommand.class.getCanonicalName();

    private Email email;

    public EmailCommand(Email email) {
        super(HystrixCommandGroupKey.Factory.asKey(COMMAND_NAME));
        HystrixCommandProperties.Setter().withExecutionTimeoutInMilliseconds(2000);
        this.email = email;
    }

    /**
     * The main method of Hystrix command which calls SendGrid API
     */
    @Override
    protected HttpResponse run() throws IOException {
        String url = DynamicPropertyFactory.getInstance().getStringProperty(SENDGRID_API_URL, StringUtils.EMPTY).getValue();
        String key = DynamicPropertyFactory.getInstance().getStringProperty(SENDGRID_API_KEY, StringUtils.EMPTY).getValue();
        String authorization = String.format("Bearer %s", key);
        HttpEntity entity = new StringEntity(SendGridMapper.map(email).toString(), ContentType.APPLICATION_JSON);

        HttpResponse httpResponse = executeCommand(url, authorization, entity);

        if (httpResponse.isError()) {
            throw new HttpResponseException(httpResponse.getStatusCode().code(), StringUtils.EMPTY);
        }

        return httpResponse;
    }

    /**
     * In case of exception from the run method, fallback will be called and request will be sent to MailGun API
     */
    @Override
    protected HttpResponse getFallback() {
        HttpResponse httpResponse;
        String url = DynamicPropertyFactory.getInstance().getStringProperty(MAILGUN_API_URL, StringUtils.EMPTY).getValue();
        String key = DynamicPropertyFactory.getInstance().getStringProperty(MAILGUN_API_KEY, StringUtils.EMPTY).getValue();
        String authorization = String.format("Basic %s", Base64.getEncoder().encodeToString(key.getBytes()));
        HttpEntity entity = new StringEntity(MailGunMapper.map(email).toString(), ContentType.APPLICATION_FORM_URLENCODED);

        try {
            httpResponse = executeCommand(url, authorization, entity);
        } catch (IOException e) {
            httpResponse = new HttpResponse(HttpResponseStatus.INTERNAL_SERVER_ERROR);
        }

        return httpResponse;
    }

    /**
     * This function is used to send the request to external Email APIs
     * @param url URL to send the request to
     * @param authorization Any authorization header that needs to be sent
     * @param entity Payload for the POST request
     * @return Returns au.com.company.data.HttpResponse object
     * @throws IOException
     */
    private HttpResponse executeCommand(String url, String authorization, HttpEntity entity) throws IOException {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader(HttpHeaders.AUTHORIZATION, authorization);
        httpPost.setEntity(entity);

        try (CloseableHttpResponse httpResponse = HttpClients.createDefault().execute(httpPost)) {
            return new HttpResponse(HttpResponseStatus.valueOf(httpResponse.getStatusLine().getStatusCode()));
        }
    }
}
