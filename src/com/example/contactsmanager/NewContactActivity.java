package com.example.contactsmanager;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;

public class NewContactActivity extends Activity {
	
	private EditText contactNameEditText;
	private EditText contactSurnameEditText;
	private EditText contactMobileEditText;
	private EditText contactWorkEditText;
	private EditText contactHomeEditText;
	private EditText contactEmailEditText;
	private EditText contactAddressEditText;
	private EditText contactDOBEditText;
	private ImageView contactPicture;
	private Bitmap photo;
	private ContactsDatabase database = new ContactsDatabase(this);
	ArrayList<HashMap<String, String>> contactList;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_contact);
		
		//Get contact information from database
		contactList = database.getAllContacts();
		database.close();
		
		//set up action bar
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.show();
		
		// Set up the EditText fields and initialize them
		contactNameEditText = (EditText) findViewById(R.id.editNameText);
		contactSurnameEditText = (EditText) findViewById(R.id.editSurnameText);
		contactMobileEditText = (EditText) findViewById(R.id.editMobileText);
		contactWorkEditText = (EditText) findViewById(R.id.editWorkText);
		contactHomeEditText = (EditText) findViewById(R.id.editHomeText);
		contactEmailEditText = (EditText) findViewById(R.id.editEmailText);
		contactAddressEditText = (EditText) findViewById(R.id.editAddressText);
		contactDOBEditText = (EditText) findViewById(R.id.editDOBText);
		
		// set the contacts image to the default
		imageSetup();
	}

	/** Inflate the menu; this adds items to the action bar if it is present. **/
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.new_contact, menu);
		return true;
	}
	
	/** Define what happens when a menu item is selected **/
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case R.id.action_save:
	        	checkBeforeSave();
	            return true;
	    }
		return false;
	}
	
	/** set the contacts image from the default found in res/drawable **/
	public void imageSetup() {
		contactPicture = (ImageView) findViewById(R.id.contactImageView);
		contactPicture.setImageResource(R.drawable.ic_contact);
		
		// Set the onClickListener to select an image from the gallery when the contacts photo is selected
		contactPicture.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
			      Intent intent = new Intent(Intent.ACTION_PICK);
			      intent.setType("image/*");
			      Log.v("Image Selection", "Image Selection Started");
			      startActivityForResult(intent, 0);
			}
		});
	}
	/** Called when the user returns with an image after selecting it from the gallery **/
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		// If the ActivityResult is good, set the contacts image to be the image returned
		if (resultCode == RESULT_OK){
			InputStream imageStream = null;
			Bitmap bitMap;
			try {
				imageStream = getContentResolver().openInputStream(data.getData());
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize =8;
			bitMap = BitmapFactory.decodeStream(imageStream,null,options);
			contactPicture.setImageBitmap(bitMap);
		}
	}
	
	/** Checks the database for duplicate contacts before saving. A duplicate exists if they share the same
	  * first name and last name **/
	public void checkBeforeSave() {
		for (int i = 0; i < contactList.size(); i++) {
			if (contactNameEditText.getText().toString().equals(contactList.get(i).get("name"))) {
				if (contactSurnameEditText.getText().toString().equals(contactList.get(i).get("surname"))) {
					Log.v("NewContactActivity Event", "Tried to save duplicate contact");
					duplicateContactAlert(); // If contact is a duplicate, inform user
					return;
				}
			}
		}
		save();
	}
	
	/** This will display a dialog informing the user that the contact they are trying to save already exists
	 * in the list of contacts**/
	public void duplicateContactAlert() {
		AlertDialog dialog = new AlertDialog.Builder(NewContactActivity.this).create();
		dialog.setTitle("Duplicate Contact Alert");
		dialog.setMessage("Contact Described Already Exists");
		dialog.setButton(-1, "OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface theDialog, int which) {
				return;
			}
		});
		dialog.show();
	}
	
	/** Save the contact to the database**/
	public void save() {
		HashMap<String, String> contactQueryMap =  new  HashMap<String, String>();
		
		// Store the contacts information in a Hashmap and pass it to the database
		contactQueryMap.put("name", contactNameEditText.getText().toString());
		contactQueryMap.put("surname", contactSurnameEditText.getText().toString());
		contactQueryMap.put("mobile", contactMobileEditText.getText().toString());
		contactQueryMap.put("homePhone", contactHomeEditText.getText().toString());
		contactQueryMap.put("workPhone", contactWorkEditText.getText().toString());
		contactQueryMap.put("email", contactEmailEditText.getText().toString());
		contactQueryMap.put("address", contactAddressEditText.getText().toString());
		contactQueryMap.put("dob", contactDOBEditText.getText().toString());
		
		// Convert contacts picture to a byte array so that it can be stored as a BLOB in the database
		photo = ((BitmapDrawable)contactPicture.getDrawable()).getBitmap();
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		photo.compress(Bitmap.CompressFormat.PNG, 100, bos);
		byte[] bArray = bos.toByteArray();
		
		database.insertContact(contactQueryMap, bArray);
		database.close();
		
		Log.v("New Contact Activity", "New contact inserted into database");
		confirmSaveAlert();
	}
	
	/** Display an AlertDialog informing the user the contact has been saved**/
	public void confirmSaveAlert() {
		AlertDialog dialog = new AlertDialog.Builder(NewContactActivity.this).create();
		dialog.setTitle("Contact Save Successful");
		dialog.setMessage("Contact Has Been Saved");
		dialog.setButton(-1, "OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface theDialog, int which) {
				// Return to MainActivity
				Intent intent = new Intent(getApplication(), MainActivity.class);
				Log.i("NewContactActivity Action", "Contact saved and returning to MainActivity");
				startActivity(intent);
			}
		});
		dialog.show();
	}
	
	public void callMainActivity() {
		Intent intent = new Intent(getApplication(), MainActivity.class);
		startActivity(intent);
	}
}
