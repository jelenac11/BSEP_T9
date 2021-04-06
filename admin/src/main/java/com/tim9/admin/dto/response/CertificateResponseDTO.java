package com.tim9.admin.dto.response;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CertificateResponseDTO {

	private String serialNumber;
    private String commonName;
    private String issuer;
    private Date notBefore;
    private Date notAfter;
}
