package com.cthulhu.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ssm.SsmClient;

@Configuration
public class SsmClientConfiguration {
    @Bean
    public SsmClient createSsmClient() {
        return SsmClient.builder().region(Region.EU_NORTH_1).build();
    }
}
