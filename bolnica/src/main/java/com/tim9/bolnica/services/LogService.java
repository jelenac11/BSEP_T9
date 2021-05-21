package com.tim9.bolnica.services;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.apache.commons.io.input.ReversedLinesFileReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tim9.bolnica.dto.SearchLogDTO;
import com.tim9.bolnica.dto.response.LogResponseDTO;
import com.tim9.bolnica.enums.Auth0Type;
import com.tim9.bolnica.enums.LogFacility;
import com.tim9.bolnica.enums.LogSeverity;
import com.tim9.bolnica.exceptions.RequestException;
import com.tim9.bolnica.model.Auth0Log;
import com.tim9.bolnica.model.Config;
import com.tim9.bolnica.model.Log;
import com.tim9.bolnica.model.LogConfig;
import com.tim9.bolnica.repositories.LogRepository;
import com.tim9.bolnica.util.Auth0Util;
import com.tim9.bolnica.util.DateUtil;

@Service
public class LogService {

	@Autowired
	private LogRepository logRepository;
	@Autowired
	private AlarmService alarmService;

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
							readLogs(lc.getFilePath(), lc.getInterval(), lc.getRegexp());
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

	public Page<LogResponseDTO> findAll(Pageable pageable, SearchLogDTO search) throws RequestException {
		List<Log> response = logRepository.findAll();
		ArrayList<Log> filtered = this.filter(response, search);
		Page<Log> logPage = logRepository.findByIdIn(pageable,
				filtered.stream().map(Log::getId).collect(Collectors.toList()));
		ArrayList<LogResponseDTO> forReturn = new ArrayList<LogResponseDTO>();
		for (Log l : logPage.getContent()) {
			forReturn.add(new LogResponseDTO(l));
		}
		return new PageImpl<LogResponseDTO>(forReturn, pageable, logPage.getTotalElements());
	}

	private ArrayList<Log> filter(List<Log> logs, SearchLogDTO search) throws RequestException {
		if (search.getTo() != null && search.getFrom() != null) {
			if (search.getTo().compareTo(search.getFrom()) < 0) {
				throw new RequestException("Start date must be before end date!");
			}
		}
		if (!search.getSeverity().equals("")) {
			logs = logs.stream().filter(log -> log.getSeverity().toString().equals(search.getSeverity()))
					.collect(Collectors.toList());
		}
		if (!search.getFacility().equals("")) {
			logs = logs.stream().filter(log -> log.getFacility().toString().equals(search.getFacility()))
					.collect(Collectors.toList());
		}
		if (!search.getIp().equals("")) {
			logs = logs.stream().filter(log -> log.getIp().matches(search.getIp())).collect(Collectors.toList());
		}
		if (!search.getMessage().equals("")) {
			logs = logs.stream().filter(log -> log.getMessage().matches(search.getMessage()))
					.collect(Collectors.toList());
		}
		if (search.getTo() != null) {
			logs = logs.stream().filter(log -> log.getTimestamp().before(search.getTo())).collect(Collectors.toList());
		}
		if (search.getFrom() != null) {
			logs = logs.stream().filter(log -> log.getTimestamp().after(search.getFrom())).collect(Collectors.toList());
		}
		if (!search.getSource().equals("")) {
			logs = logs.stream().filter(log -> log.getSource().matches(search.getSource()))
					.collect(Collectors.toList());
		}
		return (ArrayList<Log>) logs;
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
				Log log = this.parseLogFromApplication(line);
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
	
	private Log parseLogFromApplication(String line) {
		String[] tokens = line.split(" ");

		Date date = null;
		try {
			date = DateUtil.parse(tokens[0] + " " + tokens[1]);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
		String source = tokens[4];
		String ip = "";
		String severity = tokens[2];
		if (severity.equals("INFO")) {
			severity = "INFORMATIONAL";
		} else if (severity.equals("WARN")) {
			severity = "WARNING";
		}
		String message = String.join(" ", Arrays.asList(tokens).subList(6, Arrays.asList(tokens).size()));

		return new Log(null, date, source, LogFacility.LOCAL0, LogSeverity.valueOf(severity), ip, message);
	}

	private void readLogs(String path, long interval, String regexp) throws Exception {
		Date threshold = new Date();
		while (true) {
			threshold = this.readSimulatorLogs(threshold, regexp, path);
			Thread.sleep(interval);
		}
	}

	private Date readSimulatorLogs(Date threshold, String regexp, String path) throws IOException {
		ArrayList<Log> logs = new ArrayList<>();
		ReversedLinesFileReader reader = new ReversedLinesFileReader(new File(path));
		Date newThreshold = new Date();
		try {
			String line = reader.readLine();
			boolean setNewThreshold = false;
			while (line != null) {
				Log log = this.parseLogFromSimulator(line);
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

	private Log parseLogFromSimulator(String line) {
		String[] tokens = line.split(" ");

		Date date = null;
		try {
			date = DateUtil.parse(tokens[0] + " " + tokens[1]);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
		String[] host = tokens[2].split("-");
		String source = host[0];
		String ip = host[1];
		String[] fS = tokens[3].split("_");
		LogFacility lf = LogFacility.valueOf(fS[0]);
		LogSeverity ls = LogSeverity.valueOf(fS[1]);
		String message = String.join(" ", Arrays.asList(tokens).subList(4, Arrays.asList(tokens).size()));

		return new Log(null, date, source, lf, ls, ip, message);
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
		if (threshold != null) {
			from = "&from=" + threshold;
		}
		ResponseEntity<String> result = restTemplate.exchange(
				"https://dev-lsmn3kc2.eu.auth0.com/api/v2/logs?fields=date,type,ip,user_name,hostname,log_id" + from,
				HttpMethod.GET, entity, String.class);

		ArrayList<Log> logs = new ArrayList<>();
		List<Auth0Log> list = Arrays.asList(new GsonBuilder().create().fromJson(result.getBody(), Auth0Log[].class));

		String newThreshold = threshold;
		boolean setNewThreshold = false;
		for (Auth0Log auth0Log : list) {
			if (!setNewThreshold) {
				newThreshold = auth0Log.getLog_id();
				setNewThreshold = true;
			}
			Log log = this.parseAut0Log(auth0Log);
			logs.add(log);
		}

		this.save(logs);
		return newThreshold;
	}

	private Log parseAut0Log(Auth0Log auth0Log) {
		String hostname = "Auth0";
		String ip = "";
		String message = Auth0Type.valueOf(auth0Log.getType()).getValue();
		if (auth0Log.getHostname() != null) {
			if (!auth0Log.getHostname().equals("")) {
				hostname = auth0Log.getHostname();	
			}
		}
		if (auth0Log.getUser_name() != null) {
			if (!auth0Log.getUser_name().equals("")) {
				message = message + " Username: " + auth0Log.getUser_name();
			}
		}
		if (auth0Log.getIp() != null) ip = auth0Log.getIp();
		return new Log(null, auth0Log.getDate(), hostname, LogFacility.AUTH, LogSeverity.INFORMATIONAL, ip, message);
	}

}
