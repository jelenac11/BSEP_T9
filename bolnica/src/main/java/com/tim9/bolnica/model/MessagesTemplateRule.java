package com.tim9.bolnica.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "messages_rules")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MessagesTemplateRule {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
    private Long id;
	@Column
	private String messageRegexBefore;
	@Column
	private String messageRegexAfter;
	@Column
	private int timePeriod;
	@Column
	private String message;
}
