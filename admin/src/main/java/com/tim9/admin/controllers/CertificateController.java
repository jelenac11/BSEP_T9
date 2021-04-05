package com.tim9.admin.controllers;


import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tim9.admin.services.CertificateService;
import com.tim9.dto.response.CertificateResponseDTO;

@RestController
@RequestMapping(value = "/api/certificates")
@CrossOrigin(origins = "http://localhost:4201", maxAge = 3600, allowedHeaders = "*")
public class CertificateController {
	
	@Autowired
	private CertificateService certService;
	
	@GetMapping
	public ResponseEntity<?> getCertificates() {
		try {
			ArrayList<CertificateResponseDTO> certs = certService.findAll();
			return new ResponseEntity<>(certs, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Error occured!", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/ca")
	public ResponseEntity<?> getCACertificates() {
		try {
			ArrayList<CertificateResponseDTO> certs = certService.findAllCA();
			return new ResponseEntity<>(certs, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Error occured!", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
