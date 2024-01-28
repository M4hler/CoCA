package com.cthulhu.configurations;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.services.ssm.SsmClient;
import software.amazon.awssdk.services.ssm.model.GetParametersRequest;
import software.amazon.awssdk.services.ssm.model.GetParametersResponse;
import software.amazon.awssdk.services.ssm.model.Parameter;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfiguration {
    private static final String POSTGRESQL_URL = "PostgreSqlUrl";
    private static final String POSTGRESQL_USERNAME = "PostgreSqlUsername";
    private static final String POSTGRESQL_PASSWORD = "PostgreSqlPassword";

    @Bean
    @Profile("cloud")
    public DataSource dataSourceCloud(SsmClient ssmClient) {
        GetParametersRequest request = GetParametersRequest.builder()
                .names(POSTGRESQL_URL, POSTGRESQL_USERNAME, POSTGRESQL_PASSWORD)
                .withDecryption(true)
                .build();
        GetParametersResponse response = ssmClient.getParameters(request);

        DataSourceBuilder<?> builder = DataSourceBuilder.create();
        builder.driverClassName("org.postgresql.Driver");

        for(Parameter p : response.parameters()) {
            switch (p.name()) {
                case POSTGRESQL_URL:
                    builder.url(p.value());
                    break;
                case POSTGRESQL_USERNAME:
                    builder.username(p.value());
                    break;
                case POSTGRESQL_PASSWORD:
                    builder.password(p.value());
                    break;
            }
        }

        return builder.build();
    }
}
