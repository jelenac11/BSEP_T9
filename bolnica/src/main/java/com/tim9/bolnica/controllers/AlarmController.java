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

import com.tim9.bolnica.dto.response.AlarmResponseDTO;
import com.tim9.bolnica.model.CustomPageImplementation;
import com.tim9.bolnica.services.AlarmService;

@RestController
@RequestMapping(value = "/api/alarms")
@CrossOrigin(origins = "https://localhost:4205", maxAge = 3600, allowedHeaders = "*")
public class AlarmController {
	
	@Autowired
    private AlarmService alarmService;

    @GetMapping(value = "/by-page")
    public ResponseEntity<?> findAll(Pageable pageable) {
		return new ResponseEntity<>(createCustomPage(alarmService.findAll(pageable)), HttpStatus.OK);
    }
    
    private CustomPageImplementation<AlarmResponseDTO> createCustomPage(Page<AlarmResponseDTO> page) {
		return new CustomPageImplementation<>(page.getContent(), page.getNumber(), page.getSize(),
				page.getTotalElements(), null, page.isLast(), page.getTotalPages(), null, page.isFirst(),
				page.getNumberOfElements());
	}

}
