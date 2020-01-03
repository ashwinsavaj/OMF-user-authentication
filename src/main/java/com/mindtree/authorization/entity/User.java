package com.mindtree.authorization.entity;

import java.io.Serializable;

@SuppressWarnings("serial")
public class User implements Serializable {
	
	private String userName;
	private String password;
	private String number;
	private String name;
	private boolean isActive = true;
	private String address;

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Override
	public String toString() {
		return "User [userName=" + userName + ", password=" + password + ", number=" + number + ", name=" + name
				+ ", isActive=" + isActive + ", address=" + address + "]";
	}


	
}
