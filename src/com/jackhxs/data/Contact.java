package com.jackhxs.data;

public class Contact {
  private String owner;
  private String objectId; //unique id referring to a 
  
  public Contact(String _owner, String _objectId) {
    owner = _owner;
    objectId = _objectId;
  }
  
  public String getOwner() {
	  return owner;
  }
  public void setOwner(String _owner) {
	  owner = _owner;
  }
  
  public String getObjectId() {
	  return objectId;
  }
  public void setObjectId(String _objectId) {
	  objectId = _objectId;
  }
}
