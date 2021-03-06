package com.tim9.bolnica.controllers;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tim9.bolnica.dto.CSRDTO;
import com.tim9.bolnica.services.CSRService;

@RestController
@RequestMapping(value = "/api/csr", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "https://localhost:4205", maxAge = 3600, allowedHeaders = "*")
public class CSRController {

	private static final Logger logger = LoggerFactory.getLogger(CSRController.class);
	
	@Autowired
	private CSRService csrService;
	
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createCSR(@Valid @RequestBody CSRDTO csrDTO) {
		try {
			logger.trace("Creation of new certificate signing request requested");
			csrService.createCSR(csrDTO);
			return new ResponseEntity<>(HttpStatus.CREATED);
		} catch (Exception e) {
			logger.debug(e.getMessage());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
}
