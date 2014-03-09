package com.jackhxs.data;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum AccountType {
	NEW_ACCOUNT(0), EXISTING_ACCOUNT(1), LINKED_ACCOUNT(2);
	
	private static final Map<Integer,AccountType> lookup 
	= new HashMap<Integer,AccountType>();

	static {
		for(AccountType w : EnumSet.allOf(AccountType.class))
			lookup.put(w.getCode(), w);
	}

	private int code;

	private AccountType(int code) {
		this.code = code;
	}

	public int getCode() { return code; }

	public static AccountType get(int code) { 
		return lookup.get(code); 
	}	
}
