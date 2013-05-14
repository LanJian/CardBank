package com.jackhxs.cardbank;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class LoginActivity extends Activity {

    private EditText emailField, passwordField;

    @Override
    public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_login);

      emailField = (EditText) findViewById(R.id.login_email);
      passwordField = (EditText) findViewById(R.id.login_password);
    }


    public void login(View view) {
      String email = emailField.getText().toString();
      String password = passwordField.getText().toString();
      
      Intent intent = new Intent(this, CardList.class);
      startActivity(intent);
    }

}