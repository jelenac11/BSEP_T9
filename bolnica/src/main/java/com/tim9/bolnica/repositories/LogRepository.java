package com.tim9.bolnica.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tim9.bolnica.model.Log;

@Repository
public interface LogRepository extends JpaRepository<Log, Long> {

}
