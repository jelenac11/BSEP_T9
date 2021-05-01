package com.tim9.bolnica.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.kie.api.definition.type.Expires;
import org.kie.api.definition.type.Role;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "messages")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Role(Role.Type.EVENT)
@Expires("5m")
public class Message {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
    private Long id;
	
	@Column
	private Date timestamp;
	
	@Column
	private Long patientId;
	
	@Column
	private double temperature;
	
	@Column
	private int systolic;
	
	@Column
	private int diastolic;
	
	@Column
	private int heartRate;
	
	@Column
	private int oxygenLevel;
	
}
