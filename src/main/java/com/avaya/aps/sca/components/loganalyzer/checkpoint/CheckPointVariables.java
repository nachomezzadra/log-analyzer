package com.avaya.aps.sca.components.loganalyzer.checkpoint;

import java.util.HashMap;
import java.util.Map;

public class CheckPointVariables {

	private static CheckPointVariables instance;
	private final Map<String, String> variables;

	private CheckPointVariables() {
		this.variables = new HashMap<String, String>();
	}

	public static CheckPointVariables getInstance() {
		if (instance == null) {
			instance = new CheckPointVariables();
		}
		return instance;
	}

	public void save(String variableName, String value) {
		if (this.variables.containsKey(variableName)) {
			throw new RuntimeException(String.format("There is already a value for Variable %s (%s)", variableName,
					this.variables.get(variableName)));
		}
		this.variables.put(variableName, value);
	}

	public String getValueFor(String variableName) {
		return this.variables.get(variableName);
	}

	public Map<String, String> getExtractedValues() {
		return this.variables;
	}

	/**
	 * @Deprecated Use for testing purposes only
	 */
	@Deprecated
	public static void resetCheckPointVariables() {
		instance = null;
	}
}
