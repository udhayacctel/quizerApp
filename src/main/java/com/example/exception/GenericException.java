package com.example.exception;

public class GenericException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	public GenericException(String msg){
		super(msg);
	}
}

