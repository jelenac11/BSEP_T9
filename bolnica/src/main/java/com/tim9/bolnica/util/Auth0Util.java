package com.tim9.bolnica.util;

import java.util.HashMap;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.client.RestTemplate;

import com.tim9.bolnica.model.Doctor;

import net.minidev.json.JSONObject;

public class Auth0Util {

	public static String apiToken; 
	
	@SuppressWarnings("unchecked")
	public static void getManagementApiToken() {
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

	    apiToken = result.get("access_token");
	}
	
	public static String getAdminHospital() {
		String user = ((Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getSubject();
		HttpHeaders headers = new HttpHeaders();
		getManagementApiToken();
        headers.set("Authorization", "Bearer " + apiToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> result = restTemplate.exchange("https://dev-lsmn3kc2.eu.auth0.com/api/v2/users/" + user + "?fields=user_metadata", HttpMethod.GET, entity, String.class);
        try {
        	return result.getBody().split("\"")[5];
        } catch(Exception e) {
        	return "";
        }
	}
	
	public static Doctor getDoctor() {
		String user = ((Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getSubject();
		HttpHeaders headers = new HttpHeaders();
		getManagementApiToken();
        headers.set("Authorization", "Bearer " + apiToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> result = restTemplate.exchange("https://dev-lsmn3kc2.eu.auth0.com/api/v2/users/" + user + "?fields=user_metadata", HttpMethod.GET, entity, String.class);
        try {
        	return new Doctor(result.getBody().split("\"")[5], result.getBody().split("\"")[9]);
        } catch(Exception e) {
        	return new Doctor();
        }
	}
	
}
