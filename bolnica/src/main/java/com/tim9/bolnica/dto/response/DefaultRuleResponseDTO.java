package com.tim9.bolnica.dto.response;

import com.tim9.bolnica.model.DefaultRule;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DefaultRuleResponseDTO {
	
	private Long id;
	private String description;
	
	public DefaultRuleResponseDTO(DefaultRule dr) {
		this.id = dr.getId();
		this.description = dr.getDescription();
	}

}
