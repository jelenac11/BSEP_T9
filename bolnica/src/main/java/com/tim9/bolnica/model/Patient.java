package com.tim9.bolnica.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.tim9.bolnica.enums.BloodType;
import com.tim9.bolnica.enums.Gender;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "patients")
public class Patient {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Long id;
	
	@Column(unique = true)
	private String insuredNumber;
	@Column
	private String firstName;
	@Column
	private String lastName;
	@Column
	private Date birthDay;
	@Column
	private double height;
	@Column
	private double weight;
	@Column
	@Enumerated(EnumType.STRING)
	private Gender gender;
	@Column
	@Enumerated(EnumType.STRING)
	private BloodType bloodType;

}
