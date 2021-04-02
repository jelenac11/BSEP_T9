package com.tim9.admin.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "certificate_signing_requests")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CSR {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "csr", nullable = false)
    private byte[] csr;
    
    public CSR(byte[] csr) {
    	this.csr = csr;
    }
}
