package com.jackhxs.data;

import android.os.Parcel;
import android.os.Parcelable;

public class TemplateProperties implements Parcelable {
	
	public TextConfig name;
	public TextConfig phone;
	public TextConfig email;
	public TextConfig companyName;
	public TextConfig address;
	public TextConfig jobTitle;
	

    protected TemplateProperties(Parcel in) {
        name = (TextConfig) in.readValue(TextConfig.class.getClassLoader());
        phone = (TextConfig) in.readValue(TextConfig.class.getClassLoader());
        email = (TextConfig) in.readValue(TextConfig.class.getClassLoader());
        companyName = (TextConfig) in.readValue(TextConfig.class.getClassLoader());
        address = (TextConfig) in.readValue(TextConfig.class.getClassLoader());
        jobTitle = (TextConfig) in.readValue(TextConfig.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(name);
        dest.writeValue(phone);
        dest.writeValue(email);
        dest.writeValue(companyName);
        dest.writeValue(address);
        dest.writeValue(jobTitle);
    }

    public static final Parcelable.Creator<TemplateProperties> CREATOR = new Parcelable.Creator<TemplateProperties>() {
        @Override
        public TemplateProperties createFromParcel(Parcel in) {
            return new TemplateProperties(in);
        }

        @Override
        public TemplateProperties[] newArray(int size) {
            return new TemplateProperties[size];
        }
    };
    
    public class TextConfig implements Parcelable {
		public float left;
		public float top;
		public String color;
	
	    protected TextConfig(Parcel in) {
	        left = in.readFloat();
	        top = in.readFloat();
	        color = in.readString();
	    }
	
	    @Override
	    public int describeContents() {
	        return 0;
	    }
	
	    @Override
	    public void writeToParcel(Parcel dest, int flags) {
	        dest.writeFloat(left);
	        dest.writeFloat(top);
	        dest.writeString(color);
	    }
	
	    public final Parcelable.Creator<TextConfig> CREATOR = new Parcelable.Creator<TextConfig>() {
	        @Override
	        public TextConfig createFromParcel(Parcel in) {
	            return new TextConfig(in);
	        }
	
	        @Override
	        public TextConfig[] newArray(int size) {
	            return new TextConfig[size];
	        }
	    };

		@Override
		public String toString() {
			return "left: " + left + " top: " + top + " color: " + color; 
		}	
	    
	    
    }
    
    @Override
   	public String toString() {
   		return name.toString() + phone.toString() + email.toString();
   				
   	}
    
}