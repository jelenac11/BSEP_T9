package com.tim9.admin.services;

import javax.validation.Valid;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.tim9.admin.model.LogConfig;

@Service
public class ConfigurationService {

	public void createConfig(@Valid LogConfig logConfig) {
		RestTemplate rs = new RestTemplate();
        String response = rs.postForEntity("https://localhost:8080/api/configuration", logConfig, String.class).getBody();
        System.out.println(response);
	}

}
