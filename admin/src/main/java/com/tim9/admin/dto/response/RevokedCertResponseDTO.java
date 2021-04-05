package com.tim9.admin.dto.response;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RevokedCertResponseDTO {
	
    private String serialNumber;;
	private String commonName;
    private Date notBefore;
    private Date notAfter;
    private String issuer;
    private Date revokeDate;
}
