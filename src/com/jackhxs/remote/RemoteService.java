package com.jackhxs.remote;

import org.json.JSONObject;

import retrofit.RestAdapter;
import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.jackhxs.cardbank.App;
import com.jackhxs.data.ContactAndCards;
import com.jackhxs.data.SimpleCard;
import com.jackhxs.remote.Constants.Operation;

public class RemoteService extends IntentService {
    private final RestAdapter restAdapter = new RestAdapter.Builder()
    .setServer("http://www.mocky.io/v2")
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
        String accessToken = App.accessToken;

        Log.i("paul", command.toString());
        //receiver.send(Constants.STATUS_RUNNING, Bundle.EMPTY);

        switch (command) {
        case POST_SIGNUP: {
            String username = intent.getStringExtra("username");
            String password = intent.getStringExtra("password");
            String newAccessToken = service.signup(username, password);
            b.putString("accessToken", newAccessToken);
            receiver.send(Constants.STATUS_FINISHED, b);
            Log.i("remoteService", newAccessToken);
            break;
        }
        case POST_LOGIN: {
            String username = intent.getStringExtra("username");
            String password = intent.getStringExtra("password");
            
            Gson g = new Gson();
            JSONObject tmp = service.login(username, password);
            

            b.putString("accessToken", tmp.toString());

            receiver.send(Constants.STATUS_FINISHED, b);
            Log.i("remoteService", tmp.toString());
            break;
        }
        case GET_CARDS: {
            SimpleCard[] contacts = service.listOwnCards(accessToken);

            b.putParcelableArray("cards", contacts);
            b.putString("action", "initialization");
            b.putBoolean("result", true);

            receiver.send(Constants.STATUS_FINISHED, b);
            break;
        }
        case GET_CONTACTS: {
            SimpleCard[] contacts = service.listContacts(accessToken);
            b.putParcelableArray("contacts", contacts);

            b.putBoolean("result", true);
            b.putString("action", "initialization");

            receiver.send(Constants.STATUS_FINISHED, b);
            break;
        }
        case GET_BOTH_CONTACT_AND_CARD: {
            ContactAndCards contactAndCards = service.listContactAndCards(accessToken);

            b.putParcelableArray("contacts", contactAndCards.contacts);
            b.putParcelableArray("cards", contactAndCards.cards);

            b.putString("action", "initialization");
            b.putBoolean("result", true);

            receiver.send(Constants.STATUS_FINISHED, b);
            break;
        }
        case PUT_CARD:
        case POST_CARD: {
            String simpleCardJSON = intent.getStringExtra("simpleCardJSON");

            SimpleCard simpleCard = new Gson().fromJson(simpleCardJSON, SimpleCard.class);
            Boolean result = service.addCard(accessToken, simpleCard);

            b.putString("action", "updated");
            b.putBoolean("result", result);

            receiver.send(Constants.STATUS_FINISHED, b);
            break;
        }
        case POST_CONTACT: {
            String simpleCardJSON = intent.getStringExtra("newContactJSON");

            SimpleCard simpleCard = new Gson().fromJson(simpleCardJSON, SimpleCard.class);
            Boolean result = service.addContact(accessToken, simpleCard);

            b.putString("action", "updated");
            b.putBoolean("result", result);

            receiver.send(Constants.STATUS_FINISHED, b);
            break;
        }
        case DEL_CARD: {
            SimpleCard existingCard = intent.getParcelableExtra("existingCard");
            Boolean result = service.deleteCard(accessToken, existingCard);

            b.putString("action", "updated");
            b.putBoolean("result", result);

            receiver.send(Constants.STATUS_FINISHED, b);
            break;
        }
        case DEL_CONTACT: {
            SimpleCard existingCard = intent.getParcelableExtra("existingCard");
            Boolean result = service.deleteCard(accessToken, existingCard);

            b.putString("action", "updated");
            b.putBoolean("result", result);

            receiver.send(Constants.STATUS_FINISHED, b);
            break;
        }
        case REFER: {
            Log.e("cb", "referred");
            String referredTo = intent.getStringExtra("referedTo");
            String cardId = intent.getStringExtra("cardId");
            Boolean result = service.refer(accessToken, referredTo, cardId);

            b.putString("action", "updated");
            b.putBoolean("result", result);

            receiver.send(Constants.STATUS_FINISHED, b);
            break;
        }
        case LIST_REFERRALS: {
            SimpleCard[] result = service.listReferrals(accessToken);

            b.putString("action", "updated");
            b.putParcelableArray("result", result);

            receiver.send(Constants.STATUS_FINISHED, b);
            break;
        }
        default: {
            break;
        }
        }
        this.stopSelf();
    }
}
