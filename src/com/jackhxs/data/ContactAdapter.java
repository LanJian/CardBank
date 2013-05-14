package com.jackhxs.data;

import java.util.List;

import android.content.Context;

public class ContactAdapter {
	public static final String USERNAME = "username";
	public static final String OBJECT_ID = "object_id";
	private final Context mCtx;
	
	public ContactAdapter(Context ctx) {
		this.mCtx = ctx;
	}
	
	public ContactAdapter open() {
		return null;
	}
	
	public List<Card> getAllContact() {
		return null;
		/*
		List<Card> messages = new ArrayList<Card>();
		Cursor cursor = mDb.rawQuery("SELECT * FROM contact WHERE ", null);//, new String[] {});

		cursor.moveToFirst();

		while (!cursor.isAfterLast()) {
			MessageModel message = cursorToMessageModel(cursor);
			messages.add(message);
			cursor.moveToNext();
		}

		// Make sure to close the cursor
		cursor.close();
		return messages;
		 */
	}
}
