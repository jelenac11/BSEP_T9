package com.tim9.bolnickiuredjaj.service;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class DeviceService {

	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static final long SLEEP_INTERVAL = 4000;
	private static final int NO_PATIENTS = 40;
	
	private final RestTemplate restTemplate = new RestTemplate();
	@Autowired
	private SignMessageService smService;
	
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
		while (true) {
			try {
				String text = String.format("%s patient=%d temperature=%.2f systolic=%d diastolic=%d heart_rate=%d oxygen_level=%d", 
						DATE_FORMAT.format(this.getTimestamp()), (int) this.getRandomNumber(1, NO_PATIENTS + 1), this.getTemperature(), this.getSystolicBloodPressure(), this.getDiastolicBloodPressure(), this.getHeartRate(), this.getOxygenLevel());
				byte[] message = smService.sign(text);
				this.restTemplate.postForEntity("https://localhost:8080/api/messages", message, String.class);
				Thread.sleep(SLEEP_INTERVAL);
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private double getRandomNumber(int min, int max) {
	    return ((Math.random() * (max - min)) + min);
	}
	
	private Date getTimestamp() {
		return new Date();
	}
	
	private double getTemperature() {
		return getRandomNumber(32, 46);
	}
	
	private int getSystolicBloodPressure() {
		return (int) getRandomNumber(80, 211);
	}
	
	private int getDiastolicBloodPressure() {
		return (int) getRandomNumber(50, 110);
	}
	
	private int getHeartRate() {
		return (int) getRandomNumber(60, 161);
	}
	
	private int getOxygenLevel() {
		return (int) getRandomNumber(0, 100);
	}
}
