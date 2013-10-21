package com.example.contactsmanager;

import java.util.ArrayList;
import java.util.HashMap;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

public class EditContactActivity extends Activity {
	
	private EditText contactNameEditText;
	private EditText contactSurnameEditText;
	private EditText contactMobileEditText;
	private EditText contactWorkEditText;
	private EditText contactHomeEditText;
	private EditText contactEmailEditText;
	private EditText contactAddressEditText;
	private EditText contactDOBEditText;
	private ContactsDatabase database = new ContactsDatabase(this);
	HashMap<String, String> contactInfo;
	ArrayList<HashMap<String, String>> contactList;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_contact);
		
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
		
		contactList = database.getAllContacts();
		
		Intent intent = getIntent();
		String contactId = intent.getStringExtra("contactId");
		
		contactInfo = database.getContact(contactId);
		
		if(contactInfo.size() != 0) {
			contactNameEditText.setText(contactInfo.get("name"), TextView.BufferType.EDITABLE);
			contactSurnameEditText.setText(contactInfo.get("surname"), TextView.BufferType.EDITABLE);
			contactMobileEditText.setText(contactInfo.get("mobile"), TextView.BufferType.EDITABLE);
			contactWorkEditText.setText(contactInfo.get("workPhone"), TextView.BufferType.EDITABLE);
			contactHomeEditText.setText(contactInfo.get("homePhone"), TextView.BufferType.EDITABLE);
			contactEmailEditText.setText(contactInfo.get("email"), TextView.BufferType.EDITABLE);
			contactAddressEditText.setText(contactInfo.get("address"), TextView.BufferType.EDITABLE);
			contactDOBEditText.setText(contactInfo.get("dob"), TextView.BufferType.EDITABLE);
		}	
	}

	// Inflate the menu; this adds items to the action bar if it is present.
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.edit_contact, menu);
		return true;
	}
	
	/** Define what happens when a menu item is selected **/
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle selection of action bar items
	    switch (item.getItemId()) {
	        case R.id.action_save:
	            saveContact();
	            return true;
	            
	        case R.id.action_delete:
	            deleteContactAlert();
	            return true;
	    }
		return false;
	}
	
	public void saveContact() {

		for (int i = 0; i < contactList.size(); i++) {
			if (contactList.get(i).get("id").equals(contactInfo.get("id"))) {
				continue;
			}
			if (contactNameEditText.getText().toString().equals(contactList.get(i).get("name"))) {
				if (contactSurnameEditText.getText().toString().equals(contactList.get(i).get("surname"))) {
					duplicateContactAlert();
					return;
				}
			}
		}		
		updateContact();
	}
	
	/** This will display a dialog informing the user that the contact they are trying to save already exists
	 * in the list of contacts**/
	public void duplicateContactAlert() {
		AlertDialog dialog = new AlertDialog.Builder(EditContactActivity.this).create();
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
	public void deleteContactAlert() {
		AlertDialog.Builder builder = new AlertDialog.Builder(EditContactActivity.this);
		builder.setTitle("Delete Contact?");
		builder.setMessage("Are you sure you want to delete this contact?");
		
		// If the user cancels the deletion
		builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                return;
            }
        });
		
		// If the user confirms the deletion
		builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            	deleteContact();
            }
        });
		builder.show();
	}
	
	public void deleteContact() {

		Intent intent = getIntent();
		String contactId = intent.getStringExtra("contactId");
		
		database.deleteContact(contactId);
		
		callMainActivity();
	}
	
	/** Update the contacts list with any new information**/
	public void updateContact() {
		HashMap<String, String> newInfo = new HashMap<String, String>();
		
		// Set up the EditText fields and initialize them
		contactNameEditText = (EditText) findViewById(R.id.editNameText);
		contactSurnameEditText = (EditText) findViewById(R.id.editSurnameText);
		contactMobileEditText = (EditText) findViewById(R.id.editMobileText);
		contactWorkEditText = (EditText) findViewById(R.id.editWorkText);
		contactHomeEditText = (EditText) findViewById(R.id.editHomeText);
		contactEmailEditText = (EditText) findViewById(R.id.editEmailText);
		contactAddressEditText = (EditText) findViewById(R.id.editAddressText);
		contactDOBEditText = (EditText) findViewById(R.id.editDOBText);
		
		Intent intent = getIntent();
		
		String contactId = intent.getStringExtra("contactId");
		
		newInfo.put("id", contactId);
		newInfo.put("name", contactNameEditText.getText().toString());
		newInfo.put("surname", contactSurnameEditText.getText().toString());
		newInfo.put("mobile", contactMobileEditText.getText().toString());
		newInfo.put("workPhone", contactWorkEditText.getText().toString());
		newInfo.put("homePhone", contactHomeEditText.getText().toString());
		newInfo.put("email", contactEmailEditText.getText().toString());
		newInfo.put("address", contactAddressEditText.getText().toString());
		newInfo.put("dob", contactDOBEditText.getText().toString());
		
		database.updateContact(newInfo);
		
		AlertDialog dialog = new AlertDialog.Builder(EditContactActivity.this).create();
		dialog.setTitle("Save Successful");
		dialog.setMessage("Contact Successfully Saved");
		dialog.setButton(-1, "OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface theDialog, int which) {
				
			}
		});
		dialog.show();
		Log.v("Edit contact", "Contact Edit Saved");
		
		callMainActivity();
	}
	
	public void callMainActivity() {
		Intent intent = new Intent(EditContactActivity.this, MainActivity.class);
		startActivity(intent);
	}
}
