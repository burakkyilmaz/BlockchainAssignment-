package com.packagename.myapp.model;

import com.packagename.myapp.enums.Parti;

public class KisiselPartiBilgileri {

	private String tcKimlikNo;
	private Integer dogumYili;
	private Parti partiSecimi;

	public String getTcKimlikNo() {
		return tcKimlikNo;
	}

	public void setTcKimlikNo(String tcKimlikNo) {
		this.tcKimlikNo = tcKimlikNo;
	}

	public Integer getDogumYili() {
		return dogumYili;
	}

	public void setDogumYili(Integer dogumYili) {
		this.dogumYili = dogumYili;
	}

	public Parti getPartiSecimi() {
		return partiSecimi;
	}

	public void setPartiSecimi(Parti partiSecimi) {
		this.partiSecimi = partiSecimi;
	}

}
