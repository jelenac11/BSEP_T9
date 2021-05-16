package com.tim9.bolnica.controllers;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.tim9.bolnica.dto.SearchLogDTO;
import com.tim9.bolnica.dto.response.LogResponseDTO;
import com.tim9.bolnica.model.CustomPageImplementation;
import com.tim9.bolnica.services.LogService;

import net.minidev.json.JSONObject;

@RestController
@RequestMapping(value = "/api/logs")
@CrossOrigin(origins = "https://localhost:4205", maxAge = 3600, allowedHeaders = "*")
public class LogController {

	@Autowired
	private LogService logService;
	
	@PostMapping(value = "/by-page")
	public ResponseEntity<?> getLogs(Pageable pageable, @RequestBody SearchLogDTO searchLog) {
		HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + getManagementApiToken());
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> result = restTemplate.exchange("https://dev-lsmn3kc2.eu.auth0.com/api/v2/users/auth0|608343a7669922006e07333e?fields=user_metadata", HttpMethod.GET, entity, String.class);
        System.out.println(result.getBody().split("\"")[5]);
        System.out.println(result.getBody().split("\"")[9]);
		try {
			return new ResponseEntity<>(createCustomPage(logService.findAll(pageable, searchLog)), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	private CustomPageImplementation<LogResponseDTO> createCustomPage(Page<LogResponseDTO> page) {
		return new CustomPageImplementation<>(page.getContent(), page.getNumber(), page.getSize(),
				page.getTotalElements(), null, page.isLast(), page.getTotalPages(), null, page.isFirst(),
				page.getNumberOfElements());
	}
	
	public String getManagementApiToken() {
	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON);

	    JSONObject requestBody = new JSONObject();
	    requestBody.put("client_id", "tsCd8kuR6ELNl5VvNdMWsZbmdu7bQiVb");
	    requestBody.put("client_secret", "lPeVJRoqDJAMsCqiDAC4AZuxoK2JTUURMwd9FtVg9_Lk13ED4LS8gm6yCmpmbUW0");
	    requestBody.put("audience", "https://dev-lsmn3kc2.eu.auth0.com/api/v2/");
	    requestBody.put("grant_type", "client_credentials"); 

	    HttpEntity<String> request = new HttpEntity<String>(requestBody.toString(), headers);

	    RestTemplate restTemplate = new RestTemplate();
	    HashMap<String, String> result = restTemplate
	      .postForObject("https://dev-lsmn3kc2.eu.auth0.com/oauth/token", request, HashMap.class);

	    return result.get("access_token");
	}
	
}
