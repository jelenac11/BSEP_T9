package com.tim9.bolnica.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tim9.bolnica.services.PatientService;

@RestController
@RequestMapping(value = "/api/patients")
@CrossOrigin(origins = "https://localhost:4205", maxAge = 3600, allowedHeaders = "*")
public class PatientController {

	private static final Logger logger = LoggerFactory.getLogger(PatientController.class);
	
	@Autowired
	private PatientService patientService;
	
	@GetMapping
	public ResponseEntity<?> getPatients() {
		try {
			logger.trace("Patients view requested");
			return new ResponseEntity<>(patientService.findAll(), HttpStatus.OK);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
