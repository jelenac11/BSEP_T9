package com.tim9.bolnica.services;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.tim9.bolnica.model.Config;
import com.tim9.bolnica.model.LogConfig;

@Service
public class ConfigurationService {
	
	private static final Logger logger = LoggerFactory.getLogger(ConfigurationService.class);
	
	@Autowired
	private LogService logService;

	public void saveConfig(LogConfig logConfig) throws JsonIOException, IOException {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		File file = ResourceUtils.getFile("classpath:configuration.json");
		Config config = gson.fromJson(new FileReader(file), Config.class);
		config.getLogConfigs().add(logConfig);
		Writer writer = new FileWriter(file.getAbsolutePath());
		gson.toJson(config, writer);
        writer.close();
        logger.info("New log configuration saved to log config file.");
        this.logService.addNewConfig(logConfig);
	}

}
