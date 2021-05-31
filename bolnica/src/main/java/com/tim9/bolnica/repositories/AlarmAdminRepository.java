package com.tim9.bolnica.repositories;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.tim9.bolnica.model.AdminAlarm;

@Repository
public interface AlarmAdminRepository extends MongoRepository<AdminAlarm, BigInteger>{

	Page<AdminAlarm> findAllByOrderByTimestampDesc(Pageable pageable);

	List<AdminAlarm> findAllByTimestampBetween(Date from, Date to);

	Page<AdminAlarm> findByHospitalOrderByTimestampDesc(String adminHospital, Pageable pageable);

	@Query("{$and:[" +
    "{$or:[ {'hospital':{ $eq: ?0 }}, {'hospital':{ $eq: ?1 }}]}" + 
    "]}")
	List<AdminAlarm> findByHospital(String string, String adminHospital);

	Page<AdminAlarm> findByIdInOrderByTimestampDesc(Pageable pageable, List<BigInteger> adminAlarms);

}
