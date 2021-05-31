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
public class AlarmDoctorResponseDTO {
	
	private Long id;
	private Date timestamp;
	private String patient;
	private String message;
	private String hospital;
	private String department;
}
