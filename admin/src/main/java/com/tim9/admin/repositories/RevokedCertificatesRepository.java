package com.tim9.admin.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tim9.admin.model.RevokedCertificate;

@Repository
public interface RevokedCertificatesRepository extends JpaRepository<RevokedCertificate, Long>{

}
