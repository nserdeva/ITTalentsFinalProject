package com.example.model.exceptions;

public final class UserException extends Exception {
	private String message = null;

	public UserException(String msg) {
		super(msg);
		this.message = msg;
	}

	@Override
	public String getMessage() {
		return this.message;
	}

}