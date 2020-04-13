package com.oneiron.exception;

public class ApiException extends BaseRuntimeException{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 17876867943569L;
	private ErrorType errorType;

	public ApiException(ErrorType errorStatus) {
		super(errorStatus.getMessage());
		this.errorType = errorStatus;
	}

	@Override
	public Integer getCode() {
		// TODO Auto-generated method stub
		return this.errorType.getCode();
	}

}
