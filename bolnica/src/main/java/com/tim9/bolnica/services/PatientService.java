package com.tim9.bolnica.services;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tim9.bolnica.dto.response.PatientResponseDTO;
import com.tim9.bolnica.enums.BloodType;
import com.tim9.bolnica.enums.Gender;
import com.tim9.bolnica.model.Doctor;
import com.tim9.bolnica.model.Patient;
import com.tim9.bolnica.repositories.PatientRepository;
import com.tim9.bolnica.util.Auth0Util;

@Service
public class PatientService {
	
	private static final Logger logger = LoggerFactory.getLogger(PatientService.class);

	@Autowired
	private PatientRepository patientRepo;
	
	public List<PatientResponseDTO> findAll() {
		Doctor doctor = Auth0Util.getDoctor();
		List<Patient> all = patientRepo.findByHospitalAndDepartment(doctor.getHospital(), doctor.getDepartment());
		logger.info("Reading patients from database");
		return all.stream().map(PatientResponseDTO::new).collect(Collectors.toList());
	}

	@PostConstruct
	public void init() {
		Patient p1 = new Patient(1L, "1", "Ime1", "Prezime1", new Date(899157600000L), 165, 75, Gender.MALE, BloodType.A, "Covid-19, Alzheimer, Skin infection, Lactose intolerance", "Hospital 1", "Emergency");
		Patient p2 = new Patient(2L, "2", "Ime2", "Prezime2", new Date(899157600000L), 165, 75, Gender.MALE, BloodType.B, "Flu", "Hospital 1", "Mental health");
		Patient p3 = new Patient(3L, "3", "Ime3", "Prezime3", new Date(899157600000L), 165, 75, Gender.MALE, BloodType.B, "Flu", "Hospital 1", "Surgery");
		Patient p4 = new Patient(4L, "4", "Ime4", "Prezime4", new Date(899157600000L), 165, 75, Gender.MALE, BloodType.AB, "Diabetes, Hepatitis A, Alzheimer, Bronchitis", "Hospital 1", "Emergency");
		Patient p5 = new Patient(5L, "5", "Ime5", "Prezime5", new Date(899157600000L), 165, 75, Gender.MALE, BloodType.O, "Flu", "Hospital 1", "Mental health");
		Patient p6 = new Patient(6L, "6", "Ime6", "Prezime6", new Date(899157600000L), 165, 75, Gender.MALE, BloodType.A, "Sinus infection, Acne", "Hospital 1", "Emergency");
		Patient p7 = new Patient(7L, "7", "Ime7", "Prezime7", new Date(899157600000L), 165, 75, Gender.MALE, BloodType.B, "Allergie, Lactose intolerance, Bronchitis", "Hospital 1", "Emergency");
		Patient p8 = new Patient(8L, "8", "Ime8", "Prezime8", new Date(899157600000L), 165, 75, Gender.MALE, BloodType.AB, "Flu", "Hospital 1", "Surgery");
		Patient p9 = new Patient(9L, "9", "Ime9", "Prezime9", new Date(899157600000L), 165, 75, Gender.MALE, BloodType.A, "Allergie, Cancer, Dermatitis herpetiformis", "Hospital 1", "Emergency");
		Patient p10 = new Patient(10L, "10", "Ime10", "Prezime10", new Date(899157600000L), 165, 75, Gender.MALE, BloodType.B, "Covid-19, Dermatitis herpetiformis", "Hospital 1", "Emergency");
		
		Patient p11 = new Patient(11L, "11", "Ime11", "Prezime11", new Date(899157600000L), 165, 75, Gender.MALE, BloodType.A, "Flu", "Hospital 2", "Surgery");
		Patient p12 = new Patient(12L, "12", "Ime12", "Prezime12", new Date(899157600000L), 165, 75, Gender.MALE, BloodType.A, "Flu", "Hospital 2", "Emergency");
		Patient p13 = new Patient(13L, "13", "Ime13", "Prezime13", new Date(899157600000L), 165, 75, Gender.MALE, BloodType.A, "Flu", "Hospital 2", "Emergency");
		Patient p14 = new Patient(14L, "14", "Ime14", "Prezime14", new Date(899157600000L), 165, 75, Gender.MALE, BloodType.A, "Flu", "Hospital 2", "Mental health");
		Patient p15 = new Patient(15L, "15", "Ime15", "Prezime15", new Date(899157600000L), 165, 75, Gender.MALE, BloodType.A, "Flu", "Hospital 2", "Surgery");
		Patient p16 = new Patient(16L, "16", "Ime16", "Prezime16", new Date(899157600000L), 165, 75, Gender.MALE, BloodType.A, "Cancer, Lactose intolerance", "Hospital 1", "Emergency");
		Patient p17 = new Patient(17L, "17", "Ime17", "Prezime17", new Date(899157600000L), 165, 75, Gender.MALE, BloodType.A, "Flu", "Hospital 3", "Mental health");
		Patient p18 = new Patient(18L, "18", "Ime18", "Prezime18", new Date(899157600000L), 165, 75, Gender.MALE, BloodType.A, "Flu", "Hospital 3", "Emergency");
		Patient p19 = new Patient(19L, "19", "Ime19", "Prezime19", new Date(899157600000L), 165, 75, Gender.MALE, BloodType.A, "Lactose intolerance, Cancer, Hepatitis A, Cold sore", "Hospital 1", "Emergency");
		Patient p20 = new Patient(20L, "20", "Ime20", "Prezime20", new Date(899157600000L), 165, 75, Gender.MALE, BloodType.A, "Flu", "Hospital 3", "Surgery");
		this.patientRepo.save(p1);
		this.patientRepo.save(p2);
		this.patientRepo.save(p3);
		this.patientRepo.save(p4);
		this.patientRepo.save(p5);
		this.patientRepo.save(p6);
		this.patientRepo.save(p7);
		this.patientRepo.save(p8);
		this.patientRepo.save(p9);
		this.patientRepo.save(p10);
		this.patientRepo.save(p11);
		this.patientRepo.save(p12);
		this.patientRepo.save(p13);
		this.patientRepo.save(p14);
		this.patientRepo.save(p15);
		this.patientRepo.save(p16);
		this.patientRepo.save(p17);
		this.patientRepo.save(p18);
		this.patientRepo.save(p19);
		this.patientRepo.save(p20);
	}
}
