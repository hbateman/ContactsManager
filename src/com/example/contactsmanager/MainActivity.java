package com.example.contactsmanager;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class MainActivity extends Activity {
	
	private ArrayList<Contact> contacts = new ArrayList<Contact>();
	
	private ListView listView;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.show();
		
		listView = (ListView)findViewById(R.id.contactList);

		// This sets the ContactListAdapter which will populate the ListView with data from the contacts array
		setUpListView();
	}

	/** Inflate the menu; this adds items to the action bar if it is present. **/
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	/** Define what happens when a menu item is selected **/
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case R.id.action_search:
	            //openSearch();
	            return true;
	        case R.id.action_add_contact:
	        	//create a new contact
	        	newContact();
	            return true;
	    }
		return false;
	}
	
	/** Create a new contact **/
	public void newContact() {
		
		contacts.add(new Contact("", "", "", "", "", ""));
		Intent intent = new Intent(MainActivity.this, EditContactActivity.class);
		intent.putParcelableArrayListExtra("contactList", contacts);
		intent.putExtra("element", contacts.size() -1);
		startActivity(intent);
	}
	
	/** This adapter class will allow the data in the contacts array to be displayed in the  **/
	private class ContactListAdapter extends ArrayAdapter {

		// This constructs the ContactListAdapter
		public ContactListAdapter(Context context, List<Contact> contacts) {
			
			super(context, android.R.layout.simple_list_item_1, contacts);
			
		}
		
		// This gets the View of the contacts which will be used to display them on the screen
		public View getView(int position, View convertView, ViewGroup parent) {
			
			// Create a LayoutInflator to inflate the layout of the contact in the list
			LayoutInflater inflater = (LayoutInflater) MainActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			
			// Use the LayoutInflator
			View listContactView = inflater.inflate(R.layout.list_item_layout, null);
			
			// Retrieve the id's of the TextViews
			TextView name = (TextView)listContactView.findViewById(R.id.contact_text_name);
			TextView moblie = (TextView)listContactView.findViewById(R.id.contact_text_mobile);
			
			// Retrieve the specific information about the contact that needs to be displayed
			name.setText(contacts.get(position).getName());
			moblie.setText(contacts.get(position).getMobile());
			
			return listContactView;
		}
	}
	
	/** Creates the list view which will display the contacts**/
	private void setUpListView() {
		
		// Add some contacts to the list of contacts
		contacts.add(new Contact("Hugo", "Bateman", "0211081247", "2658789", "52458975", "hbat205@aucklanduni.ac.nz"));
		contacts.add(new Contact("James", "Butler", "0211475896", "12357894", "8464886", "jbaut@theplace.com"));
		contacts.add(new Contact("Luke", "Boyes", "0258964879", "1234567", "1234567", "lboy262@thePlace.com"));
		contacts.add(new Contact("Pat", "Bowen", "027896231", "1234567", "1234567", "lboy262@thePlace.com"));
		
		// Creates an adapter between the array containing the contacts and the activity
		ListAdapter listAdapter = new ContactListAdapter(MainActivity.this, contacts);
		listView.setAdapter(listAdapter);
		
		// Creates a listener which responds to clicks on list items
		listView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parentView, View clickedView, int clickedViewPosition, long id) {
				
				Intent intent = new Intent(MainActivity.this, EditContactActivity.class);
				intent.putParcelableArrayListExtra("contactList", contacts);
				intent.putExtra("element", clickedViewPosition);
				startActivity(intent);
			}			
		});
	}
}
