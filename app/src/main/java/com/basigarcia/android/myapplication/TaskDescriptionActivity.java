package com.basigarcia.android.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

//STEP 6-------------
public class TaskDescriptionActivity extends AppCompatActivity {

    public static final String EXTRA_TASK_DESCRIPTION = "task";

    //UI elements
    private EditText mDescriptionView;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    //STEP 8-------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_description);

        mDescriptionView = (EditText) findViewById(R.id.descriptionText);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    //STEP 8 & 9-------------
    public void doneClicked(View view) {
        // Get the text from the textview
        String taskDescription = mDescriptionView.getText().toString();

        if (!taskDescription.isEmpty()) {
            // 2 Result intent is passed back to the parent activity
            Intent result = new Intent();
            result.putExtra(EXTRA_TASK_DESCRIPTION, taskDescription);
            setResult(RESULT_OK, result);
        } else {
            // Indicate we had no task
            setResult(RESULT_CANCELED);
        }
        // Close the activity, calls onDestroy and returns result to parent activity
        finish();
    }

}
