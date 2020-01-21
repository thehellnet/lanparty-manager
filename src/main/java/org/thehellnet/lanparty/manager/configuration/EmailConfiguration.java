package org.thehellnet.lanparty.manager.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.thehellnet.lanparty.manager.configuration.params.MailParams;
import org.thehellnet.utility.YmlUtility;

import java.util.Properties;

@Configuration
public class EmailConfiguration {

    @Bean
    public JavaMailSender getJavaMailSender() {
        MailParams params = getMailParams();

        JavaMailSenderImpl javaMailSenderImpl = new JavaMailSenderImpl();
        javaMailSenderImpl.setHost(params.getHost());
        javaMailSenderImpl.setPort(params.getPort());
        javaMailSenderImpl.setUsername(params.getUsername());
        javaMailSenderImpl.setPassword(params.getPassword());

        Properties properties = javaMailSenderImpl.getJavaMailProperties();
        properties.put("mail.smtp.from", params.getFrom());
        properties.put("mail.transport.protocol", params.isEnableSsl() ? "smtps" : "smtp");
        properties.put("mail.smtp.auth", params.isEnableAuth() ? "true" : "false");
        properties.put("mail.smtp.starttls.enable", params.isEnableTls() ? "true" : "false");
        properties.put("mail.debug", params.isEnableDebug() ? "true" : "false");

        return javaMailSenderImpl;
    }

    @Bean
    public MailParams getMailParams() {
        return YmlUtility.loadFromResources("configuration/mail.yml", MailParams.class);
    }
}
