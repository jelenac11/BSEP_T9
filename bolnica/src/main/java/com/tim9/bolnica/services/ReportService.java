package com.tim9.bolnica.services;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tim9.bolnica.dto.ReportDTO;
import com.tim9.bolnica.dto.response.ReportResponseDTO;
import com.tim9.bolnica.enums.LogSeverity;
import com.tim9.bolnica.repositories.AlarmAdminRepository;
import com.tim9.bolnica.repositories.LogRepository;

@Service
public class ReportService {
	
	private static final Logger logger = LoggerFactory.getLogger(ReportService.class);

	@Autowired
	private LogRepository logRepo;
	
	@Autowired
	private AlarmAdminRepository alarmRepo;
	
	
	public ReportResponseDTO getReport(@Valid ReportDTO dto) {
		int alarms = alarmRepo.findAllByTimestampBetween(dto.getFrom(), dto.getTo()).size();
		int logs = logRepo.findAllByTimestampBetween(dto.getFrom(), dto.getTo()).size();
		int debug = logRepo.findAllBySeverityAndTimestampBetween(LogSeverity.DEBUG, dto.getFrom(), dto.getTo()).size();
		int informational = logRepo.countBySeverityEqualsAndTimestampBetween(LogSeverity.INFORMATIONAL, dto.getFrom(), dto.getTo());
		int notice = logRepo.countBySeverityEqualsAndTimestampBetween(LogSeverity.NOTICE, dto.getFrom(), dto.getTo());
		int warning = logRepo.countBySeverityEqualsAndTimestampBetween(LogSeverity.WARNING, dto.getFrom(), dto.getTo());
		int error = logRepo.countBySeverityEqualsAndTimestampBetween(LogSeverity.ERROR, dto.getFrom(), dto.getTo());
		int critical = logRepo.countBySeverityEqualsAndTimestampBetween(LogSeverity.CRITICAL, dto.getFrom(), dto.getTo());
		int alert = logRepo.countBySeverityEqualsAndTimestampBetween(LogSeverity.ALERT, dto.getFrom(), dto.getTo());
		int emergency = logRepo.countBySeverityEqualsAndTimestampBetween(LogSeverity.EMERGENCY, dto.getFrom(), dto.getTo());
		logger.info("New log report created");
		return new ReportResponseDTO(dto.getFrom(), dto.getTo(), logs, 0, debug, informational, notice, warning, error, critical, alert, emergency, alarms);
	}

}
