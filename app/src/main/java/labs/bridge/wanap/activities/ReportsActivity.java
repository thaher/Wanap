package labs.bridge.wanap.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import labs.bridge.wanap.R;
import labs.bridge.wanap.models.Event;
import labs.bridge.wanap.models.Status;

public class ReportsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        TextView title_message = (TextView) findViewById(R.id.title_message);
        TextView desc = (TextView) findViewById(R.id.desc);
        TextView time = (TextView) findViewById(R.id.time);
        if (bundle != null) {
            Event event=
                     (Event) bundle.getSerializable("event");

            if (event != null) {
                Log.i("WANAP_REPORTS",event.getEventName());
                title_message.setText(String.format("EVENT : %s", event.getEventName()));
                desc.setText(String.format("Information : %s \nActivity Name : %s", event.getEventDescription(), event.getEventActivity()));
                time.setText(String.format("Time Stamp : %s", event.getTimeStamp()));

            }
        }

        if (bundle != null) {
            String json=
                    (String) bundle.getSerializable("statuses");

            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<Status>>() {}.getType();
            ArrayList<Status> statuses = gson.fromJson(json, type);

            if (statuses != null) {
                desc.setText("");
                Log.i("WANAP_REPORTS", String.valueOf(statuses.size()));
                title_message.setText(String.format("STATUS REPORTS ( %d )", statuses.size()));
                for(Status status :statuses)
                {
                    desc.append(status.getName()+" "+status.getStatus()+" \n");
                }

                time.setText("");
            }
        }





        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
