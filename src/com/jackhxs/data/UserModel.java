package com.jackhxs.data;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class UserModel {
	private SQLiteDBHelper.SQLiteOpener mDbHelper;
	private SQLiteDatabase mDb;
	private final Context mCtx;
	
	private String user;
	private CardDBAdapter cardDBHelper;
	private ContactAdapter contactDBHelper;
	
	/**
	 * Constructor - takes the context to allow the database to be
	 * opened/created
	 * 
	 * @param ctx
	 *            the Context within which to work
	 */
	public UserModel(Context ctx) {
		this.mCtx = ctx;
		cardDBHelper = new CardDBAdapter(ctx);
		contactDBHelper = new ContactAdapter(ctx);
		
		cardDBHelper.open();
		contactDBHelper.open();
	}

	/**
	 * Open the database. If it cannot be opened, try to create a new
	 * instance of the database. If it cannot be created, throw an exception to
	 * signal the failure
	 * 
	 * @return this (self reference, allowing this to be chained in an
	 *         initialization call)
	 * @throws SQLException
	 *             if the database could be neither opened or created
	 */
	public UserModel open() throws SQLException {
		this.mDbHelper = new SQLiteDBHelper.SQLiteOpener(this.mCtx);
		this.mDb = this.mDbHelper.getWritableDatabase();
		return this;
	}

	/**
	 * close return type: void
	 */
	public void close() {
		this.mDbHelper.close();
	}

}
