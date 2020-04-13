package com.oneiron.exception;

public abstract class BaseRuntimeException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 195467534634L;
	
	public BaseRuntimeException(String message) {
		super(message);
	}
	
	public abstract Integer getCode();

}
