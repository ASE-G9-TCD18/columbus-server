package com.group9.columbus.entity;

import com.group9.columbus.enums.PreferenceType;

/**
 * A preference POJO
 */
public class Preference<String> {
	
	private PreferenceType preferenceType;
	
	private String value;

	public Preference() {
	}
	
	public Preference(PreferenceType preferenceType, String value) {
		super();
		this.preferenceType = preferenceType;
		this.value = value;
	}

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
