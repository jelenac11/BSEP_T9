package com.tim9.bolnica.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tim9.bolnica.dto.MessagesTemplateRuleDTO;
import com.tim9.bolnica.dto.SeverityTemplateRuleDTO;
import com.tim9.bolnica.services.RulesService;

@RestController
@RequestMapping(value = "/api/rules")
@CrossOrigin(origins = "https://localhost:4205", maxAge = 3600, allowedHeaders = "*")
public class RulesController {
	
	@Autowired
	private RulesService rulesService;

	@GetMapping(value = "/default")
    public ResponseEntity<?> findAllDefault() {
		return new ResponseEntity<>(rulesService.findAllDefault(), HttpStatus.OK);
    }
	
	@GetMapping(value = "/mtr")
    public ResponseEntity<?> findAllMTR() {
		return new ResponseEntity<>(rulesService.findAllMTR(), HttpStatus.OK);
    }
	
	@GetMapping(value = "/str")
    public ResponseEntity<?> findAllSTR() {
		return new ResponseEntity<>(rulesService.findAllSTR(), HttpStatus.OK);
    }
	
	@PostMapping(value = "/create-str")
    public ResponseEntity<?> createStr(@Valid @RequestBody SeverityTemplateRuleDTO dto) {
		try {
			rulesService.addSTR(dto);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
    }
	
	@PostMapping(value = "/create-mtr")
    public ResponseEntity<?> createMtr(@Valid @RequestBody MessagesTemplateRuleDTO dto) {
		try {
			rulesService.addMTR(dto);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
    }
}
