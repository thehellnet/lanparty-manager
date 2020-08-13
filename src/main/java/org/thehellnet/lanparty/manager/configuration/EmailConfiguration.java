package org.thehellnet.lanparty.manager.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.thehellnet.lanparty.manager.configuration.mixin.TestAwareConfiguration;
import org.thehellnet.lanparty.manager.configuration.params.MailParams;
import org.thehellnet.utility.YmlUtility;

import java.util.Properties;

@Configuration
public class EmailConfiguration implements TestAwareConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(EmailConfiguration.class);

    @Bean
    public JavaMailSender getJavaMailSender(MailParams mailParams) {
        logger.info("Init javaMailSender Bean");
        logger.debug("Mail server: Host: {} - Port: {}", mailParams.getHost(), mailParams.getPort());

        JavaMailSenderImpl javaMailSenderImpl = new JavaMailSenderImpl();
        javaMailSenderImpl.setHost(mailParams.getHost());
        javaMailSenderImpl.setPort(mailParams.getPort());
        javaMailSenderImpl.setUsername(mailParams.getUsername());
        javaMailSenderImpl.setPassword(mailParams.getPassword());

        Properties properties = javaMailSenderImpl.getJavaMailProperties();
        properties.put("mail.smtp.from", mailParams.getFrom());
        properties.put("mail.transport.protocol", mailParams.isEnableSsl() ? "smtps" : "smtp");
        properties.put("mail.smtp.auth", mailParams.isEnableAuth() ? "true" : "false");
        properties.put("mail.smtp.starttls.enable", mailParams.isEnableTls() ? "true" : "false");
        properties.put("mail.debug", mailParams.isEnableDebug() ? "true" : "false");

        return javaMailSenderImpl;
    }

    @Bean("mailParams")
    public MailParams getMailParams() {
        return YmlUtility.getInstance("configuration/mail.yml", MailParams.class).loadFromResources(runningTest);
    }
}
