package com.tim9.bolnica.model;

import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.kie.api.definition.type.Expires;
import org.kie.api.definition.type.Role;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "admin_alarms")
@Role(Role.Type.EVENT)
@Expires("10m")
public class AdminAlarm {
	
	@Id
	private BigInteger id;
	
	private BigInteger logId;
	
	private Date timestamp;
	
	private String message;

}
