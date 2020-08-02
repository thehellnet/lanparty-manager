package org.thehellnet.lanparty.manager.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.thehellnet.lanparty.manager.configuration.params.MailParams;
import org.thehellnet.utility.YmlUtility;

import java.util.Properties;

@Configuration
public class EmailConfiguration implements TestAwareConfiguration {

    private static final MailParams MAIL_PARAMS = YmlUtility.getInstance("configuration/mail.yml", MailParams.class).loadFromResources(runningTest);

    private static final Logger logger = LoggerFactory.getLogger(EmailConfiguration.class);

    @Bean
    public JavaMailSender getJavaMailSender() {
        logger.info("Init javaMailSender Bean");
        logger.debug("Mail server: Host: {} - Port: {}", MAIL_PARAMS.getHost(), MAIL_PARAMS.getPort());

        JavaMailSenderImpl javaMailSenderImpl = new JavaMailSenderImpl();
        javaMailSenderImpl.setHost(MAIL_PARAMS.getHost());
        javaMailSenderImpl.setPort(MAIL_PARAMS.getPort());
        javaMailSenderImpl.setUsername(MAIL_PARAMS.getUsername());
        javaMailSenderImpl.setPassword(MAIL_PARAMS.getPassword());

        Properties properties = javaMailSenderImpl.getJavaMailProperties();
        properties.put("mail.smtp.from", MAIL_PARAMS.getFrom());
        properties.put("mail.transport.protocol", MAIL_PARAMS.isEnableSsl() ? "smtps" : "smtp");
        properties.put("mail.smtp.auth", MAIL_PARAMS.isEnableAuth() ? "true" : "false");
        properties.put("mail.smtp.starttls.enable", MAIL_PARAMS.isEnableTls() ? "true" : "false");
        properties.put("mail.debug", MAIL_PARAMS.isEnableDebug() ? "true" : "false");

        return javaMailSenderImpl;
    }

    @Bean("mailParams")
    public MailParams getMailParams() {
        return MAIL_PARAMS;
    }
}
