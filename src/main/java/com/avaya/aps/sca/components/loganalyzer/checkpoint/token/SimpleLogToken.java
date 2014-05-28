package com.avaya.aps.sca.components.loganalyzer.checkpoint.token;

public class SimpleLogToken extends LogToken {

	private final String token;

	public SimpleLogToken(String token) {
		this.token = token;
	}

	@Override
	public String getTokenValue() {
		return this.token;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.token == null) ? 0 : this.token.hashCode());
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
		SimpleLogToken other = (SimpleLogToken) obj;
		if (this.token == null) {
			if (other.token != null) {
				return false;
			}
		} else if (!this.token.equals(other.token)) {
			return false;
		}
		return true;
	}

}
