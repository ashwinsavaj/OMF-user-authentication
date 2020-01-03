package com.mindtree.authorization.social.response.entity;

public class Response {

	private int statusCode;

	private String status;

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Response(int statusCode, String status) {
		super();
		this.statusCode = statusCode;
		this.status = status;
	}

	public Response() {

	}
}
