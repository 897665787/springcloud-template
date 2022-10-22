package com.company.admin.oss.sts.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Statement {
	@JsonProperty("Effect")
	private String Effect;
	@JsonProperty("Action")
	private List<String> Action;
	@JsonProperty("Resource")
	private List<String> Resource;

	public String getEffect() {
		return this.Effect;
	}

	public void setEffect(String Effect) {
		this.Effect = Effect;
	}

	public List<String> getAction() {
		return this.Action;
	}

	public void setAction(List<String> Action) {
		this.Action = Action;
	}

	public List<String> getResource() {
		return this.Resource;
	}

	public void setResource(List<String> Resource) {
		this.Resource = Resource;
	}
}
