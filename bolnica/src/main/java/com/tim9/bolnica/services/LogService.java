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

import javax.annotation.PostConstruct;

import org.apache.commons.io.input.ReversedLinesFileReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import com.google.gson.Gson;
import com.tim9.bolnica.dto.response.LogResponseDTO;
import com.tim9.bolnica.enums.LogFacility;
import com.tim9.bolnica.enums.LogSeverity;
import com.tim9.bolnica.model.Config;
import com.tim9.bolnica.model.Log;
import com.tim9.bolnica.model.LogConfig;
import com.tim9.bolnica.repositories.LogRepository;
import com.tim9.bolnica.util.DateUtil;


@Service
public class LogService {

	@Autowired
	private LogRepository logRepository;
	
	@PostConstruct
	public void init() {
		try {
			Gson gson = new Gson();
            File file = ResourceUtils.getFile("classpath:configuration.json");
			Config config = gson.fromJson(new FileReader(file), Config.class);

			for (LogConfig lc: config.getLogConfigs()) {
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
		}
		catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public Page<LogResponseDTO> findAll(Pageable pageable) {
		Page<Log> logs = logRepository.findAll(pageable);
		ArrayList<LogResponseDTO> response = new ArrayList<LogResponseDTO>();
		for (Log l: logs) {
			response.add(new LogResponseDTO(l));
		}
		return new PageImpl<LogResponseDTO>(response, pageable, response.size());
	}

	public List<Log> save(List<Log> logs) {
		return this.logRepository.saveAll(logs);
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
            date = DateUtil.parse(tokens[0]);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
        String[] fS = tokens[1].split("_");
        LogFacility lf = LogFacility.valueOf(fS[0]);
        LogSeverity ls = LogSeverity.valueOf(fS[1]);
        String ip = tokens[2];
        String message = String.join(" ", Arrays.asList(tokens).subList(3, Arrays.asList(tokens).size()));


        return new Log(null, date, lf, ls, ip, message);
	}
}
