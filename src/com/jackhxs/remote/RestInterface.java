package com.jackhxs.remote;

import java.util.List;

import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;

import com.jackhxs.data.Card;
import com.jackhxs.data.Contact;
import com.jackhxs.data.SimpleCard;

/**
 * Created by moomou on 8/4/13.
 */
public interface RestInterface {
  @GET("/520830efa8f933420009cda6")
  SimpleCard[] listCards();

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