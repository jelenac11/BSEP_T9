package com.tim9.bolnica.controllers;

import javax.validation.Valid;

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

import com.tim9.bolnica.dto.ReportDTO;
import com.tim9.bolnica.services.ReportService;

@RestController
@RequestMapping(value = "/api/reports")
@CrossOrigin(origins = "https://localhost:4205", maxAge = 3600, allowedHeaders = "*")
public class ReportController {
	
	private static final Logger logger = LoggerFactory.getLogger(ReportController.class);
	
	@Autowired
	private ReportService reportService;
	
	@PostMapping
    public ResponseEntity<?> findAll(@Valid @RequestBody ReportDTO dto) {
		logger.trace("New report creation requested");
		return new ResponseEntity<>(reportService.getReport(dto), HttpStatus.OK);
    }

}
