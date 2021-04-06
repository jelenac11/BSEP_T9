package com.tim9.admin.services;


import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.bouncycastle.asn1.x500.RDN;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.tim9.admin.dto.response.CertificateResponseDTO;
import com.tim9.admin.dto.response.RevokedCertResponseDTO;
import com.tim9.admin.exceptions.CertificateAlreadyRevokedException;
import com.tim9.admin.model.RevokedCertificate;
import com.tim9.admin.repositories.RevokedCertificatesRepository;
import com.tim9.admin.util.KeyStoreUtil;


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
    
    @Autowired
    private RevokedCertificatesRepository revokedCertRepo;
    
    @Autowired
    private EmailService emailService;
    
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

	public String revokeCertificate(String serialNumber, String reason) throws CertificateAlreadyRevokedException, KeyStoreException, CertificateException, NoSuchAlgorithmException, NoSuchProviderException,
		IOException {
		Optional<RevokedCertificate> rvc = revokedCertRepo.findById(Long.parseLong(serialNumber));
		if (rvc.isPresent()) {
			throw new CertificateAlreadyRevokedException("Certificate with serial number " + serialNumber + " is already revoked!");
		}
		KeyStore ks = KeyStoreUtil.loadKeyStore(KEYSTORE_FILE_PATH, KEYSTORE_PASSWORD);
		X509Certificate certificate = (X509Certificate) ks.getCertificate(serialNumber);
        if (certificate == null) {
            throw new NoSuchElementException("Certificate with serial number" + serialNumber + " doesn't exist");
        }
        X500Name x500name = new JcaX509CertificateHolder(certificate).getSubject();
        RDN cn = x500name.getRDNs(BCStyle.CN)[0];
        String issuerData = certificate.getIssuerX500Principal().getName();
        String[] rdns = issuerData.split("CN=");
        int index = rdns[1].indexOf(',') == -1 ? rdns[1].length() : rdns[1].indexOf(',');

        RevokedCertificate rc = new RevokedCertificate(Long.parseLong(serialNumber), cn.getFirst().getValue().toString(), certificate.getNotBefore(), certificate.getNotAfter(), rdns[1].substring(0, index), new Date());
        revokedCertRepo.save(rc);
        
        KeyStoreUtil.deleteEntry(ks, serialNumber);
        KeyStoreUtil.saveKeyStore(ks, KEYSTORE_FILE_PATH, KEYSTORE_PASSWORD);

        KeyStore ts = KeyStoreUtil.loadKeyStore(TRUSTSTORE_FILE_PATH, TRUSTSTORE_PASSWORD);
        KeyStoreUtil.deleteEntry(ts, serialNumber);
        KeyStoreUtil.saveKeyStore(ts, TRUSTSTORE_FILE_PATH, TRUSTSTORE_PASSWORD);
        
		emailService.sendMail(new JcaX509CertificateHolder(certificate).getIssuer().getRDNs(BCStyle.E)[0].getFirst().getValue().toString(), "Certificate Revoke", "Hi,\n\nYour CSR is rejected." + 
				"\n\nCSR info:\n\tCommon Name: " + rc.getCommonName() +
				"\n\tSerial number: " + rc.getId() +
				"\n\tIssuer: " + rc.getIssuer() +
				"\n\tReason: " + reason +
				"\n\nTeam 9\n");
		
		return "Certificate successfully revoked";
	}

	public Page<RevokedCertResponseDTO> getRevokedCerts(Pageable pageable) {
		Page<RevokedCertificate> page =  revokedCertRepo.findAll(pageable);
		List<RevokedCertResponseDTO> revoked = new ArrayList<RevokedCertResponseDTO>();
		for (RevokedCertificate cert: page.getContent()) {
			revoked.add(new RevokedCertResponseDTO(cert.getId().toString(), cert.getCommonName(), cert.getNotBefore(), cert.getNotAfter(), cert.getIssuer(), cert.getRevokeDate()));
		}
		return new PageImpl<RevokedCertResponseDTO>(revoked, pageable, revoked.size());
	}

	public String checkCertStatus(String serialNumber, KeyStore ks) throws KeyStoreException, CertificateException, NoSuchAlgorithmException, NoSuchProviderException, IOException {
        if (ks == null) {
            ks = KeyStoreUtil.loadKeyStore(KEYSTORE_FILE_PATH, KEYSTORE_PASSWORD);
            X509Certificate cert = (X509Certificate) ks.getCertificate(serialNumber);
            if (cert == null) {
                return "Unknown certificate with serial number " + serialNumber + "!";
            }
            if (new Date().compareTo(cert.getNotAfter()) > 0) {
            	return "Certificate with serial number " + serialNumber + " is not valid!";
            }
            if (new Date().compareTo(cert.getNotBefore()) < 0) {
            	return "Certificate with serial number " + serialNumber + " is not valid yet!";
            }
        }
        Optional<RevokedCertificate> r = revokedCertRepo.findById(Long.parseLong(serialNumber));
        if (r.isPresent()) {
            return "Certificate with serial number " + serialNumber + " is revoked!";
        }

        return "Certificate with serial number " + serialNumber + " is valid!";
	}

	public void deleteAllExceptRoot() throws KeyStoreException, CertificateException, NoSuchAlgorithmException, NoSuchProviderException, IOException {
		KeyStore ks = KeyStoreUtil.loadKeyStore(KEYSTORE_FILE_PATH, KEYSTORE_PASSWORD);
	    Enumeration<String> enumeration = ks.aliases();
		while (enumeration.hasMoreElements()) {
			String current = enumeration.nextElement();
			if (!current.equals("root")) {
				ks.deleteEntry(current);
			}
		}
		KeyStoreUtil.saveKeyStore(ks, KEYSTORE_FILE_PATH, KEYSTORE_PASSWORD);
		ks = KeyStoreUtil.loadKeyStore(TRUSTSTORE_FILE_PATH, TRUSTSTORE_PASSWORD);
	    enumeration = ks.aliases();
		while (enumeration.hasMoreElements()) {
			String current = enumeration.nextElement();
			if (!current.equals("root")) {
				ks.deleteEntry(current);
			}
		}
		KeyStoreUtil.saveKeyStore(ks, TRUSTSTORE_FILE_PATH, TRUSTSTORE_PASSWORD);
		
	}

}
