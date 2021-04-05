package com.tim9.admin.exceptions;

public class CertificateAlreadyExistsException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public CertificateAlreadyExistsException(String errorMessage) {
        super(errorMessage);
    }

}
