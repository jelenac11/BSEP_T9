package com.tim9.bolnica.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tim9.bolnica.model.Patient;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

	//https://www.hipaavault.com/uncategorized/choosing-the-right-type-of-encryption-for-hipaa-data/
	
	List<Patient> findByHospitalAndDepartment(String hospital, String department);

}
