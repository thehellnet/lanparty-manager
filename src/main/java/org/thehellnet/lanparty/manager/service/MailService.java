package org.thehellnet.lanparty.manager.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.thehellnet.lanparty.manager.configuration.params.MailParams;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

@Service
public class MailService {

    private static final Logger logger = LoggerFactory.getLogger(MailService.class);

    private final JavaMailSender javaMailSender;
    private final MailParams mailParams;

    public MailService(JavaMailSender javaMailSender, MailParams mailParams) {
        this.javaMailSender = javaMailSender;
        this.mailParams = mailParams;
    }

    public void send(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(mailParams.getFrom());
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        javaMailSender.send(message);
    }

    public void sendHtml(String to, String subject, String body) {
        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            InternetAddress[] internetAddresses = InternetAddress.parse(mailParams.getFrom());
            InternetAddress internetAddress = internetAddresses[0];
            message.setFrom(internetAddress);

            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setContent(body, MediaType.TEXT_HTML_VALUE);

            javaMailSender.send(message);
        } catch (MessagingException e) {
            logger.error(e.getMessage());
        }
    }
}
