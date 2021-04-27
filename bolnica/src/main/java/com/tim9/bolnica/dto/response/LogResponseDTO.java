package com.tim9.bolnica.dto.response;

import java.util.Date;

import com.tim9.bolnica.enums.LogFacility;
import com.tim9.bolnica.enums.LogSeverity;
import com.tim9.bolnica.model.Log;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LogResponseDTO {

	private Long id;
	private Date timestamp;
	private LogFacility facility;
	private LogSeverity severity;
	private String ip;
	private String message;
	
	public LogResponseDTO(Log log) {
		this.id = log.getId();
		this.timestamp = log.getTimestamp();
		this.facility = log.getFacility();
		this.severity = log.getSeverity();
		this.ip = log.getIp();
		this.message = log.getMessage();
	}
}
