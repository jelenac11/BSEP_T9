package com.tim9.bolnica.dto.response;


import com.tim9.bolnica.model.MessagesTemplateRule;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MessagesTRuleResponseDTO {
	
	private String messageRegexBefore;
	private String messageRegexAfter;
	private int timePeriod;
	private String message;
	
	public MessagesTRuleResponseDTO(MessagesTemplateRule mtr) {
		this.messageRegexBefore = mtr.getMessageRegexBefore();
		this.messageRegexAfter = mtr.getMessageRegexAfter();
		this.timePeriod = mtr.getTimePeriod();
		this.message = mtr.getMessage();
	}
}
