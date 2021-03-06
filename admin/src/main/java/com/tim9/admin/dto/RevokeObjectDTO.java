package com.tim9.admin.dto;

import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RevokeObjectDTO {

	@NotEmpty
	private String serialNumber;
	@NotEmpty
	private String reason;
}
