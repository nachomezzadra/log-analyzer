package com.avaya.aps.sca.components.loganalyzer.container;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.log4j.Logger;

import com.avaya.aps.sca.components.loganalyzer.checkpoint.CheckPoint;
import com.avaya.aps.sca.components.loganalyzer.checkpoint.LogPattern;

public class LogTemplate {

	private static final String CHECKPOINT_PREFIX = "checkpoint";
	private static final String CHECKPOINT_NAME = "name";
	private static final String CHECKPOINT_PATTERN = "pattern";
	private static final String CHECKPOINT_TOKEN = "token";
	private static final String CHECKPOINT_IS_REG_EX = "isRegEx";
	private static final String CHECKPOINT_EXTRACT_VALUE_FROM = "extractValueFrom";
	private static final String CHECKPOINT_VARIABLE_NAME = "variableName";
	private static final String CHECKPOINT_TOKEN_VARIABLE = "tokenVariable";

	private String name;
	private TreeSet<CheckPoint> checkPoints;
	Properties properties = new Properties();
	Logger logger = Logger.getLogger(LogTemplate.class);

	public LogTemplate() {
		this.checkPoints = new TreeSet<CheckPoint>();
	}

	public LogTemplate(InputStream propertiesFile) throws IOException {
		this.checkPoints = new TreeSet<CheckPoint>();
		this.properties.load(propertiesFile);
		this.loadCheckPoints();
	}

	public LogTemplate(String pathToFile) throws IOException {
		new LogTemplate(new FileInputStream(pathToFile));
	}

	public SortedSet<CheckPoint> getCheckPoints() {
		return this.checkPoints;
	}

	public void addCheckPoint(CheckPoint aCheckPoint) {
		this.checkPoints.add(aCheckPoint);
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	private void loadCheckPoints() {
		boolean stillMoreCheckPoints = true;
		int index = 1;
		while (stillMoreCheckPoints) {
			String token = this.properties.getProperty(CHECKPOINT_PREFIX + "." + index + "." + CHECKPOINT_TOKEN);
			String name = this.properties.getProperty(CHECKPOINT_PREFIX + "." + index + "." + CHECKPOINT_NAME);
			boolean isRegEx = Boolean.parseBoolean(this.properties.getProperty(CHECKPOINT_PREFIX + "." + index + "."
					+ CHECKPOINT_IS_REG_EX));
			String extractValueFrom = this.properties.getProperty(CHECKPOINT_PREFIX + "." + index + "."
					+ CHECKPOINT_EXTRACT_VALUE_FROM);
			String variableName = this.properties.getProperty(CHECKPOINT_PREFIX + "." + index + "."
					+ CHECKPOINT_VARIABLE_NAME);
			String tokenVariable = this.properties.getProperty(CHECKPOINT_PREFIX + "." + index + "."
					+ CHECKPOINT_TOKEN_VARIABLE);

			LogPattern logPattern = getLogPattern(index);

			if ((logPattern.hasLinePattern())) {
				CheckPoint checkPoint = new CheckPoint.CheckPointBuilder().name(name).logPattern(logPattern)
						.token(token).isRegEx(isRegEx).extractValueFrom(extractValueFrom).variableName(variableName)
						.tokenVariable(tokenVariable).build();

				this.checkPoints.add(checkPoint);
				this.logger.debug("Added: " + checkPoint);

			} else {
				stillMoreCheckPoints = false;
			}
			index++;
		}

	}

	private LogPattern getLogPattern(int checkPointIndex) {
		LogPattern aLogPattern = new LogPattern();

		String linePattern = this.properties.getProperty(CHECKPOINT_PREFIX + "." + checkPointIndex + "."
				+ CHECKPOINT_PATTERN);
		if (linePattern != null) {
			aLogPattern.addLinePattern(linePattern);
		}

		boolean hasMoreLines = true;
		int linesIndex = 1;
		while (hasMoreLines) {
			String anotherLine = this.properties.getProperty(CHECKPOINT_PREFIX + "." + checkPointIndex + "."
					+ CHECKPOINT_PATTERN + "." + linesIndex);
			if (anotherLine != null) {
				aLogPattern.addLinePattern(anotherLine);
				linesIndex++;
			} else {
				hasMoreLines = false;
			}
		}

		return aLogPattern;
	}
}
