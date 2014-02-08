package com.jackhxs.remote;

import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.Query;

import com.google.gson.JsonObject;
import com.jackhxs.data.APIResult;
import com.jackhxs.data.LoginSignupResponse;
import com.jackhxs.data.SimpleCard;

/**
 * Created by moomou on 8/4/13.
 */
public interface RestInterface {
	/** Cards */
	@GET("/users/{userId}/cards") // get own cards
	APIResult listOwnCards(@Path("userId") String userId, @Query("sessionId") String sessionId);

	// update card
	@PUT("/users/{userId}/cards/{cardId}") // /card/{id}")
	JsonObject updateCard(
		@Path("userId") String userId, 
		@Path("cardId") String cardId, 
		@Query("sessionId") String sessionId, 
		@Body SimpleCard simpleCard);

	// Add a new Card
	@POST("/users/{userId}/cards") // /card/new")
	JsonObject addCard(@Path("userId") String userId, @Query("sessionId") String sessionId, @Body SimpleCard newCard);

	@DELETE("/users/{userId}/cards") // delete Card
	JsonObject deleteCard(@Path("userId") String userId, @Query("sessionId") String sessionId, @Body SimpleCard existingCard);

	/** Contacts */
	@GET("/users/{userId}/contacts") // get contact cards
	APIResult listContacts(@Path("userId") String userId, @Query("sessionId") String sessionId);

	// Add new Contact
	@POST("/users/{userId}/contacts") // /contact/new")
	JsonObject addContact(@Path("userId") String userId, @Query("sessionId") String sessionId, @Body SimpleCard contactCard);

	@DELETE("/users/{userId}/contacts") // delete a contact
	JsonObject deleteContact(@Path("userId") String userId, @Query("sessionId") String sessionId, @Body SimpleCard existingContact);

    // Referrals
    @FormUrlEncoded
	@POST("/users/{userId}/referrals") // refer a card
    JsonObject refer(
		@Path("userId") String userId,
		@Query("sessionId") String sessionId,
	    @Field("referredTo") String referredTo, 
	    @Field("cardId") String cardId);

	@GET("/users/{userId}/referrals") // get list of cards referred to me
	APIResult listReferrals(@Path("userId") String userId, @Query("sessionId") String sessionId);

	@DELETE("/users/{userId}/referrals/{referralId}")
	void deleteReferrals(@Path("userId") String userId, @Query("sessionId") String sessionId, @Path("referralId") String referralId);
	
	/** Misc. */
	//Signing up
	@FormUrlEncoded
	@POST("/users")
	LoginSignupResponse signup(@Field("email") String email, @Field("password") String password);
	
	// Logging In
	@FormUrlEncoded
	@POST("/sessions")
	LoginSignupResponse login(@Field("email") String email, @Field("password") String password);
}
