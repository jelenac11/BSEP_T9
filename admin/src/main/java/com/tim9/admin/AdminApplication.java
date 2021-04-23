package com.tim9.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class AdminApplication {
    
	public static void main(String[] args) throws Exception {
		ConfigurableApplicationContext appContext = SpringApplication.run(AdminApplication.class, args);
	    
	   // CertificateService cert = appContext.getBean(CertificateService.class);
	    //cert.deleteAllExceptRoot();
	}

}