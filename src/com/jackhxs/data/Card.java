package com.jackhxs.data;

import android.os.Parcel;
import android.os.Parcelable;

public class Card implements Parcelable {
	private long rowId;
	private String firstName;
	private String lastName;
	private String email;
	private String phoneNumber;
	private String id; //unique id in remote db
	private Integer version;

	public Card(Long _rowId,
			String _firstName,
			String _lastName,
			String _email,
			String _phoneString,
			String _id,
			Integer _version) {

		rowId = _rowId;
		firstName = _firstName;
		lastName = _lastName;
		email = _email;
		phoneNumber = _phoneString;
		id = _id;
		version = _version;
	}
	
	public Card(String _firstName,
			String _lastName,
			String _email,
			String _phoneString,
			String _id,
			Integer _version) {
		this(null, _firstName, _lastName, _email, _phoneString, _id, _version);
	} 

	public Long getRowId() {
		return rowId;
	}

	public String getName() {
		return firstName + lastName;
	}

	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String _firstName) {
		firstName = _firstName;
	}

	public String getLastName() {
		return firstName;
	}
	public void setLastName(String _lastName) {
		lastName = _lastName;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String _phoneNumber) {
		phoneNumber = _phoneNumber;
	}

	public String getEmail() {
		return email;
	}
	public void setEmail(String _email) {
		email = _email;
	}

	public String getId() {
		return id;
	}
	public void setId(String _id) {
		id = _id;
	}

	public Integer getVersion() {
		return version;
	}

	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	public void writeToParcel(Parcel arg0, int arg1) {
		// TODO Auto-generated method stub

	}
}