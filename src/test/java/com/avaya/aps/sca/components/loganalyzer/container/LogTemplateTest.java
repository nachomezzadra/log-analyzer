package com.avaya.aps.sca.components.loganalyzer.container;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.InputStream;
import java.util.Iterator;

import org.junit.Test;

import com.avaya.aps.sca.components.loganalyzer.checkpoint.CheckPoint;

public class LogTemplateTest {

	@Test
	public void testCheckPointOrder() throws Exception {
		LogTemplate logTemplate = new LogTemplate();
		CheckPoint expectedCheckPoint1 = new CheckPoint.CheckPointBuilder().linePattern("First Check Point").build();
		CheckPoint expectedCheckPoint2 = new CheckPoint.CheckPointBuilder().linePattern("Second Check Point").build();
		CheckPoint expectedCheckPoint3 = new CheckPoint.CheckPointBuilder().linePattern("Third Check Point").build();
		CheckPoint expectedCheckPoint4 = new CheckPoint.CheckPointBuilder().linePattern("Fourth Check Point").build();
		CheckPoint expectedCheckPoint5 = new CheckPoint.CheckPointBuilder().linePattern("Fifth Check Point").build();
		CheckPoint expectedCheckPoint6 = new CheckPoint.CheckPointBuilder().linePattern("Sixth Check Point").build();
		logTemplate.addCheckPoint(expectedCheckPoint1);
		logTemplate.addCheckPoint(expectedCheckPoint2);
		logTemplate.addCheckPoint(expectedCheckPoint3);
		logTemplate.addCheckPoint(expectedCheckPoint4);
		logTemplate.addCheckPoint(expectedCheckPoint5);
		logTemplate.addCheckPoint(expectedCheckPoint6);

		Iterator<CheckPoint> iterator = logTemplate.getCheckPoints().iterator();

		CheckPoint actualCheckPoint1 = iterator.next();
		CheckPoint actualCheckPoint2 = iterator.next();
		CheckPoint actualCheckPoint3 = iterator.next();
		CheckPoint actualCheckPoint4 = iterator.next();
		CheckPoint actualCheckPoint5 = iterator.next();
		CheckPoint actualCheckPoint6 = iterator.next();

		assertNotNull(actualCheckPoint1);
		assertEquals(expectedCheckPoint1, actualCheckPoint1);
		assertNotNull(actualCheckPoint2);
		assertEquals(expectedCheckPoint2, actualCheckPoint2);
		assertNotNull(actualCheckPoint3);
		assertEquals(expectedCheckPoint3, actualCheckPoint3);
		assertNotNull(actualCheckPoint4);
		assertEquals(expectedCheckPoint4, actualCheckPoint4);
		assertNotNull(actualCheckPoint5);
		assertEquals(expectedCheckPoint5, actualCheckPoint5);
		assertNotNull(actualCheckPoint6);
		assertEquals(expectedCheckPoint6, actualCheckPoint6);

	}

	@Test
	public void testLogTemplateFromProperties() throws Exception {
		InputStream propertiesFile = System.class
				.getResourceAsStream("/checkPoints/test/test-engineCheckPoints.properties");
		LogTemplate logTemplate = new LogTemplate(propertiesFile);
		assertNotNull(logTemplate.getCheckPoints());
		assertTrue(!logTemplate.getCheckPoints().isEmpty());
		assertEquals("There should be 4 Check Points in the properties file", 4, logTemplate.getCheckPoints().size());
	}

}
