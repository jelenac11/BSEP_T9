package com.tim9.bolnica.config;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;

import lombok.AllArgsConstructor;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

/*@Configuration
@EnableTransactionManagement
@EnableAsync
@AllArgsConstructor*/
public class RestTemplateConfig {

	/*private final SSLConfig sslConfig;

	@Bean
	RestTemplate restTemplate() throws KeyStoreException, IOException, NoSuchAlgorithmException,
			UnrecoverableKeyException, KeyManagementException, CertificateException {
		KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
		keyStore.load(new FileInputStream(new File(sslConfig.getKeyStore())),
				sslConfig.getKeyStorePassword().toCharArray());

		KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
		trustStore.load(new FileInputStream(new File(sslConfig.getTrustStore())),
				sslConfig.getTrustStorePassword().toCharArray());

		SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(
				new SSLContextBuilder().loadTrustMaterial(trustStore, new TrustSelfSignedStrategy())
						.loadKeyMaterial(keyStore, sslConfig.getKeyStorePassword().toCharArray()).build());

		CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(socketFactory).build();

		ClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);

		return new RestTemplate(requestFactory);
	}*/

}
