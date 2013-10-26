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
	@GET("/users/{userId}/cards") // get own cards
	SimpleCard[] listOwnCards(@Path("userId") String userId);

	// update card
	@PUT("/users/{userId}/cards") // /card/{id}")
	Boolean updateCard(@Path("userId") String userId, @Body SimpleCard simpleCard);

	// Add a new Card
	@POST("/users/{userId}/cards") // /card/new")
	Boolean addCard(@Path("userId") String userId, @Body SimpleCard newCard);

	@DELETE("/users/{userId}/cards") // delete Card
	Boolean deleteCard(@Path("userId") String userId, @Body SimpleCard existingCard);

	/** Contacts */
	@GET("/users/{userId}/contacts") // get contact cards
	SimpleCard[] listContacts(@Path("userId") String userId);

	// Add new Contact
	@POST("/users/{userId}/contacts") // /contact/new")
	Boolean addContact(@Path("userId") String userId, @Body SimpleCard contactCard);

	@DELETE("/users/{userId}/contacts") // delete a contact
	Boolean deleteContact(@Path("userId") String userId, @Body SimpleCard existingContact);

    // Referrals
    @FormUrlEncoded
	@POST("/users/{userId}/referrals") // refer a card
	Boolean refer(@Path("userId") String userId,
            @Field("referredTo") String referredTo, @Field("cardId") String cardId);

	@GET("/users/{userId}/referrals") // get list of cards referred to me
	SimpleCard[] listReferrals(@Path("userId") String userId);

	/** Misc. */
	//Signing up
	@FormUrlEncoded
	@POST("/users")
	String signup(@Field("email") String email, @Field("password") String password);

	// Logging In
	@FormUrlEncoded
	@POST("/sessions")
	String login(@Field("email") String email, @Field("password") String password);
}
