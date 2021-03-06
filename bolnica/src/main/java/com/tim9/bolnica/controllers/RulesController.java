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

import com.tim9.bolnica.dto.TemperatureRuleDTO;
import com.tim9.bolnica.dto.MessagesTemplateRuleDTO;
import com.tim9.bolnica.dto.OxygenLevelRuleDTO;
import com.tim9.bolnica.dto.OxygenLevelTemperatureRuleDTO;
import com.tim9.bolnica.dto.PressureRuleDTO;
import com.tim9.bolnica.dto.SeverityTemplateRuleDTO;
import com.tim9.bolnica.services.RulesService;

@RestController
@RequestMapping(value = "/api/rules")
@CrossOrigin(origins = "https://localhost:4205", maxAge = 3600, allowedHeaders = "*")
public class RulesController {
	
	private static final Logger logger = LoggerFactory.getLogger(RulesController.class);
	
	@Autowired
	private RulesService rulesService;

	@PostMapping(value = "/create-str")
    public ResponseEntity<?> createStr(@Valid @RequestBody SeverityTemplateRuleDTO dto) {
		try {
			logger.trace("Severity template rule creation requested");
			rulesService.addSTR(dto);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
    }
	
	@PostMapping(value = "/create-mtr")
    public ResponseEntity<?> createMtr(@Valid @RequestBody MessagesTemplateRuleDTO dto) {
		try {
			logger.trace("Messages template rule creation requested");
			rulesService.addMTR(dto);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
    }
	
	@PostMapping(value = "/create-lowt")
    public ResponseEntity<?> createLowTemperatureRule(@Valid @RequestBody TemperatureRuleDTO dto) {
		try {
			logger.trace("Low temperature template rule creation requested");
			rulesService.addT(dto, true);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
    }
	
	@PostMapping(value = "/create-hight")
    public ResponseEntity<?> createHighTemperatureRule(@Valid @RequestBody TemperatureRuleDTO dto) {
		try {
			logger.trace("High temperature template rule creation requested");
			rulesService.addT(dto, false);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
    }
	
	@PostMapping(value = "/create-olr")
    public ResponseEntity<?> createOLR(@Valid @RequestBody OxygenLevelRuleDTO dto) {
		try {
			logger.trace("Oxygen level template rule creation requested");
			rulesService.addOLR(dto);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
    }
	
	@PostMapping(value = "/create-oltr")
    public ResponseEntity<?> createOxygenTemperatureRule(@Valid @RequestBody OxygenLevelTemperatureRuleDTO dto) {
		try {
			logger.trace("Oxygen level and temperature template rule creation requested");
			rulesService.addOxygenLevelTemperatureRule(dto);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
    }
	
	@PostMapping(value = "/create-prule")
    public ResponseEntity<?> createPressureRule(@Valid @RequestBody PressureRuleDTO dto) {
		try {
			logger.trace("Pressure template rule creation requested");
			rulesService.addPressureRule(dto);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
    }
}
