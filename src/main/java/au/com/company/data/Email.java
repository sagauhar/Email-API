package au.com.company.data;

import org.apache.commons.lang.StringUtils;

public class Email {
    private String to;
    private String cc;
    private String bcc;
    private String subject;
    private String body;

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

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public boolean isInvalid() {
        if (StringUtils.isBlank(getTo())) {
            return true;
        }

        if (StringUtils.isBlank(getSubject())) {
            return true;
        }

        if (StringUtils.isBlank(getBody())) {
            return true;
        }

        return false;
    }
}
