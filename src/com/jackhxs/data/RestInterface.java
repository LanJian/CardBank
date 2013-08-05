package com.jackhxs.data;

import java.util.List;

import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;

/**
 * Created by moomou on 8/4/13.
 */
public interface RestInterface {
  @GET("/users/{user}/cards")
  List<Card> listCards(@Path("user") String user);

  @GET("/users/{user}/contacts")
  List<Contact> listContacts(@Path("user") String user);

  @POST("/users/{user}/contacts")
  List<Contact> createContact(@Path("user") String user);

  @POST("/users/{user}/cards")
  List<Card> createCard(@Path("user") String user);

  /* for login */
  @POST("/session")
  String authenticate(String username, String password);
}