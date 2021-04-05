package com.tim9.admin.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tim9.admin.model.CSR;

@Repository
public interface CsrRepository extends JpaRepository<CSR, Long>{

	Page<CSR> findByVerifiedTrue(Pageable pageable);

}
