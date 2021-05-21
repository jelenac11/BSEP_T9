package com.tim9.admin.services;


import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import java.util.NoSuchElementException;
import java.util.Optional;


import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.ContentVerifierProvider;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.operator.jcajce.JcaContentVerifierProviderBuilder;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.pkcs.PKCSException;
import org.bouncycastle.pkcs.jcajce.JcaPKCS10CertificationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.tim9.admin.dto.CertDataDTO;
import com.tim9.admin.dto.response.CSRResponseDTO;
import com.tim9.admin.exceptions.CertificateAlreadyExistsException;
import com.tim9.admin.exceptions.CertificateDoesNotExistException;
import com.tim9.admin.exceptions.InvalidCertificateDateException;
import com.tim9.admin.exceptions.InvalidDigitalSignatureException;
import com.tim9.admin.model.CSR;
import com.tim9.admin.model.VerificationToken;
import com.tim9.admin.repositories.CsrRepository;
import com.tim9.admin.util.CertUtil;
import com.tim9.admin.util.KeyStoreUtil;


@Service
public class CSRService {
	
	/*
	 * Izvori:
	 * https://smallstep.com/blog/everything-pki/
	 * https://www.bouncycastle.org/docs/pkixdocs1.5on/org/bouncycastle/pkcs/PKCS10CertificationRequest.html
	 * https://www.programcreek.com/java-api-examples/?api=org.bouncycastle.asn1.x509.Extension
	 * https://tools.ietf.org/html/rfc3279
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
    private EmailService emailService;
    
    @Autowired
    private VerificationTokenService verificationTokenService;
    
	@Autowired
	private CsrRepository csrRepository;
	
	public CSR saveCSR(byte[] csr) throws IOException, InvalidKeyException, OperatorCreationException, NoSuchAlgorithmException, PKCSException, InvalidDigitalSignatureException {
		boolean valid = isValidSigned(new PKCS10CertificationRequest(csr));
		if (!valid) {
			throw new InvalidDigitalSignatureException("Invalid digital signature!");
		}
		CSR certReq = new CSR(csr);
		certReq.setVerified(false);
		csrRepository.save(certReq);
		return certReq;
	}
	
	public void verifyCSR(String token) throws Exception {
		VerificationToken vt = verificationTokenService.findByToken(token);
		if (vt != null) {
			vt.getCsr().setVerified(true);
			csrRepository.save(vt.getCsr());
		} else {
			throw new NoSuchElementException("Token doesn't exist.");
		}
	}

	private boolean isValidSigned(PKCS10CertificationRequest csr) throws InvalidKeyException, OperatorCreationException, NoSuchAlgorithmException, PKCSException {
		BouncyCastleProvider bc = new BouncyCastleProvider();
		ContentVerifierProvider provider = new JcaContentVerifierProviderBuilder().setProvider(bc).build(csr.getSubjectPublicKeyInfo());
		boolean valid = csr.isSignatureValid(provider);
		return valid;
	}

	public Page<CSRResponseDTO> findAll(Pageable pageable) throws IOException {
		Page<CSR> page =  csrRepository.findByVerifiedTrue(pageable);
		List<CSRResponseDTO> csrList = new ArrayList<CSRResponseDTO>();
		for (CSR csr: page.getContent()) {
			JcaPKCS10CertificationRequest req = new JcaPKCS10CertificationRequest(csr.getCsr());
			X500Name subject = req.getSubject();
			CSRResponseDTO dto = new CSRResponseDTO(csr.getId(), subject.getRDNs(BCStyle.CN)[0].getFirst().getValue().toString(), subject.getRDNs(BCStyle.O)[0].getFirst().getValue().toString(), subject.getRDNs(BCStyle.OU)[0].getFirst().getValue().toString(), subject.getRDNs(BCStyle.L)[0].getFirst().getValue().toString(),
					subject.getRDNs(BCStyle.ST)[0].getFirst().getValue().toString(), subject.getRDNs(BCStyle.C)[0].getFirst().getValue().toString(), subject.getRDNs(BCStyle.E)[0].getFirst().getValue().toString());
			csrList.add(dto);
		}
		return new PageImpl<CSRResponseDTO>(csrList, pageable, csrList.size());
	}

	public String rejectCSR(Long serialNumber) throws IOException {
		Optional<CSR> csr = csrRepository.findById(serialNumber);
		if (csr.isPresent()) {
			VerificationToken vt = verificationTokenService.findByCsr(csr.get());
			verificationTokenService.deleteById(vt.getId());
			csrRepository.deleteById(serialNumber);
			
			JcaPKCS10CertificationRequest req = new JcaPKCS10CertificationRequest(csr.get().getCsr());
			X500Name subject = req.getSubject();
			emailService.sendMail(subject.getRDNs(BCStyle.E)[0].getFirst().getValue().toString(), "CSR Rejection", "Hi,\n\nYour CSR is rejected." + 
					"\n\nCSR info:\n\tCommon Name: " + subject.getRDNs(BCStyle.CN)[0].getFirst().getValue().toString() +
					"\n\tOrganization: " + subject.getRDNs(BCStyle.O)[0].getFirst().getValue().toString() +
					"\n\tOrganizational Unit: " + subject.getRDNs(BCStyle.OU)[0].getFirst().getValue().toString() +
					"\n\tCity/Locality: " + subject.getRDNs(BCStyle.L)[0].getFirst().getValue().toString() +
					"\n\tState/County/Region: " + subject.getRDNs(BCStyle.ST)[0].getFirst().getValue().toString() +
					"\n\tCountry: " + subject.getRDNs(BCStyle.C)[0].getFirst().getValue().toString() +
					"\n\nTeam 9\n");
			
			return "CSR successfully rejected!";
		} else {
			throw new NoSuchElementException("CSR with given serial number does not exist!");
		}
	}

	public CSRResponseDTO getCSR(Long serialNumber) throws IOException {
		Optional<CSR> csr = csrRepository.findById(serialNumber);
		if (csr.isPresent()) {
			JcaPKCS10CertificationRequest req = new JcaPKCS10CertificationRequest(csr.get().getCsr());
			X500Name subject = req.getSubject();
			CSRResponseDTO dto = new CSRResponseDTO(csr.get().getId(), subject.getRDNs(BCStyle.CN)[0].getFirst().getValue().toString(), subject.getRDNs(BCStyle.O)[0].getFirst().getValue().toString(), subject.getRDNs(BCStyle.OU)[0].getFirst().getValue().toString(), subject.getRDNs(BCStyle.L)[0].getFirst().getValue().toString(),
					subject.getRDNs(BCStyle.ST)[0].getFirst().getValue().toString(), subject.getRDNs(BCStyle.C)[0].getFirst().getValue().toString(), subject.getRDNs(BCStyle.E)[0].getFirst().getValue().toString());
			return dto;
		} else {
			throw new NoSuchElementException("CSR with given serial number does not exist!");
		}
	}

	public String createNonCACertificate(CertDataDTO dto) throws Exception {
		KeyStore ks = KeyStoreUtil.loadKeyStore(KEYSTORE_FILE_PATH, KEYSTORE_PASSWORD);
		
		X509Certificate cert = (X509Certificate) ks.getCertificate(dto.getSerialNumber());
		if (cert != null) {
		    throw new CertificateAlreadyExistsException("Certificate with given serial number already exists");
		}
		if (dto.getNotBefore().compareTo(dto.getNotAfter()) >= 0)
		    throw new InvalidCertificateDateException("Certificate start date must be before expiration date");
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		if (formatter.parse(formatter.format(new Date())).compareTo(dto.getNotBefore()) > 0)
		    throw new InvalidCertificateDateException("Certificate start date must be after today");
		Date novo = dto.getNotBefore();
	    Calendar cal = Calendar.getInstance();
	    cal.setTime(novo);
	    cal.add(Calendar.YEAR,  2);
	    Date newDate = cal.getTime();
	    if (newDate.compareTo(dto.getNotAfter()) < 0)
		    throw new InvalidCertificateDateException("Maximum validity period is two years");
	    Optional<CSR> csr = csrRepository.findById(Long.parseLong(dto.getSerialNumber()));
	    if (!csr.isPresent()) {
	    	throw new NoSuchElementException("CSR with given serial number doesn't exist!");
	    }
	    
	    
	    X509Certificate certCA = (X509Certificate) ks.getCertificate(dto.getIssuer());
	    if (certCA == null) {
            throw new CertificateDoesNotExistException("CA certificate with given serial number does not exist");
        }
	    if (dto.getNotAfter().compareTo(certCA.getNotAfter()) > 0) {
	    	throw new InvalidCertificateDateException("CA Certificate will expire before end date.");
	    }
	    try {
	    	certCA.checkValidity();
	    } catch (Exception e) {
	    	throw new InvalidCertificateDateException("CA Certificate is not valid.");
	    }
	    
	    String signingAlg = "";
	    if (dto.getSigningAlgorithm().equals("")) {
	    	signingAlg = "sha256WithRSAEncryption";
	    } else {
	    	signingAlg = dto.getSigningAlgorithm();
	    }
	    JcaContentSignerBuilder builder = new JcaContentSignerBuilder(signingAlg);
        BouncyCastleProvider bcp = new BouncyCastleProvider();
        builder = builder.setProvider(bcp); 
        String caPassword = KEYSTORE_PASSWORD + dto.getIssuer();
        PrivateKey privateKey = (PrivateKey) ks.getKey(dto.getIssuer(), caPassword.toCharArray());
        ContentSigner contentSigner = builder.build(privateKey);
        
        X500Name issuerName = new JcaX509CertificateHolder(certCA).getSubject();
        JcaPKCS10CertificationRequest csrData = new JcaPKCS10CertificationRequest(csr.get().getCsr());
        X500Name subject = CertUtil.createSubjectX500Name(csrData.getSubject(), certCA.getSerialNumber().toString());
        
        X509v3CertificateBuilder certGen = new JcaX509v3CertificateBuilder(issuerName, new BigInteger(dto.getSerialNumber()), dto.getNotBefore(), dto.getNotAfter(), subject, csrData.getPublicKey());
        CertUtil.addBasicConstraints(certGen, false);
        CertUtil.addKeyUsage(certGen, dto.getKeyUsage());
        CertUtil.addExtendedKeyUsage(certGen, dto.getExtendedKeyUsage());
        if (dto.isKeyIdentifierExtension()) {
        	CertUtil.addKeyIdentifierExtensions(certGen, csrData.getPublicKey(), certCA.getPublicKey());
        }
        String email = csrData.getSubject().getRDNs(BCStyle.EmailAddress)[0].getFirst().getValue().toString();
        String caEmail = issuerName.getRDNs(BCStyle.EmailAddress)[0].getFirst().getValue().toString();
        if (dto.isAlternativeNameExtension()) {
        	CertUtil.addAlternativeNamesExtensions(certGen, email, caEmail);
        }
        
        X509CertificateHolder certHolder = certGen.build(contentSigner);
        JcaX509CertificateConverter certConverter = new JcaX509CertificateConverter();
        certConverter = certConverter.setProvider(bcp);
   
        X509Certificate newCertificate =  certConverter.getCertificate(certHolder);
        
        ks.setCertificateEntry(dto.getSerialNumber(), newCertificate);
        KeyStoreUtil.saveKeyStore(ks, KEYSTORE_FILE_PATH, KEYSTORE_PASSWORD);
        KeyStore truststore = KeyStoreUtil.loadKeyStore(TRUSTSTORE_FILE_PATH, TRUSTSTORE_PASSWORD);
        truststore.setCertificateEntry(dto.getSerialNumber(), newCertificate);
        KeyStoreUtil.saveKeyStore(truststore, TRUSTSTORE_FILE_PATH, TRUSTSTORE_PASSWORD);
      
        File certFile = CertUtil.x509CertificateToPem(newCertificate, "", dto.getSerialNumber());
        
        emailService.sendEmailWithCertificate(email, certFile);
        
        VerificationToken vt = verificationTokenService.findByCsr(csr.get());
		verificationTokenService.deleteById(vt.getId());
        
        csrRepository.delete(csr.get());
		return "Successfully created certificate!";
	}
}
