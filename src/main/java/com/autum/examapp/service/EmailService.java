package com.autum.examapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String to, String subject, String body) {
        try {
            System.out.println("API KEY: " + System.getenv("SENDGRID_API_KEY"));
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            message.setFrom("shaikajith2001@gmail.com");

            mailSender.send(message);

            System.out.println("Email sent successfully");

        } catch (Exception e) {
            System.out.println("Email sending failed: " + e.getMessage());
        }
    }
}