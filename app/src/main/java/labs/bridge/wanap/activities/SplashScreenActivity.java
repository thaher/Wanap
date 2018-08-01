package labs.bridge.wanap.activities;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import labs.bridge.wanap.R;
import labs.bridge.wanap.helper.SharedPrefManager;
import labs.bridge.wanap.models.Status;
import labs.bridge.wanap.network.ApiUtils;
import labs.bridge.wanap.network.Backend;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashScreenActivity extends AppCompatActivity {

    ArrayList<Status> statuses ;
    private Backend backend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        backend = ApiUtils.getbackend();
        getStatus();
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                Intent mainIntent = new Intent(SplashScreenActivity.this,HomeActivity.class);
                startActivity(mainIntent);
                finish();
            }
        }, 2000);

    }



    private void getStatus() {
        statuses = new ArrayList<>();
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            statuses.add(new Status("BLUETOOTH","Device does not support Bluetooth"));
        } else {
            if (!mBluetoothAdapter.isEnabled()) {
                statuses.add(new Status("BLUETOOTH","Bluetooth is not enabled"));
            }
            else {
                statuses.add(new Status("BLUETOOTH","Bluetooth is enabled"));
            }
        }
        statuses.add(new Status("GPS","GPS_PROVIDER ENABLED :"+isGPSEnabled(SplashScreenActivity.this)));
        statuses.add(new Status("INTERNET","CONNECTED :"+getInternetStatus()));


        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Status>>() {}.getType();
        String json = gson.toJson(statuses, type);
        Log.i("WANAP_STATUS"," : "+json);
        Log.i("WANAP_STATUS","token : "+ SharedPrefManager.getInstance(getApplicationContext()).getDeviceToken());
       sendStatus( SharedPrefManager.getInstance(getApplicationContext()).getDeviceToken(),json);

    }

    private void sendStatus(String deviceToken, String datas) {


        backend.sendStatus(deviceToken,datas).enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {

                if(response.isSuccessful()) {
                    Log.i("WANAP_RETRO", "post submitted to API." + response.toString());
                }
                else {
                    Log.i("WANAP_RETRO", "post submitted to API." + response.toString());

                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
//                Log.e("WANAP_RETRO", "Unable to submit post to API.");
            }
        });
    }

    private boolean getInternetStatus() {
        ConnectivityManager cm =
                (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm != null ? cm.getActiveNetworkInfo() : null;
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        boolean isWiFi = activeNetwork.getType() == ConnectivityManager.TYPE_WIFI;
        statuses.add(new Status("INTERNET","IS WIFI :"+isWiFi));

        return isConnected;
    }

    public boolean isGPSEnabled (Context mContext) {
        LocationManager locationManager = (LocationManager)
                mContext.getSystemService(Context.LOCATION_SERVICE);
        return locationManager != null && locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }





}
