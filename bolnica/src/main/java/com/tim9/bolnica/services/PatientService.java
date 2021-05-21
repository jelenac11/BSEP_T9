package com.tim9.bolnica.services;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tim9.bolnica.dto.response.PatientResponseDTO;
import com.tim9.bolnica.model.Patient;
import com.tim9.bolnica.repositories.PatientRepository;

@Service
public class PatientService {
	
	private static final Logger logger = LoggerFactory.getLogger(PatientService.class);

	@Autowired
	private PatientRepository patientRepo;
	
	public List<PatientResponseDTO> findAll() {
		List<Patient> all = patientRepo.findAll();
		logger.info("Reading patients from database");
		return all.stream().map(PatientResponseDTO::new).collect(Collectors.toList());
	}
}
