package com.tim9.bolnica.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tim9.bolnica.dto.response.LogResponseDTO;
import com.tim9.bolnica.model.CustomPageImplementation;
import com.tim9.bolnica.services.LogService;

@RestController
@RequestMapping(value = "/api/logs")
@CrossOrigin(origins = "https://localhost:4205", maxAge = 3600, allowedHeaders = "*")
public class LogController {

	@Autowired
	private LogService logService;
	
	@GetMapping(value = "/by-page")
	public ResponseEntity<?> getLogs(Pageable pageable) {
		try {
			return new ResponseEntity<>(createCustomPage(logService.findAll(pageable)), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	private CustomPageImplementation<LogResponseDTO> createCustomPage(Page<LogResponseDTO> page) {
		return new CustomPageImplementation<>(page.getContent(), page.getNumber(), page.getSize(),
				page.getTotalElements(), null, page.isLast(), page.getTotalPages(), null, page.isFirst(),
				page.getNumberOfElements());
	}
	
}
