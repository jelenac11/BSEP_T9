package com.tim9.admin.services;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentVerifierProvider;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentVerifierProviderBuilder;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.pkcs.PKCSException;
import org.bouncycastle.pkcs.jcajce.JcaPKCS10CertificationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.tim9.admin.exceptions.InvalidDigitalSignatureException;
import com.tim9.admin.model.CSR;
import com.tim9.admin.repositories.CsrRepository;
import com.tim9.dto.response.CSRResponseDTO;

@Service
public class CSRService {
	
	/*
	 * Izvori:
	 * https://www.bouncycastle.org/docs/pkixdocs1.5on/org/bouncycastle/pkcs/PKCS10CertificationRequest.html
	 * 
	 */

	@Autowired
	private CsrRepository csrRepository;
	
	public String saveCSR(byte[] csr) throws IOException, InvalidKeyException, OperatorCreationException, NoSuchAlgorithmException, PKCSException, InvalidDigitalSignatureException {
		boolean valid = isValidSigned(new PKCS10CertificationRequest(csr));
		if (!valid) {
			throw new InvalidDigitalSignatureException("Invalid digital signature!");
		}
		CSR certReq = new CSR(csr);
		csrRepository.save(certReq);
		return "CSR successfully saved!";
	}

	private boolean isValidSigned(PKCS10CertificationRequest csr) throws InvalidKeyException, OperatorCreationException, NoSuchAlgorithmException, PKCSException {
		BouncyCastleProvider bc = new BouncyCastleProvider();
		ContentVerifierProvider provider = new JcaContentVerifierProviderBuilder().setProvider(bc).build(csr.getSubjectPublicKeyInfo());
		boolean valid = csr.isSignatureValid(provider);
		return valid;
	}

	public Page<CSRResponseDTO> findAll(Pageable pageable) throws IOException {
		Page<CSR> page =  csrRepository.findAll(pageable);
		List<CSRResponseDTO> csrList = new ArrayList<CSRResponseDTO>();
		for (CSR csr: page.getContent()) {
			JcaPKCS10CertificationRequest req = new JcaPKCS10CertificationRequest(csr.getCsr());
			X500Name subject = req.getSubject();
			System.out.println(subject.toString());
			CSRResponseDTO dto = new CSRResponseDTO(csr.getId(), subject.getRDNs(BCStyle.CN)[0].getFirst().getValue().toString(), subject.getRDNs(BCStyle.O)[0].getFirst().getValue().toString(), subject.getRDNs(BCStyle.OU)[0].getFirst().getValue().toString(), subject.getRDNs(BCStyle.L)[0].getFirst().getValue().toString(),
					subject.getRDNs(BCStyle.ST)[0].getFirst().getValue().toString(), subject.getRDNs(BCStyle.C)[0].getFirst().getValue().toString(), subject.getRDNs(BCStyle.E)[0].getFirst().getValue().toString());
			csrList.add(dto);
		}
		return new PageImpl<CSRResponseDTO>(csrList, pageable, csrList.size());
	}

	public String rejectCSR(Long serialNumber) {
		Optional<CSR> csr = csrRepository.findById(serialNumber);
		if (csr.isPresent()) {
			csrRepository.deleteById(serialNumber);
			return "CSR successfully rejected!";
		} else {
			throw new NoSuchElementException("CSR with given serial number does not exist!");
		}
	}

}
