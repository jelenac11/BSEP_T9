package com.tim9.bolnica.controllers;

import java.security.PublicKey;
import java.security.cert.X509Certificate;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

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
import com.tim9.bolnica.dto.MessageDTO;
import com.tim9.bolnica.dto.SearchLogDTO;
import com.tim9.bolnica.dto.response.LogResponseDTO;
import com.tim9.bolnica.dto.response.MessageResponseDTO;
import com.tim9.bolnica.model.CustomPageImplementation;
import com.tim9.bolnica.services.CertificateService;
import com.tim9.bolnica.services.MessageService;

@RestController
@RequestMapping(value = "/api/messages")
@CrossOrigin(origins = "https://localhost:4205", maxAge = 3600, allowedHeaders = "*")
public class MessageController {
	
	@Autowired
	private CertificateService certService;
	
	@Autowired
	private MessageService messageService;
	
	@PostMapping
	public ResponseEntity<?> create(@Valid @RequestBody MessageDTO message, HttpServletRequest request) {
		X509Certificate[] certs = (X509Certificate[])request.getAttribute("javax.servlet.request.X509Certificate");
		boolean valid = certService.validateCert(certs[0]);
		if (!valid) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
		PublicKey publicKey = certs[0].getPublicKey();
		try {
			messageService.checkSign(message, publicKey);
			messageService.save(message);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping(value = "/by-page")
	public ResponseEntity<?> getLogs(Pageable pageable, @RequestBody FilterDTO filter) {
		try {
			return new ResponseEntity<>(createCustomPage(messageService.findAll(pageable, filter)), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	private CustomPageImplementation<MessageResponseDTO> createCustomPage(Page<MessageResponseDTO> page) {
		return new CustomPageImplementation<>(page.getContent(), page.getNumber(), page.getSize(),
				page.getTotalElements(), null, page.isLast(), page.getTotalPages(), null, page.isFirst(),
				page.getNumberOfElements());
	}

}
