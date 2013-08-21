package com.jackhxs.remote;

import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Query;

import com.jackhxs.data.ContactAndCards;
import com.jackhxs.data.SimpleCard;

/**
 * Created by moomou on 8/4/13.
 */
public interface RestInterface {
  /** Both */
  @GET("/521438f3cfaf853802b35c98") // get own cards and contacts
  ContactAndCards listContactAndCards(@Query("acccessToken") String accessToken);
 
  /** Cards */
  @GET("/52116ea87d9210870034dd2f") // get own cards
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
  @GET("/521173867d9210870034dd31") // get contact cards
  SimpleCard[] listContacts(@Query("acccessToken") String accessToken);
 
  // Add new Contact
  @POST("/52116e117d92108b0034dd2e") // /contact/new")
  Boolean addContact(@Query("acccessToken") String accessToken, @Body SimpleCard contactCard);

  @DELETE("/52116e117d92108b0034dd2e") // delete a contact
  Boolean deleteContact(@Query("acccessToken") String accessToken, @Body SimpleCard existingContact);
 
  /** Misc. */
  // Logging In
  @FormUrlEncoded
  @POST("/5211492f7d9210460034dd2d")
  String login(@Field("username") String username, @Field("password") String password);
}