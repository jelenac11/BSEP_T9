package com.tim9.bolnica.dto;

import java.util.UUID;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SeverityTemplateRuleDTO {
	
	private UUID id;
	@NotBlank(message = "Severity before must not be empty")
	private String severity;
	@Min(1)
	private int timePeriod;
	@Min(1)
	private int count;
	@NotBlank(message = "Alarm message must not be empty")
	private String message;
}
