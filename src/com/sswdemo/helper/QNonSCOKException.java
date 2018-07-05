package com.sswdemo.helper;

/**
 * @author liziqiang 该类用于在HTTPClient返回的状态非200的情况
 */
public class QNonSCOKException extends Exception {
	private static final long serialVersionUID = 1L;

	public QNonSCOKException(String detailMessage) {
		super(detailMessage);
		// TODO Auto-generated constructor stub
	}

	public QNonSCOKException() {
		super();
	}


	public QNonSCOKException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public QNonSCOKException(Throwable cause) {
		super(cause);
	}

}
