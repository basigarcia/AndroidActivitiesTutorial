package com.basigarcia.android.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    //UI elements
    TextView mDateTimeTextView;
    ArrayList<String> mList;
    ArrayAdapter<String> mAdapter;

    //Codes we will use later to identify what we are doing
    private final int ADD_TASK_REQUEST = 1;

    //Logic elements
    private BroadcastReceiver mTickReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //1
        super.onCreate(savedInstanceState);

        //2
        setContentView(R.layout.activity_main);

        // 4
        mDateTimeTextView = (TextView) findViewById(R.id.dateTimeTextView);
        final Button addTaskBtn = (Button) findViewById(R.id.addTaskBtn);
        final ListView listview = (ListView) findViewById(R.id.taskListview);
        mList = new ArrayList<String>();

        // 5
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mList);
        listview.setAdapter(mAdapter);

        // 6
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });

        mTickReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //Listen for actions of type time_tick
                if (intent.getAction().equals(Intent.ACTION_TIME_TICK)) {
                    mDateTimeTextView.setText(getCurrentTimeStamp());
                }
            }
        };
    }

    //Listener for the button defined in the activity xml
    public void addTaskClicked(View view){
        Intent intent = new Intent(MainActivity.this, TaskDescriptionActivity.class);

        //Need to know if there is a task to add, so we use startActivityForResult, otherwise
        //just use startActivity(intent). When the taskDescriptionActivity finishes, it will return
        //a result in an intent
        startActivityForResult(intent, ADD_TASK_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 1 - Check which request you're responding to. We used ADD_TASK_REQUEST
        if (requestCode == ADD_TASK_REQUEST) {
            // 2 - Make sure the request was successful
            if (resultCode == RESULT_OK) {
                // 3 - The user entered a task. Add a task to the list.
                String task = data.getStringExtra(TaskDescriptionActivity.EXTRA_TASK_DESCRIPTION);
                mList.add(task);
                // 4 Need to update the content of the adapter
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    private static String getCurrentTimeStamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");//dd/MM/yyyy
        Date now = new Date();
        String strDate = sdf.format(now);
        return strDate;
    }

    @Override
    protected void onResume() {
        // 1
        super.onResume();
        // 2 Update to current value
        mDateTimeTextView.setText(getCurrentTimeStamp());
        // 3 We need to register receiver so it listens for the events
        registerReceiver(mTickReceiver, new IntentFilter(Intent.ACTION_TIME_TICK));
    }

    @Override
    protected void onPause() {
        // 4
        super.onPause();
        // 5
        if (mTickReceiver != null) {
            try {
                //We don't need to be listening for events when the app is not in this activity
                //this is good performance
                unregisterReceiver(mTickReceiver);
            } catch (IllegalArgumentException e) {
                Log.e("MAINACTIVITY", "Timetick Receiver not registered", e);
            }
        }
    }


}
