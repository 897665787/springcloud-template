package com.company.admin.oss.sts.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Policy {
	@JsonProperty("Version")
	private String Version;

	@JsonProperty("Statement")
	private List<Statement> Statement;

	public String getVersion() {
		return this.Version;
	}

	public void setVersion(String Version) {
		this.Version = Version;
	}

	public List<Statement> getStatement() {
		return this.Statement;
	}

	public void setStatement(List<Statement> Statement) {
		this.Statement = Statement;
	}
}
