package com.tranxitpro.provider.FCM;

/**
 * Created by jayakumar on 16/02/17.
 */


import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.tranxitpro.provider.Helper.SharedHelper;
import com.tranxitpro.provider.Utilities.Utilities;

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG = MyFirebaseInstanceIDService.class.getSimpleName();
    Utilities utils = new Utilities();

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        SharedHelper.putKey(getApplicationContext(),"device_token",""+refreshedToken);
        utils.print(TAG,""+refreshedToken);
    }
}