package com.tim9.bolnica.services;

import java.security.cert.X509Certificate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.tim9.bolnica.util.KeyStoreUtil;

@Service
public class CertificateService {

	private static final Logger logger = LoggerFactory.getLogger(CertificateService.class);
	
	@Value("${truststore.password}")
    private String keyStorePassword;
    
    @Value("${truststore.path}")
    private String keyStoreFilePath;
    
	public boolean validateCert(X509Certificate cert) {
		String alias = KeyStoreUtil.getCertificateAlias(cert, keyStoreFilePath, keyStorePassword);
		if (alias == null) {
			logger.debug("Hospital device certificate is not trusted. Alias: " + alias);
			return false;
		}
		logger.debug("Hospital device certificate is trusted. Alias: " + alias);
		X509Certificate x509certificate = (X509Certificate) KeyStoreUtil.getCertificate(alias,  keyStoreFilePath, keyStorePassword);
		RestTemplate restTemplate = new RestTemplate();
		logger.debug("Sending request to check if hospital certificate is valid. Alias: " + alias);
		return restTemplate.getForEntity("https://localhost:8081/api/certificates/validate/" + x509certificate.getSerialNumber().longValue(), Boolean.class).getBody().booleanValue();
	}

}
