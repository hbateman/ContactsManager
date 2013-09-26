package com.example.contactsmanager;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
	private ArrayList<Contact> contacts;
	private Contact selectedContact;
	private int element;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_contact);
		
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.show();
		
		Bundle data = getIntent().getExtras(); // Get extras from the intent
		element = data.getInt("element"); // Get the element of the selected contact
		contacts = data.getParcelableArrayList("contactList"); // Get the array of contacts
		selectedContact = contacts.get(element);
		
		// Set up the EditText fields and initialize them
		contactNameEditText = (EditText) findViewById(R.id.editNameText);
		contactNameEditText.setText(selectedContact.getName(), TextView.BufferType.EDITABLE);
		
		contactSurnameEditText = (EditText) findViewById(R.id.editSurnameText);
		contactSurnameEditText.setText(selectedContact.getSurname(), TextView.BufferType.EDITABLE);
		
		contactMobileEditText = (EditText) findViewById(R.id.editMobileText);
		contactMobileEditText.setText(selectedContact.getMobile(), TextView.BufferType.EDITABLE);
		
		contactWorkEditText = (EditText) findViewById(R.id.editWorkText);
		contactWorkEditText.setText(selectedContact.getWorkPhone(), TextView.BufferType.EDITABLE);
		
		contactHomeEditText = (EditText) findViewById(R.id.editHomeText);
		contactHomeEditText.setText(selectedContact.getHomePhone(), TextView.BufferType.EDITABLE);
		
		contactEmailEditText = (EditText) findViewById(R.id.editEmailText);
		contactEmailEditText.setText(selectedContact.getEmail(), TextView.BufferType.EDITABLE);
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
	            saveContact();
	            return true;
	    }
		return false;
	}
	
	public void saveContact() {
		
		selectedContact.setName(contactNameEditText.getText().toString());
		selectedContact.setSurname(contactSurnameEditText.getText().toString());
		selectedContact.setMobile(contactMobileEditText.getText().toString());
		selectedContact.setWork(contactWorkEditText.getText().toString());
		selectedContact.setHome(contactHomeEditText.getText().toString());
		selectedContact.setEmail(contactEmailEditText.getText().toString());
		
		contacts.set(element, selectedContact);
		
		for (int i = 0; i < contacts.size(); i++) {
			if (i == element) {
				continue;
			}
			if (selectedContact.equals(contacts.get(i))) {
				duplicateContactAlert();
				return;
			}
		}		
		AlertDialog dialog = new AlertDialog.Builder(EditContactActivity.this).create();
		dialog.setTitle("Save Successful");
		dialog.setMessage("Contact Successfully Saved");
		dialog.setButton(-1, "OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface theDialog, int which) {
				
			}
		});
		dialog.show();
	}
	
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

}
