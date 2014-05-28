package com.avaya.aps.sca.components.loganalyzer.checkpoint.token;

import com.avaya.aps.sca.components.loganalyzer.checkpoint.CheckPointVariables;

public class VariableLogToken extends LogToken {

	private final String key;

	public VariableLogToken(String variableName) {
		this.key = variableName;
	}

	@Override
	public String getTokenValue() {
		return CheckPointVariables.getInstance().getValueFor(this.key);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.key == null) ? 0 : this.key.hashCode());
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
		VariableLogToken other = (VariableLogToken) obj;
		if (this.key == null) {
			if (other.key != null) {
				return false;
			}
		} else if (!this.key.equals(other.key)) {
			return false;
		}
		return true;
	}

}
