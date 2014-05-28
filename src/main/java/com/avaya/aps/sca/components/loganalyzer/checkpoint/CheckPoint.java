package com.avaya.aps.sca.components.loganalyzer.checkpoint;

import java.util.StringTokenizer;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.avaya.aps.sca.components.loganalyzer.checkpoint.token.EmptyLogToken;
import com.avaya.aps.sca.components.loganalyzer.checkpoint.token.LogToken;
import com.avaya.aps.sca.components.loganalyzer.checkpoint.token.SimpleLogToken;
import com.avaya.aps.sca.components.loganalyzer.checkpoint.token.VariableLogToken;

public class CheckPoint implements Comparable<CheckPoint> {

	private static final Logger logger = Logger.getLogger(CheckPoint.class);
	private static final String END_OF_LINE_SIGN = "$";
	private final LogPattern logPattern;
	private String name;
	private String stringToMatch;
	private Pattern regExPattern = null;
	private final boolean hasExtractVariable;
	private final String extractValueFrom;
	private final String variableName;
	private final boolean isRegex;
	private final LogToken logToken;

	public static class CheckPointBuilder {
		private String name;
		private LogPattern logPattern;
		private String token;
		private String extractValueFrom;
		private String variableName;
		private boolean isRegEx;
		private LogToken logToken;
		private String tokenVariable;

		public CheckPointBuilder name(String name) {
			this.name = name;
			return this;
		}

		public CheckPointBuilder logPattern(LogPattern logPattern) {
			this.logPattern = logPattern;
			return this;
		}

		public CheckPointBuilder token(String token) {
			this.token = token;
			return this;
		}

		public CheckPointBuilder extractValueFrom(String extractValueFrom) {
			this.extractValueFrom = extractValueFrom;
			return this;
		}

		public CheckPointBuilder variableName(String variableName) {
			this.variableName = variableName;
			return this;
		}

		public CheckPointBuilder isRegEx(boolean isRegex) {
			this.isRegEx = isRegex;
			return this;
		}

		public CheckPointBuilder tokenVariable(String tokenVariable) {
			this.tokenVariable = tokenVariable;
			return this;
		}

		public CheckPointBuilder linePattern(String linePattern) {
			if (this.logPattern == null) {
				this.logPattern = new LogPattern();
			}
			this.logPattern.addLinePattern(linePattern);
			return this;
		}

		public CheckPoint build() {
			buildToken();
			return new CheckPoint(this);
		}

		private void buildToken() {
			if (this.tokenVariable != null && !this.tokenVariable.isEmpty()) {
				this.logToken = new VariableLogToken(this.tokenVariable);
			} else if (this.token != null && !this.token.equals("")) {
				this.logToken = new SimpleLogToken(this.token);
			} else {
				this.logToken = new EmptyLogToken();
			}
		}

	}

	public CheckPoint(CheckPointBuilder checkPointBuilder) {
		if (checkPointBuilder.logPattern == null) {
			throw new IllegalArgumentException("Line Pattern cannot be null");
		}
		this.logPattern = checkPointBuilder.logPattern;
		this.name = checkPointBuilder.name;

		this.extractValueFrom = checkPointBuilder.extractValueFrom;
		this.hasExtractVariable = (this.extractValueFrom != null);
		this.variableName = checkPointBuilder.variableName;
		this.isRegex = checkPointBuilder.isRegEx;

		if (checkPointBuilder.logToken == null) {
			throw new IllegalArgumentException("Log Token cnanot be null.  At least an Empty Token be created.");
		}
		this.logToken = checkPointBuilder.logToken;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean matches(String aLineOfLog) {
		if (aLineOfLog == null) {
			return false;
		}

		if (getRexExPattern().matcher(aLineOfLog).find()) {
			if (this.hasExtractVariable) {
				String extractedValue = extractValueFrom(aLineOfLog);
				logger.info(String.format("Extracted value from CheckPoint [%s] is %s", this.name, extractedValue));
				if (this.variableName != null) {
					if (logger.isDebugEnabled()) {
						logger.debug(String.format("Saving value %s into variable %s.", extractedValue,
								this.variableName));
					}
					CheckPointVariables.getInstance().save(this.variableName, extractedValue);
				}
			}
			logCheckPointReached();
			return true;
		}
		return false;
	}

	private Pattern getRexExPattern() {
		if (this.regExPattern == null) {
			this.stringToMatch = this.logPattern.getLogPatternLinesFor(this.logToken);

			if (!this.isRegex) {
				this.stringToMatch = Pattern.quote(this.stringToMatch);
			}

			this.regExPattern = Pattern.compile(this.stringToMatch);
		}
		return this.regExPattern;
	}

	private void logCheckPointReached() {
		if (logger.isInfoEnabled()) {
			if (this.getToken() != null) {
				logger.info(String.format("Check Point reached for token %s [%s]", this.getToken(), this.getName()));
			} else {
				logger.info(String.format("Check Point reached [%s]", this.getName()));
			}
		}
	}

	public String extractValueFrom(String aLineOfLog) {
		String aLineOfLogFormatted = getFormattedLineToExtractValueFrom();
		String leftWords = StringUtils.substringBefore(aLineOfLogFormatted, this.extractValueFrom);
		String rightWords = StringUtils.substringAfter(aLineOfLogFormatted, this.extractValueFrom);

		if (logger.isTraceEnabled()) {
			logger.trace("Left words: " + leftWords);
			logger.trace("Right words: " + rightWords);
		}

		String value = "";
		if (!leftWords.equals("") && !rightWords.equals("")) {
			logger.trace("Middle ");
			value = StringUtils.substringBetween(aLineOfLog, leftWords, rightWords);
		} else if (leftWords.equals("")) {
			logger.trace("Left");

			StringTokenizer linePatternTokenizer = new StringTokenizer(this.logPattern.getLogPatternLinesFor(null));
			linePatternTokenizer.nextToken();
			// Actual Second word
			String firstRealWordAfterTokenInLinePattern = linePatternTokenizer.nextToken();
			String subStringUntilToken = StringUtils.substringBefore(aLineOfLog, firstRealWordAfterTokenInLinePattern);
			// removing empty spaces at the beginning and at the end of the substring
			subStringUntilToken = StringUtils.trim(subStringUntilToken);

			if (logger.isTraceEnabled()) {
				logger.trace("First word after token in Line Pattern: " + firstRealWordAfterTokenInLinePattern);
				logger.trace("Substring until token in a Line of log: " + subStringUntilToken);
			}

			value = getLastWordIn(subStringUntilToken);
		} else if (rightWords.equals("")) {
			logger.trace("Right");
			value = getLastWordIn(aLineOfLog);
		}
		logger.trace(String.format("Extracted Value from %s is: %s", this.getName(), value));
		return value;
	}

	private String getFormattedLineToExtractValueFrom() {
		String lineToExtractValueFrom = getRexExPattern().pattern().toString();
		if (this.isRegex) {
			if (lineToExtractValueFrom.endsWith(END_OF_LINE_SIGN)) {
				lineToExtractValueFrom = StringUtils.removeEnd(lineToExtractValueFrom, END_OF_LINE_SIGN);
			}
		}
		return lineToExtractValueFrom;
	}

	private String getLastWordIn(String aString) {
		// last word in string
		StringUtils.trim(aString);
		return aString.substring(aString.lastIndexOf(" ") + 1);
	}

	public String getStringToMatch() {
		return this.stringToMatch;
	}

	public String getToken() {
		return this.logToken.getTokenValue();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.extractValueFrom == null) ? 0 : this.extractValueFrom.hashCode());
		result = prime * result + (this.hasExtractVariable ? 1231 : 1237);
		result = prime * result + (this.isRegex ? 1231 : 1237);
		result = prime * result + ((this.logPattern == null) ? 0 : this.logPattern.hashCode());
		result = prime * result + ((this.logToken == null) ? 0 : this.logToken.hashCode());
		result = prime * result + ((this.name == null) ? 0 : this.name.hashCode());
		result = prime * result + ((this.regExPattern == null) ? 0 : this.regExPattern.hashCode());
		result = prime * result + ((this.stringToMatch == null) ? 0 : this.stringToMatch.hashCode());
		result = prime * result + ((this.variableName == null) ? 0 : this.variableName.hashCode());
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
		CheckPoint other = (CheckPoint) obj;
		if (this.extractValueFrom == null) {
			if (other.extractValueFrom != null) {
				return false;
			}
		} else if (!this.extractValueFrom.equals(other.extractValueFrom)) {
			return false;
		}
		if (this.hasExtractVariable != other.hasExtractVariable) {
			return false;
		}
		if (this.isRegex != other.isRegex) {
			return false;
		}
		if (this.logPattern == null) {
			if (other.logPattern != null) {
				return false;
			}
		} else if (!this.logPattern.equals(other.logPattern)) {
			return false;
		}
		if (this.logToken == null) {
			if (other.logToken != null) {
				return false;
			}
		} else if (!this.logToken.equals(other.logToken)) {
			return false;
		}
		if (this.name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!this.name.equals(other.name)) {
			return false;
		}
		if (this.regExPattern == null) {
			if (other.regExPattern != null) {
				return false;
			}
		} else if (!this.regExPattern.equals(other.regExPattern)) {
			return false;
		}
		if (this.stringToMatch == null) {
			if (other.stringToMatch != null) {
				return false;
			}
		} else if (!this.stringToMatch.equals(other.stringToMatch)) {
			return false;
		}
		if (this.variableName == null) {
			if (other.variableName != null) {
				return false;
			}
		} else if (!this.variableName.equals(other.variableName)) {
			return false;
		}
		return true;
	}

	@Override
	public int compareTo(CheckPoint otherObject) {
		if (this.equals(otherObject)) {
			return 0;
		}
		return 1;
	}

	@Override
	public String toString() {
		if (this.getName() != null) {
			return String.format("Check Point [%s]", this.getName());
		}
		return String.format("Check Point [%s]", this.stringToMatch);
	}

	public boolean isMultiLine() {
		return this.logPattern.getNumberOfLines() > 1;
	}

	public int getNumberOfLines() {
		return this.logPattern.getNumberOfLines();
	}

}
