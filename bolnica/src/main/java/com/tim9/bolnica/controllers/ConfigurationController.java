package com.tim9.bolnica.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tim9.bolnica.model.LogConfig;
import com.tim9.bolnica.services.ConfigurationService;

@RestController
@RequestMapping(value = "/api/configuration")
@CrossOrigin(origins = "https://localhost:4205", maxAge = 3600, allowedHeaders = "*")
public class ConfigurationController {

	private static final Logger logger = LoggerFactory.getLogger(ConfigurationController.class);
	
	@Autowired
	private ConfigurationService configurationService;
	
	@PostMapping
	public ResponseEntity<?> saveConfig(@RequestBody LogConfig logConfig) {
		try {
			logger.trace("New log configuration added.");
			this.configurationService.saveConfig(logConfig);
			return new ResponseEntity<>("Success", HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
}
