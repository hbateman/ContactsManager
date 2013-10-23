package com.example.contactsmanager;

import java.util.ArrayList;
import java.util.HashMap;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class MainActivity extends ListActivity implements OnItemSelectedListener{
	
	private String[] sortOptions = {"Name", "Surname", "Mobile"};
	private ListView listView;
	private ContactsDatabase database = new ContactsDatabase(this);
	private TextView contactId;
	ArrayList<HashMap<String, String>> contactList;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// Set up action bar
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.show();
		
		// Load contacts from database
		contactList = database.getAllContacts();
		database.close();

		// This sets the ContactListAdapter which will populate the ListView with data from the contacts array
		setUpListView();
	}

	/** Inflate the menu; this adds items to the action bar if it is present. **/
	public boolean onCreateOptionsMenu(Menu menu) {
		
		getMenuInflater().inflate(R.menu.main, menu);
		
		// Create the search icon
		/*SearchManager searchManager =
		           (SearchManager) getSystemService(Context.SEARCH_SERVICE);*/
		MenuItem searchItem = menu.findItem(R.id.action_search);
		SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
		/*searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));*/
		
		// Create the sort spinner
		MenuItem spinnerItem = menu.findItem(R.id.sort_spinner);
		Spinner sortSpinnerView = (Spinner) spinnerItem.getActionView();
		sortSpinnerView.setOnItemSelectedListener(this);
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
	        	newContact();
	            return true;
	    }
		return false;
	}
	
	/** This changes the sort order when selected from the sort drop-down menu in the action bar **/
	public void onItemSelected(AdapterView<?> parentView, View view, int position, long id) {
		
		if (position == 0) contactList = database.getAllContacts();
		else if (position == 1) contactList = database.getContactsBySurname();
		else if (position == 2) contactList = database.getContactsByNumber();
		database.close();

		Log.v("Main Activity Action", "Changing sort order");
		setUpListView(); // reset the list with new order
	}

	public void onNothingSelected(AdapterView<?> parentView) {}
	
	/** Create a new contact **/
	public void newContact() {
		Intent intent = new Intent(MainActivity.this, NewContactActivity.class);
		Log.v("Main Activity Action", "Launching NewContactActivity");
		startActivityForResult(intent, 1);
	}
	
	/** This adapter class will allow the data in the contactList to be displayed in the ListView **/
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
		
		ListAdapter adapter = new ContactListAdapter(MainActivity.this, contactList);
		
		listView = getListView();
		listView.setAdapter(adapter);
		
		if (contactList.size() != 0) {
			listView.setOnItemClickListener(new OnItemClickListener() {

				// Create the method for handling clicks on contacts in the list
				public void onItemClick(AdapterView<?> parentView, View clickedView,
						int clickedViewPosition, long id) {
					
					contactId = (TextView) clickedView.findViewById(R.id.id);
					String contactIdValue = contactId.getText().toString();
					
					Intent intent = new Intent(getApplication(), EditContactActivity.class);
					intent.putExtra("contactId", contactIdValue);
					Log.v("Main Activity Action", "Launching EditContactActivity");
					startActivityForResult(intent, 1);					
				}
			});
		}
	}
	
	/** Called when the user returns with an image after selecting it from the gallery **/
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		// If result is ok, refresh the contact list to display any changes
		if (resultCode == RESULT_OK) {
			contactList = database.getAllContacts();
			database.close();
			Log.v("MainActivity Event", "Returned from other activity");
			setUpListView();
		}
	}
}
