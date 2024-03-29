package com.jackhxs.remote;

import java.util.ArrayList;

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
import com.jackhxs.data.LoginSignupResponse;
import com.jackhxs.data.BusinessCard;
import com.jackhxs.data.template.Template;
import com.jackhxs.remote.Constants.Operation;
import com.squareup.okhttp.OkHttpClient;

public class RemoteService extends IntentService {
	private static final String TAG = "RemoteService";
	
	OkHttpClient client = new OkHttpClient();

    private final RestAdapter restAdapter = new RestAdapter.Builder()
    	.setServer(Constants.API_ADDRESS_V1)
    	.setClient(new OkClient(client))
    	.setDebug(true)
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
        Bundle resultBundle = new Bundle();

        Operation command = intent.getParcelableExtra("operation");
        
        String sessionId = App.sessionId;
        String userId = App.userId;
        Boolean success = true;
        
        resultBundle.putBoolean("longPoll", intent.getBooleanExtra("longPoll", false));
        resultBundle.putParcelable("operation", command);

    	Log.i(TAG, intent.getParcelableExtra("operation").toString());
    	
    	try {
    		switch (command) {
            case POST_SIGNUP:
            case POST_LOGIN: {
                
            	String email = intent.getStringExtra("email");
                String password = intent.getStringExtra("password");
                
                LoginSignupResponse mLoginSignupResponse;;
                
                if (command.equals(Operation.POST_SIGNUP)) {
                	mLoginSignupResponse = service.signup(email, password);
                }
                else {
                	mLoginSignupResponse = service.login(email, password);
                }
                
                Boolean ok = mLoginSignupResponse.getStatus().equals("success");
                
                if (ok) {
                	String newUserId = mLoginSignupResponse.getUserId();
                    String newSessionId = mLoginSignupResponse.getSessionId();
                    
                	resultBundle.putString("sessionId", newSessionId);
                	resultBundle.putString("userId", newUserId);
                	
                	Log.i("Logged In UserId", newUserId);
                    Log.i("Logged In sessionId", newSessionId);
                } else {
                	resultBundle.putString("error", mLoginSignupResponse.getErr());
                }
                
                break;
            }
            case GET_CARDS: {
                APIResult result = service.listOwnCards(userId, sessionId);

                resultBundle.putParcelableArray("cards", result.cards);
                
                /*
                 *  TODO is dataType needed anymore? data is loaded in the fragments (only 1 datatype at a time) so is it really needed???
                 *  same thing for action. 
                 */
                resultBundle.putString("dataType", "cards");
                resultBundle.putBoolean("result", true);
                break;
            }
            case GET_CONTACTS: {
            	APIResult result = service.listContacts(userId, sessionId);
            	
            	BusinessCard[] contacts = result.cards;
            	
            	/*
                 *  TODO is dataType needed anymore? data is loaded in the fragments (only 1 datatype at a time) so is it really needed???
                 *  same thing for action. 
                 */
                
            	resultBundle.putParcelableArray("contacts", contacts);
                resultBundle.putString("dataType", "contacts");
                resultBundle.putString("updatedAt", result.updatedAt);
                resultBundle.putBoolean("result", true);
                
                break;
            }
            case UPDATE_CARD:
            case POST_CARD: {
            	String simpleCardJSON = intent.getStringExtra("simpleCardJSON");
            	BusinessCard mBusinessCard = intent.getParcelableExtra("BusinessCard");

                //BusinessCard simpleCard = new Gson().fromJson(simpleCardJSON, BusinessCard.class);
                JsonObject res;
                
                if (command.equals(Operation.UPDATE_CARD)) {
                	res = service.updateCard(userId, mBusinessCard.get_id(), sessionId, mBusinessCard);
                }
                else {
                	BusinessCard simpleCard = new Gson().fromJson(simpleCardJSON, BusinessCard.class);
                    
                	res = service.addCard(userId, sessionId, simpleCard);
                }
                
                Boolean result = res.get("status").getAsString().equals("success") ? true : false;
                resultBundle.putBoolean("result", result);
                break;
            }
            case POST_CONTACT: {
                String simpleCardJSON = intent.getStringExtra("newContactJSON");

                BusinessCard simpleCard = new Gson().fromJson(simpleCardJSON, BusinessCard.class);
                JsonObject res = service.addContact(userId, sessionId, simpleCard);
                Boolean result = res.get("status").getAsString().equals("success") ? true : false;
                
                resultBundle.putString("dataType", "postCard");
                resultBundle.putBoolean("result", result);
                break;
            }
            case DEL_CARD: {
                //SimpleCard existingCard = intent.getParcelableExtra("existingCard");
                //Boolean result = service.deleteCard(userId, sessionId, existingCard);
                resultBundle.putBoolean("result", false);
                break;
            }
            case DEL_CONTACT: {
            	//SimpleCard existingCard = intent.getParcelableExtra("existingCard");
                //Boolean result = service.deleteCard(userId, sessionId, existingCard);
                resultBundle.putBoolean("result", false);
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
                resultBundle.putBoolean("result", result);
                break;
            }
            case LIST_REFERRALS: {
            	APIResult res = service.listReferrals(userId, sessionId);
                BusinessCard[] referrals = res.cards;

                resultBundle.putString("dataType", "referrals");
                resultBundle.putBoolean("result", true);
                resultBundle.putParcelableArray("referrals", referrals);
                
                break;
            }
            case GET_TEMPLATES: {
            	ArrayList<Template> templates = (ArrayList<Template>) service.getTemplates();
            	
            	resultBundle.putString("dataType", "templates");
                resultBundle.putParcelableArrayList("templates", templates);
            }
            default: {
                break;
            }
            }
        }
        catch (RetrofitError re) {
        	
        	if (re != null || re.getResponse() != null) {
                	
	        	Log.e("response body", re.getResponse().getReason());
	        	
	        	// re.isNetworkError() - network error (no connection)
	        	// re.getResponse().getStatus() == 403 - API error (ex: wrong email/password)
	        	
	        	if (re.getResponse() != null && re.getResponse().getStatus() == 403) { // token expired
	        	}
        	}
        	success = false;
        }
        finally {
        	if (success) {
        		receiver.send(Constants.STATUS_FINISHED, resultBundle);		
        	}
        	else {
        		receiver.send(Constants.STATUS_ERROR, resultBundle);
        	}
        }
        
        this.stopSelf();
    }
}