package com.tim9.admin.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tim9.admin.model.VerificationToken;
import com.tim9.admin.repositories.VerificationTokenRepository;

@Service
public class VerificationTokenService {

	@Autowired
	private VerificationTokenRepository verificationTokenRepository;

	public VerificationToken findByToken(String token) {
		return verificationTokenRepository.findByToken(token);
	}

	public void saveToken(VerificationToken token) {
		verificationTokenRepository.save(token);
	}
}
