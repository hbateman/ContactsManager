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
		
		contactList = database.getAllContacts();
		database.close();
		
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
		
		imageSetup();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
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
	
	public void imageSetup() {
		contactPicture = (ImageView) findViewById(R.id.contactImageView);
		contactPicture.setImageResource(R.drawable.ic_contact);
		
		contactPicture.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
			      Intent intent = new Intent(Intent.ACTION_PICK);
			      intent.setType("image/*");
			      Log.v("Image Selection", "Image Selection Started");
			      startActivityForResult(intent, 0);
			}
		});
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		super.onActivityResult(requestCode, resultCode, data);

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
		
		photo = ((BitmapDrawable)contactPicture.getDrawable()).getBitmap();
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		photo.compress(Bitmap.CompressFormat.PNG, 100, bos);
		byte[] bArray = bos.toByteArray();
		
		database.insertContact(contactQueryMap, bArray);
		database.close();
		
		confirmSaveAlert();
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
	
	public void confirmSaveAlert() {
		AlertDialog dialog = new AlertDialog.Builder(NewContactActivity.this).create();
		dialog.setTitle("Contact Save Successful");
		dialog.setMessage("Contact Has Been Saved");
		dialog.setButton(-1, "OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface theDialog, int which) {
				Intent intent = new Intent(getApplication(), MainActivity.class);
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
