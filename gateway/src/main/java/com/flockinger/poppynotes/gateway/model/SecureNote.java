package com.flockinger.poppynotes.gateway.model;

import javax.crypto.spec.IvParameterSpec;

public class SecureNote {
	private String content;
	private IvParameterSpec initVector;
	
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public IvParameterSpec getInitVector() {
		return initVector;
	}
	public void setInitVector(IvParameterSpec initVector) {
		this.initVector = initVector;
	}
}
