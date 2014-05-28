package com.avaya.aps.sca.components.loganalyzer;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class DDappsBusinessTest {

	@Test
	public void testValidateDDappsBusinessLog() throws Exception {
		final String logFilePath = "/logFiles/ddapps_business.log";
		final String logTemplatePath = "/checkPoints/ddappsBusinessCheckPoints.properties";
		LogAnalyzer logAnalyzer = new LogAnalyzer(logFilePath, logTemplatePath);

		assertTrue(logAnalyzer.analyze());
		assertNotNull(logAnalyzer.getResults());
	}
}
