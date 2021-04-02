package com.tim9.admin.exceptions;

public class InvalidDigitalSignatureException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public InvalidDigitalSignatureException(String errorMessage) {
        super(errorMessage);
    }

}
