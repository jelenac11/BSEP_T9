package com.tim9.admin.services;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tim9.admin.model.VerificationToken;
import com.tim9.admin.repositories.VerificationTokenRepository;

@Service
public class VerificationTokenService {

	@Autowired
	private VerificationTokenRepository verificationTokenRepository;
	
	@Autowired
	private UserService userService;

	public VerificationToken findByToken(String token) {
		return verificationTokenRepository.findByToken(token);
	}

	public void saveToken(VerificationToken token) {
		if (userService.findByEmail(token.getUser().getEmail()) == null) {
			throw new NoSuchElementException("User doesn't exist.");
		}
		verificationTokenRepository.save(token);
	}
}
