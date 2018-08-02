package au.com.company.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SendGridRequest {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private String subject;
    private Content [] content;
    private EmailAddress from;
    private Personalization [] personalizations;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Content [] getContent() {
        return content;
    }

    public void setContent(Content [] content) {
        this.content = content;
    }

    public EmailAddress getFrom() {
        return from;
    }

    public void setFrom(EmailAddress from) {
        this.from = from;
    }

    public Personalization [] getPersonalizations() {
        return personalizations;
    }

    public void setPersonalizations(Personalization [] personalizations) {
        this.personalizations = personalizations;
    }

    @Override
    public String toString() {
        try {
            return MAPPER.writeValueAsString(this);
        } catch (JsonProcessingException e) {}

        return null;
    }

    public static class Personalization {
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private EmailAddress [] to;
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private EmailAddress [] cc;
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private EmailAddress [] bcc;

        public EmailAddress[] getTo() {
            return to;
        }

        public void setTo(EmailAddress[] to) {
            this.to = to;
        }

        public EmailAddress[] getCc() {
            return cc;
        }

        public void setCc(EmailAddress[] cc) {
            this.cc = cc;
        }

        public EmailAddress[] getBcc() {
            return bcc;
        }

        public void setBcc(EmailAddress[] bcc) {
            this.bcc = bcc;
        }
    }

    public static class Content {
        private String type;
        private String value;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    public static class EmailAddress {
        private String email;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }
}
