package com.tim9.bolnica.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tim9.bolnica.model.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long>{

	Page<Message> findByIdIn(Pageable pageable, List<Long> collect);

	List<Message> findAllByOrderByTimestampDesc();

	List<Message> findByPatientIdInOrderByTimestampDesc(List<Long> patientIds);

}
