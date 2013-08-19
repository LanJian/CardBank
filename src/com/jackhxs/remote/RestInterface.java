package com.jackhxs.remote;

import retrofit.http.Body;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
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
  
  @FormUrlEncoded
  @POST("/5211492f7d9210460034dd2d")
  String login(@Field("username") String username, @Field("password") String password);
}