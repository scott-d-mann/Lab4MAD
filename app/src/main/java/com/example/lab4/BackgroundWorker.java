package com.example.lab4;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class BackgroundWorker extends Worker {

    public static final String URLS = "URLS"; // from the MainActivity

    public static final String RETURNTEXT = "receivedWorkerText"; // to the MainActivity

    public BackgroundWorker(
            @NonNull Context context,
            @NonNull WorkerParameters params) {
        super(context, params);
    }

    @Override
    public Result doWork() {

        Context appContext = getApplicationContext();

        try {
            // do something that may take some time
            downloadFile(getInputData().getStringArray(URLS));

            // Indicate the work finished successfully with the Result
            Data data = new Data.Builder()
                    .putString(RETURNTEXT, "Work Done!!")
                    .build();

            return Result.success(data);
        } catch (Throwable throwable) {
            Log.e("ERROR", "doWork() Failed", throwable);
            // Indicate  the work failed with the Result
            Data data = new Data.Builder()
                    .putString(RETURNTEXT, "Work Failed!!")
                    .build();
            return Result.failure();
        }
    }


     private void downloadFile(String[] urls) {
        try {

            for (String url: urls) {
                //---simulate taking some time to download a file---
                Thread.sleep( 2000);
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}