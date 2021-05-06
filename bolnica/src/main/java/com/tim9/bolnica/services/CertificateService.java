package com.tim9.bolnica.services;

import java.security.cert.X509Certificate;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.tim9.bolnica.util.KeyStoreUtil;

@Service
public class CertificateService {

	@Value("${truststore.password}")
    private String keyStorePassword;
    
    @Value("${truststore.path}")
    private String keyStoreFilePath;
    
	public boolean validateCert(X509Certificate cert) {
		String alias = KeyStoreUtil.getCertificateAlias(cert, keyStoreFilePath, keyStorePassword);
		if (alias == null) {
			return false;
		}
		X509Certificate x509certificate = (X509Certificate) KeyStoreUtil.getCertificate(alias,  keyStoreFilePath, keyStorePassword);
		RestTemplate restTemplate = new RestTemplate();
		return restTemplate.getForEntity("https://localhost:8081/api/certificates/validate/" + x509certificate.getSerialNumber().longValue(), Boolean.class).getBody().booleanValue();
	}

}
