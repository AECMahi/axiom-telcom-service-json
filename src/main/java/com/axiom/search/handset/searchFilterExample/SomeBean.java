package com.axiom.search.handset.searchFilterExample;

import com.fasterxml.jackson.annotation.JsonFilter;

@JsonFilter("SomeBeanFilter")
public class SomeBean {
	private String phone;
	private String sim;
//JsonIgnore indicates that the annotated method or field is to be ignored
//@JsonIgnore
	private String resolution;

//generating constructor
	public SomeBean(String phone, String sim, String resolution) {
		super();
		this.phone = phone;
		this.sim = sim;
		this.resolution = resolution;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getSim() {
		return sim;
	}

	public void setSim(String sim) {
		this.sim = sim;
	}

	public String getResolution() {
		return resolution;
	}

	public void setResolution(String resolution) {
		this.resolution = resolution;
	}

}
