package com.thanhpham.chat.config;

import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Slf4j
@Configuration
public class DataSourceConfig {
    @Bean
    @ConditionalOnProperty(
            name = "app.database.enabled",
            havingValue = "true"
    )
    public DataSource dataSource(
            @Value("${spring.datasource.url}") String url,
            @Value("${spring.datasource.username}") String username,
            @Value("${spring.datasource.password}") String password,
            @Value("${spring.datasource.driver-class-name}") String driverClassName) {
        log.info(">> Init DataSource manually for URL: {}", url);

        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setDriverClassName(driverClassName);
        dataSource.setMaximumPoolSize(10);
        dataSource.setMinimumIdle(2);
        return dataSource;
    }

    @Bean
    @ConditionalOnProperty(
            name = "app.database.enabled",
            havingValue = "true"
    )
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        log.info(">> Init JdbcTemplate manually");
        return new JdbcTemplate(dataSource);
    }
}
