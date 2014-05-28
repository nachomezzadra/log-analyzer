package com.avaya.aps.sca.components.loganalyzer.checkpoint;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.avaya.aps.sca.components.loganalyzer.checkpoint.token.LogToken;

public class LogPattern {

	private static final String TOKEN_PLACE_HOLDER = "%s";
	private static final String NEW_LINE = "\n";
	private static final String EMPTY_LINE = "";
	List<String> linePatterns = new ArrayList<String>();

	public void addLinePattern(String aLinePattern) {
		this.linePatterns.add(aLinePattern);
	}

	private String getAllLinesAppended() {
		StringBuilder appended = new StringBuilder();
		for (String eachLinePattern : this.linePatterns) {
			appended.append(eachLinePattern);
			appended.append(NEW_LINE);
		}
		// Removing last '\n'
		return StringUtils.removeEnd(appended.toString(), NEW_LINE);
	}

	public boolean isMultiLine() {
		return this.linePatterns.size() > 1;
	}

	public int getNumberOfLines() {
		return this.linePatterns.size();
	}

	public String getLogPatternLinesFor(LogToken token) {
		String allLines = this.getAllLinesAppended();
		if (token != null) {
			allLines = StringUtils.replace(allLines, TOKEN_PLACE_HOLDER, token.getTokenValue());
		}
		return allLines;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.linePatterns == null) ? 0 : this.linePatterns.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		LogPattern other = (LogPattern) obj;
		if (this.linePatterns == null) {
			if (other.linePatterns != null) {
				return false;
			}
		} else if (!this.linePatterns.equals(other.linePatterns)) {
			return false;
		}
		return true;
	}

	public boolean hasLinePattern() {
		return !this.linePatterns.isEmpty() && !this.linePatterns.get(0).equals(EMPTY_LINE);
	}

}
