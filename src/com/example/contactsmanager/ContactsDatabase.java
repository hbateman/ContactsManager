package com.example.contactsmanager;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

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
	private static final String CONTACT_ADDRESS = "address";
	private static final String CONTACT_DOB = "DOB";
	
	private static final String CREATE_CONTACTS_TABLE = 
			"CREATE TABLE IF NO EXISTS" + TABLE_CONTACTS + " ("
			+ CONTACT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ CONTACT_NAME + " TEXT,"
			+ CONTACT_SURNAME + " TEXT,"
			+ CONTACT_MOBILE + " TEXT ,"
			+ CONTACT_HOMEPHONE + " TEXT,"
			+ CONTACT_WORKPHONE + " TEXT,"
			+ CONTACT_EMAIL + " TEXT, "
			+ CONTACT_ADDRESS + " TEXT "
			+ CONTACT_DOB + " TEXT)";
	
	private static final String SQL_DELETE_CARS_TABLE = "???";
	
	public ContactsDatabase(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public void onCreate(SQLiteDatabase db) {
		Log.v("DATABASE", "DATABASE_CREATE");
		db.execSQL(CREATE_CONTACTS_TABLE);
		final String FIRST_ENTRY = "INSERT INTO " + TABLE_CONTACTS + "VALUES('Hugo', 'Bateman', '0211238597', '8109351', '8464886', 'hbat206@gmail.com', '72 Martin Ave', '07/08/1991')";
		db.execSQL(FIRST_ENTRY);
	}
	
	public void insertContact(HashMap<String, String> queryValues) {
		
		SQLiteDatabase database = this.getWritableDatabase();
		
		ContentValues contentValues = new ContentValues();
		
		contentValues.put(ContactsDatabase.CONTACT_NAME, queryValues.get("name"));
		contentValues.put(ContactsDatabase.CONTACT_SURNAME, queryValues.get("surname"));
		contentValues.put(ContactsDatabase.CONTACT_MOBILE, queryValues.get("mobile"));
		contentValues.put(ContactsDatabase.CONTACT_HOMEPHONE, queryValues.get("homePhone"));
		contentValues.put(ContactsDatabase.CONTACT_WORKPHONE, queryValues.get("workPhone"));
		contentValues.put(ContactsDatabase.CONTACT_EMAIL, queryValues.get("email"));
		contentValues.put(ContactsDatabase.CONTACT_ADDRESS, queryValues.get("address"));
		contentValues.put(ContactsDatabase.CONTACT_DOB, queryValues.get("dob"));
		
		database.insert(ContactsDatabase.TABLE_CONTACTS, null, contentValues);
		
		database.close();	
	}
	
	public int updateContact(HashMap<String, String> queryValues) {
		
		SQLiteDatabase database = this.getWritableDatabase();
		
		ContentValues contentValues = new ContentValues();
		
		contentValues.put(ContactsDatabase.CONTACT_NAME, queryValues.get("name"));
		contentValues.put(ContactsDatabase.CONTACT_SURNAME, queryValues.get("surname"));
		contentValues.put(ContactsDatabase.CONTACT_MOBILE, queryValues.get("mobile"));
		contentValues.put(ContactsDatabase.CONTACT_HOMEPHONE, queryValues.get("homePhone"));
		contentValues.put(ContactsDatabase.CONTACT_WORKPHONE, queryValues.get("workPhone"));
		contentValues.put(ContactsDatabase.CONTACT_EMAIL, queryValues.get("email"));
		contentValues.put(ContactsDatabase.CONTACT_ADDRESS, queryValues.get("address"));
		contentValues.put(ContactsDatabase.CONTACT_DOB, queryValues.get("dob"));
		
		return database.update(TABLE_CONTACTS, contentValues,
				CONTACT_ID + " = ?", new String[] { queryValues.get(CONTACT_ID) });
	}
	
	public void deleteContact(String id) {
		
		SQLiteDatabase database = this.getWritableDatabase();
		
		String deleteQuery = "DELETE * FROM " + TABLE_CONTACTS + " where " + CONTACT_ID + "='" + id + "'";
		
		database.execSQL(deleteQuery);
	}

	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(SQL_DELETE_CARS_TABLE);
		onCreate(db);
	}
	
	public ArrayList<HashMap<String, String>> getAllContacts() {
		
		ArrayList<HashMap<String, String>> contactArrayList = new ArrayList<HashMap<String, String>>();
		
		String selectAllQuery = "SELECT * FROM " + TABLE_CONTACTS;
		
		SQLiteDatabase database = this.getWritableDatabase();
		
		Cursor cursor = database.rawQuery(selectAllQuery, null);
		
		if (cursor.moveToFirst()) {
			do {
				HashMap<String, String> contactMap = new HashMap<String, String>();
				
				contactMap.put("id", cursor.getString(0));
				contactMap.put("name", cursor.getString(1));
				contactMap.put("surname", cursor.getString(2));
				contactMap.put("mobile", cursor.getString(3));
				contactMap.put("homePhone", cursor.getString(4));
				contactMap.put("workPhone", cursor.getString(5));
				contactMap.put("email", cursor.getString(6));
				contactMap.put("address", cursor.getString(7));
				contactMap.put("dob", cursor.getString(8));
				
				contactArrayList.add(contactMap);
			} while (cursor.moveToNext());
		}
		return contactArrayList;
	}

	public ArrayList<HashMap<String, String>> getContact(String id) {
		
		ArrayList<HashMap<String, String>> contactArrayList = new ArrayList<HashMap<String, String>>();
		
		SQLiteDatabase database = this.getWritableDatabase();
		
		String contactQuery = "SELECT * FROM " + TABLE_CONTACTS + " where " + CONTACT_ID + "='" + id + "'";
		
		Cursor cursor = database.rawQuery(contactQuery, null);
		
		if (cursor.moveToFirst()) {
			do {
				HashMap<String, String> contactMap = new HashMap<String, String>();
				
				contactMap.put("id", cursor.getString(0));
				contactMap.put("name", cursor.getString(1));
				contactMap.put("surname", cursor.getString(2));
				contactMap.put("mobile", cursor.getString(3));
				contactMap.put("homePhone", cursor.getString(4));
				contactMap.put("workPhone", cursor.getString(5));
				contactMap.put("email", cursor.getString(6));
				contactMap.put("address", cursor.getString(7));
				contactMap.put("dob", cursor.getString(8));
				
				contactArrayList.add(contactMap);
			} while (cursor.moveToNext());
		}
		return contactArrayList;
	}
}
