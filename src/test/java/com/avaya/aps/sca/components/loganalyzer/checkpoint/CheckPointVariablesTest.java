package com.avaya.aps.sca.components.loganalyzer.checkpoint;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Map;
import java.util.UUID;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.avaya.aps.sca.components.loganalyzer.LogAnalyzer;

public class CheckPointVariablesTest {

	@Before
	@SuppressWarnings("deprecation")
	public void setUp() {
		CheckPointVariables.resetCheckPointVariables();
	}

	@Test
	public void testSaveVariable() throws Exception {
		final String aKey = "aKey-" + UUID.randomUUID();
		final String aValue = "aValue";

		CheckPointVariables.getInstance().save(aKey, aValue);
		String actualValue = CheckPointVariables.getInstance().getValueFor(aKey);
		assertNotNull(actualValue);
		assertEquals(aValue, actualValue);
	}

	@Test
	public void testSaveSameVariableDifferentValue() throws Exception {
		final String aKey = "aKey-" + UUID.randomUUID();
		final String aValue = "aValue";

		final String anotherValue = "anotherValue";

		CheckPointVariables.getInstance().save(aKey, aValue);
		try {
			CheckPointVariables.getInstance().save(aKey, anotherValue);
			Assert.fail("CheckPointVariables cannot replace a value for a key that has already been saved");
		} catch (RuntimeException e) {
			// expected
		}

		String actualValue = CheckPointVariables.getInstance().getValueFor(aKey);
		assertNotNull(aValue);
		assertEquals(aValue, actualValue);
	}

	@Test
	public void testGetVariablesValuesFromLogAnalyzer() throws Exception {
		final String logFilePath = "/logFiles/engine-execution.log.3";
		final String logTemplatePath = "/checkPoints/test/test-engineCheckPoints-SIP-AF-extract-variable.properties";
		LogAnalyzer logAnalyzer = new LogAnalyzer(logFilePath, logTemplatePath);

		assertTrue(logAnalyzer.analyze());
		assertNotNull(logAnalyzer.getResults());

		Map<String, String> extractedValues = logAnalyzer.getExtractedValues();
		assertNotNull(extractedValues);
		assertFalse(extractedValues.isEmpty());

		for (Map.Entry<String, String> eachElement : extractedValues.entrySet()) {
			assertNotNull(eachElement);
			assertNotNull(eachElement.getKey());
			System.out.println(eachElement.getKey());
			assertNotNull(eachElement.getValue());
			System.out.println(eachElement.getValue());
		}

	}
}
