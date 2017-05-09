package com.skilrock.lms.common.exception;

public class LMSLogOutException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public LMSLogOutException() {
		super();
		System.out.println("hello  ");
	}

	public LMSLogOutException(Exception exp) {
		super(exp);
		System.out.println("hello");
	}

	public LMSLogOutException(String message) {

		super(message);
		System.out.println("hello  ");
	}

	public LMSLogOutException(String message, Exception exp) {
		super(message, exp);
		System.out.println("hello  " + message);
	}

}
