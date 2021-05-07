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
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.maven.shared.invoker.MavenInvocationException;
import org.drools.template.ObjectDataCompiler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.tim9.bolnica.dto.TemperatureRuleDTO;
import com.tim9.bolnica.dto.MessagesTemplateRuleDTO;
import com.tim9.bolnica.dto.OxygenLevelRuleDTO;
import com.tim9.bolnica.dto.PressureRuleDTO;
import com.tim9.bolnica.dto.SeverityTemplateRuleDTO;
import com.tim9.bolnica.dto.response.DefaultRuleResponseDTO;
import com.tim9.bolnica.dto.response.MessagesTRuleResponseDTO;
import com.tim9.bolnica.dto.response.SeverityTRuleResponseDTO;
import com.tim9.bolnica.exceptions.RequestException;
import com.tim9.bolnica.model.DefaultRule;
import com.tim9.bolnica.model.MessagesTemplateRule;
import com.tim9.bolnica.model.SeverityTemplateRule;
import com.tim9.bolnica.repositories.DefaultRulesRepository;
import com.tim9.bolnica.repositories.MessagesTemplateRuleRepository;
import com.tim9.bolnica.repositories.SeverityTemplateRuleRepository;
import com.tim9.bolnica.util.RuleBasedSystemUtil;


@Service
public class RulesService {

	@Autowired
	private DefaultRulesRepository defaultRepo;
	
	@Autowired
	private MessagesTemplateRuleRepository mrtRepo;
	
	@Autowired
	private SeverityTemplateRuleRepository strRepo;
	
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
	
	public List<DefaultRuleResponseDTO> findAllDefault() {
		List<DefaultRule> defaultRules = defaultRepo.findAll();
		List<DefaultRuleResponseDTO> resp = defaultRules.stream().map(DefaultRuleResponseDTO::new).collect(Collectors.toList());
		return resp;
	}

	public List<MessagesTRuleResponseDTO> findAllMTR() {
		List<MessagesTemplateRule> mtRules = mrtRepo.findAll();
		List<MessagesTRuleResponseDTO> resp = mtRules.stream().map(MessagesTRuleResponseDTO::new).collect(Collectors.toList());
		return resp;
	}

	public List<SeverityTRuleResponseDTO> findAllSTR() {
		List<SeverityTemplateRule> mtRules = strRepo.findAll();
		List<SeverityTRuleResponseDTO> resp = mtRules.stream().map(SeverityTRuleResponseDTO::new).collect(Collectors.toList());
		return resp;
	}

	public void addSTR(@Valid SeverityTemplateRuleDTO dto) throws IOException, MavenInvocationException {
		strRepo.save(new SeverityTemplateRule(null, dto.getSeverity(), dto.getTimePeriod(), dto.getCount(), dto.getMessage()));
		List<SeverityTemplateRuleDTO> data = new ArrayList<>();
        data.add(dto);
        InputStream template = new FileInputStream(severityTemplate);
        String drl = (new ObjectDataCompiler()).compile(data, template);
        UUID id = UUID.randomUUID();
        Files.write(Paths.get(severityDRL + id.toString() + ".drl"), drl.getBytes(), StandardOpenOption.CREATE);
        RuleBasedSystemUtil.mavenCleanAndInstall();
	}

	public void addMTR(@Valid MessagesTemplateRuleDTO dto) throws IOException, MavenInvocationException {
		mrtRepo.save(new MessagesTemplateRule(null, dto.getMessageRegexBefore(), dto.getMessageRegexAfter(), dto.getTimePeriod(), dto.getMessage()));
		List<MessagesTemplateRuleDTO> data = new ArrayList<>();
        data.add(dto);
        InputStream template = new FileInputStream(messagesTemplate);
        String drl = (new ObjectDataCompiler()).compile(data, template);
        UUID id = UUID.randomUUID();
        Files.write(Paths.get(messagesDRL + id.toString() + ".drl"), drl.getBytes(), StandardOpenOption.CREATE);
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
        	templatePath = lowTemperatureTemplate;
        	drlPath = lowTemperatureDRL;
        } else {
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
        RuleBasedSystemUtil.mavenCleanAndInstall();	
		
	}

	public void addPressureRule(@Valid PressureRuleDTO dto) throws IOException, MavenInvocationException, RequestException {
		if (dto.getSystolicFrom() > dto.getSystolicTo() || dto.getDiastolicFrom() > dto.getDiastolicTo() || dto.getHeartRateFrom() > dto.getHeartRateTo()) {
			throw new RequestException("Invalid params for creating pressure rule!");
		}
		List<PressureRuleDTO> data = new ArrayList<>();
		UUID id = UUID.randomUUID();
		dto.setId(id);
        data.add(dto);
        InputStream template = new FileInputStream(pressureTemplate);
        String drl = (new ObjectDataCompiler()).compile(data, template);
        Files.write(Paths.get(pressureDRL + id.toString() + ".drl"), drl.getBytes(), StandardOpenOption.CREATE);
        RuleBasedSystemUtil.mavenCleanAndInstall();	
	}

}
