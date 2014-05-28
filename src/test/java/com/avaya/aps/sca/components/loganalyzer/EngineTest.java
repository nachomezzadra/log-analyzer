package com.avaya.aps.sca.components.loganalyzer;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class EngineTest extends BaseTest {

	@Test
	public void testValidateEngineSipAgentFirstLog() throws Exception {

		final String logFilePath = "/logFiles/engine-execution.log.3";
		final String logTemplatePath = "/checkPoints/engineCheckPoints-SIP-AF.properties";
		LogAnalyzer logAnalyzer = new LogAnalyzer(logFilePath, logTemplatePath);

		assertTrue(logAnalyzer.analyze());
		assertNotNull(logAnalyzer.getResults());
	}

	@Test
	public void testValidateEngineCTILog() throws Exception {

		final String logFilePath = "/logFiles/engine-execution-CTI.log.4";
		final String logTemplatePath = "/checkPoints/engineCheckPoints-CTI.properties";
		LogAnalyzer logAnalyzer = new LogAnalyzer(logFilePath, logTemplatePath);

		assertTrue(logAnalyzer.analyze());
		assertNotNull(logAnalyzer.getResults());
	}

}
