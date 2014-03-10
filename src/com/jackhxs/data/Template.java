package com.jackhxs.data;

import android.os.Parcel;
import android.os.Parcelable;

public class Template implements Parcelable {
	
	private TemplateProperties properties;
	private String imageUrl;
	private String templateName;
	
	
   

    protected Template(Parcel in) {
        properties = (TemplateProperties) in.readValue(TemplateProperties.class.getClassLoader());
        imageUrl = in.readString();
        templateName = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(properties);
        dest.writeString(imageUrl);
        dest.writeString(templateName);
    }

    public static final Parcelable.Creator<Template> CREATOR = new Parcelable.Creator<Template>() {
        @Override
        public Template createFromParcel(Parcel in) {
            return new Template(in);
        }

        @Override
        public Template[] newArray(int size) {
            return new Template[size];
        }
    };




	public TemplateProperties getProperties() {
		return properties;
	}

	public void setProperties(TemplateProperties properties) {
		this.properties = properties;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "imageUrl: " + imageUrl + " templateName: " + templateName + " properties: " + properties.toString();
		
		
	}
    
    
}