package com.jackhxs.remote;

public final class Constants {
	/** Status Constants */
	public static final int STATUS_RUNNING = 0;
	public static final int STATUS_FINISHED = 1;
	public static final int STATUS_ERROR  = 2;

	/** REST Interface Constants */
	public enum Operation {
		GET_CARDS,
		GET_CONTACTS,
		POST_LOGIN,
		POST_CARD
	}
	
	public final static Operation[] OperationVals = Operation.values();
	
	/**
	   The caller references the constants using <tt>Consts.EMPTY_STRING</tt>, 
	   and so on. Thus, the caller should be prevented from constructing objects of 
	   this class, by declaring this private constructor. 
	 */
	private Constants(){
		//this prevents even the native class from 
		//calling this ctor as well :
		throw new AssertionError();
	}
}
