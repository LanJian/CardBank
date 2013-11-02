package com.jackhxs.remote;

import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.jackhxs.cardbank.App;
import com.jackhxs.data.APIResult;
import com.jackhxs.data.SimpleCard;
import com.jackhxs.remote.Constants.Operation;
import com.squareup.okhttp.OkHttpClient;

public class RemoteService extends IntentService {
    OkHttpClient client = new OkHttpClient();

    private final RestAdapter restAdapter = new RestAdapter.Builder()
    	.setServer("http://10.8.104.15:3000") //"http://cardbeam-server.herokuapp.com/")
    	.setClient(new OkClient(client))
    	.build();

    private final RestInterface service;

    public RemoteService() {
        this("remoteService");
    }

    public RemoteService(String name) {
        super(name);
        service = restAdapter.create(RestInterface.class);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        ResultReceiver receiver = intent.getParcelableExtra("receiver");
        if (receiver == null)
            receiver = new ResultReceiver(null);
        Bundle b = new Bundle();

        Operation command = intent.getParcelableExtra("operation");
        
        String sessionId = App.sessionId;
        String userId = App.userId;
        Boolean success = true;
        
    	b.putBoolean("longPoll", intent.getBooleanExtra("longPoll", false));
        
        try {
        	switch (command) {
            case POST_SIGNUP:
            case POST_LOGIN: {
                String email = intent.getStringExtra("email");
                String password = intent.getStringExtra("password");
                
                JsonObject res;
                if (command.equals(Operation.POST_SIGNUP)) {
                	res = service.signup(email, password);
                }
                else {
                	res = service.login(email, password);
                }
                
                Boolean ok = res.get("status").getAsString().equals("success");
                
                if (ok) {
                	String newUserId = res.get("userId").getAsString();
                    String newSessionId = res.get("sessionId").getAsString();
                    
                	b.putString("sessionId", newSessionId);
                	b.putString("userId", newUserId);
                	
                	Log.i("Logged In UserId", newUserId);
                    Log.i("Logged In sessionId", newSessionId);
                } else {
                	b.putString("error", res.get("err").getAsString());
                }
                
                break;
            }
            case GET_CARDS: {
                APIResult result = service.listOwnCards(userId, sessionId);

                b.putParcelableArray("cards", result.cards);
                
                b.putString("dataType", "cards");
                b.putString("action", Operation.GET_CARDS.toString());
                b.putBoolean("result", true);
                break;
            }
            case GET_CONTACTS: {
            	APIResult result = service.listContacts(userId, sessionId);
            	
            	SimpleCard[] contacts = result.cards;
            	
            	b.putParcelableArray("contacts", contacts);
                b.putString("dataType", "contacts");
                b.putString("updatedAt", result.updatedAt);
                b.putBoolean("result", true);
                b.putString("action", Operation.GET_CONTACTS.toString());
                
                break;
            }
            case PUT_CARD:
            case POST_CARD: {
                String simpleCardJSON = intent.getStringExtra("simpleCardJSON");

                SimpleCard simpleCard = new Gson().fromJson(simpleCardJSON, SimpleCard.class);
                JsonObject res;
                
                if (command.equals(Operation.PUT_CARD)) {
                	res = service.updateCard(userId, simpleCard._id, sessionId, simpleCard);
                }
                else {
                	res = service.addCard(userId, sessionId, simpleCard);
                }
                
                Boolean result = res.get("status").getAsString().equals("success") ? true : false;
                b.putString("action", Operation.POST_CARD.toString());
                b.putBoolean("result", result);
                break;
            }
            case POST_CONTACT: {
                String simpleCardJSON = intent.getStringExtra("newContactJSON");

                SimpleCard simpleCard = new Gson().fromJson(simpleCardJSON, SimpleCard.class);
                JsonObject res = service.addContact(userId, sessionId, simpleCard);
                Boolean result = res.get("status").getAsString().equals("success") ? true : false;
                
                b.putString("action", Operation.POST_CONTACT.toString());
                b.putBoolean("result", result);
                break;
            }
            case DEL_CARD: {
                //SimpleCard existingCard = intent.getParcelableExtra("existingCard");
                //Boolean result = service.deleteCard(userId, sessionId, existingCard);
                b.putString("action", "Not Supported");
                b.putBoolean("result", false);
                break;
            }
            case DEL_CONTACT: {
            	//SimpleCard existingCard = intent.getParcelableExtra("existingCard");
                //Boolean result = service.deleteCard(userId, sessionId, existingCard);
                b.putString("action", "Not Supported");
                b.putBoolean("result", false);
                break;
            }
            case DEL_REFER: {
            	//SimpleCard existingCard = intent.getParcelableExtra("existingCard");
                //Boolean result = service.deleteCard(userId, sessionId, existingCard);
                
                break;
            }
            case REFER: {
                String referredTo = intent.getStringExtra("referredTo");
                String cardId = intent.getStringExtra("cardId");
                
                JsonObject res = service.refer(userId, sessionId, referredTo, cardId);
                Boolean result = res.get("status").getAsString().equals("success") ? true : false;
                b.putString("action", Operation.REFER.toString());
                b.putBoolean("result", result);
                break;
            }
            case LIST_REFERRALS: {
            	APIResult res = service.listReferrals(userId, sessionId);
                SimpleCard[] referrals = res.cards;

                b.putString("action", Operation.LIST_REFERRALS.toString());
                b.putString("dataType", "referrals");
                b.putBoolean("result", true);
                b.putParcelableArray("referrals", referrals);

                
                break;
            }
            default: {
                break;
            }
            }
        }
        catch (RetrofitError re) {
        	success = false;
        }
        finally {
        	if (success) {
        		receiver.send(Constants.STATUS_FINISHED, b);		
        	}
        	else {
        		receiver.send(Constants.STATUS_ERROR, b);
        	}
        }
        
        this.stopSelf();
    }
}