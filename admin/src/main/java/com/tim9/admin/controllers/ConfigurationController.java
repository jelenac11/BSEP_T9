package com.tim9.admin.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tim9.admin.model.Hospital;
import com.tim9.admin.model.LogConfig;
import com.tim9.admin.repositories.HospitalRepository;
import com.tim9.admin.services.ConfigurationService;

@RestController
@RequestMapping(value = "/api/configuration", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "https://localhost:4201", maxAge = 3600, allowedHeaders = "*")
public class ConfigurationController {
	
	@Autowired
	private ConfigurationService configurationService;
	
	@Autowired
	private HospitalRepository hospitalRepository;
	
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createConfig(@Valid @RequestBody LogConfig logConfig) {
		try {
			configurationService.createConfig(logConfig);
			return new ResponseEntity<>(HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/hospitals")
	public ResponseEntity<?> getHospitals() {
		try {
			List<Hospital> hospitals = hospitalRepository.findAll();
			return new ResponseEntity<>(hospitals, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
