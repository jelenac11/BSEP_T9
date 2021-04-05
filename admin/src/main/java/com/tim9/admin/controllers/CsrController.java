package com.tim9.admin.controllers;

import java.io.IOException;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tim9.admin.dto.CertDataDTO;
import com.tim9.admin.services.CSRService;
import com.tim9.admin.util.CustomPageImplementation;
import com.tim9.dto.response.CSRResponseDTO;


@RestController
@RequestMapping(value = "/api/csr")
@CrossOrigin(origins = "http://localhost:4201", maxAge = 3600, allowedHeaders = "*")
public class CsrController {
	
	@Autowired
	private CSRService csrService;
	
	@PostMapping
	public ResponseEntity<?> saveCSR(@RequestBody byte[] csr) {
		try {
			return new ResponseEntity<>(csrService.saveCSR(csr), HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping(value = "/by-page")
	public ResponseEntity<?> getCSRs(Pageable pageable) {
		Page<CSRResponseDTO> page;
		try {
			page = csrService.findAll(pageable);
			return new ResponseEntity<>(createCustomPage(page), HttpStatus.OK);
		} catch (IOException e) {
			return new ResponseEntity<>("Error occured!", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping("/{serialNumber}")
    public ResponseEntity<String> rejectCSR(@PathVariable(value = "serialNumber") Long serialNumber) {
        try {
            return new ResponseEntity<>(csrService.rejectCSR(serialNumber), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
	
	@GetMapping("/{serialNumber}")
    public ResponseEntity<?> getCSR(@PathVariable(value = "serialNumber") Long serialNumber) {
        try {
            return new ResponseEntity<>(csrService.getCSR(serialNumber), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
	
	@PostMapping("/approve")
    public ResponseEntity<?> createNonCACertificate(@Valid @RequestBody CertDataDTO dto) {
        try {
            return new ResponseEntity<>(csrService.createNonCACertificate(dto), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
	
	private CustomPageImplementation<CSRResponseDTO> createCustomPage(Page<CSRResponseDTO> page) {
		return new CustomPageImplementation<>(page.getContent(), page.getNumber(), page.getSize(),
				page.getTotalElements(), null, page.isLast(), page.getTotalPages(), null, page.isFirst(),
				page.getNumberOfElements());
	}
}
