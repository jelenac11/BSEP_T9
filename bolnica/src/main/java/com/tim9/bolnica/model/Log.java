package com.tim9.bolnica.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.kie.api.definition.type.Role;

import com.tim9.bolnica.enums.LogFacility;
import com.tim9.bolnica.enums.LogSeverity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "logs")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Role(Role.Type.EVENT)
public class Log {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
    private Long id;
	
	@Column
	private Date timestamp;
	
	@Column
	@Enumerated(EnumType.STRING)
	private LogFacility facility;
	
	@Column
	@Enumerated(EnumType.STRING)
	private LogSeverity severity;
	
	@Column
	private String ip;
	
	@Column
	private String message;
	
	
}
