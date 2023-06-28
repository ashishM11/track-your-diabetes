package com.tyd.user.module.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
@Service
public class MailService {

    private final JavaMailSender mailSender;

    public MailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }



    public void sendEmailFromTemplate(String from,String to) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();

        message.setFrom(new InternetAddress(from));
        message.setRecipients(MimeMessage.RecipientType.TO, to);
        message.setSubject("Test email from Spring");

        String htmlContent = "<h1>This is a test Spring Boot email</h1>" +
                "<p>It can contain <strong>HTML</strong> content.</p>";
        message.setContent(htmlContent, "text/html; charset=utf-8");

        mailSender.send(message);
    }
}
