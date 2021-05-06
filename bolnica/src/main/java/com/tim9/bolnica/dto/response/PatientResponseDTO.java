package com.tim9.bolnica.dto.response;

import com.tim9.bolnica.model.Patient;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PatientResponseDTO {
	
	private Long id;
	private String firstName;
	private String lastName;
	
	public PatientResponseDTO(Patient p) {
		this.id = p.getId();
		this.firstName = p.getFirstName();
		this.lastName = p.getLastName();
	}

}
