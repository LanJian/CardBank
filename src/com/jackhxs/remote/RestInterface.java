package com.jackhxs.remote;

import org.json.JSONObject;

import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Query;

import com.google.gson.JsonArray;
import com.jackhxs.data.ContactAndCards;
import com.jackhxs.data.SimpleCard;

/**
 * Created by moomou on 8/4/13.
 */
public interface RestInterface {
	/** Both */
	@GET("/5248d6c3c9dc305a0071cd10") // get own cards and contacts
	ContactAndCards listContactAndCards(@Query("acccessToken") String accessToken);

	/** Cards */
	@GET("/522fe9acfd83b34e00fd8614") // get own cards
	SimpleCard[] listOwnCards(@Query("acccessToken") String accessToken);

	// update card
	@PUT("/52116e117d92108b0034dd2e") // /card/{id}") 
	Boolean updateCard(@Query("acccessToken") String accessToken, @Body SimpleCard simpleCard);

	// Add a new Card
	@POST("/52116e117d92108b0034dd2e") // /card/new")
	Boolean addCard(@Query("acccessToken") String accessToken, @Body SimpleCard newCard);

	@DELETE("/52116e117d92108b0034dd2e") // delete Card 
	Boolean deleteCard(@Query("acccessToken") String accessToken, @Body SimpleCard existingCard);

	/** Contacts */
	@GET("/522fe9e3fd83b34e00fd8615") // get contact cards
	SimpleCard[] listContacts(@Query("acccessToken") String accessToken);

	// Add new Contact
	@POST("/52116e117d92108b0034dd2e") // /contact/new")
	Boolean addContact(@Query("acccessToken") String accessToken, @Body SimpleCard contactCard);

	@DELETE("/52116e117d92108b0034dd2e") // delete a contact
	Boolean deleteContact(@Query("acccessToken") String accessToken, @Body SimpleCard existingContact);

    // Referrals
    @FormUrlEncoded
	@POST("/52116e117d92108b0034dd2e") // refer a card
	Boolean refer(@Query("acccessToken") String accessToken,
            @Field("referredTo") String referredTo, @Field("cardId") String cardId);

	@GET("/52116e117d92108b0034dd2e") // get list of cards referred to me
	SimpleCard[] listReferrals(@Query("acccessToken") String accessToken);

	/** Misc. */
	//Signing up
	@FormUrlEncoded
	@POST("/5211492f7d9210460034dd2d")
	String signup(@Field("username") String username, @Field("password") String password);

	
	// Logging In
	@FormUrlEncoded
	@POST("/526b1abb56f61f5100381d77")
	JSONObject login(@Field("username") String username, @Field("password") String password);
}
