package com.tim9.bolnica.controllers;


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

import com.tim9.bolnica.dto.SearchLogDTO;
import com.tim9.bolnica.dto.response.LogResponseDTO;
import com.tim9.bolnica.model.CustomPageImplementation;
import com.tim9.bolnica.services.LogService;

@RestController
@RequestMapping(value = "/api/logs")
@CrossOrigin(origins = "https://localhost:4205", maxAge = 3600, allowedHeaders = "*")
public class LogController {

	private static final Logger logger = LoggerFactory.getLogger(LogController.class);
	
	@Autowired
	private LogService logService;
	
	@PostMapping(value = "/by-page")
	public ResponseEntity<?> getLogs(Pageable pageable, @RequestBody SearchLogDTO searchLog) {
		try {
			logger.trace("Logs view requested");
			return new ResponseEntity<>(createCustomPage(logService.findAll(pageable, searchLog)), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	private CustomPageImplementation<LogResponseDTO> createCustomPage(Page<LogResponseDTO> page) {
		return new CustomPageImplementation<>(page.getContent(), page.getNumber(), page.getSize(),
				page.getTotalElements(), null, page.isLast(), page.getTotalPages(), null, page.isFirst(),
				page.getNumberOfElements());
	}
	
}
