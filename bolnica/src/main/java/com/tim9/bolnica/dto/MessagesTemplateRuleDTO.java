package com.tim9.bolnica.dto;


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
public class MessagesTemplateRuleDTO {

	@NotBlank(message = "Message regex before must not be empty")
	private String messageRegexBefore;
	@NotBlank(message = "Message regex after must not be empty")
	private String messageRegexAfter;
	@Min(1)
	private int timePeriod;
	@NotBlank(message = "Alarm message must not be empty")
	private String message;
}
