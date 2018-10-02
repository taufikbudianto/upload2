/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.app.config.datasource;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

/**
 *
 * @author Randy
 */
@Configuration
@EnableJpaAuditing
//@EnableJpaRepositories(basePackages = {"prosia.app.repo", "prosia.basarnas.repo"}, 
//        entityManagerFactoryRef = "entityManagerMySql", 
//        transactionManagerRef = "transactionManagerMySql")
@EnableJpaRepositories(basePackages = {"prosia.app.repo", "prosia.basarnas.repo"})
@EntityScan(basePackages = {"prosia.app.model","prosia.basarnas.model"})
public class MySqlDataSource {
//    
//    @Primary
//    @Bean
//    @ConfigurationProperties(prefix = "spring.datasource")
//    public DataSource dataSourceMySql() {
//        return DataSourceBuilder.create().build();
//    }
//    
//    @Primary
//    @Bean
//    public LocalContainerEntityManagerFactoryBean entityManagerMySql(EntityManagerFactoryBuilder builder) {
//        return builder
//                .dataSource(dataSourceMySql())
//                .packages("prosia.app.model", "prosia.basarnas.model")
//                .persistenceUnit("oracle")
//                .build();
//    }
//
//    @Primary
//    @Bean
//    public PlatformTransactionManager transactionManagerMySql(
//            @Qualifier("entityManagerMySql") EntityManagerFactory emf) {
//        JpaTransactionManager txManager = new JpaTransactionManager();
//        txManager.setEntityManagerFactory(emf);
//        return txManager;
//    }
//    
//    @Bean
//    public JdbcTemplate jdbcMySql(@Qualifier("dataSourceMySql") DataSource dsMySql) {
//        return new JdbcTemplate(dsMySql);
//    }
    
}
