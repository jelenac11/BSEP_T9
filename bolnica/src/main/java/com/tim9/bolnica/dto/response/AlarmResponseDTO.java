package com.tim9.bolnica.dto.response;

import java.math.BigInteger;
import java.util.Date;

import com.tim9.bolnica.model.AdminAlarm;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AlarmResponseDTO {
	
	private BigInteger id;
	private Date timestamp;
	private String message;
	private String hospital;
	
	public AlarmResponseDTO(AdminAlarm a) {
		this.id = a.getId();
		this.timestamp = a.getTimestamp();
		this.message = a.getMessage();
		this.hospital = a.getHospital();
	}

}
