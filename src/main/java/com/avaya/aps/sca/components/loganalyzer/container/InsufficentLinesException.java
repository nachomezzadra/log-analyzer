package com.avaya.aps.sca.components.loganalyzer.container;

public class InsufficentLinesException extends Exception {

	private static final long serialVersionUID = 7115456745456308015L;

	public InsufficentLinesException(String message) {
		super("Could not retrieve from Log file the requested amount of lines. ".concat(message));
	}
}
