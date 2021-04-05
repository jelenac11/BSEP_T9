package com.tim9.bolnica.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserResDTO {

	private Long id;
	
	private String email;
	
	private String password;
	
	private String firstName;
	
	private String lastName;
	
}
