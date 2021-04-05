package com.tim9.admin.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "revoked_certificates")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RevokedCertificate {
	
	@Id
    private Long id;
	@Column(name = "common_name", nullable = false)
	private String commonName;
	@Column(name = "not_before", nullable = false)
    private Date notBefore;
	@Column(name = "not_after", nullable = false)
    private Date notAfter;
	@Column(name = "issuer", nullable = false)
    private String issuer;
	@Column(name = "revoke_date", nullable = false)
    private Date revokeDate;
}
