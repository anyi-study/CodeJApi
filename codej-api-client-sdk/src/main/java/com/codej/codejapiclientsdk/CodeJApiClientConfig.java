package com.codej.codejapiclientsdk;

import com.codej.codejapiclientsdk.client.CodeJApiClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("codej.client")
@Data
@ComponentScan
public class CodeJApiClientConfig {
    private String accessKey;
    private String secretKey;
    @Bean
    public CodeJApiClient codeJApiClient(){
        return new CodeJApiClient(accessKey,secretKey);
    }
}
