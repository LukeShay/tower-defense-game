package com.example.towerDefender.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.towerDefender.Activities.NavigationActivity;
import com.example.towerDefender.VolleyServices.VolleyResponseListener;
import com.example.towerDefender.VolleyServices.VolleyUtilities;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.provider.Settings.Secure.ANDROID_ID;

public class registerUserActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(com.example.towerDefender.R.layout.register_user_activity);

        Map<String, String> MyData = new HashMap<String, String>();
        MyData.put("phoneId", "abc123");
        MyData.put("userName", "abc123");
        MyData.put("email", "email@email.com");
        MyData.put("firstName", "FirstNameTest");
        MyData.put("lastName", "LastNameTest");
        MyData.put("userType", "Admin"); //Add the data you'd like to send to the server.

        Button submit = (Button) findViewById(com.example.towerDefender.R.id.submitButton);
        final EditText screenName = (EditText) findViewById(com.example.towerDefender.R.id.usernameField);

       final Intent intent = new Intent(this, NavigationActivity.class);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("phoneId", "123456");
                    jsonObject.put("userName","test_user");
                    jsonObject.put("email", "email@email.com");
                    jsonObject.put("firstName", "FirstNameTest");
                    jsonObject.put("lastName", "LastNameTest");
                    jsonObject.put("userType", "Admin");
                }
                catch(JSONException e){Log.e("e", e.toString());}
                VolleyUtilities.postRequest(getApplicationContext(), "http://coms-309-ss-5.misc.iastate.edu:8080/users", new VolleyResponseListener() {
                    @Override
                    public void onError(String message) {
                        Log.e("post-error", message);
                    }

                    @Override
                    public void onResponse(Object response) {
                        Log.e("user-request", response.toString());
                    }
                },jsonObject);

                startActivity(intent);
            }
        });
    }

    private String generateUUID(){
       return Settings.Secure.getString(getApplicationContext().getContentResolver(), ANDROID_ID);
    }
}