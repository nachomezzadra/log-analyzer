package com.avaya.aps.sca.components.loganalyzer.validation;

import java.util.HashMap;
import java.util.Iterator;

import com.avaya.aps.sca.components.loganalyzer.checkpoint.CheckPoint;

public class ValidatorResult {

	private HashMap<CheckPoint, Boolean> results;

	public ValidatorResult(Iterator<CheckPoint> checkPoints) {
		this.results = initResults(checkPoints);
	}

	private HashMap<CheckPoint, Boolean> initResults(Iterator<CheckPoint> checkPoints) {
		HashMap<CheckPoint, Boolean> map = new HashMap<CheckPoint, Boolean>();
		while (checkPoints.hasNext()) {
			CheckPoint eachCheckPoint = (CheckPoint) checkPoints.next();
			map.put(eachCheckPoint, false);
		}
		return map;
	}

	public void setPassed(CheckPoint checkPoint) {
		this.results.put(checkPoint, true);

	}

	public HashMap<CheckPoint, Boolean> getResults() {
		return this.results;
	}

	public void setFailed(CheckPoint checkPoint) {
		this.results.put(checkPoint, false);
	}

	public boolean isValid(CheckPoint aCheckPoint) {
		return this.results.get(aCheckPoint).booleanValue();
	}
}
