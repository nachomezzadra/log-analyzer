package com.avaya.aps.sca.components.loganalyzer.validation;

public class UnexpectedEndOfLogFileException extends Exception {

	private static final long serialVersionUID = -5660830243568575920L;

	public UnexpectedEndOfLogFileException(String message) {
		super(message);
	}

}
