package com.tim9.bolnica.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfiguration {

	@Value("${truststore.path}")
    private String trustStorePath;

    @Value("${truststore.password}")
    private String trustStorePass;

    @Value("${keystore.filepath}")
    private String keyStorePath;

    @Value("${keystore.password}")
    private String keyStorePass;

    
	@Bean
	 RestTemplate restTemplate() throws KeyStoreException, IOException, NoSuchAlgorithmException,
	            UnrecoverableKeyException, KeyManagementException, CertificateException {
	        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
	        keyStore.load(new FileInputStream(new ClassPathResource("keyStore/keyStore.jks").getFile()),
	                keyStorePass.toCharArray());
	        KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
	        trustStore.load(new FileInputStream(new ClassPathResource("keyStore/trustStore.jks").getFile()),
	                trustStorePass.toCharArray());
	        SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(
	                new SSLContextBuilder().loadTrustMaterial(trustStore, new TrustSelfSignedStrategy())
	                .loadKeyMaterial(keyStore, keyStorePass.toCharArray()).build(), NoopHostnameVerifier.INSTANCE);
	        CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(socketFactory).build();
	        ClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
	        return new RestTemplate(requestFactory);

	        		
	}
}
