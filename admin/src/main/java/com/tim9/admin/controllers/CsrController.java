package com.tim9.admin.controllers;

import java.io.IOException;
import java.util.UUID;

import javax.validation.Valid;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.pkcs.jcajce.JcaPKCS10CertificationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tim9.admin.dto.CertDataDTO;
import com.tim9.admin.dto.response.CSRResponseDTO;
import com.tim9.admin.model.CSR;
import com.tim9.admin.model.VerificationToken;
import com.tim9.admin.services.CSRService;
import com.tim9.admin.services.EmailService;
import com.tim9.admin.services.VerificationTokenService;
import com.tim9.admin.util.CustomPageImplementation;


@RestController
@RequestMapping(value = "/api/csr")
@CrossOrigin(origins = "https://localhost:4201", maxAge = 3600, allowedHeaders = "*")
public class CsrController {
	
	@Autowired
	private CSRService csrService;
	
	@Autowired
	private VerificationTokenService verificationTokenService;
	
	@Autowired
    private EmailService emailService;
	
	@PostMapping
	public ResponseEntity<?> saveCSR(@RequestBody byte[] csr) {
		try {
			CSR newCSR = csrService.saveCSR(csr);
			String token = UUID.randomUUID().toString();
			VerificationToken vtoken = new VerificationToken();
			vtoken.setId(null);
			vtoken.setToken(token);
			vtoken.setCsr(newCSR);
			verificationTokenService.saveToken(vtoken);
			String confirmationUrl = "/verify-csr/" + token;
			JcaPKCS10CertificationRequest req = new JcaPKCS10CertificationRequest(newCSR.getCsr());
			X500Name subject = req.getSubject();
			emailService.sendMail(subject.getRDNs(BCStyle.E)[0].getFirst().getValue().toString(), "CSR Verification", "Hi,\n\nClick below to confirm your CSR:\n" + 
					"\nhttps://localhost:4201" + confirmationUrl + "\n\nCSR info:\n\tCommon Name: " + subject.getRDNs(BCStyle.CN)[0].getFirst().getValue().toString() +
					"\n\tOrganization: " + subject.getRDNs(BCStyle.O)[0].getFirst().getValue().toString() +
					"\n\tOrganizational Unit: " + subject.getRDNs(BCStyle.OU)[0].getFirst().getValue().toString() +
					"\n\tCity/Locality: " + subject.getRDNs(BCStyle.L)[0].getFirst().getValue().toString() +
					"\n\tState/County/Region: " + subject.getRDNs(BCStyle.ST)[0].getFirst().getValue().toString() +
					"\n\tCountry: " + subject.getRDNs(BCStyle.C)[0].getFirst().getValue().toString() +
					"\n\nThanks,\nTeam 9\n");
			return new ResponseEntity<>(newCSR, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping(value = "/verify-csr/{token}")
	public ResponseEntity<String> verifyCSR(@PathVariable("token") String url) {
		try {
			csrService.verifyCSR(url);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>("CSR verified!", HttpStatus.OK);
	}
	
	@GetMapping(value = "/by-page")
	public ResponseEntity<?> getCSRs(Pageable pageable) {
		Page<CSRResponseDTO> page;
		try {
			page = csrService.findAll(pageable);
			return new ResponseEntity<>(createCustomPage(page), HttpStatus.OK);
		} catch (IOException e) {
			return new ResponseEntity<>("Error occured!", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping("/{serialNumber}")
    public ResponseEntity<String> rejectCSR(@PathVariable(value = "serialNumber") Long serialNumber) {
        try {
            return new ResponseEntity<>(csrService.rejectCSR(serialNumber), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
	
	@GetMapping("/{serialNumber}")
    public ResponseEntity<?> getCSR(@PathVariable(value = "serialNumber") Long serialNumber) {
        try {
            return new ResponseEntity<>(csrService.getCSR(serialNumber), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
	
	@PostMapping("/approve")
    public ResponseEntity<?> createNonCACertificate(@Valid @RequestBody CertDataDTO dto) {
        try {
            return new ResponseEntity<>(csrService.createNonCACertificate(dto), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
	
	private CustomPageImplementation<CSRResponseDTO> createCustomPage(Page<CSRResponseDTO> page) {
		return new CustomPageImplementation<>(page.getContent(), page.getNumber(), page.getSize(),
				page.getTotalElements(), null, page.isLast(), page.getTotalPages(), null, page.isFirst(),
				page.getNumberOfElements());
	}
}
