package com.group9.columbus.entity;

import com.group9.columbus.enums.PreferenceType;

public class Preference {
	
	private PreferenceType preferenceType;
	
	private String value;

	public PreferenceType getPreferenceType() {
		return preferenceType;
	}

	public void setPreferenceType(PreferenceType preferenceType) {
		this.preferenceType = preferenceType;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
