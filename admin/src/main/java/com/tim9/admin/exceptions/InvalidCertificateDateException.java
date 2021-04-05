package com.tim9.admin.exceptions;

public class InvalidCertificateDateException  extends Exception {
	
	private static final long serialVersionUID = 1L;

	public InvalidCertificateDateException(String errorMessage) {
        super(errorMessage);
    }

}
