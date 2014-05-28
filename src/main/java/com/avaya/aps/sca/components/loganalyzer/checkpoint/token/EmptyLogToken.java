package com.avaya.aps.sca.components.loganalyzer.checkpoint.token;


public class EmptyLogToken extends LogToken {

	@Override
	public String getTokenValue() {
		return new String("");
	}

}
