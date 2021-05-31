package com.tim9.bolnica.dto;

import java.util.UUID;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PressureRuleDTO {

	private UUID id;
	@Min(1)
	@NotNull
	private int systolicFrom;
	@Min(1)
	@NotNull
	private int systolicTo;
	@Min(1)
	@NotNull
	private int diastolicFrom;
	@Min(1)
	@NotNull
	private int diastolicTo;
	@Min(1)
	@NotNull
	private int heartRateFrom;
	@Min(1)
	@NotNull
	private int heartRateTo;
	@NotBlank(message = "Alarm message must not be empty")
	private String message;
	@NotBlank(message = "Hospital must not be empty")
	private String hospital;
}
