package com.tim9.bolnica.dto.response;

import com.tim9.bolnica.model.SeverityTemplateRule;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SeverityTRuleResponseDTO {

	private String severity;
	private int timePeriod;
	private int count;
	private String message;
	
	public SeverityTRuleResponseDTO(SeverityTemplateRule str) {
		this.severity = str.getSeverity();
		this.timePeriod = str.getTimePeriod();
		this.count = str.getCount();
		this.message = str.getMessage();
	}
}
