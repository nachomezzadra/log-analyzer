package com.avaya.aps.sca.components.loganalyzer;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CCXMLTest {

	@Test
	public void testValidateCCXMLImmediateDeliveredLog() throws Exception {

		final String logFilePath = "/logFiles/CCXML-SessionSlot-011.log";
		final String logTemplatePath = "/checkPoints/test/test-ccxmlCheckPoints-immediate-delivered.properties";
		LogAnalyzer logAnalyzer = new LogAnalyzer(logFilePath, logTemplatePath);

		assertTrue(logAnalyzer.analyze());
		assertNotNull(logAnalyzer.getResults());
	}

}
