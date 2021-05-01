package com.tim9.bolnica.dto.response;

import java.util.Date;

import com.tim9.bolnica.model.Message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MessageResponseDTO {

	private Date timestamp;
	private String patient;
	private double temperature;
	private int systolic;
	private int diastolic;
	private int heartRate;
	private int oxygenLevel;
	
	public MessageResponseDTO(Message m) {
		this.timestamp = m.getTimestamp();
		this.temperature = m.getTemperature();
		this.systolic = m.getSystolic();
		this.diastolic = m.getDiastolic();
		this.heartRate = m.getHeartRate();
		this.oxygenLevel = m.getOxygenLevel();
	}
}
