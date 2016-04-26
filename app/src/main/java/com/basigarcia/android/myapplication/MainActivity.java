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
import java.util.Arrays;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    ////STEP 2-------------   UI elements
    TextView mDateTimeTextView;
    ArrayList<String> mList;
    ArrayAdapter<String> mAdapter;

    ////STEP 3------------- Codes we will use later to identify what we are doing
    private final int ADD_TASK_REQUEST = 1;

    //STEP 16-------------
    private final String PREFS_TASKS = "prefs_tasks";
    private final String KEY_TASKS_LIST = "list";

    //STEP 13-------------    Logic elements
    private BroadcastReceiver mTickReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Explain
        super.onCreate(savedInstanceState);

        //Explain
        setContentView(R.layout.activity_main);

        //STEP 2------------
        mDateTimeTextView = (TextView) findViewById(R.id.dateTimeTextView);
        final Button addTaskBtn = (Button) findViewById(R.id.addTaskBtn);
        final ListView listview = (ListView) findViewById(R.id.taskListview);
        mList = new ArrayList<String>();

        ////STEP 18------------- Get shared preferences saved tasks
        String savedList = getSharedPreferences(PREFS_TASKS, MODE_PRIVATE).getString(KEY_TASKS_LIST, null);
        if (savedList != null) {
            String[] items = savedList.split(",");
            mList = new ArrayList<String>(Arrays.asList(items));
        }
        //STEP 2------------- Set the adapter for the listview
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mList);
        listview.setAdapter(mAdapter);

        //STEP 2------------- set Onclick listener for items on the list, we won't cover this in the tutorial
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });

        ////STEP 14------------- Set the broadcast receiver
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

    ////STEP 4------------- Listener for the button defined in the activity xml
    public void addTaskClicked(View view){
        Intent intent = new Intent(MainActivity.this, TaskDescriptionActivity.class);

        //Need to know if there is a task to add, so we use startActivityForResult, otherwise
        //just use startActivity(intent). When the taskDescriptionActivity finishes, it will return
        //a result in an intent
        startActivityForResult(intent, ADD_TASK_REQUEST);

    }


    //Create in STEP 5, Implement in STEP 11 -------------
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

    //STEP 12-------------
    private static String getCurrentTimeStamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");//dd/MM/yyyy
        Date now = new Date();
        String strDate = sdf.format(now);
        return strDate;
    }


    //STEP 15-------------
    @Override
    protected void onResume() {
        // Explain
        super.onResume();
        // 2 Update to current value
        mDateTimeTextView.setText(getCurrentTimeStamp());
        // 3 We need to register receiver so it listens for the events
        registerReceiver(mTickReceiver, new IntentFilter(Intent.ACTION_TIME_TICK));
    }


    //STEP 15-------------
    @Override
    protected void onPause() {
        // Explain
        super.onPause();
        // If we have a receiver initialized
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

    //STEP 17-------------
    @Override
    protected void onStop() {
        super.onStop();

        // Save all data which you want to persist.
        StringBuilder savedList = new StringBuilder();
        for (String s : mList) {
            savedList.append(s);
            savedList.append(",");
        }
        getSharedPreferences(PREFS_TASKS, MODE_PRIVATE).edit()
                .putString(KEY_TASKS_LIST, savedList.toString()).commit();
    }


}
