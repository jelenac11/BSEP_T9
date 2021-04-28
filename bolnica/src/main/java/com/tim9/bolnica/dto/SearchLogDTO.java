package com.tim9.bolnica.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SearchLogDTO {

	private Date from;
	private Date to;
	private String facility;
	private String severity;
	private String ip;
	private String message;
}
