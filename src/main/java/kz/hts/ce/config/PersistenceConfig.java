package kz.hts.ce.config;

import org.hibernate.envers.strategy.ValidityAuditStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.envers.repository.support.EnversRevisionRepositoryFactoryBean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import java.util.Collections;
import java.util.Map;

@Configuration
@EnableTransactionManagement(proxyTargetClass = true)
@EnableJpaRepositories(value = "kz.hts.ce.repository", repositoryFactoryBeanClass = EnversRevisionRepositoryFactoryBean.class)
@ComponentScan("kz.hts.ce")
public class PersistenceConfig {

    @Bean(name = "dataSource")
    public DriverManagerDataSource dataSource() {
        DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
        driverManagerDataSource.setDriverClassName("org.h2.Driver");
        driverManagerDataSource.setUrl("jdbc:h2:tcp://localhost/~/test");
        driverManagerDataSource.setUsername("test");
        driverManagerDataSource.setPassword("test");
        return driverManagerDataSource;
    }

    @Bean(name = "entityManagerFactory")
    public EntityManagerFactory entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
        emf.setPackagesToScan("kz.hts.ce");
        emf.setPersistenceUnitName("primary");
        emf.setDataSource(dataSource());
        emf.afterPropertiesSet();
        emf.setJpaPropertyMap(jpaProperties());/*TODO*/
        return emf.getObject();
    }

    @Bean(name = "transactionManager")
    public PlatformTransactionManager transactionManager() {
        return new JpaTransactionManager(entityManagerFactory());
    }

    private Map<String, String> jpaProperties() {
        return Collections.singletonMap("org.hibernate.envers.audit_strategy", ValidityAuditStrategy.class.getName());
    }
}
