package com.tim9.bolnickiuredjaj;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.tim9.bolnickiuredjaj.service.DeviceService;

@SpringBootApplication
public class BolnickiUredjajApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext appContext = SpringApplication.run(BolnickiUredjajApplication.class, args);
		
		DeviceService device = appContext.getBean(DeviceService.class);
	    device.monitorPatients();
	}

}
