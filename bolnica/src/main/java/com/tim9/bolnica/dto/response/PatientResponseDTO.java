package com.tim9.bolnica.dto.response;

import java.util.Date;

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
	private String insuredNumber;
	private String firstName;
	private String lastName;
	private Date birthDay;
	private double height;
	private double weight;
	private String gender;
	private String bloodType;
	private String medicalHistory;
	
	public PatientResponseDTO(Patient p) {
		this(p.getId(), p.getInsuredNumber(), p.getFirstName(), p.getLastName(), p.getBirthDay(), p.getHeight(), p.getWeight(), p.getGender().toString(), p.getBloodType().toString(), p.getMedicalHistory());
	}

}
