package com.tim9.bolnica.repositories;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import com.tim9.bolnica.model.Log;

@Repository
public interface LogRepository extends JpaRepository<Log, Long> {

	Page<Log> findByIdIn(Pageable pageable, List<Long> list);

	List<Log> findAllByOrderByTimestampDesc();

}
