package com.example.contactsmanager;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class CustomCursorAdapter extends CursorAdapter{

	public CustomCursorAdapter(Context context, Cursor c) {
		super(context, c);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		
		TextView name = (TextView)view.findViewById(R.id.contact_text_name);
		TextView mobile = (TextView)view.findViewById(R.id.contact_text_mobile);
		
		name.setText(cursor.getColumnIndex(cursor.getColumnName(1)));
		mobile.setText(cursor.getColumnIndex(cursor.getColumnName(3)));
		
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
		
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View listItemView = inflater.inflate(R.layout.contact_list_item_layout, null);
		
		return listItemView;
	}
	
	

}
