package com.tim9.bolnica.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tim9.bolnica.dto.response.AlarmDoctorResponseDTO;
import com.tim9.bolnica.dto.response.AlarmResponseDTO;
import com.tim9.bolnica.model.CustomPageImplementation;
import com.tim9.bolnica.services.AlarmService;

@RestController
@RequestMapping(value = "/api/alarms")
@CrossOrigin(origins = "https://localhost:4205", maxAge = 3600, allowedHeaders = "*")
public class AlarmController {
	
	private static final Logger logger = LoggerFactory.getLogger(AlarmController.class);
	
	@Autowired
    private AlarmService alarmService;

    @GetMapping(value = "/by-page")
    public ResponseEntity<?> findAll(Pageable pageable) {
    	logger.trace("Security alarm view requested");
		return new ResponseEntity<>(createCustomPage(alarmService.findAll(pageable)), HttpStatus.OK);
    }
    
    @GetMapping(value = "/by-page-doctor")
    public ResponseEntity<?> findAllDoctorAlarms(Pageable pageable) {
    	logger.trace("Patient alarm view requested");
		return new ResponseEntity<>(createCustomPageDoctor(alarmService.findAllDoctorAlarms(pageable)), HttpStatus.OK);
    }
    
    private CustomPageImplementation<AlarmResponseDTO> createCustomPage(Page<AlarmResponseDTO> page) {
		return new CustomPageImplementation<>(page.getContent(), page.getNumber(), page.getSize(),
				page.getTotalElements(), null, page.isLast(), page.getTotalPages(), null, page.isFirst(),
				page.getNumberOfElements());
	}
    
    private CustomPageImplementation<AlarmDoctorResponseDTO> createCustomPageDoctor(Page<AlarmDoctorResponseDTO> page) {
		return new CustomPageImplementation<>(page.getContent(), page.getNumber(), page.getSize(),
				page.getTotalElements(), null, page.isLast(), page.getTotalPages(), null, page.isFirst(),
				page.getNumberOfElements());
	}

}
