package com.tim9.bolnica.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tim9.bolnica.model.Authority;
import com.tim9.bolnica.repositories.AuthorityRepository;

@Service
public class AuthorityService {

	@Autowired
    private AuthorityRepository authorityRepository;
	
    public List<Authority> findById(Long id) {
        Optional<Authority> auth = this.authorityRepository.findById(id);
        List<Authority> auths = new ArrayList<>();
        if (auth.isPresent()) {
        	auths.add(auth.get());
        }
        return auths;
    }

    public List<Authority> findByName(String name) {
        Authority auth = this.authorityRepository.findByName(name);
        List<Authority> auths = new ArrayList<>();
        if (auth != null) {
        	 auths.add(auth);
        }
        return auths;
    }
    
    public Authority create(Authority entity) throws Exception {
		authorityRepository.save(entity);
		return entity;
	}
	
}
