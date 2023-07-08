package org.example.email;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

import java.util.Collections;
import java.util.Map;
import java.util.Properties;

public class MailSender {

    private final Properties properties;

    private String username;

    private String password;

    private MailSender() {
        properties = new Properties();
    }

    public static MailSender create(String host, String username, String password, Map<String, Object> properties) {
        MailSender mailSender = new MailSender();
        mailSender.properties.put("mail.host", host);
        mailSender.properties.put("mail.user", username);
        mailSender.properties.put("mail.password", password);
        mailSender.properties.putAll(properties);
        mailSender.username = username;
        mailSender.password = password;
        return mailSender;
    }

    public static MailSender create(String host, String username, String password) {
        return create(host, username, password, Collections.emptyMap());
    }


    public void sendText(String from, String to, String subject, String content) throws MessagingException {
        Session session = getSession();
        MimeMessage message = new MimeMessage(session);
        message.setHeader("Disposition-Notification-To", from);
        message.setFrom(new InternetAddress(from));
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
        message.setSubject(subject);
        message.setContent(content, "text/html;charset=utf-8");
        //Transport.send(message);
        Transport transport = session.getTransport();
        transport.connect(username, password);
        transport.sendMessage(message, message.getAllRecipients());
    }

    public void sendCommonTextMail(String from, String to, String subject, String content) throws EmailException {
        SimpleEmail email = new SimpleEmail();
        email.setHostName(properties.getProperty("mail.host"));
        email.setAuthentication(username, password);
        email.setFrom(from, "张三", "utf-8");

        email.addTo(to);
        email.setSubject(subject);
        email.setContent(content, "text/html;charset=utf-8");
        email.send();
    }

    private Session getSession() {
        if (StringUtils.isNoneBlank(username, password)) {
            return Session.getInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });
        }
        return Session.getInstance(properties);
    }

}
