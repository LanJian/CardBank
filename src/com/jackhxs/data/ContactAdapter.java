package com.jackhxs.data;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jackhxs.data.SQLiteDBHelper.SQLiteOpener;

public class ContactAdapter {
	public static final String USERNAME = "username";
	public static final String OBJECT_ID = "object_id";
	
	private final Context mCtx;
	private SQLiteOpener mDbHelper;
	private SQLiteDatabase mDb;

	public ContactAdapter(Context ctx) {
		this.mCtx = ctx;
	}
	
	public ContactAdapter open() {
		this.mDbHelper = new SQLiteDBHelper.SQLiteOpener(this.mCtx);
		this.mDb = this.mDbHelper.getWritableDatabase();
		return this;
	}
	
	public void close() {
		this.mDbHelper.close();
	}
	
	public List<String> getAllContact(String username) {
		List<String> objectIds = new ArrayList<String>();
		Cursor cursor = mDb.rawQuery("SELECT * FROM contact WHERE username = ? ", new String[] {username});
		cursor.moveToFirst();

		while (!cursor.isAfterLast()) {
			objectIds.add(cursor.getString(1));
		}

		cursor.close();
		return objectIds;
	}
	
	public Boolean addContact(Contact newContact) {
		return null;
	}
}
