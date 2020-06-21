package com.packagename.myapp.enums;

public enum Parti {

	A_PARTISI("A_PARTISI"),
	B_PARTISI("B_PARTISI"),
	C_PARTISI("C_PARTISI");

	String code;

	private Parti(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}

}
