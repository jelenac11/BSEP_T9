package com.tim9.bolnica.services;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tim9.bolnica.dto.ReportDTO;
import com.tim9.bolnica.dto.response.ReportResponseDTO;
import com.tim9.bolnica.enums.LogSeverity;
import com.tim9.bolnica.repositories.AlarmAdminRepository;
import com.tim9.bolnica.repositories.LogRepository;

@Service
public class ReportService {

	@Autowired
	private LogRepository logRepo;
	@Autowired
	private AlarmAdminRepository alarmRepo;
	
	
	public ReportResponseDTO getReport(@Valid ReportDTO dto) {
		int alarms = alarmRepo.findAllByTimestampBetween(dto.getFrom(), dto.getTo()).size();
		int logs = logRepo.findAllByTimestampBetween(dto.getFrom(), dto.getTo()).size();
		int debug = logRepo.findAllBySeverityAndTimestampBetween(LogSeverity.DEBUG, dto.getFrom(), dto.getTo()).size();
		int informational = logRepo.findAllBySeverityAndTimestampBetween(LogSeverity.INFORMATIONAL, dto.getFrom(), dto.getTo()).size();
		int notice = logRepo.findAllBySeverityAndTimestampBetween(LogSeverity.NOTICE, dto.getFrom(), dto.getTo()).size();
		int warning = logRepo.findAllBySeverityAndTimestampBetween(LogSeverity.WARNING, dto.getFrom(), dto.getTo()).size();
		int error = logRepo.findAllBySeverityAndTimestampBetween(LogSeverity.ERROR, dto.getFrom(), dto.getTo()).size();
		int critical = logRepo.findAllBySeverityAndTimestampBetween(LogSeverity.CRITICAL, dto.getFrom(), dto.getTo()).size();
		int alert = logRepo.findAllBySeverityAndTimestampBetween(LogSeverity.ALERT, dto.getFrom(), dto.getTo()).size();
		int emergency = logRepo.findAllBySeverityAndTimestampBetween(LogSeverity.EMERGENCY, dto.getFrom(), dto.getTo()).size();
		return new ReportResponseDTO(dto.getFrom(), dto.getTo(), logs, debug, informational, notice, warning, error, critical, alert, emergency, alarms);
	}

}
