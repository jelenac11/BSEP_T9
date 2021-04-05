package com.tim9.admin.services;

import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Enumeration;

import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.tim9.admin.util.KeyStoreUtil;
import com.tim9.dto.response.CertificateResponseDTO;

@Service
public class CertificateService {

	/*
	 * Izvori:
	 * vezbe
	 * https://www.baeldung.com/java-keystore
	 * https://docs.oracle.com/javase/7/docs/api/java/security/KeyStore.html
	 * 
	 * 
	 */
	
	@Value("${keystore.filepath}")
    private String KEYSTORE_FILE_PATH;

    @Value("${keystore.password}")
    private String KEYSTORE_PASSWORD;
    
    @Value("${truststore.path}")
    private String TRUSTSTORE_FILE_PATH;

    @Value("${truststore.password}")
    private String TRUSTSTORE_PASSWORD;
    
	public ArrayList<CertificateResponseDTO> findAll() throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException, NoSuchProviderException {
		KeyStore ks = KeyStoreUtil.loadKeyStore(KEYSTORE_FILE_PATH, KEYSTORE_PASSWORD);
		ArrayList<CertificateResponseDTO> certificates = new ArrayList<>();
		Enumeration<String> enumeration = ks.aliases();
		while (enumeration.hasMoreElements()) {
			String alias = enumeration.nextElement();
			X509Certificate certificate = (X509Certificate) ks.getCertificate(alias);
			String commonName = new JcaX509CertificateHolder(certificate).getSubject().getRDNs(BCStyle.CN)[0].getFirst().getValue().toString();
			String issuer = certificate.getIssuerX500Principal().getName();
			String [] rdns = issuer.split("CN=");
			int len;
			if (rdns[1].indexOf(',') == -1) 
				len = rdns[1].length();
			else 
				len = rdns[1].indexOf(',');
			CertificateResponseDTO dto = new CertificateResponseDTO(alias, commonName, rdns[1].substring(0, len), certificate.getNotBefore(), certificate.getNotAfter());
			certificates.add(dto);
		}
		return certificates;
	}

	public ArrayList<CertificateResponseDTO> findAllCA()  throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException, NoSuchProviderException {
		KeyStore ks = KeyStoreUtil.loadKeyStore(KEYSTORE_FILE_PATH, KEYSTORE_PASSWORD);
		ArrayList<CertificateResponseDTO> certificates = new ArrayList<>();
		Enumeration<String> enumeration = ks.aliases();
		while (enumeration.hasMoreElements()) {
			String alias = enumeration.nextElement();
			X509Certificate certificate = (X509Certificate) ks.getCertificate(alias);
			boolean[] keyusage = certificate.getKeyUsage();
			if (keyusage != null && keyusage[5]) {
				String commonName = new JcaX509CertificateHolder(certificate).getSubject().getRDNs(BCStyle.CN)[0].getFirst().getValue().toString();
				String issuer = certificate.getIssuerX500Principal().getName();
				String [] rdns = issuer.split("CN=");
				int len;
				if (rdns[1].indexOf(',') == -1) 
					len = rdns[1].length();
				else 
					len = rdns[1].indexOf(',');
				CertificateResponseDTO dto = new CertificateResponseDTO(alias, commonName, rdns[1].substring(0, len), certificate.getNotBefore(), certificate.getNotAfter());
				certificates.add(dto);
			}
		}
		return certificates;
	}

}
