package com.tim9.admin.dto;

import java.util.ArrayList;
import java.util.Date;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CertDataDTO {
	
	@Pattern(regexp = "^(?!.*\\s).*$", message = "Serial number can not contain whitespaces")
	private String serialNumber;
	@NotEmpty
	private Date notBefore;
	@NotEmpty
	private Date notAfter;
	@Pattern(regexp = "^(?!.*\\s).*$", message = "CA serial number can not contain whitespaces")
	private String issuer;
	private String signingAlgorithm;
	private String type;
	private ArrayList<String> keyUsage;
	private ArrayList<String> extendedKeyUsage;
	private boolean keyIdentifierExtension;
	private boolean alternativeNameExtension;

}
