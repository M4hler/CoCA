package com.cthulhu.configurations;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.services.ssm.SsmClient;
import software.amazon.awssdk.services.ssm.model.GetParametersRequest;
import software.amazon.awssdk.services.ssm.model.GetParametersResponse;
import software.amazon.awssdk.services.ssm.model.Parameter;

@Configuration
@Profile("cloud")
public class ActiveMqConfiguration {
    private static final String ACTIVEMQ_OPENWIRE_URL = "ActiveMqOpenWireUrl";
    private static final String ACTIVEMQ_USERNAME = "ActiveMqUsername";
    private static final String ACTIVEMQ_PASSWORD = "ActiveMqPassword";

    @Bean
    public ActiveMQConnectionFactory activeMQConnectionFactory(SsmClient ssmClient) {
        GetParametersRequest request = GetParametersRequest.builder()
                .names(ACTIVEMQ_OPENWIRE_URL, ACTIVEMQ_USERNAME, ACTIVEMQ_PASSWORD)
                .withDecryption(true)
                .build();
        GetParametersResponse response = ssmClient.getParameters(request);

        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory();
        for(Parameter p : response.parameters()) {
            switch (p.name()) {
                case ACTIVEMQ_OPENWIRE_URL:
                    factory.setBrokerURL(p.value());
                    break;
                case ACTIVEMQ_USERNAME:
                    factory.setUserName(p.value());
                    break;
                case ACTIVEMQ_PASSWORD:
                    factory.setPassword(p.value());
                    break;
            }
        }

        return factory;
    }
}
