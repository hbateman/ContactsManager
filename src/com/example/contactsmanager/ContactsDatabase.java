package com.example.contactsmanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class ContactsDatabase extends SQLiteOpenHelper {
	
	// Database version
	public static final int DATABASE_VERSION = 1;
	
	// Database name
	public static final String DATABASE_NAME = "contactsdatabase.db";
	
	// Cars table name
	public static final String TABLE_CONTACTS = "ContactsTable";
	
	// Cars table column
	private static final String CONTACT_ID = "id";
	private static final String CONTACT_NAME = "name";
	private static final String CONTACT_SURNAME = "surname";
	private static final String CONTACT_MOBILE = "mobile";
	private static final String CONTACT_HOMEPHONE = "workPhone";
	private static final String CONTACT_WORKPHONE = "homePhone";
	private static final String CONTACT_EMAIL = "email";
	
	private static final String CREATE_CARS_TABLE = 
			"CREATE TABLE IF NO EXISTS" + TABLE_CONTACTS + " ("
			+ CONTACT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ CONTACT_NAME + " TEXT,"
			+ CONTACT_SURNAME + " TEXT,"
			+ CONTACT_MOBILE + " TEXT ,"
			+ CONTACT_HOMEPHONE + " TEXT,"
			+ CONTACT_WORKPHONE + " TEXT,"
			+ CONTACT_EMAIL + " TEXT);";
	
	private static final String SQL_DELETE_CARS_TABLE = "???";
	
	public ContactsDatabase(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_CARS_TABLE);
		final String FIRST_ENTRY = "INSERT INTO " + "TABLE CARS " + "VALUES('Honda', 'Civic', 'Red')";
		db.execSQL(FIRST_ENTRY);
		final String SECOND_ENTRY = "INSERT INTO " + "TABLE CARS " + "VALUES('Mazda', 'Familia', 'Red')";
		db.execSQL(SECOND_ENTRY);
		final String THIRD_ENTRY = "INSERT INTO " + "TABLE CARS " + "VALUES('Nissan', 'Primera', 'Black')";
		db.execSQL(THIRD_ENTRY);
	}
	
	/*public void insertData(String make, String model, String colour) {
		ContentValues contentValues = new ContentValues();
		contentValues.put(ContactsDatabase.CARS_MAKE, make);
		contentValues.put(ContactsDatabase.CARS_MODEL, model);
		contentValues.put(ContactsDatabase.CARS_COLOUR, colour);
		
		this.getWritableDatabase().insert(ContactsDatabase.TABLE_CARS, null, contentValues);
	}*/

	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(SQL_DELETE_CARS_TABLE);
		onCreate(db);
	}
	
	public Cursor getAllData() {
		String buildSQL = "SELECT " + "FROM " + this.TABLE_CONTACTS;
		return this.getReadableDatabase().rawQuery(buildSQL, null);
	}

}
