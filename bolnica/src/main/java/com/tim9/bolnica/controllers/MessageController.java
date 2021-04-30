package com.tim9.bolnica.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tim9.bolnica.dto.MessageDTO;

@RestController
@RequestMapping(value = "/api/messages")
@CrossOrigin(origins = "https://localhost:4205", maxAge = 3600, allowedHeaders = "*")
public class MessageController {
	
	@PostMapping
	public ResponseEntity<?> create(@Valid @RequestBody MessageDTO message, HttpServletRequest request) {
		System.out.println(message);
		return ResponseEntity.ok(message);
	}

}
