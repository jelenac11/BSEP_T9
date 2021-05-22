package com.tim9.bolnica.services;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

import org.apache.maven.shared.invoker.MavenInvocationException;
import org.drools.template.ObjectDataCompiler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.tim9.bolnica.dto.TemperatureRuleDTO;
import com.tim9.bolnica.dto.MessagesTemplateRuleDTO;
import com.tim9.bolnica.dto.OxygenLevelRuleDTO;
import com.tim9.bolnica.dto.OxygenLevelTemperatureRuleDTO;
import com.tim9.bolnica.dto.PressureRuleDTO;
import com.tim9.bolnica.dto.SeverityTemplateRuleDTO;
import com.tim9.bolnica.exceptions.RequestException;
import com.tim9.bolnica.util.RuleBasedSystemUtil;


@Service
public class RulesService {
	
	private static final Logger logger = LoggerFactory.getLogger(RulesService.class);
	
	@Value("${rules.template.severity}")
    private String severityTemplate;
	
	@Value("${rules.drl.severity}")
    private String severityDRL;
	
	@Value("${rules.template.messages}")
    private String messagesTemplate;
	
	@Value("${rules.drl.messages}")
    private String messagesDRL;
	
	@Value("${rules.template.lowT}")
    private String lowTemperatureTemplate;
	
	@Value("${rules.drl.lowT}")
    private String lowTemperatureDRL;
	
	@Value("${rules.template.highT}")
    private String highTemperatureTemplate;
	
	@Value("${rules.drl.highT}")
    private String highTemperatureDRL;
	
	@Value("${rules.template.lowOL}")
    private String lowOxygenLevelTemplate;
	
	@Value("${rules.drl.lowOL}")
    private String lowOxygenLevelDRL;
	
	@Value("${rules.template.pressure}")
    private String pressureTemplate;
	
	@Value("${rules.drl.pressure}")
    private String pressureDRL;
	
	@Value("${rules.template.oxygentemperature}")
    private String oxygenTemperatureTemplate;
	
	@Value("${rules.drl.oxygentemperature}")
    private String oxygenTemperatureDRL;
	
	public void addSTR(@Valid SeverityTemplateRuleDTO dto) throws IOException, MavenInvocationException {
		List<SeverityTemplateRuleDTO> data = new ArrayList<>();
        data.add(dto);
        InputStream template = new FileInputStream(severityTemplate);
        String drl = (new ObjectDataCompiler()).compile(data, template);
        UUID id = UUID.randomUUID();
        dto.setId(id);
        Files.write(Paths.get(severityDRL + id.toString() + ".drl"), drl.getBytes(), StandardOpenOption.CREATE);
        logger.info("New severity template rule created");
        RuleBasedSystemUtil.mavenCleanAndInstall();
	}
	
	public void addMTR(@Valid MessagesTemplateRuleDTO dto) throws IOException, MavenInvocationException {
		List<MessagesTemplateRuleDTO> data = new ArrayList<>();
        data.add(dto);
        InputStream template = new FileInputStream(messagesTemplate);
        String drl = (new ObjectDataCompiler()).compile(data, template);
        UUID id = UUID.randomUUID();
        dto.setId(id);
        Files.write(Paths.get(messagesDRL + id.toString() + ".drl"), drl.getBytes(), StandardOpenOption.CREATE);
        logger.info("New messages template rule created");
        RuleBasedSystemUtil.mavenCleanAndInstall();	
	}

	public void addT(@Valid TemperatureRuleDTO dto, boolean low) throws IOException, MavenInvocationException {
		List<TemperatureRuleDTO> data = new ArrayList<>();
		UUID id = UUID.randomUUID();
		dto.setId(id);
        data.add(dto);
        String templatePath;
        String drlPath;
        if (low) {
        	logger.info("New low temperature template rule created");
        	templatePath = lowTemperatureTemplate;
        	drlPath = lowTemperatureDRL;
        } else {
        	logger.info("New high temperature template rule created");
        	templatePath = highTemperatureTemplate;
        	drlPath = highTemperatureDRL;
        }
        
        InputStream template = new FileInputStream(templatePath);
        String drl = (new ObjectDataCompiler()).compile(data, template);
        Files.write(Paths.get(drlPath + id.toString() + ".drl"), drl.getBytes(), StandardOpenOption.CREATE);
        RuleBasedSystemUtil.mavenCleanAndInstall();		
	}

	public void addOLR(@Valid OxygenLevelRuleDTO dto) throws IOException, MavenInvocationException {
		List<OxygenLevelRuleDTO> data = new ArrayList<>();
		UUID id = UUID.randomUUID();
		dto.setId(id);
        data.add(dto);
        InputStream template = new FileInputStream(lowOxygenLevelTemplate);
        String drl = (new ObjectDataCompiler()).compile(data, template);
        Files.write(Paths.get(lowOxygenLevelDRL + id.toString() + ".drl"), drl.getBytes(), StandardOpenOption.CREATE);
        logger.info("New low oxygen level template rule created");
        RuleBasedSystemUtil.mavenCleanAndInstall();	
		
	}

	public void addOxygenLevelTemperatureRule(@Valid OxygenLevelTemperatureRuleDTO dto) throws IOException, MavenInvocationException {
		List<OxygenLevelTemperatureRuleDTO> data = new ArrayList<>();
		UUID id = UUID.randomUUID();
		dto.setId(id);
        data.add(dto);
        InputStream template = new FileInputStream(oxygenTemperatureTemplate);
        String drl = (new ObjectDataCompiler()).compile(data, template);
        Files.write(Paths.get(oxygenTemperatureDRL + id.toString() + ".drl"), drl.getBytes(), StandardOpenOption.CREATE);
        logger.info("New oxygen level and temperature template rule created");
        RuleBasedSystemUtil.mavenCleanAndInstall();	
	}
	
	public void addPressureRule(@Valid PressureRuleDTO dto) throws IOException, MavenInvocationException, RequestException {
		if (dto.getSystolicFrom() > dto.getSystolicTo() || dto.getDiastolicFrom() > dto.getDiastolicTo() || dto.getHeartRateFrom() > dto.getHeartRateTo()) {
			logger.info("Invalid params for creating pressure rule");
			throw new RequestException("Invalid params for creating pressure rule!");
		}
		List<PressureRuleDTO> data = new ArrayList<>();
		UUID id = UUID.randomUUID();
		dto.setId(id);
        data.add(dto);
        InputStream template = new FileInputStream(pressureTemplate);
        String drl = (new ObjectDataCompiler()).compile(data, template);
        Files.write(Paths.get(pressureDRL + id.toString() + ".drl"), drl.getBytes(), StandardOpenOption.CREATE);
        logger.info("New pressure template rule created");
        RuleBasedSystemUtil.mavenCleanAndInstall();	
	}
	
}
