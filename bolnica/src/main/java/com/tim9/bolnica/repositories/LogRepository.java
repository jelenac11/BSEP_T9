package com.tim9.bolnica.repositories;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.tim9.bolnica.enums.LogSeverity;
import com.tim9.bolnica.model.Log;

@Repository
public interface LogRepository extends MongoRepository<Log, BigInteger> {

	List<Log> findAll();

	List<Log> findAllByTimestampBetween(Date from, Date to);

	List<Log> findAllBySeverityAndTimestampBetween(LogSeverity string, Date from, Date to);

	Page<Log> findByIdIn(Pageable pageable, List<BigInteger> collect);
	
    int countBySeverityEqualsAndTimestampBetween(LogSeverity severity, Date startDate, Date endDate);

}
