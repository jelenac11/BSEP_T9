package com.tim9.bolnickiuredjaj.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.tim9.bolnickiuredjaj.dto.MessageDTO;

@Service
public class DeviceService {

	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy-HH:mm:ss");
	private static final long SLEEP_INTERVAL = 4000;
	private static final int NO_PATIENTS = 40;
	
	private final RestTemplate restTemplate = new RestTemplate();
	
	@PostConstruct
	public void init() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				monitorPatients();
			}
		}).start();
	}
	
	public void monitorPatients() {
		//while (true) {
		try {
			String text = String.format("Timestamp=%s patient=%d temperature=%d systolic=%d diastolic=%d heart_rate=%d oxygen_level=%d", 
					DATE_FORMAT.format(this.getTimestamp()), this.getRandomNumber(0, NO_PATIENTS), this.getTemperature(), this.getSystolicBloodPressure(), this.getDiastolicBloodPressure(), this.getHeartRate(), this.getOxygenLevel());
			this.restTemplate.postForEntity("https://localhost:8080/api/messages", new MessageDTO(text), String.class);
			Thread.sleep(SLEEP_INTERVAL);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		//}
	}
	
	private int getRandomNumber(int min, int max) {
	    return (int) ((Math.random() * (max - min)) + min);
	}
	
	private Date getTimestamp() {
		return new Date();
	}
	
	private int getTemperature() {
		return getRandomNumber(32, 46);
	}
	
	private int getSystolicBloodPressure() {
		return getRandomNumber(80, 211);
	}
	
	private int getDiastolicBloodPressure() {
		return getRandomNumber(50, 110);
	}
	
	private int getHeartRate() {
		return getRandomNumber(60, 161);
	}
	
	private int getOxygenLevel() {
		return getRandomNumber(60, 131);
	}
}
