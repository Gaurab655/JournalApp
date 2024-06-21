package com.journalApp.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.test.context.ContextConfiguration;

import static org.mockito.Mockito.verify;

@SpringBootTest
@ContextConfiguration(classes = {EmailService.class})
public class EmailServiceTests {

    @Autowired
    private EmailService emailService;

    @MockBean
    private JavaMailSender javaMailSender;

    @Test
    void testSendMail() {
        String to = "gaurabchand655@gmail.com";
        String subject = "Testing java mail sender";
        String body = "how r uh?";

        emailService.sendEmail(to, subject, body);

        verify(javaMailSender).send(new SimpleMailMessage() {{
            setTo(to);
            setSubject(subject);
            setText(body);
        }});
    }
}
