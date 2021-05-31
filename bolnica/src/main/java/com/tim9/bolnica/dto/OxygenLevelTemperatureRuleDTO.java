package com.tim9.bolnica.dto;

import java.util.UUID;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OxygenLevelTemperatureRuleDTO {

	private UUID id;
	@Min(1)
	private double temperature;
	@Min(1)
	@Max(100)
	@NotNull
	private int oxygenLevel;
	@NotBlank(message = "Alarm message must not be empty")
	private String message;
	@NotBlank(message = "Hospital must not be empty")
	private String hospital;
}
