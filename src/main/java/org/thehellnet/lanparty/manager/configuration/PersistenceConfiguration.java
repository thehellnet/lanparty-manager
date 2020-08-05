package org.thehellnet.lanparty.manager.configuration;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.thehellnet.lanparty.manager.configuration.mixin.TestAwareConfiguration;
import org.thehellnet.lanparty.manager.configuration.params.PersistenceParams;
import org.thehellnet.lanparty.manager.model.persistence.AppUser;
import org.thehellnet.utility.YmlUtility;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "org.thehellnet.lanparty.manager.repository")
@EnableJpaAuditing(dateTimeProviderRef = "auditDateTimeProvider")
public class PersistenceConfiguration implements TestAwareConfiguration {

    private final PersistenceParams params = YmlUtility.getInstance("configuration/persistence.yml", PersistenceParams.class).loadFromResources(runningTest);

    private static final Logger logger = LoggerFactory.getLogger(PersistenceConfiguration.class);

    @Bean("dataSource")
    public DataSource getDataSource() {
        logger.info("Init dataSource Bean");
        logger.debug("Database server: URL: {} - Username: {} - Hbm2ddlhi: {}",
                params.getJdbcUrl(), params.getUsername(), params.getHbm2ddl());

        ComboPooledDataSource dataSource = new ComboPooledDataSource();

        try {
            dataSource.setDriverClass(params.getDriverClass());
        } catch (PropertyVetoException e) {
            logger.error("Unable to initialize database driver");
            return null;
        }

        dataSource.setJdbcUrl(params.getJdbcUrl());
        dataSource.setUser(params.getUsername());
        dataSource.setPassword(params.getPassword());
        dataSource.setMaxPoolSize(params.getMaxPoolSize());
        dataSource.setMaxIdleTime(params.getMaxIdleTime());

        return dataSource;
    }

    @Bean("entityManagerFactory")
    public LocalContainerEntityManagerFactoryBean getLocalContainerEntityManagerFactoryBean() {
        LocalContainerEntityManagerFactoryBean entityManagerFactory = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactory.setPackagesToScan("org.thehellnet.lanparty.manager.model.persistence");
        entityManagerFactory.setDataSource(getDataSource());
        entityManagerFactory.setJpaVendorAdapter(getHibernateJpaVendorAdapter());
        entityManagerFactory.setJpaProperties(getHibernateProperties());
        return entityManagerFactory;
    }

    @Bean("transactionManager")
    public JpaTransactionManager getJpaTransactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(getLocalContainerEntityManagerFactoryBean().getObject());
        return transactionManager;
    }

    @Bean("hibernateJpaAutoConfiguration")
    public HibernateJpaVendorAdapter getHibernateJpaVendorAdapter() {
        return new HibernateJpaVendorAdapter();
    }

    @Bean("auditDateTimeProvider")
    public DateTimeProvider getAuditDateTimeProvider() {
        return () -> Optional.of(LocalDateTime.now(Clock.systemUTC()));
    }

    @Bean("auditorProvider")
    public AuditorAware<AppUser> getAuditorProvider() {
        return () -> {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication == null || !authentication.isAuthenticated()) {
                return Optional.empty();
            }

            Object principal = authentication.getPrincipal();

            if (!(principal instanceof AppUser)) {
                logger.warn("Principal is not AppUser: {} - {}", principal.getClass().getName(), principal);
                return Optional.empty();
            }

            return Optional.of((AppUser) principal);
        };
    }

    private Properties getHibernateProperties() {
        Properties properties = new Properties();
        properties.put("hibernate.dialect", params.getDialect());
        properties.put("hibernate.show_sql", params.isShowSql());
        properties.put("hibernate.hbm2ddl.auto", params.getHbm2ddl());
        properties.put("hibernate.c3p0.min_size", 2);
        properties.put("hibernate.c3p0.max_size", 20);
        properties.put("hibernate.c3p0.timeout", 30);
        properties.put("hibernate.c3p0.max_statements", 60);
//        properties.put("hibernate.enable_lazy_load_no_trans", true);
        properties.put("jadira.usertype.autoRegisterUserTypes", "true");
        properties.put("org.hibernate.envers.audit_table_suffix", "_audit");
        return properties;
    }
}
