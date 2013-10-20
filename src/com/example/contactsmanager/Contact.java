package com.example.contactsmanager;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class Contact implements Parcelable {
	
	private String name = "";
	private String surname = "";
	private String mobile = "";
	private String homePhone = "";
	private String workPhone = "";
	private String email = "";
	private String address = "";
	private String dob = "";
	
	public Contact(String name, String surname, String mobile, String homePhone, String workPhone, String email, String address, String dob) {
		this.name = name;
		this.surname = surname;
		this.mobile = mobile;
		this.workPhone = workPhone;
		this.homePhone = homePhone;
		this.email = email;
		this.address = address;
		this.dob = dob;
	}
	
	// Used when unpacking contacts that have been parceled
	public Contact(Parcel in) {
		String[] data = new String[8];

        in.readStringArray(data);
        this.name = data[0];
        this.surname = data[1];
        this.mobile = data[2];
        this.workPhone = data[3];
        this.homePhone = data[4];
        this.email = data[5];
        this.address = data[6];
        this.dob = data[7];
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getSurname() {
		return this.surname;
	}
	
	public String getMobile() {
		return this.mobile;
	}
	
	public String getHomePhone() {
		return this.homePhone;
	}
	
	public String getWorkPhone() {
		return this.workPhone;
	}
	
	public String getEmail() {
		return this.email;
	}
	
	public void setName(String newName) {
		this.name = newName.replaceAll("\\s+","");
	}
	
	public void setSurname(String newSurname) {
		this.surname = newSurname.replaceAll("\\s+","");
	}
	
	public void setMobile(String newMobile) {
		this.mobile = newMobile.replaceAll("\\s+","");
	}
	
	public void setWork(String newWork) {
		this.workPhone = newWork.replaceAll("\\s+","");
	}
	
	public void setHome(String newHome) {
		this.homePhone = newHome.replaceAll("\\s+","");
	}
	
	public void setEmail(String newEmail) {
		this.email = newEmail.replaceAll("\\s+","");
	}
	
	public boolean equals(Contact otherContact) {
		if (this.name.equals(otherContact.getName()) && this.surname.equals(otherContact.getSurname())) {
			return true;
		}
		return false;
	}
	
	public int describeContents() {
		return 0;
	}

	// Used to 'flatten' Contact objects so they can be parceled
	public void writeToParcel(Parcel dest, int flags) {
		Log.v("Contact", "writeToParcel..."+ flags);
		dest.writeStringArray(new String[]{this.name, this.surname, this.mobile,
				this.workPhone,this.homePhone, this.email, this.address, this.dob});
	}
	
	// Creates a CREATOR which is used to make the Parcelable object
	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Contact createFromParcel(Parcel in) {
            return new Contact(in); 
        }

        public Contact[] newArray(int size) {
            return new Contact[size];
        }
    };

}
