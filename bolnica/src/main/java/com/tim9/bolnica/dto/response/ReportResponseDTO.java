package com.tim9.bolnica.dto.response;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReportResponseDTO {
	
	private Date from;
	private Date to;
	private int logs;
	private int debug;
	private int trace;
	private int informational;
	private int notice;
	private int warning;
	private int error;
	private int critical;
	private int alert;
	private int emergency;
	private int alarms;

}
