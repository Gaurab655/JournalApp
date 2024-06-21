package com.journalApp.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
@Slf4j
public class EmailService {
    private static final Logger LOGGER = Logger.getLogger(EmailService.class.getName());

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendEmail(String to, String subject, String body) {
        LOGGER.info("Attempting to send email...");
        try {
            SimpleMailMessage mail = new SimpleMailMessage();
            mail.setTo(to);
            mail.setSubject(subject);
            mail.setText(body);
            javaMailSender.send(mail);
            LOGGER.info("Email sent successfully");
        } catch (Exception e) {
            LOGGER.severe("Failed to send email: " + e.getMessage());
        }
    }
}
