package com.jackhxs.data;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteDBHelper {
  /*
    3 Table Architecture
    -> user table: 
        username
        password
        objectId - unique
        
    -> card entry table: 
        objectId - unique
        owner - a list
        
    -> contact table:
    	username
    	objectId
  */

  public static final String DATABASE_NAME = "cardbank";
  public static final int DATABASE_VERSION = 1;

  private static final String CREATE_TABLE_USER = 
    "create table user (_id integer primary key autoincrement, " //$NON-NLS-1$
    + UserDBAdapter.USERNAME +" TEXT," //$NON-NLS-1$
    + UserDBAdapter.HASHEDPWD +" TEXT"+ ");"; //$NON-NLS-1$  //$NON-NLS-2$

  private static final String CREATE_TABLE_CARD =
    "create table card (_id integer autoincrement, " //$NON-NLS-1$
    + CardDBAdapter.FIRST_NAME + " TEXT," //$NON-NLS-1$
    + CardDBAdapter.LAST_NAME + " TEXT," //$NON-NLS-1$
    + CardDBAdapter.EMAIL + " TEXT," //$NON-NLS-1$
    + CardDBAdapter.PHONE_NUMBER + " TEXT," //$NON-NLS-1$
    + CardDBAdapter.OBJECT_ID + " INTEGER primary key" + ");"; //$NON-NLS-1$ //$NON-NLS-2$

  private static final String CREATE_TABLE_CONTACT = "create table contact (_id integer primary key autoincrement, " //$NON-NLS-1$
  +ContactAdapter.USERNAME + " TEXT," //$NON-NLS-1$
  +ContactAdapter.OBJECT_ID+" INTEGER"+ ");"; //$NON-NLS-1$  //$NON-NLS-2$

  private final Context context; 
  private DatabaseHelper DBHelper;
  private SQLiteDatabase db;


  public static class SQLiteOpener extends SQLiteOpenHelper {
	  SQLiteOpener(Context context) {
          super(context, SQLiteDBHelper.DATABASE_NAME, null, SQLiteDBHelper.DATABASE_VERSION);
      }

      @Override
      public void onCreate(SQLiteDatabase db) {
      }

      @Override
      public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
      }
  }

  /**
   * Constructor
   * @param ctx
   */
  public SQLiteDBHelper(Context ctx)
  {
      this.context = ctx;
      this.DBHelper = new DatabaseHelper(this.context);
  }

  private static class DatabaseHelper extends SQLiteOpenHelper 
  {
      DatabaseHelper(Context context) {
          super(context, DATABASE_NAME, null, DATABASE_VERSION);
      }

      @Override
      public void onCreate(SQLiteDatabase db) {
          db.execSQL(CREATE_TABLE_USER);
          db.execSQL(CREATE_TABLE_CARD);
          db.execSQL(CREATE_TABLE_CONTACT);           
      }

      @Override
      public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {               
          // Adding any table mods to this guy here
      }
  } 

 /**
   * open the db
   * @return this
   * @throws SQLException
   * return type: DBAdapter
   */
  public SQLiteDBHelper open() throws SQLException 
  {
      this.db = this.DBHelper.getWritableDatabase();
      return this;
  }

  /**
   * close the db 
   * return type: void
   */
  public void close() 
  {
      this.DBHelper.close();
  }
}