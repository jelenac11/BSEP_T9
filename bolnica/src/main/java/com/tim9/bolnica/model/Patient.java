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

import org.hibernate.annotations.ColumnTransformer;

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
	@ColumnTransformer(forColumn = "insured_number", read = "pgp_sym_decrypt(insured_number, current_setting('encrypt.key'), 'cipher-algo=aes256')", write = "pgp_sym_encrypt(?, current_setting('encrypt.key'), 'cipher-algo=aes256')")
	private String insuredNumber;
	@Column
	@ColumnTransformer(forColumn = "first_name", read = "pgp_sym_decrypt(first_name, current_setting('encrypt.key'), 'cipher-algo=aes256')", write = "pgp_sym_encrypt(?, current_setting('encrypt.key'), 'cipher-algo=aes256')")
	private String firstName;
	@Column
	@ColumnTransformer(forColumn = "last_name", read = "pgp_sym_decrypt(last_name, current_setting('encrypt.key'), 'cipher-algo=aes256')", write = "pgp_sym_encrypt(?, current_setting('encrypt.key'), 'cipher-algo=aes256')")
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
	@ColumnTransformer(forColumn = "blood_type", read = "pgp_sym_decrypt(blood_type, current_setting('encrypt.key'), 'cipher-algo=aes256')", write = "pgp_sym_encrypt(?, current_setting('encrypt.key'), 'cipher-algo=aes256')")
	@Enumerated(EnumType.STRING)
	private BloodType bloodType;
	@Column
	@ColumnTransformer(forColumn = "medical_history", read = "pgp_sym_decrypt(medical_history, current_setting('encrypt.key'), 'cipher-algo=aes256')", write = "pgp_sym_encrypt(?, current_setting('encrypt.key'), 'cipher-algo=aes256')")
	private String medicalHistory;
	@Column
	private String hospital;
	@Column
	private String department;
}
