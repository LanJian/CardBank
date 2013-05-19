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
  public static final String VERSION = "version";
 
  private static final String DATABASE_TABLE = "cards";

  private SQLiteOpener mDbHelper;
  private SQLiteDatabase mDb;
  private final Context mCtx;

  private Card cursorToCard(Cursor cardCursor) {
	  Card card = new Card(cardCursor.getLong(0),
			  				cardCursor.getString(1),
			  				cardCursor.getString(2),
			  				cardCursor.getString(3),
			  				cardCursor.getString(4),
			  				cardCursor.getString(5),
			  				cardCursor.getInt(6));
	  return card;
  }
  
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
  public long createCard(Card newCard) {
	  
      ContentValues initialValues = new ContentValues();
      
      initialValues.put(FIRST_NAME, newCard.getFirstName());
      initialValues.put(LAST_NAME, newCard.getLastName());
      initialValues.put(EMAIL, newCard.getEmail());
      initialValues.put(PHONE_NUMBER, newCard.getPhoneNumber());
      initialValues.put(OBJECT_ID, newCard.getObjectId());
      initialValues.put(VERSION, newCard.getVersion() + 1);
	  
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
   * Return a Cursor positioned at the car that matches the given rowId
   * @param rowId
   * @return Cursor positioned to matching car, if found
   * @throws SQLException if car could not be found/retrieved
   */
  public Card getCard(String objectId) throws SQLException {

      Cursor mCursor = mDb.rawQuery("FROM cards WHERE object_id = ?", new String[]{objectId});

      if (mCursor != null) {
          mCursor.moveToFirst();
          return cursorToCard(mCursor);
      }
      
	  return null;
  }

  public boolean updateCard(Card card) {

	  ContentValues newValues = new ContentValues();

	  newValues.put(FIRST_NAME, card.getFirstName());
	  newValues.put(LAST_NAME, card.getLastName());
	  newValues.put(EMAIL, card.getEmail());
	  newValues.put(PHONE_NUMBER, card.getPhoneNumber());
	  newValues.put(OBJECT_ID, card.getObjectId());
	  newValues.put(VERSION, card.getVersion() + 1);

	  return this.mDb.update(DATABASE_TABLE, newValues, ROW_ID + "=" + card.getRowId(), null) >0;
  }
}