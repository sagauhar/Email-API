package au.com.company.data;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MailGunRequest {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private String from;
    private String to;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String cc;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String bcc;
    private String subject;
    private String text;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getCc() {
        return cc;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }

    public String getBcc() {
        return bcc;
    }

    public void setBcc(String bcc) {
        this.bcc = bcc;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return MAPPER.convertValue(this, UriConverter.class).toString();
    }

    public static class UriConverter {
        private StringBuilder builder = new StringBuilder();

        @JsonAnySetter
        public void addToUri(String key, Object value) {
            builder.append(key).append("=").append(value).append("&");
        }

        @Override
        public String toString() {
            return builder.toString();
        }
    }
}
