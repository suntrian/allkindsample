package org.example.email;

import jakarta.mail.MessagingException;
import org.apache.commons.mail.EmailException;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MailSenderTest {

    @ParameterizedTest
    @CsvFileSource(resources = "/account.csv")
    void testSendMail(String host, String username, String password) throws MessagingException, EmailException {
        MailSender mailSender = MailSender.create(host, username, password);
        mailSender.sendText(username, username, "test email1 from java code", "test1, test1");
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/account.csv")
    void testSendMail2(String host, String username, String password) throws EmailException {
        MailSender mailSender = MailSender.create(host, username, password);
        mailSender.sendCommonTextMail(username, username, "test email2 from java code", "test2, test2");
    }
}


