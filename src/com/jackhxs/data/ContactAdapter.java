package com.jackhxs.data;

import java.util.HashMap;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jackhxs.data.SQLiteDBHelper.SQLiteOpener;

public class ContactAdapter {
	public static final String ROW_ID = "_id";
	public static final String USERNAME = "username";
	public static final String REMOTE_ID = "remote_id";

	private static final String DATABASE_TABLE = "contact";

	private final Context mCtx;
	private SQLiteOpener mDbHelper;
	private SQLiteDatabase mDb;

	private Contact cursorToContact(Cursor contactCursor) {
		Contact contact = new Contact(contactCursor.getLong(0),
				contactCursor.getString(1), 
				contactCursor.getString(2));

		return contact;
	}

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

	public Map<String, String> getAllContactIds(String username) {
		Map<String, String> objectIds = new HashMap<String, String>();
		Cursor cursor = mDb.rawQuery("SELECT * FROM contact WHERE username = ? ", new String[] {username});
		cursor.moveToFirst();

		while (!cursor.isAfterLast()) {
			Long dbId = cursor.getLong(0);
			Integer remoteId = cursor.getInt(2);
			objectIds.put(remoteId.toString(), dbId.toString());
		}

		cursor.close();
		return objectIds;
	}

	public Contact getContact(String remoteId, String username) {
		Cursor cursor = mDb.rawQuery("SELECT * FROM contact WHERE remote_id = ? and username = ? ", new String[] {remoteId, username});
		cursor.moveToFirst();

		if (!cursor.isAfterLast()) {
			return this.cursorToContact(cursor);
		}

		return null;
	}

	public long createContact(Contact contact) {
		ContentValues initialValues = new ContentValues();

		initialValues.put(USERNAME, contact.getOwner());
		initialValues.put(REMOTE_ID, contact.getId());

		return this.mDb.insert(DATABASE_TABLE, null, initialValues);
	}


	public boolean deleteContact(long rowId) {
		return this.mDb.delete(DATABASE_TABLE, ROW_ID + "=" + rowId, null) > 0;
	}
}
