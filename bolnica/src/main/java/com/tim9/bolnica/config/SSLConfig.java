package com.tim9.bolnica.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Configuration
@ConfigurationProperties(prefix = "server.ssl")
public class SSLConfig {

	private String keyStore;
	private String keyStorePassword;
	
	private String trustStore;
	private String trustStorePassword;
}
