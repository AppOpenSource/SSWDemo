package com.sswdemo.helper;

/**
 * 该类用于在HTTPClient返回的状态非200的情况
 */
public class AppNonSCOKException extends Exception {
	private static final long serialVersionUID = 1L;

	public AppNonSCOKException(String detailMessage) {
		super(detailMessage);
	}

	public AppNonSCOKException() {
		super();
	}


	public AppNonSCOKException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public AppNonSCOKException(Throwable cause) {
		super(cause);
	}

}
