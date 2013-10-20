package com.example.contactsmanager;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
//import android.widget.TextView;

public class NewContactActivity extends Activity {
	private EditText contactNameEditText;
	private EditText contactSurnameEditText;
	private EditText contactMobileEditText;
	private EditText contactWorkEditText;
	private EditText contactHomeEditText;
	private EditText contactEmailEditText;
	private EditText contactAddressEditText;
	private EditText contactDOBEditText;
	private ContactsDatabase database = new ContactsDatabase(this);
	ArrayList<HashMap<String, String>> contactList;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_edit_contact);
		
		contactList = database.getAllContacts();
		
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
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_contact, menu);
		return true;
	}
	
	/** Define what happens when a menu item is selected **/
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case R.id.action_save:
	        	checkBeforeSave();
	            return true;
	            
	        case R.id.action_delete:
	            deleteContact();
	            return true;
	    }
		return false;
	}
	
	public void checkBeforeSave() {
		
		for (int i = 0; i < contactList.size(); i++) {
			if (contactNameEditText.getText().toString().equals(contactList.get(i).get("name"))) {
				if (contactSurnameEditText.getText().toString().equals(contactList.get(i).get("surname"))) {
					duplicateContactAlert();
					return;
				}
			}
		}
		save();
	}
	
	public void save() {
		
		HashMap<String, String> contactQueryMap =  new  HashMap<String, String>();
		
		contactQueryMap.put("name", contactNameEditText.getText().toString());
		contactQueryMap.put("surname", contactSurnameEditText.getText().toString());
		contactQueryMap.put("mobile", contactMobileEditText.getText().toString());
		contactQueryMap.put("homePhone", contactHomeEditText.getText().toString());
		contactQueryMap.put("workPhone", contactWorkEditText.getText().toString());
		contactQueryMap.put("email", contactEmailEditText.getText().toString());
		contactQueryMap.put("address", contactAddressEditText.getText().toString());
		contactQueryMap.put("dob", contactDOBEditText.getText().toString());
		
		database.insertContact(contactQueryMap);
		
		AlertDialog dialog = new AlertDialog.Builder(NewContactActivity.this).create();
		dialog.setTitle("Save Successful");
		dialog.setMessage("Contact Successfully Saved");
		dialog.setButton(-1, "OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface theDialog, int which) {
				
			}
		});
		dialog.show();
		Log.v("Edit contact", "Contact Saved");
		
		Intent intent = new Intent(NewContactActivity.this, MainActivity.class);
		startActivity(intent);
	}
	
	/** This will display a dialog informing the user that the contact they re trying to save already exists
	 * in the list of contacts**/
	public void duplicateContactAlert() {
		AlertDialog dialog = new AlertDialog.Builder(NewContactActivity.this).create();
		dialog.setTitle("Duplicate Contact Alert");
		dialog.setMessage("Contact Described Already Exists");
		dialog.setButton(-1, "OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface theDialog, int which) {
				
			}
		});
		dialog.show();
	}
	
	/** This will display a dialog confirming that the user wants to delete this contact. If the action is cancelled
	 * they will be returned to the activity, otherwise the contact will be deleted and they will be returned to
	 * the MainActivity**/
	public void deleteContact() {
		AlertDialog.Builder builder = new AlertDialog.Builder(NewContactActivity.this);
		builder.setTitle("No Can Do");
		builder.setMessage("Cannot Delete Contact That Has Not Yet Been Created");
		
		// If the user confirms the deletion
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            	//contacts.remove(element);
            	return;
            }
        });
		builder.show();
	}
}
