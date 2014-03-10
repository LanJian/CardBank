package com.jackhxs.data;

import android.os.Parcel;
import android.os.Parcelable;

public class TextConfig implements Parcelable {
	
	public float left;
	public float top;
	public String color;
	
	
    public TextConfig() {
		super();
	}

	public TextConfig(float left, float top, String color) {
		super();
		this.left = left;
		this.top = top;
		this.color = color;
	}

	

	@Override
	public String toString() {
		return "left: " + left + " top: " + top + " color: " + color; 
	}	
    
    

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

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<TextConfig> CREATOR = new Parcelable.Creator<TextConfig>() {
        @Override
        public TextConfig createFromParcel(Parcel in) {
            return new TextConfig(in);
        }

        @Override
        public TextConfig[] newArray(int size) {
            return new TextConfig[size];
        }
    };
}