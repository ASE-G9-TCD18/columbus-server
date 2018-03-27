package com.group9.columbus.entity;

import com.group9.columbus.enums.PreferenceType;

/**
 * A preference POJO
 */
public class Preference<T> {
	
	private PreferenceType preferenceType;
	
	private T value;

	public Preference() {
	}
	
	public Preference(PreferenceType preferenceType, T value) {
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

	public T getValue() {
		return value;
	}

	public void setValue(T value) {
		this.value = value;
	}

}
