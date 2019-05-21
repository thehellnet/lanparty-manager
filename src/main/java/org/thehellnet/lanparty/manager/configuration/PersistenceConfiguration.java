package org.thehellnet.lanparty.manager.configuration;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.util.Properties;

@Configuration
@EnableJpaRepositories(basePackages = "org.thehellnet.lanparty.manager.repository")
@EnableTransactionManagement
public class PersistenceConfiguration {

    @Bean(name = "dataSource")
    public DataSource getDataSource() {
        ComboPooledDataSource dataSource = new ComboPooledDataSource();

        try {
            dataSource.setDriverClass(org.postgresql.Driver.class.getName());
        } catch (PropertyVetoException e) {
            e.printStackTrace();
            return null;
        }

        dataSource.setJdbcUrl("jdbc:postgresql://127.0.0.1:5432/lanparty");
        dataSource.setUser("lanparty");
        dataSource.setPassword("lanparty");
        dataSource.setMaxPoolSize(50);
        dataSource.setMaxIdleTime(10);

        return dataSource;
    }

    @Bean(name = "entityManagerFactory")
    public LocalContainerEntityManagerFactoryBean getLocalContainerEntityManagerFactoryBean() {
        LocalContainerEntityManagerFactoryBean entityManagerFactory = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactory.setPackagesToScan("org.thehellnet.lanparty.manager.model.persistence");
        entityManagerFactory.setDataSource(getDataSource());
        entityManagerFactory.setJpaVendorAdapter(getHibernateJpaVendorAdapter());
        entityManagerFactory.setJpaProperties(getHibernateProperties());
        return entityManagerFactory;
    }

    @Bean(name = "transactionManager")
    public JpaTransactionManager getJpaTransactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(getLocalContainerEntityManagerFactoryBean().getObject());
        return transactionManager;
    }

    @Bean(name = "hibernateJpaAutoConfiguration")
    public HibernateJpaVendorAdapter getHibernateJpaVendorAdapter() {
        return new HibernateJpaVendorAdapter();
    }

    private Properties getHibernateProperties() {
        Properties properties = new Properties();
        properties.put("hibernate.dialect", org.hibernate.dialect.PostgreSQL10Dialect.class.getName());
        properties.put("hibernate.show_sql", false);
        properties.put("hibernate.hbm2ddl.auto", "update");
        properties.put("hibernate.c3p0.min_size", 2);
        properties.put("hibernate.c3p0.max_size", 20);
        properties.put("hibernate.c3p0.timeout", 30);
        properties.put("hibernate.c3p0.max_statements", 60);
//        properties.put("hibernate.enable_lazy_load_no_trans", true);
        properties.put("jadira.usertype.autoRegisterUserTypes", "true");
        return properties;
    }
}
