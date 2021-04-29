package com.tim9.bolnica.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tim9.bolnica.model.MessagesTemplateRule;

@Repository
public interface MessagesTemplateRuleRepository extends JpaRepository<MessagesTemplateRule, Long>{

}
