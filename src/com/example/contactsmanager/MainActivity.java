package com.example.contactsmanager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class MainActivity extends ListActivity {
	
	private String[] sortOptions = {"Name", "Surname"};
	private ListView listView;
	private ContactsDatabase database = new ContactsDatabase(this);
	private TextView contactId;
	ArrayList<HashMap<String, String>> contactList;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// set up action bar
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.show();

		// This sets the ContactListAdapter which will populate the ListView with data from the contacts array
		setUpListView();
	}

	/** Inflate the menu; this adds items to the action bar if it is present. **/
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		
		// Create the search icon
		MenuItem searchItem = menu.findItem(R.id.action_search);
		SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
		
		// Create the sort spinner
		MenuItem spinnerItem = menu.findItem(R.id.sort_spinner);
		Spinner sortSpinnerView = (Spinner) spinnerItem.getActionView();
		ArrayAdapter<String> sortAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item, sortOptions);
		sortSpinnerView.setAdapter(sortAdapter);
		
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
		Intent intent = new Intent(MainActivity.this, NewContactActivity.class);
		startActivity(intent);
	}
	
	/** This adapter class will allow the data in the contacts array to be displayed in the  **/
	private class ContactListAdapter extends ArrayAdapter<HashMap<String, String>> {

		// This constructs the ContactListAdapter
		public ContactListAdapter(Context context, ArrayList<HashMap<String, String>> contactList) {
			
			super(context, android.R.layout.simple_list_item_1, contactList);
			
		}
		
		// This gets the View of the contacts which will be used to display them on the screen
		public View getView(int position, View convertView, ViewGroup parent) {
			
			// Create a LayoutInflator to inflate the layout of the contact in the list
			LayoutInflater inflater = (LayoutInflater) MainActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			
			// Use the LayoutInflator
			View listContactView = inflater.inflate(R.layout.contact_list_item_layout, null);
			
			// Retrieve the id's of the TextViews
			TextView name = (TextView)listContactView.findViewById(R.id.contact_text_name);
			TextView moblie = (TextView)listContactView.findViewById(R.id.contact_text_mobile);
			TextView id = (TextView)listContactView.findViewById(R.id.id);
			
			// Retrieve the specific information about the contact that needs to be displayed
			name.setText(contactList.get(position).get("name"));
			moblie.setText(contactList.get(position).get("mobile"));
			id.setText(contactList.get(position).get("id"));
			
			return listContactView;
		}
	}
	
	/** Creates the list view which will display the contacts**/
	private void setUpListView() {
		
		contactList = database.getAllContacts();
		
		Log.i("Database", contactList.get(0).get("name"));
		
		ListAdapter adapter = new ContactListAdapter(MainActivity.this, contactList);
		
		listView = getListView();
		listView.setAdapter(adapter);
		
		if (contactList.size() != 0) {
			listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parentView, View clickedView,
						int clickedViewPosition, long id) {
					
					contactId = (TextView) clickedView.findViewById(R.id.id);
					String contactIdValue = contactId.getText().toString();
					
					Intent intent = new Intent(getApplication(), EditContactActivity.class);
					intent.putExtra("contactId", contactIdValue);
					startActivity(intent);					
				}
			});
		}
	}
}
