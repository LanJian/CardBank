package com.jackhxs.data;

import com.jackhxs.data.SQLiteDBHelper.SQLiteOpener;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CardDBAdapter {
	
  public static final String ROW_ID = "_id";
  public static final String FIRST_NAME = "first_name";
  public static final String LAST_NAME = "last_name";
  public static final String EMAIL = "email";
  public static final String PHONE_NUMBER = "phone_number";
  public static final String OBJECT_ID = "object_id";
 
  private static final String DATABASE_TABLE = "cards";

  private SQLiteOpener mDbHelper;
  private SQLiteDatabase mDb;
  private final Context mCtx;

  /**
   * Constructor - takes the context to allow the database to be
   * opened/created
   * 
   * @param ctx
   *            the Context within which to work
   */
  public CardDBAdapter(Context ctx) {
      this.mCtx = ctx;
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
  public CardDBAdapter open() throws SQLException {
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

  /**
   * Create a new car. If the car is successfully created return the new
   * rowId for that car, otherwise return a -1 to indicate failure.
   * 
   * @param name
   * @param model
   * @param year
   * @return rowId or -1 if failed
   */
  public long createCard(String firstName,
		  				 String lastName,
		  				 String email,
		  				 String phoneNumber,
		  				 String objectId) {
	  
      ContentValues initialValues = new ContentValues();
      
      initialValues.put(FIRST_NAME, firstName);
      initialValues.put(LAST_NAME, lastName);
      initialValues.put(EMAIL, email);
      initialValues.put(PHONE_NUMBER, phoneNumber);
      initialValues.put(OBJECT_ID, objectId);
      
      return this.mDb.insert(DATABASE_TABLE, null, initialValues);
  }

  /**
   * Delete the car with the given rowId
   * 
   * @param rowId
   * @return true if deleted, false otherwise
   */
  public boolean deleteCard(long rowId) {

      return this.mDb.delete(DATABASE_TABLE, ROW_ID + "=" + rowId, null) > 0;
  }

  /**
   * Return a Cursor over the list of all cars in the database
   * 
   * @return Cursor over all cars
   */
  public Cursor getAllCards() {
      /*return this.mDb.query(DATABASE_TABLE, new String[] { ROW_ID,
              NAME, MODEL, YEAR }, null, null, null, null, null);
       */
	  return null;
  }
  
  /**
   * Return a Cursor positioned at the car that matches the given rowId
   * @param rowId
   * @return Cursor positioned to matching car, if found
   * @throws SQLException if car could not be found/retrieved
   */
  public Cursor getCard(long rowId) throws SQLException {
	  /*
      Cursor mCursor =

      this.mDb.query(true, DATABASE_TABLE, new String[] { ROW_ID, NAME,
              MODEL, YEAR}, ROW_ID + "=" + rowId, null, null, null, null, null);
      if (mCursor != null) {
          mCursor.moveToFirst();
      }
      return mCursor;
      */
	  return null;
  }

  /**
   * Update the car.
   * 
   * @param rowId
   * @param name
   * @param model
   * @param year
   * @return true if the note was successfully updated, false otherwise
   */
  public boolean updateCard(long rowId, String name, String model,
          String year){
	  /*
      ContentValues args = new ContentValues();
      args.put(NAME, name);
      args.put(MODEL, model);
      args.put(YEAR, year);

      return this.mDb.update(DATABASE_TABLE, args, ROW_ID + "=" + rowId, null) >0; 
	  */
	  return true;
  }

}