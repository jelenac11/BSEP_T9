package com.tim9.bolnica.dto;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MessageDTO {

	@NotBlank(message = "Text cannot be blank")
	private String text;
	
	@NotBlank(message = "Message must be signed")
	private String signature;
}
