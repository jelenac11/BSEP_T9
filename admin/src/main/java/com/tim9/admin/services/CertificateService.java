package com.tim9.admin.services;


import java.io.IOException;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;

import org.bouncycastle.asn1.x500.RDN;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.tim9.admin.dto.CaCertDTO;
import com.tim9.admin.dto.response.CertificateResponseDTO;
import com.tim9.admin.dto.response.RevokedCertResponseDTO;
import com.tim9.admin.exceptions.CertificateAlreadyRevokedException;
import com.tim9.admin.exceptions.CertificateDoesNotExistException;
import com.tim9.admin.exceptions.InvalidCertificateDateException;
import com.tim9.admin.model.CSR;
import com.tim9.admin.model.RevokedCertificate;
import com.tim9.admin.repositories.CsrRepository;
import com.tim9.admin.repositories.RevokedCertificatesRepository;
import com.tim9.admin.util.CertUtil;
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
    
    @Autowired
    private CsrRepository csrRepository;
    
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

	public String revokeCertificate(String serialNumber, String reason) throws Exception {
		Optional<RevokedCertificate> rvc = revokedCertRepo.findById(Long.parseLong(serialNumber));
		if (rvc.isPresent()) {
			throw new CertificateAlreadyRevokedException("Certificate with serial number " + serialNumber + " is already revoked!");
		}
		KeyStore ks = KeyStoreUtil.loadKeyStore(KEYSTORE_FILE_PATH, KEYSTORE_PASSWORD);
		X509Certificate certificate = (X509Certificate) ks.getCertificate(serialNumber);
		
        if (certificate == null) {
            throw new NoSuchElementException("Certificate with serial number" + serialNumber + " doesn't exist");
        }
        
        Certificate[] chain = ks.getCertificateChain(serialNumber);
        if (chain != null) {
        	revokeFromChain(certificate, ks);
        }
        
        RevokedCertificate rc = createRevokedCertificate(certificate);
        revokedCertRepo.save(rc);
        
        KeyStoreUtil.deleteEntry(ks, serialNumber);
        KeyStoreUtil.saveKeyStore(ks, KEYSTORE_FILE_PATH, KEYSTORE_PASSWORD);

        KeyStore ts = KeyStoreUtil.loadKeyStore(TRUSTSTORE_FILE_PATH, TRUSTSTORE_PASSWORD);
        KeyStoreUtil.deleteEntry(ts, serialNumber);
        KeyStoreUtil.saveKeyStore(ts, TRUSTSTORE_FILE_PATH, TRUSTSTORE_PASSWORD);
        
		emailService.sendMail(new JcaX509CertificateHolder(certificate).getIssuer().getRDNs(BCStyle.E)[0].getFirst().getValue().toString(), "Certificate Revoke", "Hi,\n\nYour Certificate is revoked." + 
				"\n\nCertificate info:\n\tCommon Name: " + rc.getCommonName() +
				"\n\tSerial number: " + rc.getId() +
				"\n\tIssuer: " + rc.getIssuer() +
				"\n\tReason: " + reason +
				"\n\nTeam 9\n");
		
		return "Certificate successfully revoked";
	}

	private void revokeFromChain(X509Certificate ca, KeyStore ks) throws Exception {
		Enumeration<String> enumeration = ks.aliases();
		while (enumeration.hasMoreElements()) {
			String alias = enumeration.nextElement();
			X509Certificate certificate = (X509Certificate) ks.getCertificate(alias);
			if (certificate.getIssuerDN().equals(ca.getSubjectDN())) {
				RevokedCertificate rc = createRevokedCertificate(certificate);
		        revokedCertRepo.save(rc);
		        
		        KeyStoreUtil.deleteEntry(ks, alias);
		        KeyStoreUtil.saveKeyStore(ks, KEYSTORE_FILE_PATH, KEYSTORE_PASSWORD);

		        KeyStore ts = KeyStoreUtil.loadKeyStore(TRUSTSTORE_FILE_PATH, TRUSTSTORE_PASSWORD);
		        KeyStoreUtil.deleteEntry(ts, alias);
		        KeyStoreUtil.saveKeyStore(ts, TRUSTSTORE_FILE_PATH, TRUSTSTORE_PASSWORD);
			}
		}
	}

	public Page<RevokedCertResponseDTO> getRevokedCerts(Pageable pageable) {
		Page<RevokedCertificate> page =  revokedCertRepo.findAll(pageable);
		List<RevokedCertResponseDTO> revoked = new ArrayList<RevokedCertResponseDTO>();
		for (RevokedCertificate cert: page.getContent()) {
			revoked.add(new RevokedCertResponseDTO(cert.getId().toString(), cert.getCommonName(), cert.getNotBefore(), cert.getNotAfter(), cert.getIssuer(), cert.getRevokeDate()));
		}
		return new PageImpl<RevokedCertResponseDTO>(revoked, pageable, revoked.size());
	}

	public String checkCertStatus(String serialNumber) throws KeyStoreException, CertificateException, NoSuchAlgorithmException, NoSuchProviderException, IOException {
        KeyStore ks = KeyStoreUtil.loadKeyStore(KEYSTORE_FILE_PATH, KEYSTORE_PASSWORD);
        X509Certificate cert = (X509Certificate) ks.getCertificate(serialNumber);
        if (cert == null) {
        	try {
        		Optional<RevokedCertificate> r = revokedCertRepo.findById(Long.parseLong(serialNumber));
                if (r.isPresent()) {
                    return "Certificate with serial number " + serialNumber + " is revoked!";
                }
                return "Unknown certificate with serial number " + serialNumber + "!";
        	} catch (NumberFormatException e) {
        		return "Unknown certificate with serial number " + serialNumber + "!";
        	}
        }
        if (new Date().compareTo(cert.getNotAfter()) > 0) {
        	return "Certificate with serial number " + serialNumber + " is not valid!";
        }
        if (new Date().compareTo(cert.getNotBefore()) < 0) {
        	return "Certificate with serial number " + serialNumber + " is not valid yet!";
        }
        return "Certificate with serial number " + serialNumber + " is valid!";
	}

	public String createCACertificate(CaCertDTO dto) throws Exception {
		KeyStore ks = KeyStoreUtil.loadKeyStore(KEYSTORE_FILE_PATH, KEYSTORE_PASSWORD);
		
		//provera da li je not after pre not before
		if (dto.getNotBefore().compareTo(dto.getNotAfter()) >= 0)
		    throw new InvalidCertificateDateException("Certificate start date must be before expiration date");
		
		//provera da li je not before pre danas
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		if (formatter.parse(formatter.format(new Date())).compareTo(dto.getNotBefore()) > 0)
		    throw new InvalidCertificateDateException("Certificate start date must be after today");
		
		//provera da li ce sertifikat da vazi vise od 5 godina, za CA ogranicavamo na maks 5
		Date novo = dto.getNotBefore();
	    Calendar cal = Calendar.getInstance();
	    cal.setTime(novo);
	    cal.add(Calendar.YEAR,  5);
	    Date newDate = cal.getTime();
	    if (newDate.compareTo(dto.getNotAfter()) < 0)
		    throw new InvalidCertificateDateException("Maximum validity period is five years");
	    
	    //dobavlja se CA sertifikat, odnosno root i proverava se i on
	    X509Certificate certCA = (X509Certificate) ks.getCertificate("root");
	    if (certCA == null) {
            throw new CertificateDoesNotExistException("CA certificate with given serial number does not exist");
        }
	    if (dto.getNotAfter().compareTo(certCA.getNotAfter()) > 0) {
	    	throw new InvalidCertificateDateException("CA Certificate will expire before end date.");
	    }
	    certCA.checkValidity();
	    
	    String signingAlg = "";
	    if (dto.getSigningAlgorithm().equals("")) {
	    	signingAlg = "sha256WithRSAEncryption";
	    } else {
	    	signingAlg = dto.getSigningAlgorithm();
	    }
	    JcaContentSignerBuilder builder = new JcaContentSignerBuilder(signingAlg);
        BouncyCastleProvider bcp = new BouncyCastleProvider();
        builder = builder.setProvider(bcp); 
        String caPassword = KEYSTORE_PASSWORD + "root";
        PrivateKey privateKey = (PrivateKey) ks.getKey("root", caPassword.toCharArray());
        ContentSigner contentSigner = builder.build(privateKey);
        
        //kreiramo subject, issuer i par kljuceva 
        X500Name issuerName = new JcaX509CertificateHolder(certCA).getSubject();
        X500Name subject = CertUtil.createX500Name(dto.getCommonName(), dto.getOrganization(), dto.getOrganizationalUnit(), dto.getCityLocality(), dto.getStateCountyRegion(), dto.getCountry(), dto.getEmailAddress());
        KeyPair keys = generateKeyPair();
        
        BigInteger serialNumber = generateSerialNumber();
        X509v3CertificateBuilder certGen = new JcaX509v3CertificateBuilder(issuerName, serialNumber, dto.getNotBefore(), dto.getNotAfter(), subject, keys.getPublic());
               
        //dodavanje ekstenzija
        CertUtil.addBasicConstraints(certGen, true);
        CertUtil.addCAKeyUsage(certGen);
        if (dto.isKeyIdentifierExtension()) {
        	CertUtil.addKeyIdentifierExtensions(certGen, keys.getPublic(), certCA.getPublicKey());
        }
        String caEmail = issuerName.getRDNs(BCStyle.EmailAddress)[0].getFirst().getValue().toString();
        if (dto.isAlternativeNameExtension()) {
        	CertUtil.addAlternativeNamesExtensions(certGen, dto.getEmailAddress(), caEmail);
        }
        
        
        X509CertificateHolder certHolder = certGen.build(contentSigner);
        JcaX509CertificateConverter certConverter = new JcaX509CertificateConverter();
        certConverter = certConverter.setProvider(bcp);
        X509Certificate newCertificate =  certConverter.getCertificate(certHolder);
        //upisivanje u keystore i truststore
        ks.setKeyEntry(serialNumber.toString(), keys.getPrivate(), (KEYSTORE_PASSWORD + serialNumber).toCharArray(), new Certificate[]{newCertificate});
        KeyStoreUtil.saveKeyStore(ks, KEYSTORE_FILE_PATH, KEYSTORE_PASSWORD);
        KeyStore truststore = KeyStoreUtil.loadKeyStore(TRUSTSTORE_FILE_PATH, TRUSTSTORE_PASSWORD);
        truststore.setCertificateEntry(serialNumber.toString(), newCertificate);
        KeyStoreUtil.saveKeyStore(truststore, TRUSTSTORE_FILE_PATH, TRUSTSTORE_PASSWORD);
        
        //kreiraj pkcs12, ovo ce se fizickim putem preneti do bolnice za koju je kreirano
        CertUtil.createPKCS12(serialNumber, keys.getPrivate(), newCertificate);
        
		return "Successfully created CA certificate!";
	}
	
	private RevokedCertificate createRevokedCertificate(X509Certificate cert) throws CertificateEncodingException {
		X500Name x500name = new JcaX509CertificateHolder(cert).getSubject();
        RDN cn = x500name.getRDNs(BCStyle.CN)[0];
        String issuerData = cert.getIssuerX500Principal().getName();
        String[] rdns = issuerData.split("CN=");
        int index = rdns[1].indexOf(',') == -1 ? rdns[1].length() : rdns[1].indexOf(',');

        RevokedCertificate rc = new RevokedCertificate(cert.getSerialNumber().longValue(), cn.getFirst().getValue().toString(), cert.getNotBefore(), cert.getNotAfter(), rdns[1].substring(0, index), new Date());
        return rc;
	}
	
	private BigInteger generateSerialNumber() {
        BigInteger serialNumber;
        Optional<CSR> csr;
        do {
            serialNumber = new BigInteger(32, new SecureRandom());
            csr = csrRepository.findById(serialNumber.longValue());
        } while (csr.isPresent());
        return serialNumber;
	}
	
	private KeyPair generateKeyPair() {
		try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
            keyGen.initialize(2048, random);
            return keyGen.generateKeyPair();
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            e.printStackTrace();
        }
        return null;
	}

	public boolean checkCert(String serialNumber) throws KeyStoreException, CertificateException, NoSuchAlgorithmException, NoSuchProviderException, IOException {
		KeyStore ks = KeyStoreUtil.loadKeyStore(KEYSTORE_FILE_PATH, KEYSTORE_PASSWORD);
        X509Certificate cert = (X509Certificate) ks.getCertificate(serialNumber);
        if (cert == null) {
        	try {
        		Optional<RevokedCertificate> r = revokedCertRepo.findById(Long.parseLong(serialNumber));
                if (r.isPresent()) {
                    return false;
                }
                return false;
        	} catch (NumberFormatException e) {
        		return false;
        	}
        }
        if (new Date().compareTo(cert.getNotAfter()) > 0) {
        	return false;
        }
        if (new Date().compareTo(cert.getNotBefore()) < 0) {
        	return false;
        }
        return true;
	}

}
