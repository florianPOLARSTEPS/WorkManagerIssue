package com.polarsteps.test.workmanagerissue;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.util.concurrent.TimeUnit;

import androidx.work.Data;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import workmanagertest.test.polarsteps.com.workmanagerissue.R;

public class OnetimeAndPeriodicSameUniqueActivity extends AppCompatActivity {

    //region ------- ANDROID -------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OneTimeWorkRequest build = new OneTimeWorkRequest.Builder(SyncWorker.class).setInputData(new Data.Builder().putString("how", "onetime")
                        .build())
                        .build();
                WorkManager.getInstance()
                        .beginUniqueWork("unique", ExistingWorkPolicy.KEEP, build)
                        .enqueue();
            }
        });

        PeriodicWorkRequest periodic = new PeriodicWorkRequest.Builder(SyncWorker.class, 15, TimeUnit.MINUTES).setInputData(new Data.Builder().putString("how", "periodic")
                .build())
                .build();

        WorkManager.getInstance()
                .enqueueUniquePeriodicWork("unique", ExistingPeriodicWorkPolicy.KEEP, periodic);
    }

    @Override
    protected void onPause() {
        WorkManager.getInstance()
                .cancelAllWork();
        super.onPause();
    }
    //endregion ------- ANDROID -------

    public static class SyncWorker extends Worker {

        @NonNull
        @Override
        public Result doWork() {
            Log.d("SyncWorker", "Starting SyncWorker... " + getInputData().getString("how", "unknown"));
            return Result.SUCCESS;
        }
    }
}
