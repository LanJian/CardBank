package com.jackhxs.data;

import android.os.Parcel;
import android.os.Parcelable;

public class SimpleCard implements Parcelable {
	public String firstName;
	public String lastName;
	public String phoneNo;
	public String email;
	public String imageUrl;
	
	public String _id;
	
	public SimpleCard() {
		firstName = lastName = phoneNo = email = imageUrl = _id = "null";
	};
	
	public SimpleCard(Parcel in) {
		readFromParcel(in);
	}
	
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public void writeToParcel(Parcel dst, int flags) {
		dst.writeString(firstName);
		dst.writeString(lastName);
		dst.writeString(phoneNo);
		dst.writeString(email);
		dst.writeString(imageUrl);
		
		dst.writeString(_id);
	}
	
	private void readFromParcel(Parcel in) {
		firstName = in.readString();
		lastName = in.readString();
		phoneNo = in.readString();
		email = in.readString();
		imageUrl = in.readString();
		
		_id = in.readString();
	}
	
	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
		public SimpleCard createFromParcel(Parcel in) {
			return new SimpleCard(in);
		}
		public SimpleCard[] newArray(int size) {
			return new SimpleCard[size];
		}
	};
}
