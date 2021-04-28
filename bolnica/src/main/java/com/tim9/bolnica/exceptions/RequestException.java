package com.tim9.bolnica.exceptions;

public class RequestException extends Exception{

	private static final long serialVersionUID = 1L;

	public RequestException(String errorMessage) {
        super(errorMessage);
    }
}
