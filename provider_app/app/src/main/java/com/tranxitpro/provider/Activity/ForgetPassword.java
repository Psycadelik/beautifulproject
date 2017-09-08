package com.tranxitpro.provider.Activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.tranxitpro.provider.R;
import com.tranxitpro.provider.Helper.CustomDialog;
import com.tranxitpro.provider.Utilities.Utilities;

/**
 * Created by jayakumar on 31/01/17.
 */

public class ForgetPassword extends AppCompatActivity {

    ImageView nextICON, backArrow;
    EditText email;
    CustomDialog customDialog;
    Utilities utils = new Utilities();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        findViewById();

        if (Build.VERSION.SDK_INT > 15) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }


        nextICON.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(email.getText().toString().equals("") || email.getText().toString().equalsIgnoreCase(getString(R.string.sample_mail_id))){
                    displayMessage(getString(R.string.email_validation));
                }else{
                    forgetPassword();
                }
            }
        });

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainIntent = new Intent(ForgetPassword.this, ActivityPassword.class);
                mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(mainIntent);
                ForgetPassword.this.finish();
            }
        });

    }


    private void forgetPassword() {
//        customDialog = new CustomDialog(ForgetPassword.this);
//        customDialog.setCancelable(false);
//        customDialog.show();
//        JSONObject object = new JSONObject();
//        try {
//
//            object.put("grant_type", "password");
//            object.put("client_id", URLHelper.client_id);
//            object.put("client_secret", URLHelper.client_secret);
//            object.put("username", SharedHelper.getKey(ActivityPassword.this,"email"));
//            object.put("password", SharedHelper.getKey(ActivityPassword.this, "password"));
//            object.put("scope", "");
//            utils.print("InputToLoginAPI",""+object);
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URLHelper.login, object, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                customDialog.dismiss();
//                Log.v("SignUpResponse",response.toString());
//                SharedHelper.putKey(ActivityPassword.this, "access_token", response.optString("access_token"));
//                SharedHelper.putKey(ActivityPassword.this, "refresh_token", response.optString("refresh_token"));
//                SharedHelper.putKey(ActivityPassword.this,"loggedIn",getString(R.string.True));
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                customDialog.dismiss();
//                String json = null;
//                String Message;
//                NetworkResponse response = error.networkResponse;
//                utils.print("MyTest",""+error);
//                utils.print("MyTestError",""+error.networkResponse);
//                utils.print("MyTestError1",""+response.statusCode);
//                if(response != null && response.data != null){
//                    try {
//                        JSONObject errorObj = new JSONObject(new String(response.data));
//
//                        if(response.statusCode == 400 || response.statusCode == 405 || response.statusCode == 500){
//                            try{
//                                displayMessage(errorObj.optString("message"));
//                            }catch (Exception e){
//                                displayMessage("Something went wrong.");
//                            }
//                        }else if(response.statusCode == 401){
//                            try{
//                                if(errorObj.optString("message").equalsIgnoreCase("invalid_token")){
//                                    //Call Refresh token
//                                }else{
//                                    displayMessage(errorObj.optString("message"));
//                                }
//                            }catch (Exception e){
//                                displayMessage("Something went wrong.");
//                            }
//
//                        }else if(response.statusCode == 422){
//
//                            json = trimMessage(new String(response.data));
//                            if(json !="" && json != null) {
//                                displayMessage(json);
//                            }else{
//                                displayMessage("Please try again.");
//                            }
//
//                        }else{
//                            displayMessage("Please try again.");
//                        }
//
//                    }catch (Exception e){
//                        displayMessage("Something went wrong.");
//                    }
//
//
//                }
//            }
//        }){
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                HashMap<String, String> headers = new HashMap<String, String>();
//                headers.put("X-Requested-With", "XMLHttpRequest");
//                return headers;
//            }
//        };
//
//        XuberApplication.getInstance().addToRequestQueue(jsonObjectRequest);

    }

    public void findViewById(){
        email = (EditText)findViewById(R.id.email);
        nextICON = (ImageView) findViewById(R.id.nextIcon);
        backArrow = (ImageView) findViewById(R.id.backArrow);
    }

    public void displayMessage(String toastString){
        utils.print("displayMessage",""+toastString);
        Snackbar.make(getCurrentFocus(),toastString, Snackbar.LENGTH_SHORT)
                .setAction("Action", null).show();
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        Intent mainIntent = new Intent(ForgetPassword.this, ActivityPassword.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(mainIntent);
        ForgetPassword.this.finish();
    }
}
