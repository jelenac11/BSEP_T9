package com.tim9.admin.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tim9.admin.model.Hospital;

@Repository
public interface HospitalRepository extends JpaRepository<Hospital, Long>{

	Hospital findByName(String name);
	
	Hospital findByAdmin(String admin);

}
