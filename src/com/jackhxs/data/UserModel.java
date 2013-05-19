package com.jackhxs.data;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.database.SQLException;

/**
 * Main Entry for Getting Card Array;
 * 
 * Once authenticated, initiate this object
 * 
 * @author MooMou
 *
 */

public class UserModel {
	private final Context mCtx;
	private final String username;
	private final CardDBAdapter cardDBHelper;
	private final ContactAdapter contactDBHelper;
	
	/**
	 * Constructor - takes the context to allow the database to be
	 * opened/created
	 * 
	 * @param ctx
	 *            the Context within which to work
	 */
	public UserModel(Context ctx, String _username) {
		this.mCtx = ctx;
		username = _username;
		
		//initiating specific table helper
		cardDBHelper = new CardDBAdapter(ctx);
		contactDBHelper = new ContactAdapter(ctx);
	}

	/**
	 * Open the database. If it cannot be opened, try to create a new
	 * instance of the database. If it cannot be created, throw an exception to
	 * signal the failure
	 * 
	 * @return this (self reference, allowing this to be chained in an
	 *         initialization call)
	 * @throws SQLException
	 *             if the database could be neither opened or created
	 */
	public UserModel open() throws SQLException {
		cardDBHelper.open();
		contactDBHelper.open();
		
		return this;
	}

	/**
	 * close return type: void
	 */
	public void close() {
		cardDBHelper.close();
		contactDBHelper.close();
	}
	
	public List<Card> getContactCards() {
		List<String> objectIds = contactDBHelper.getAllContact(username);
		List<Card> cards = new ArrayList<Card>();
		
		for(Iterator<String> i = objectIds.iterator(); i.hasNext(); ) {
			String objectId = i.next();
			Card contactCard = cardDBHelper.getCard(objectId);
			
			if (contactCard != null) {
				cards.add(contactCard);
			}
			else { //not in the database
				//if online, retrieve it,
				//else add a blank card
			}
		}
		
		return cards;
	}
	
	public Boolean addContact(Card newContact) {
		/*
		 * Add card to table if not already in
		 * Add an entry to contact table if not already there
		 */
		
		if (newContact != null) {
			if (cardDBHelper.getCard(newContact.getObjectId()) == null) {
				cardDBHelper.createCard(newContact);
			}
			
			return contactDBHelper.addContact(new Contact(username, newContact.getObjectId()));
		}
		
		return false;
	}
	
	public Boolean removeContact(Card contact) {
		return null;
	}
	
	public Boolean syncContact() {
		//Connect to the server to retrieve new cards and update old cards
		return null;
	}

}
