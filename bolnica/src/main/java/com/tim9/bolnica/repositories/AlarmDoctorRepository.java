package com.tim9.bolnica.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tim9.bolnica.model.DoctorAlarm;

@Repository
public interface AlarmDoctorRepository extends JpaRepository<DoctorAlarm, Long> {
	
	Page<DoctorAlarm> findAllByOrderByTimestampDesc(Pageable pageable);

	Page<DoctorAlarm> findByPatientIdInOrderByTimestampDesc(Pageable pageable, List<Long> patientIds);
}
