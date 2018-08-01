package labs.bridge.wanap.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Calendar;

import labs.bridge.wanap.R;
import labs.bridge.wanap.helper.SharedPrefManager;
import labs.bridge.wanap.models.Event;
import labs.bridge.wanap.network.ApiUtils;
import labs.bridge.wanap.network.Backend;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static labs.bridge.wanap.activities.HomeActivity.DIRECTION.DOWN;
import static labs.bridge.wanap.activities.HomeActivity.DIRECTION.LEFT;
import static labs.bridge.wanap.activities.HomeActivity.DIRECTION.RIGHT;
import static labs.bridge.wanap.activities.HomeActivity.DIRECTION.UP;

public class HomeActivity extends AppCompatActivity    {
    private  String TAG =this.getClass().getName();
    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
    private GestureDetectorCompat mDetector;
    enum DIRECTION  {LEFT,RIGHT,UP,DOWN};
    private Backend backend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        backend = ApiUtils.getbackend();

        mDetector = new GestureDetectorCompat(this, new MyGestureListener());

        this.registerReceiver(this.mBatInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Event eve= new Event("BUTTON CLICKED","FAB Button", String.valueOf(Calendar.getInstance().getTime()),TAG);
                Gson gson = new Gson();
                Type type = new TypeToken<Event>() {}.getType();
                String json = gson.toJson(eve, type);
                Log.i("WANAP_EVENT"," : "+json);


                sendEvent( SharedPrefManager.getInstance(getApplicationContext()).getDeviceToken(),json);


                Snackbar.make(view, "Button Clicked", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }

    private void sendEvent(String deviceToken, String event) {

        backend.sendEvent(deviceToken,event).enqueue(new Callback<String>() {
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


    private BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context ctxt, Intent intent) {
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
            Event eve= new Event("BATTERY CHANGED","Battery "+String.valueOf(level) + "%", String.valueOf(Calendar.getInstance().getTime()),TAG);
            Gson gson = new Gson();
            Type type = new TypeToken<Event>() {}.getType();
            String json = gson.toJson(eve, type);
            Log.i("WANAP_EVENT"," : "+json);
        }
    };

    @Override
    public boolean onTouchEvent(MotionEvent event){
        this.mDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    class MyGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDown(MotionEvent event) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2,
                               float velocityX, float velocityY) {

            try {
                // right to left swipe
                if (event1.getX() - event2.getX() > SWIPE_MIN_DISTANCE
                        && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    onSwiped(LEFT);
                }
                // left to right swipe
                else if (event2.getX() - event1.getX() > SWIPE_MIN_DISTANCE
                        && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    onSwiped(RIGHT);

                }

                // down to up swipe
                if (event1.getY() - event2.getY() > SWIPE_MIN_DISTANCE
                        && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
                    onSwiped(UP);

                }
                // up to down swipe
                else if (event2.getY() - event1.getY() > SWIPE_MIN_DISTANCE
                        && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
                    onSwiped(DOWN);

                }
            } catch (Exception ignored) {

            }
            return false;
        }
    }

    private void onSwiped(DIRECTION direction) {
        Event eve= new Event("ACTION SWIPED",direction+" Direction", String.valueOf(Calendar.getInstance().getTime()),TAG);
        Gson gson = new Gson();
        Type type = new TypeToken<Event>() {}.getType();
        String json = gson.toJson(eve, type);
        Log.i("WANAP_EVENT"," : "+json);

        sendEvent( SharedPrefManager.getInstance(getApplicationContext()).getDeviceToken(),json);

        Snackbar.make(getWindow().getDecorView().getRootView(), "Swiped : "+direction, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }
}
