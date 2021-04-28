package com.tim9.bolnica.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tim9.bolnica.model.AdminAlarm;

@Repository
public interface AlarmAdminRepository extends JpaRepository<AdminAlarm, Long>{

	Page<AdminAlarm> findAllByOrderByTimestampDesc(Pageable pageable);

}
