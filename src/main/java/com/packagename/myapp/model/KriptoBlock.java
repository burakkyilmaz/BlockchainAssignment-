package com.packagename.myapp.model;

public class KriptoBlock {

	private Integer index;
	private Long currentTimes;
	private String previousHash;
	private String value;
	private Integer nonce = 0;
	private String hash;

	public Long getCurrentTimes() {
		return currentTimes;
	}

	public void setCurrentTimes(Long currentTimes) {
		this.currentTimes = currentTimes;
	}

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	public String getPreviousHash() {
		return previousHash;
	}

	public void setPreviousHash(String previousHash) {
		this.previousHash = previousHash;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Integer getNonce() {
		return nonce;
	}

	public void setNonce(Integer nonce) {
		this.nonce = nonce;
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public String getConcatFields() {
		return index + currentTimes + previousHash + value + nonce;
	}

}
