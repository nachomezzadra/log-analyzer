package com.avaya.aps.sca.components.loganalyzer.checkpoint;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import junit.framework.Assert;

import org.apache.log4j.Logger;
import org.junit.Test;

import com.avaya.aps.sca.components.loganalyzer.LogAnalyzer;

public class ExtractVariableTest {

	Logger logger = Logger.getLogger(ExtractVariableTest.class);

	@Test
	public void testExtractVariable() throws Exception {
		final String logFilePath = "/logFiles/engine-execution.log.3";
		final String logTemplatePath = "/checkPoints/test/test-engineCheckPoints-SIP-AF-extract-variable.properties";
		LogAnalyzer logAnalyzer = new LogAnalyzer(logFilePath, logTemplatePath);

		assertTrue(logAnalyzer.analyze());
		assertNotNull(logAnalyzer.getResults());
	}

	@Test
	public void testExtractWordOnTheVeryRight() throws Exception {
		final String linePattern = "INFO  RequestTaskCCXML  - CCXML Application was launched with Session ID UCEPMPP-2013046184632-29 for Request ID (.)*{1}";
		final String aLineOfLog = "2013-02-15 10:46:32,697 [pool-2-thread-18] INFO  RequestTaskCCXML  - CCXML Application was launched with Session ID UCEPMPP-2013046184632-29 for Request ID 699";

		final String extractValueFrom = "(.)*{1}";
		final String expectedValue = "699";

		CheckPoint aCheckPoint = new CheckPoint.CheckPointBuilder().linePattern(linePattern)
				.extractValueFrom(extractValueFrom).name("Checkpoint to extract word on the very right").isRegEx(true)
				.variableName("RequestId").build();

		String value = aCheckPoint.extractValueFrom(aLineOfLog);

		assertNotNull(value);
		Assert.assertTrue(!value.isEmpty());
		assertEquals(expectedValue, value);
	}

	@Test
	public void testExtractWordFromTheMiddle() throws Exception {
		final String linePattern = "INFO  RequestTaskCCXML  - CCXML Application was launched with Session ID (.)*{1} for Request ID 699";
		final String aLineOfLog = "2013-02-15 10:46:32,697 [pool-2-thread-18] INFO  RequestTaskCCXML  - CCXML Application was launched with Session ID UCEPMPP-2013046184632-29 for Request ID 699";

		final String extractValueFrom = "(.)*{1}";
		final String expectedValue = "UCEPMPP-2013046184632-29";

		CheckPoint aCheckPoint = new CheckPoint.CheckPointBuilder().linePattern(linePattern)
				.extractValueFrom(extractValueFrom).name("Checkpoint to extract word from the middle").isRegEx(true)
				.variableName("SessionId").build();

		String value = aCheckPoint.extractValueFrom(aLineOfLog);

		assertNotNull(value);
		Assert.assertTrue(!value.isEmpty());
		assertEquals(expectedValue, value);
	}

	@Test
	public void testExtractWordOnTheVeryLeft() throws Exception {
		final String linePattern = "(.)*{1} INFO  RequestTaskCCXML  - CCXML Application was launched with Session ID UCEPMPP-2013046184632-29 for Request ID 699";
		final String aLineOfLog = "2013-02-15 10:46:32,697 [pool-2-thread-18] INFO  RequestTaskCCXML  - CCXML Application was launched with Session ID UCEPMPP-2013046184632-29 for Request ID 699";

		final String extractValueFrom = "(.)*{1}";
		final String expectedValue = "[pool-2-thread-18]";

		CheckPoint aCheckPoint = new CheckPoint.CheckPointBuilder().linePattern(linePattern)
				.extractValueFrom(extractValueFrom).name("Checkpoint to extract word on the very left").isRegEx(true)
				.variableName("threadId").build();

		String value = aCheckPoint.extractValueFrom(aLineOfLog);

		this.logger.debug("Extracted Value: " + value);
		assertNotNull(value);
		Assert.assertTrue(!value.isEmpty());
		assertEquals(expectedValue, value);
	}

	@Test
	public void testExtractWordWithEndingDolarSign() throws Exception {
		final String linePattern = "INFO  RequestTaskCCXML  - CCXML Application was launched with Session ID (.)*{1} for Request ID 699$";
		final String aLineOfLog = "2013-02-15 10:46:32,697 [pool-2-thread-18] INFO  RequestTaskCCXML  - CCXML Application was launched with Session ID UCEPMPP-2013046184632-29 for Request ID 699";

		final String extractValueFrom = "(.)*{1}";
		final String expectedValue = "UCEPMPP-2013046184632-29";

		CheckPoint aCheckPoint = new CheckPoint.CheckPointBuilder().linePattern(linePattern)
				.extractValueFrom(extractValueFrom)
				.name("Checkpoint to extract word in the middle with ending dolar sign").isRegEx(true)
				.variableName("SessionId").build();

		String value = aCheckPoint.extractValueFrom(aLineOfLog);

		this.logger.debug("Extracted Value: " + value);
		assertNotNull(value);
		Assert.assertTrue(!value.isEmpty());
		assertEquals(expectedValue, value);
	}

	@Test
	// TODO implement this, currently there is no support for this kind of pattern, where the regex is not used to
	// extract a value only but also appears in any other part of the line pattern
	public void testExtractWordInTheMiddleWithExtraRegEx() throws Exception {
		final String linePattern = "INFO  RequestTaskCCXM(.)  - CCXML Application was launched with Session ID (.)*{1} for Request ID 699";
		final String aLineOfLog = "2013-02-15 10:46:32,697 [pool-2-thread-18] INFO  RequestTaskCCXML  - CCXML Application was launched with Session ID UCEPMPP-2013046184632-29 for Request ID 699";

		final String extractValueFrom = "(.)*{1}";
		final String expectedValue = "UCEPMPP-2013046184632-29";

		CheckPoint aCheckPoint = new CheckPoint.CheckPointBuilder().linePattern(linePattern)
				.extractValueFrom(extractValueFrom).name("Checkpoint to extract word from the middle").isRegEx(true)
				.variableName("SessionId").build();

		String value = aCheckPoint.extractValueFrom(aLineOfLog);

		assertNotNull(value);
		Assert.assertTrue(!value.isEmpty());
		assertEquals(expectedValue, value);
	}

	@Test
	public void testExtractVariableAndUseItLater() throws Exception {
		final String logFilePath = "/logFiles/engine-execution-CTI.log.4";
		final String logTemplatePath = "/checkPoints/test/engineCheckPoints-CTI-extract-variable.properties";
		LogAnalyzer logAnalyzer = new LogAnalyzer(logFilePath, logTemplatePath);

		assertTrue(logAnalyzer.analyze());
		assertNotNull(logAnalyzer.getResults());
	}

}
