package com.jackhxs.data;

public class Card {
  private String firstName;
  private String lastName;
  private String email;
  private String phoneNumber;
  private Long objectId; //unique id in remote db
  private String owner; //unique username who this card belongs
  
  public Card() {
	  
  }
  public Card(String name) {
	  firstName = name;
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
  
  public Long getObjectId() {
	  return objectId;
  }
  public void setObjectId(Long _objectId) {
	  objectId = _objectId;
  }
}
