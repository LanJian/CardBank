package com.jackhxs.data;

import com.jackhxs.data.template.Template;
import com.jackhxs.data.template.TemplateConfig;

import android.os.Parcel;
import android.os.Parcelable;

public class BusinessCard implements Parcelable {
	private String firstName;
	private String lastName;
	private String phone;
	private String email;
	private String companyName;
	private String address;
	private String jobTitle;
	
	private String imageUrl;
	
	private String referralId;
	private String userId;
	private String _id;
	
	private TemplateConfig templateConfig;
	
	private Template template;
	
	public BusinessCard() {
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
	
	
	protected BusinessCard(Parcel in) {
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
        templateConfig = (TemplateConfig) in.readValue(TemplateConfig.class.getClassLoader());
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
        dest.writeValue(templateConfig);
        dest.writeValue(template);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<BusinessCard> CREATOR = new Parcelable.Creator<BusinessCard>() {
        @Override
        public BusinessCard createFromParcel(Parcel in) {
            return new BusinessCard(in);
        }

        @Override
        public BusinessCard[] newArray(int size) {
            return new BusinessCard[size];
        }
    };
	
	
    public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getJobTitle() {
		return jobTitle;
	}

	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getReferralId() {
		return referralId;
	}

	public void setReferralId(String referralId) {
		this.referralId = referralId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}
	
	public Template getTemplate() {
		return template;
	}

	public void setTemplate(Template template) {
		this.template = template;
	}
	
	public TemplateConfig getTemplateConfig() {
		return templateConfig;
	}

	public void setTemplateConfig(TemplateConfig templateConfig) {
		this.templateConfig = templateConfig;
	}
}