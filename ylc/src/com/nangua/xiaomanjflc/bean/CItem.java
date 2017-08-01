package com.nangua.xiaomanjflc.bean;

public class CItem {
	private String name;
	private String value;

	public CItem() {
		name = "";
		value = "";
	}

	public CItem(String name, String value) {
		this.name = name;
		this.value = value;
	}

	@Override
	public String toString() {
		return name;
	}

	public String getNmae() {
		return name;
	}

	public String getValue() {
		return value;
	}
}
