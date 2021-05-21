package com.tim9.bolnica.model;

import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Id;

import org.kie.api.definition.type.Expires;
import org.kie.api.definition.type.Role;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.tim9.bolnica.enums.LogFacility;
import com.tim9.bolnica.enums.LogSeverity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Document(collection = "logs")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Role(Role.Type.EVENT)
@Expires("5m")
public class Log {

	@Id
    private BigInteger id;
	
	@Indexed
	private Date timestamp;
	
	@Indexed
	private String source;
	
	@Indexed
	private LogFacility facility;
	
	@Indexed
	private LogSeverity severity;
	
	@Indexed
	private String ip;
	
	@Indexed
	private String message;
	
	
}
