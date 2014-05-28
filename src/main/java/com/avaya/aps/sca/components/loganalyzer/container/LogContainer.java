package com.avaya.aps.sca.components.loganalyzer.container;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

import org.apache.commons.lang.StringUtils;

public class LogContainer {

	private static final String NEW_LINE = "\n";
	private final Scanner scanner;

	public LogContainer(String pathToLogFile) throws FileNotFoundException {
		this.scanner = new Scanner(new File(pathToLogFile));
	}

	public LogContainer(File aFile) throws FileNotFoundException {
		this.scanner = new Scanner(aFile);
	}

	public LogContainer(InputStream aFile) throws FileNotFoundException {
		this.scanner = new Scanner(aFile);
	}

	public String getNextLine() {
		if (this.scanner.hasNextLine()) {
			return this.scanner.nextLine();
		} else {
			this.scanner.close();
			return null;
		}
	}

	/**
	 * This method will return the next following N lines in the Log file. If for example there are 4 lines remaining
	 * and 5 more lines are required, it will return null as it doesn't satisfy the full requirement.
	 * 
	 * @param numberOfLines
	 * @return the following N lines in provided Log File
	 */
	public String getNextLines(int numberOfLines) {
		int lineNumber = 1;
		StringBuilder lineBuilder = new StringBuilder();
		while (lineNumber <= numberOfLines) {
			if (this.hasMoreLogLines()) {
				lineBuilder.append(this.getNextLine());
				lineBuilder.append(NEW_LINE);
				lineNumber++;
			} else {
				return null;
			}
		}
		return StringUtils.removeEnd(lineBuilder.toString(), NEW_LINE);
	}

	private boolean hasMoreLogLines() {
		return this.scanner.hasNextLine();
	}

	public void close() {
		this.scanner.close();
	}

}
