package com.avaya.aps.sca.components.loganalyzer.validation;

import java.util.Iterator;

import org.apache.log4j.Logger;

import com.avaya.aps.sca.components.loganalyzer.checkpoint.CheckPoint;
import com.avaya.aps.sca.components.loganalyzer.container.LogContainer;
import com.avaya.aps.sca.components.loganalyzer.container.LogTemplate;

public class Validator {

	private final Iterator<CheckPoint> checkPointIterator;
	private final LogContainer logContainer;
	private final ValidatorResult results;
	private static final Logger logger = Logger.getLogger(Validator.class);

	public Validator(LogTemplate template, LogContainer logContainer) {
		this.checkPointIterator = template.getCheckPoints().iterator();
		this.logContainer = logContainer;
		this.results = new ValidatorResult(template.getCheckPoints().iterator());
	}

	public boolean isValid() {
		while (this.checkPointIterator.hasNext()) {
			CheckPoint currentCheckPoint = this.checkPointIterator.next();
			boolean keepLooking = true;
			while (keepLooking) {
				String currentLine = getCurrentLineFor(currentCheckPoint);
				if (currentLine != null) {
					if (currentCheckPoint.matches(currentLine)) {
						this.results.setPassed(currentCheckPoint);

						logger.trace("Check Point definition: " + currentCheckPoint.getStringToMatch());
						if (this.checkPointIterator.hasNext()) {
							currentCheckPoint = this.checkPointIterator.next();
						} else {
							// if there are no more check points to check, then that means that we've reached the last
							// one and all of them have been verified.
							logger.info("All Check Points have SUCCESSFULLY passed.");
							return true;
						}
					} else { // if line does not match
						if (logger.isTraceEnabled()) {
							String message = String.format("Line doesn't match.  Checkpoint: %s.  Current Line: %s",
									currentCheckPoint.getStringToMatch(), currentLine);
							logger.trace(message);
						}
					}

				} else { // there are no more lines to check in log file
					setFailed(currentCheckPoint);
					return false;
				}
			}
		}
		return true;
	}

	private String getCurrentLineFor(CheckPoint currentCheckPoint) {
		return this.logContainer.getNextLines(currentCheckPoint.getNumberOfLines());
	}

	private void setFailed(CheckPoint currentCheckPoint) {
		String message = String.format("There are no more lines in Log file to check.  Check Point [%s] NOT reached.",
				currentCheckPoint.getName());
		logger.info(message);
		logger.debug(String.format(("Check Point definition: %s"), currentCheckPoint.getStringToMatch()));
		this.results.setFailed(currentCheckPoint);
		this.logContainer.close();
	}

	public ValidatorResult getResults() {
		return this.results;
	}

}
