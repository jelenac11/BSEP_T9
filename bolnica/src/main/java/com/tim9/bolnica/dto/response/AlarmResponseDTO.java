package com.tim9.bolnica.dto.response;

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
	
	private Long id;
	private Date timestamp;
	private String message;
	
	public AlarmResponseDTO(AdminAlarm a) {
		this.id = a.getId();
		this.timestamp = a.getTimestamp();
		this.message = a.getMessage();
	}

}
