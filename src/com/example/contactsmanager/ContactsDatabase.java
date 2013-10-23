package com.example.contactsmanager;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ContactsDatabase extends SQLiteOpenHelper {
	
	// Database version
	public static final int DATABASE_VERSION = 2;
	
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
	private static final String CONTACT_PHOTO = "photo";
	
	private static final String CREATE_CONTACTS_TABLE = 
			"CREATE TABLE IF NOT EXISTS " + TABLE_CONTACTS + " ("
			+ CONTACT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ CONTACT_NAME + " TEXT,"
			+ CONTACT_SURNAME + " TEXT,"
			+ CONTACT_MOBILE + " TEXT ,"
			+ CONTACT_HOMEPHONE + " TEXT,"
			+ CONTACT_WORKPHONE + " TEXT,"
			+ CONTACT_EMAIL + " TEXT,"
			+ CONTACT_ADDRESS + " TEXT,"
			+ CONTACT_DOB + " TEXT,"
			+ "photo" + " BLOB);";
	
	public ContactsDatabase(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public void onCreate(SQLiteDatabase db) {
		Log.v("DATABASE", "DATABASE_CREATE");
		db.execSQL(CREATE_CONTACTS_TABLE);
	}

	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		String query = "DROP TABLE IF EXISTS " + TABLE_CONTACTS;
		db.execSQL(query);
		onCreate(db);
	}
	
	/** Insert a new contact into the table **/
	public void insertContact(HashMap<String, String> queryValues, byte[] photo) {
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
		contentValues.put(ContactsDatabase.CONTACT_PHOTO, photo);
		
		database.insert(ContactsDatabase.TABLE_CONTACTS, null, contentValues);
		
		database.close();	
	}
	
	/** Update a specified contacts information in the table **/
	public int updateContact(HashMap<String, String> queryValues, byte[] photo) {
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
		contentValues.put(ContactsDatabase.CONTACT_PHOTO, photo);
		
		return database.update(TABLE_CONTACTS, contentValues,
				CONTACT_ID + " = ?", new String[] { queryValues.get(CONTACT_ID) });
	}
	
	/** Delete a specified contact in the table **/
	public void deleteContact(String id) {
		SQLiteDatabase database = this.getWritableDatabase();
		
		String deleteQuery = "DELETE FROM " + TABLE_CONTACTS + " where " + CONTACT_ID + "='" + id + "'";
		
		database.execSQL(deleteQuery);
	}
	
	/** Retrieve all the contacts from the table and order them by their name **/
	public ArrayList<HashMap<String, String>> getAllContacts() {
		
		ArrayList<HashMap<String, String>> contactArrayList = new ArrayList<HashMap<String, String>>();
		
		String selectAllQuery = "SELECT * FROM " + TABLE_CONTACTS + " ORDER BY " + CONTACT_NAME;
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
	
	/** Retrieve all the contacts from the table and order them by their surname's **/
	public ArrayList<HashMap<String, String>> getContactsBySurname() {
		
		ArrayList<HashMap<String, String>> contactArrayList = new ArrayList<HashMap<String, String>>();
		
		String selectAllQuery = "SELECT * FROM " + TABLE_CONTACTS + " ORDER BY " + CONTACT_SURNAME;
		
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
	
	/** Retrieve all the contacts from the table and order them by their mobile numbers **/
	public ArrayList<HashMap<String, String>> getContactsByNumber() {
		
		ArrayList<HashMap<String, String>> contactArrayList = new ArrayList<HashMap<String, String>>();
		
		String selectAllQuery = "SELECT * FROM " + TABLE_CONTACTS + " ORDER BY " + CONTACT_MOBILE;
		
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

	/** Retrieve the information about a specific contact **/
	public HashMap<String, String> getContact(String id) {
		
		HashMap<String, String> contactMap = new HashMap<String, String>();
		
		SQLiteDatabase database = this.getWritableDatabase();
		
		String contactQuery = "SELECT * FROM " + TABLE_CONTACTS + " WHERE " + CONTACT_ID + "='" + id + "'";
		
		Cursor cursor = database.rawQuery(contactQuery, null);
		
		if (cursor.moveToFirst()) {
			do {
				contactMap.put("id", cursor.getString(0));
				contactMap.put("name", cursor.getString(1));
				contactMap.put("surname", cursor.getString(2));
				contactMap.put("mobile", cursor.getString(3));
				contactMap.put("homePhone", cursor.getString(4));
				contactMap.put("workPhone", cursor.getString(5));
				contactMap.put("email", cursor.getString(6));
				contactMap.put("address", cursor.getString(7));
				contactMap.put("dob", cursor.getString(8));

			} while (cursor.moveToNext());
		}
		return contactMap;
	}
	
	/** Retrieve the byte code which represents the specified contacts photo **/
	public byte[] getContactPhoto(String id) {
		
		SQLiteDatabase database = this.getWritableDatabase();
		
		String contactQuery = "SELECT * FROM " + TABLE_CONTACTS + " WHERE " + CONTACT_ID + "='" + id + "'";
		
		Cursor cursor = database.rawQuery(contactQuery, null);
		
		byte[] photo = null;
		
		if (cursor.moveToFirst()) {
			do {
				photo = cursor.getBlob(9);
			} while (cursor.moveToNext());
		}
		
		return photo;	
	}
}
