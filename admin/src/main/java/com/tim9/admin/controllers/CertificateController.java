package com.tim9.admin.controllers;


import java.util.ArrayList;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tim9.admin.dto.RevokeObjectDTO;
import com.tim9.admin.dto.response.CertificateResponseDTO;
import com.tim9.admin.dto.response.RevokedCertResponseDTO;
import com.tim9.admin.services.CertificateService;
import com.tim9.admin.util.CustomPageImplementation;

@RestController
@RequestMapping(value = "/api/certificates")
@CrossOrigin(origins = "http://localhost:4201", maxAge = 3600, allowedHeaders = "*")
public class CertificateController {
	
	@Autowired
	private CertificateService certService;
	
	@GetMapping
	public ResponseEntity<?> getCertificates() {
		try {
			ArrayList<CertificateResponseDTO> certs = certService.findAll();
			return new ResponseEntity<>(certs, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/ca")
	public ResponseEntity<?> getCACertificates() {
		try {
			ArrayList<CertificateResponseDTO> certs = certService.findAllCA();
			return new ResponseEntity<>(certs, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping("/revoke")
	public ResponseEntity<String> revokeCertificate(@Valid @RequestBody RevokeObjectDTO dto) {
		try {
			String message = certService.revokeCertificate(dto.getSerialNumber(), dto.getReason());
			return new ResponseEntity<>(message, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/revoked/by-page")
    public ResponseEntity<?> getRevokedCertificates(Pageable pageable) {
		Page<RevokedCertResponseDTO> page;
        try {
        	page = certService.getRevokedCerts(pageable);
            return new ResponseEntity<>(createCustomPage(page), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

	@GetMapping("/status/{serialNumber}")
    public ResponseEntity<String> getCertificateStatus(@PathVariable(value = "serialNumber") String serialNumber) {
        try {
            return new ResponseEntity<>(certService.checkCertStatus(serialNumber), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
	
	private CustomPageImplementation<RevokedCertResponseDTO> createCustomPage(Page<RevokedCertResponseDTO> page) {
		return new CustomPageImplementation<>(page.getContent(), page.getNumber(), page.getSize(),
				page.getTotalElements(), null, page.isLast(), page.getTotalPages(), null, page.isFirst(),
				page.getNumberOfElements());
	}
}
