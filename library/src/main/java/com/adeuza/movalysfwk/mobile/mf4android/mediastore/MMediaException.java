package com.adeuza.movalysfwk.mobile.mf4android.mediastore;

public class MMediaException extends Exception {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -9114363519667056732L;

	public MMediaException() {
		super();
	}

	public MMediaException(String p_oMessage, Throwable p_oCause) {
		super(p_oMessage, p_oCause);
	}

	public MMediaException(String p_oMessage) {
		super(p_oMessage);
	}

	public MMediaException(Throwable p_oCause) {
		super(p_oCause);
	}
}
