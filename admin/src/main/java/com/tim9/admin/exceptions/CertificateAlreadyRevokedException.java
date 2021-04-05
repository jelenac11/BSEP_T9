package com.tim9.admin.exceptions;

public class CertificateAlreadyRevokedException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public CertificateAlreadyRevokedException(String errorMessage) {
        super(errorMessage);
    }

}
