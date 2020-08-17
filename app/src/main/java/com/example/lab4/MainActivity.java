package com.example.lab4;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private Button startWorkBtn = null;
    private TextView txtView = null;
    private Spinner spinner = null;

    private String[] urls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initialise the class member with some data
        initURLS();

        // initialise the UI widgets
        startWorkBtn = (Button) findViewById(R.id.WorkButton);
        txtView = (TextView) findViewById(R.id.textView);
        spinner = (Spinner) findViewById(R.id.spinner);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.planets_array, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);


        // data to pass to worker
        Data data = new Data.Builder().putStringArray(BackgroundWorker.URLS, urls)
                .build();

        // build the work request (BackgroundWorker.class will do the 'work' for us
        final OneTimeWorkRequest workRequest =
                new OneTimeWorkRequest.Builder(BackgroundWorker.class)
                        .setInputData(data)
                        .build();


        startWorkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtView.setText("Starting Work ....." + "\n");
                startWorkBtn.setEnabled(false);
                WorkManager.getInstance(getApplication()).enqueue(workRequest);
            }
        });

        // listen for changes of the worker
        WorkManager.getInstance(this.getApplication()).getWorkInfoByIdLiveData(workRequest.getId())
                .observe(this, new Observer<WorkInfo>() {

                    @Override
                    public void onChanged(@Nullable WorkInfo workInfo) {

                        //Displaying the status into TextView
                        txtView.append(workInfo.getState().name() + "\n");

                        // check to see if the worker has completed
                        if (workInfo != null && workInfo.getState().isFinished()) {
                            txtView.append(workInfo.getOutputData().getString(BackgroundWorker.RETURNTEXT) + "\n");
                            startWorkBtn.setEnabled(true);
                        }
                    }
                });
    }


    // this will be the data to pass to the worker
    // init a class member of MainActivity
    private void initURLS() {
        try {
            urls = new String[]{
                    new String("http://www.amazon.com/somefiles.pdf"),
                    new String("http://www.wrox.com/somefiles.pdf"),
                    new String("http://www.google.com/somefiles.pdf"),
                    new String("http://www.learn2develop.net/somefiles.pdf"),
                    new String("http://www.wrox.com/somefiles.pdf")};
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

