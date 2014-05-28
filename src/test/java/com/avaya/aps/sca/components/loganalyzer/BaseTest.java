package com.avaya.aps.sca.components.loganalyzer;

import org.junit.Before;

import com.avaya.aps.sca.components.loganalyzer.checkpoint.CheckPointVariables;

public class BaseTest {

	@Before
	@SuppressWarnings("deprecation")
	public void setUp() {
		CheckPointVariables.resetCheckPointVariables();
	}
}
