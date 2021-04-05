package com.tim9.bolnica.controllers;

import java.util.List;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tim9.bolnica.dto.UserLoginDTO;
import com.tim9.bolnica.dto.response.UserResDTO;
import com.tim9.bolnica.dto.response.UserTokenStateDTO;
import com.tim9.bolnica.model.Authority;
import com.tim9.bolnica.model.User;
import com.tim9.bolnica.security.TokenUtils;
import com.tim9.bolnica.util.UserMapper;

@RestController
@CrossOrigin(origins = "http://localhost:4205", maxAge = 3600, allowedHeaders = "*")
@RequestMapping(value = "/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthenticationController {

	@Autowired
	private TokenUtils tokenUtils;

	@Autowired
	private AuthenticationManager authenticationManager;

	
	private UserMapper userMapper;

	public AuthenticationController() {
		userMapper = new UserMapper();
	}

	@PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createAuthenticationToken(@Valid @RequestBody UserLoginDTO authenticationRequest) {
		Authentication authentication = null;
		
		try {
			authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
					authenticationRequest.getEmail(), authenticationRequest.getPassword()));
		} catch (Exception e) {
			return new ResponseEntity<>("Incorrect email or password.", HttpStatus.UNAUTHORIZED);
		}

		// Kreiraj token za tog korisnika
		User user = (User) authentication.getPrincipal();
		@SuppressWarnings("unchecked")
		List<Authority> auth = (List<Authority>) user.getAuthorities();
		
		// Ubaci korisnika u trenutni security kontekst
		SecurityContextHolder.getContext().setAuthentication(authentication);
 
		String jwt = tokenUtils.generateToken(user.getEmail(), auth.get(0).getName()); // prijavljujemo se na sistem sa
																						// email adresom
		int expiresIn = tokenUtils.getExpiredIn();

		// Vrati token kao odgovor na uspesnu autentifikaciju
		return ResponseEntity.ok(new UserTokenStateDTO(jwt, (long) expiresIn));
	}

	@PreAuthorize("hasAnyRole('ROLE_ADMIN, ROLE_DOCTOR')")
	@GetMapping(value = "/current-user")
	public ResponseEntity<UserResDTO> currentUser() {
		User current = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return new ResponseEntity<>(userMapper.toResDTO(current), HttpStatus.OK);
	}

}
