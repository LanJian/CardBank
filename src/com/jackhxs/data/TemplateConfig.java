package com.jackhxs.data;

import android.os.Parcel;
import android.os.Parcelable;

public class TemplateConfig implements Parcelable {

	private String baseTemplate;
	private TemplateProperties properties;
	
	@Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(baseTemplate);
        dest.writeValue(properties);
    }

    public static final Parcelable.Creator<TemplateConfig> CREATOR = new Parcelable.Creator<TemplateConfig>() {
        @Override
        public TemplateConfig createFromParcel(Parcel in) {
            return new TemplateConfig(in);
        }

        @Override
        public TemplateConfig[] newArray(int size) {
            return new TemplateConfig[size];
        }
    };
    
    @Override
	public String toString() {
		// TODO Auto-generated method stub
		return "baseTemplate: " + baseTemplate;
	}
    
    public String getBaseTemplate() {
		return baseTemplate;
	}

	public void setBaseTemplate(String baseTemplate) {
		this.baseTemplate = baseTemplate;
	}
	

    public TemplateProperties getProperties() {
		return properties;
	}

	public void setProperties(TemplateProperties properties) {
		this.properties = properties;
	}

	protected TemplateConfig(Parcel in) {
        baseTemplate = in.readString();
        properties = (TemplateProperties) in.readValue(TemplateProperties.class.getClassLoader());
        
    }
}
