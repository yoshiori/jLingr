package org.yoshiori.lingr.api;

/**
 * @author yoshiori
 * 
 */
public class LingrException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2723667249266872227L;

	/**
	 * 
	 */
	public LingrException() {
	}

	/**
	 * @param arg0
	 */
	public LingrException(String arg0) {
		super(arg0);
	}

	/**
	 * @param arg0
	 */
	public LingrException(Throwable arg0) {
		super(arg0);
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public LingrException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

}
