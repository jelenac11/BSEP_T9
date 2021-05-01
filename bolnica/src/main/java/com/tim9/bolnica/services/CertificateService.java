package com.tim9.bolnica.services;

import java.security.cert.X509Certificate;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CertificateService {

	public boolean validateCert(X509Certificate x509Certificate) {
		RestTemplate restTemplate = new RestTemplate();
		return restTemplate.getForEntity("https://localhost:8080/api/certificates/validate/" + x509Certificate.getSerialNumber().longValue(), Boolean.class).getBody().booleanValue();
	}

}
