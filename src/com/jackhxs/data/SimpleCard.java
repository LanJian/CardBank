package com.jackhxs.data;

import android.os.Parcel;
import android.os.Parcelable;

public class SimpleCard implements Parcelable {
	public String firstName;
	public String lastName;
	public String phone;
	public String email;
	public String companyName;
	public String address;
	public String jobTitle;
	
	public String imageUrl;
	
	public String referralId;
	public String userId;
	public String _id;
	
	public Template template;
	
	public SimpleCard() {
		firstName = "Your"; 
		lastName = "Name";
		phone = "000-0000";
		email = "your@email.com";
		companyName = "companyName Name";
		address = "Your Address";
		jobTitle = "Ex. Financial Analyst";
		
		imageUrl = "0";
		
		referralId = "null";
		userId = "";
		_id = "null";
	};
	
	protected SimpleCard(Parcel in) {
        firstName = in.readString();
        lastName = in.readString();
        phone = in.readString();
        email = in.readString();
        companyName = in.readString();
        address = in.readString();
        jobTitle = in.readString();
        imageUrl = in.readString();
        referralId = in.readString();
        userId = in.readString();
        _id = in.readString();
        template = (Template) in.readValue(Template.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(phone);
        dest.writeString(email);
        dest.writeString(companyName);
        dest.writeString(address);
        dest.writeString(jobTitle);
        dest.writeString(imageUrl);
        dest.writeString(referralId);
        dest.writeString(userId);
        dest.writeString(_id);
        dest.writeValue(template);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<SimpleCard> CREATOR = new Parcelable.Creator<SimpleCard>() {
        @Override
        public SimpleCard createFromParcel(Parcel in) {
            return new SimpleCard(in);
        }

        @Override
        public SimpleCard[] newArray(int size) {
            return new SimpleCard[size];
        }
    };
}