package com.jackhxs.data;

public class Contact {
  private Long rowId;
  private String owner;
  private String id; //unique id referring to a 
  
  public Contact(Long _rowId, String _owner, String _id) {
    rowId = _rowId;
    owner = _owner;
    id = _id;
  }
  public Contact(String _owner, String _id) {
	this(null, _owner, _id);
  }
  
  public String getOwner() {
	  return owner;
  }
  public void setOwner(String _owner) {
	  owner = _owner;
  }
  
  public String getId() {
	  return id;
  }
  public void setId(String _id) {
	  id = _id;
  }
}
