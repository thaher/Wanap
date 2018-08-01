package labs.bridge.wanap.services;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import labs.bridge.wanap.helper.SharedPrefManager;

public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String token = FirebaseInstanceId.getInstance().getToken();
        Log.d("WANAP_MyRefreshedToken", token);
        SharedPrefManager.getInstance(getApplicationContext()).saveDeviceToken(token);
    }
}
