package com.tim9.bolnica.services;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.io.input.ReversedLinesFileReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.tim9.bolnica.dto.SearchLogDTO;
import com.tim9.bolnica.dto.response.LogResponseDTO;
import com.tim9.bolnica.exceptions.RequestException;
import com.tim9.bolnica.model.Config;
import com.tim9.bolnica.model.Log;
import com.tim9.bolnica.model.LogConfig;
import com.tim9.bolnica.repositories.LogRepository;
import com.tim9.bolnica.util.ApplicationLogParser;
import com.tim9.bolnica.util.Auth0Parser;
import com.tim9.bolnica.util.Auth0Util;
import com.tim9.bolnica.util.LogParser;
import com.tim9.bolnica.util.SimulatorLogParser;

@Service
public class LogService {

	@Autowired
	private LogRepository logRepository;
	
	@Autowired
	private AlarmService alarmService;
	
	private static LogParser applicationLogParser = new ApplicationLogParser();
	private static LogParser simulatorLogParser = new SimulatorLogParser();
	private static LogParser auth0LogParser = new Auth0Parser();

	@PostConstruct
	public void init() {
		try {
			Auth0Util.getManagementApiToken();
			Gson gson = new Gson();
			File file = ResourceUtils.getFile("classpath:configuration.json");
			Config config = gson.fromJson(new FileReader(file), Config.class);
			for (LogConfig lc : config.getLogConfigs()) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							readLogs(lc.getFilePath(), lc.getInterval(), lc.getRegexp(), lc.getHospital());
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}).start();
			}
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						readAuth0Logs();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}).start();
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						readApplicationLogs();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}).start();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public void addNewConfig(LogConfig lc) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					readLogs(lc.getFilePath(), lc.getInterval(), lc.getRegexp(), lc.getHospital());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	public Page<LogResponseDTO> findAll(Pageable pageable, SearchLogDTO search) throws RequestException {
		String hospital = Auth0Util.getAdminHospital();
		Page<Log> logPage = logRepository.searchLogs(search.getFrom(), search.getTo(), search.getIp(), search.getFacility(), search.getSeverity(), search.getMessage(), search.getSource(), hospital, pageable);
		ArrayList<LogResponseDTO> forReturn = new ArrayList<LogResponseDTO>();
		for (Log l : logPage.getContent()) {
			forReturn.add(new LogResponseDTO(l));
		}
		return new PageImpl<LogResponseDTO>(forReturn, pageable, logPage.getTotalElements());
	}

	public void save(List<Log> logs) {
		this.logRepository.saveAll(logs);
		this.alarmService.checkAlarms(logs);
	}
	
	protected void readApplicationLogs() throws IOException, InterruptedException {
		Date threshold = new Date();
		while (true) {
			threshold = this.readAppLogs(threshold, "log4j/target/baeldung-logback.log");
			Thread.sleep(5000);
		}
	}
	
	private Date readAppLogs(Date threshold, String path) throws IOException {
		ArrayList<Log> logs = new ArrayList<>();
		ReversedLinesFileReader reader = new ReversedLinesFileReader(new File(path));
		Date newThreshold = new Date();
		try {
			String line = reader.readLine();
			boolean setNewThreshold = false;
			while (line != null) {
				Log log = applicationLogParser.parse(line);
				if (!setNewThreshold) {
					newThreshold = log.getTimestamp();
					setNewThreshold = true;
				}
				if (!log.getTimestamp().after(threshold)) {
					break;
				}
				logs.add(log);
				line = reader.readLine();
			}
		} finally {
			reader.close();
		}
		Collections.reverse(logs);
		this.save(logs);
		return newThreshold;
	}

	private void readLogs(String path, long interval, String regexp, String hospital) throws Exception {
		Date threshold = new Date();
		while (true) {
			threshold = this.readSimulatorLogs(threshold, regexp, path, hospital);
			Thread.sleep(interval);
		}
	}

	private Date readSimulatorLogs(Date threshold, String regexp, String path, String hospital) throws IOException {
		ArrayList<Log> logs = new ArrayList<>();
		ReversedLinesFileReader reader = new ReversedLinesFileReader(new File(path));
		Date newThreshold = new Date();
		try {
			String line = reader.readLine();
			boolean setNewThreshold = false;
			while (line != null) {
				Log log = simulatorLogParser.parse(line);
				log.setHospital(hospital);
				if (!setNewThreshold) {
					newThreshold = log.getTimestamp();
					setNewThreshold = true;
				}
				if (!log.getTimestamp().after(threshold)) {
					break;
				}
				if (log.getMessage().matches(regexp)) {
					logs.add(log);
				}

				line = reader.readLine();
			}
		} finally {
			reader.close();
		}
		Collections.reverse(logs);
		this.save(logs);
		return newThreshold;
	}
	
	private void readAuth0Logs() throws Exception {
		String threshold = null;
		while (true) {
			threshold = this.readAuth0(threshold);
			Thread.sleep(5000);
		}
	}

	private String readAuth0(String threshold) {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "Bearer " + Auth0Util.apiToken);
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<String>(headers);
		RestTemplate restTemplate = new RestTemplate();

		String from = "";
		//System.out.println(threshold);
		if (threshold != null) {
			from = "&from=" + threshold;
		}
		ResponseEntity<String> result = restTemplate.exchange(
				"https://dev-lsmn3kc2.eu.auth0.com/api/v2/logs?fields=date,type,ip,user_name,hostname,log_id" + from,
				HttpMethod.GET, entity, String.class);

		ArrayList<Log> logs = new ArrayList<>();
		String newThreshold = threshold;
		try {
			String[] list = result.getBody().substring(2, result.getBody().length() - 2).split("\\},\\{");
			boolean setNewThreshold = false;
			for (String auth0Log : list) {
				if (!setNewThreshold) {
					//System.out.println(auth0Log);
					newThreshold = auth0Log.split("log_id")[1].split("\"")[2];
					//System.out.println("NEWWWWWWWWW" + newThreshold);
					setNewThreshold = true;
				}
				Log log = auth0LogParser.parse(auth0Log);
				logs.add(log);
			}

			this.save(logs);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return newThreshold;
	}

}
