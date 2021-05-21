package com.tim9.bolnica.controllers;

import java.security.cert.X509Certificate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tim9.bolnica.dto.FilterDTO;

import com.tim9.bolnica.dto.response.MessageResponseDTO;
import com.tim9.bolnica.model.CustomPageImplementation;
import com.tim9.bolnica.services.CertificateService;
import com.tim9.bolnica.services.MessageService;
import com.tim9.bolnica.util.SignatureUtil;

@RestController
@RequestMapping(value = "/api/messages")
@CrossOrigin(origins = "https://localhost:4205", maxAge = 3600, allowedHeaders = "*")
public class MessageController {
	
	private static final Logger logger = LoggerFactory.getLogger(MessageController.class);
	
	@Autowired
	private CertificateService certService;
	
	@Autowired
	private MessageService messageService;
	
	@PostMapping
	public ResponseEntity<?> create(@RequestBody byte[] msg) {
		try {
			X509Certificate cert = SignatureUtil.extractCertificate(msg);
			boolean valid = certService.validateCert(cert);
			if (!valid) {
				logger.debug("Message is signed with non trusted certificate.");
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
			}
			boolean validMessage = SignatureUtil.verifySignature(msg, cert);
			if (!validMessage) {
				logger.debug("Message signature not valid.");
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
			}
			messageService.save(msg);
			return new ResponseEntity<>("OK",HttpStatus.OK);
		} catch (Exception e) {
			logger.debug(e.getMessage());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping(value = "/by-page")
	public ResponseEntity<?> getMessages(Pageable pageable, @RequestBody FilterDTO filter) {
		try {
			logger.trace("Patient messages view requested");
			return new ResponseEntity<>(createCustomPage(messageService.findAll(pageable, filter)), HttpStatus.OK);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	private CustomPageImplementation<MessageResponseDTO> createCustomPage(Page<MessageResponseDTO> page) {
		return new CustomPageImplementation<>(page.getContent(), page.getNumber(), page.getSize(),
				page.getTotalElements(), null, page.isLast(), page.getTotalPages(), null, page.isFirst(),
				page.getNumberOfElements());
	}

}
