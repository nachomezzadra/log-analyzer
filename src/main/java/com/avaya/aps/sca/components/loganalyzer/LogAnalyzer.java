package com.avaya.aps.sca.components.loganalyzer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import com.avaya.aps.sca.components.loganalyzer.checkpoint.CheckPoint;
import com.avaya.aps.sca.components.loganalyzer.checkpoint.CheckPointVariables;
import com.avaya.aps.sca.components.loganalyzer.container.LogContainer;
import com.avaya.aps.sca.components.loganalyzer.container.LogTemplate;
import com.avaya.aps.sca.components.loganalyzer.validation.Validator;

public class LogAnalyzer {

	private final LogContainer logContainer;
	private final LogTemplate logTemplate;
	private final Validator validator;

	public LogAnalyzer(String logFilePath, String logTemplatePath) {
		try {
			this.logContainer = getLogContainerFrom(logFilePath);
		} catch (FileNotFoundException e) {
			throw new RuntimeException("Log File could not be found.");
		}
		try {
			this.logTemplate = new LogTemplate(this.getClass().getResourceAsStream(logTemplatePath));
		} catch (IOException e) {
			throw new RuntimeException("Log Template could not be found.");
		}
		this.validator = new Validator(this.logTemplate, this.logContainer);
	}

	private LogContainer getLogContainerFrom(String logFilePath) throws FileNotFoundException {
		LogContainer aContainer = null;
		File fileLogFile = new File(logFilePath);
		if (fileLogFile.exists()) {
			aContainer = new LogContainer(fileLogFile);
		} else {
			// if physical file does not exist, then we try looking for it at the classpath
			InputStream inputStreamLogFile = this.getClass().getResourceAsStream(logFilePath);
			aContainer = new LogContainer(inputStreamLogFile);
		}
		return aContainer;
	}

	public boolean analyze() {
		return this.validator.isValid();
	}

	/**
	 * This method will return a HashMap where the key is the Check Point name and the value is a boolean value where it
	 * indicates whether the checkpoint has successfully passed or not.
	 * 
	 * @return null if {@link #analyze()} was never run
	 */
	public Map<String, Boolean> getResults() {
		HashMap<String, Boolean> results = null;

		if (this.validator.getResults() != null) {
			results = new HashMap<String, Boolean>();
		}

		for (Map.Entry<CheckPoint, Boolean> eachElement : this.validator.getResults().getResults().entrySet()) {
			results.put(eachElement.getKey().getName(), eachElement.getValue().booleanValue());
		}

		return results;
	}

	/**
	 * This method returns a Map with the variables and their extracted values, as defined in the Log Template
	 * properties
	 * 
	 * @return
	 */
	public Map<String, String> getExtractedValues() {
		return CheckPointVariables.getInstance().getExtractedValues();
	}

	public String getExtractedValueFor(String variableName) {
		return CheckPointVariables.getInstance().getValueFor(variableName);
	}

}
