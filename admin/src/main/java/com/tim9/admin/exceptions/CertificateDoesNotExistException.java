package com.tim9.admin.exceptions;

public class CertificateDoesNotExistException  extends Exception {
	
	private static final long serialVersionUID = 1L;

	public CertificateDoesNotExistException(String errorMessage) {
        super(errorMessage);
    }

}
