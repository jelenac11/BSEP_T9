package com.tim9.bolnica.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tim9.bolnica.dto.response.PatientResponseDTO;
import com.tim9.bolnica.model.Patient;
import com.tim9.bolnica.repositories.PatientRepository;

@Service
public class PatientService {

	@Autowired
	private PatientRepository patientRepo;
	
	public List<PatientResponseDTO> findAll() {
		List<Patient> all = patientRepo.findAll();
		return all.stream().map(PatientResponseDTO::new).collect(Collectors.toList());
	}
}
