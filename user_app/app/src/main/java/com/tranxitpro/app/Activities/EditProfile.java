package com.tranxitpro.app.Activities;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;
import com.tranxitpro.app.Helper.AppHelper;
import com.tranxitpro.app.Helper.ConnectionHelper;
import com.tranxitpro.app.Helper.CustomDialog;
import com.tranxitpro.app.Helper.SharedHelper;
import com.tranxitpro.app.Helper.URLHelper;
import com.tranxitpro.app.Helper.VolleyMultipartRequest;
import com.tranxitpro.app.R;
import com.tranxitpro.app.Utils.MyBoldTextView;
import com.tranxitpro.app.XuberApplication;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class EditProfile extends AppCompatActivity {

    private static final int SELECT_PHOTO = 100;
    public Context context = EditProfile.this;
    public Activity activity = EditProfile.this;
    String TAG = "EditActivity";
    CustomDialog customDialog;
    ConnectionHelper helper;
    Boolean isInternet;
    Button saveBTN;
    ImageView backArrow;
    MyBoldTextView changePasswordTxt;
    EditText email, first_name, last_name, mobile_no;
    ImageView profile_Image;
    Boolean isImageChanged = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        findViewByIdandInitialization();

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoToMainActivity();
            }
        });

        saveBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (email.getText().toString().equals("") || email.getText().toString().length() == 0) {
                    displayMessage(getString(R.string.email_validation));
                } else if (mobile_no.getText().toString().equals("") || mobile_no.getText().toString().length() == 0) {
                    displayMessage(getString(R.string.mobile_number_empty));
                } else if (first_name.getText().toString().equals("") || first_name.getText().toString().length() == 0) {
                    displayMessage(getString(R.string.first_name_empty));
                } else if (last_name.getText().toString().equals("") || last_name.getText().toString().length() == 0) {
                    displayMessage(getString(R.string.last_name_empty));
                } else {
                    if (isInternet) {
                        updateProfile();
                    } else {
                        displayMessage(getString(R.string.something_went_wrong_net));
                    }
                }


            }
        });


        changePasswordTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(activity, ChangePassword.class));
            }
        });


        profile_Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PHOTO);
            }
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_PHOTO && resultCode == activity.RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();
            Bitmap bitmap = null;

            try {
                isImageChanged = true;
                bitmap = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), uri);
                profile_Image.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public void updateProfile() {

        if (isImageChanged == true) {
            isImageChanged = false;
            customDialog = new CustomDialog(context);
            customDialog.setCancelable(false);
            customDialog.show();
            VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, URLHelper.UseProfileUpdate, new Response.Listener<NetworkResponse>() {
                @Override
                public void onResponse(NetworkResponse response) {
                    customDialog.dismiss();

                    String res = new String(response.data);
                    try {
                        JSONObject jsonObject = new JSONObject(res);
                        SharedHelper.putKey(context, "id", jsonObject.optString("id"));
                        SharedHelper.putKey(context, "first_name", jsonObject.optString("first_name"));
                        SharedHelper.putKey(context, "last_name", jsonObject.optString("last_name"));
                        SharedHelper.putKey(context, "email", jsonObject.optString("email"));
                        if (jsonObject.optString("picture").equals("") || jsonObject.optString("picture") == null) {
                            SharedHelper.putKey(context, "picture", "");
                        } else {
                            SharedHelper.putKey(context, "picture", URLHelper.base+"storage/"+jsonObject.optString("picture"));
                        }

                        SharedHelper.putKey(context, "gender", jsonObject.optString("gender"));
                        SharedHelper.putKey(context, "mobile", jsonObject.optString("mobile"));
                        SharedHelper.putKey(context, "wallet_balance", jsonObject.optString("wallet_balance"));
                        SharedHelper.putKey(context, "payment_mode", jsonObject.optString("payment_mode"));
                        displayMessage(getString(R.string.update_success));

                    } catch (JSONException e) {
                        e.printStackTrace();
                        displayMessage(getString(R.string.something_went_wrong));
                    }


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    customDialog.dismiss();
                    Log.e(TAG, "" + error);
                    displayMessage(getString(R.string.something_went_wrong));

                }
            }) {
                @Override
                public Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("first_name", first_name.getText().toString());
                    params.put("last_name", last_name.getText().toString());
                    params.put("email", email.getText().toString());
                    params.put("mobile", mobile_no.getText().toString());
                    return params;
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("X-Requested-With", "XMLHttpRequest");
                    headers.put("Authorization", "" + SharedHelper.getKey(context, "token_type") + " " + SharedHelper.getKey(context, "access_token"));
                    return headers;
                }

                @Override
                protected Map<String, VolleyMultipartRequest.DataPart> getByteData() throws AuthFailureError {
                    Map<String, VolleyMultipartRequest.DataPart> params = new HashMap<>();
                    params.put("picture", new VolleyMultipartRequest.DataPart("userImage.jpg", AppHelper.getFileDataFromDrawable(profile_Image.getDrawable()), "image/jpeg"));
                    return params;
                }
            };
            XuberApplication.getInstance().addToRequestQueue(volleyMultipartRequest);
        } else {
            customDialog = new CustomDialog(context);
            customDialog.setCancelable(false);
            customDialog.show();
            VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, URLHelper.UseProfileUpdate, new Response.Listener<NetworkResponse>() {
                @Override
                public void onResponse(NetworkResponse response) {
                    customDialog.dismiss();

                    String res = new String(response.data);
                    try {
                        JSONObject jsonObject = new JSONObject(res);
                        SharedHelper.putKey(context, "id", jsonObject.optString("id"));
                        SharedHelper.putKey(context, "first_name", jsonObject.optString("first_name"));
                        SharedHelper.putKey(context, "last_name", jsonObject.optString("last_name"));
                        SharedHelper.putKey(context, "email", jsonObject.optString("email"));
                        if (jsonObject.optString("picture").equals("") || jsonObject.optString("picture") == null) {
                            SharedHelper.putKey(context, "picture", "");
                        } else {
                            SharedHelper.putKey(context, "picture", URLHelper.base+"storage/"+jsonObject.optString("picture"));
                        }

                        SharedHelper.putKey(context, "gender", jsonObject.optString("gender"));
                        SharedHelper.putKey(context, "mobile", jsonObject.optString("mobile"));
                        SharedHelper.putKey(context, "wallet_balance", jsonObject.optString("wallet_balance"));
                        SharedHelper.putKey(context, "payment_mode", jsonObject.optString("payment_mode"));

                        displayMessage(getString(R.string.update_success));

                    } catch (JSONException e) {
                        e.printStackTrace();
                        displayMessage(getString(R.string.something_went_wrong));
                    }


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    customDialog.dismiss();
                    Log.e(TAG, "" + error);
                    displayMessage(getString(R.string.something_went_wrong));

                }
            }) {
                @Override
                public Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("first_name", first_name.getText().toString());
                    params.put("last_name", last_name.getText().toString());
                    params.put("email", email.getText().toString());
                    params.put("mobile", mobile_no.getText().toString());
                    params.put("picture", "");

                    return params;
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("X-Requested-With", "XMLHttpRequest");
                    headers.put("Authorization", "" + SharedHelper.getKey(context, "token_type") + " " + SharedHelper.getKey(context, "access_token"));
                    return headers;
                }
            };
            XuberApplication.getInstance().addToRequestQueue(volleyMultipartRequest);
        }
    }


    public void findViewByIdandInitialization() {
        email = (EditText) findViewById(R.id.email);
        first_name = (EditText) findViewById(R.id.first_name);
        last_name = (EditText) findViewById(R.id.last_name);
        mobile_no = (EditText) findViewById(R.id.mobile_no);
        saveBTN = (Button) findViewById(R.id.saveBTN);
        changePasswordTxt = (MyBoldTextView) findViewById(R.id.changePasswordTxt);
        backArrow = (ImageView) findViewById(R.id.backArrow);
        profile_Image = (ImageView) findViewById(R.id.img_profile);
        helper = new ConnectionHelper(context);
        isInternet = helper.isConnectingToInternet();

        //Assign current profile values to the edittext
        //Glide.with(activity).load(SharedHelper.getKey(context, "picture")).placeholder(R.drawable.loading).error(R.drawable.ic_dummy_user).into(profile_Image);
        Picasso.with(context).load(SharedHelper.getKey(context,"picture")).placeholder(R.drawable.ic_dummy_user).error(R.drawable.ic_dummy_user).into(profile_Image);
        email.setText(SharedHelper.getKey(context, "email"));
        first_name.setText(SharedHelper.getKey(context, "first_name"));
        last_name.setText(SharedHelper.getKey(context, "last_name"));
        if (SharedHelper.getKey(context, "mobile") != null
                && !SharedHelper.getKey(context, "mobile").equals("null")) {
            mobile_no.setText(SharedHelper.getKey(context, "mobile"));
        }
    }


    public void GoToMainActivity() {
        Intent mainIntent = new Intent(activity, MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(mainIntent);
        activity.finish();
    }

    public void displayMessage(String toastString) {
        Log.e("displayMessage", "" + toastString);
        Snackbar.make(getCurrentFocus(), toastString, Snackbar.LENGTH_SHORT)
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
    public void onBackPressed() {
        GoToMainActivity();
    }

}
