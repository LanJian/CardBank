package com.jackhxs.remote;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;

import com.jackhxs.data.SimpleCard;

/**
 * Created by moomou on 8/4/13.
 */
public interface RestInterface {
  @GET("/520830efa8f933420009cda6")
  SimpleCard[] listCards();

  @POST("/card/new")
  Boolean createNewCard(String accessToken, @Body SimpleCard simpleCard);
  	  
  @POST("/contact/new")
  Boolean createNewContact(String accessToken, @Body SimpleCard simpleCard);
  
  @POST("/session/")
  String login(String username, String password);
}