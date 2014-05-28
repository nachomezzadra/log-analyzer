package com.avaya.aps.sca.components.loganalyzer.validation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.InputStream;

import org.junit.Test;

import com.avaya.aps.sca.components.loganalyzer.LogAnalyzer;
import com.avaya.aps.sca.components.loganalyzer.checkpoint.CheckPoint;
import com.avaya.aps.sca.components.loganalyzer.container.LogContainer;
import com.avaya.aps.sca.components.loganalyzer.container.LogTemplate;

public class ValidatorTest {

	@Test
	public void testValidatorAllCheckPointsValid() throws Exception {
		InputStream logInputStream = this.getClass().getResourceAsStream("/logFiles/logExample1.log");
		LogContainer logContainer = new LogContainer(logInputStream);
		LogTemplate logTemplate = new LogTemplate();
		final CheckPoint actualCheckPoint1 = createCheckPoint("[SoapUI] Used java version: 1.6.0_26");
		final CheckPoint actualCheckPoint2 = createCheckPoint("[SchemaUtils] Getting schema http://135.122.63.237:8081");
		final CheckPoint actualCheckPoint3 = createCheckPoint("[errorlog] Failed to delete temporary project file");
		logTemplate.addCheckPoint(actualCheckPoint1);
		logTemplate.addCheckPoint(actualCheckPoint2);
		logTemplate.addCheckPoint(actualCheckPoint3);
		Validator validator = new Validator(logTemplate, logContainer);
		assertTrue(validator.isValid());
		ValidatorResult validatorResults = validator.getResults();
		assertNotNull(validatorResults);

	}

	@Test
	public void testValidatorLastCheckPointInvalid() throws Exception {
		InputStream logInputStream = this.getClass().getResourceAsStream("/logFiles/logExample1.log");
		LogContainer logContainer = new LogContainer(logInputStream);
		LogTemplate logTemplate = new LogTemplate();
		final CheckPoint actualCheckPoint1 = createCheckPoint("[SoapUI] Used java version: 1.6.0_26");
		final CheckPoint actualCheckPoint2 = createCheckPoint("[SchemaUtils] Getting schema http://135.122.63.237:8081");
		final CheckPoint actualCheckPoint3 = createCheckPoint("[errorlog] Failed to delete temporary project file");
		final CheckPoint actualCheckPoint4 = createCheckPoint("A Check point that will not pass");
		logTemplate.addCheckPoint(actualCheckPoint1);
		logTemplate.addCheckPoint(actualCheckPoint2);
		logTemplate.addCheckPoint(actualCheckPoint3);
		logTemplate.addCheckPoint(actualCheckPoint4);
		Validator validator = new Validator(logTemplate, logContainer);
		assertFalse(validator.isValid());
		ValidatorResult validatorResults = validator.getResults();
		assertNotNull(validatorResults);
		assertTrue(validatorResults.isValid(actualCheckPoint1));
		assertTrue(validatorResults.isValid(actualCheckPoint2));
		assertTrue(validatorResults.isValid(actualCheckPoint3));
		assertFalse(validatorResults.isValid(actualCheckPoint4));
	}

	@Test
	public void testValidatorWithToken() throws Exception {
		LogContainer logContainer = new LogContainer(this.getClass().getResourceAsStream(
				"/logFiles/engine-execution.log"));
		LogTemplate logTemplate = new LogTemplate();
		final CheckPoint actualCheckPoint1 = createCheckPoint(
				"TRACE EngineStrategyCF  - Processing request: RequestMessage [callbackRequestId=%s", "1139");
		final CheckPoint actualCheckPoint2 = createCheckPoint(
				"DEBUG EngineStrategyCF  - Scheduled callback request id %s for Callback Config with id 1 is not allowed to be launched.",
				"1139");
		logTemplate.addCheckPoint(actualCheckPoint1);
		logTemplate.addCheckPoint(actualCheckPoint2);
		Validator validator = new Validator(logTemplate, logContainer);
		assertTrue(validator.isValid());
		ValidatorResult validatorResults = validator.getResults();
		assertNotNull(validatorResults);

	}

	@Test
	public void testValidatorWithNoCheckPoints() throws Exception {
		InputStream logInputStream = this.getClass().getResourceAsStream("/logFiles/logExample1.log");
		LogContainer logContainer = new LogContainer(logInputStream);
		LogTemplate logTemplate = new LogTemplate();
		Validator validator = new Validator(logTemplate, logContainer);
		assertTrue(validator.isValid());
		ValidatorResult validatorResults = validator.getResults();
		assertNotNull(validatorResults);
	}

	private CheckPoint createCheckPoint(String linePattern) {
		return new CheckPoint.CheckPointBuilder().name("A CheckPoint").linePattern(linePattern).build();
	}

	private CheckPoint createCheckPoint(String linePattern, String tokenToMatch) {
		return new CheckPoint.CheckPointBuilder().name("Name for line to match" + linePattern).linePattern(linePattern)
				.token(tokenToMatch).build();
	}

	@Test
	public void testValidateMultilineLog() throws Exception {

		final String logFilePath = "/logFiles/engine-execution-CTI.log.4";
		final String logTemplatePath = "/checkPoints/test/multilineCheckpoints.properties";
		LogAnalyzer logAnalyzer = new LogAnalyzer(logFilePath, logTemplatePath);

		assertTrue(logAnalyzer.analyze());
		assertNotNull(logAnalyzer.getResults());
	}
}
