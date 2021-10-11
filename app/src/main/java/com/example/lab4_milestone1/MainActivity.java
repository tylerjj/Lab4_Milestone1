package com.example.lab4_milestone1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private Button startButton;
    private TextView downloadProg;
    private volatile boolean stopThread = false;
    private volatile boolean downloading = false;

    //This interface will be used to tell our thread what task we want it
    // to work on - specifically we put whatever we want it to do in the run() method.
    // For us, this means that we put our call to mockFileDownloader()in there.
    class ExampleRunnable implements Runnable {
        @Override
        public void run() {
            mockFileDownloader();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startButton = findViewById(R.id.startButton);
        downloadProg = findViewById(R.id.downloadProgress);
    }

    // Outputs the progress of our 'download' to Logcat as if
    // we were actually downloading a file.
    public void mockFileDownloader() {

        runOnUiThread(new Runnable(){
            @Override
            public void run() {
                downloadProg.setText("Download Progress: 0%");
                startButton.setText("DOWNLOADING...");
            }
        });

        for (int downloadProgress = 0; downloadProgress <= 100; downloadProgress+=10){
           if (stopThread){
               runOnUiThread(new Runnable(){
                   @Override
                   public void run() {
                       downloadProg.setText("");
                       startButton.setText("Start");
                   }
               });
               downloading = false;
               return;
           }

            Log.d(TAG, "Download Progress: " + downloadProgress + "%");

            int finalDownloadProgress = downloadProgress;

            runOnUiThread(new Runnable(){
               @Override
               public void run() {
                   String str = "Download Progress: "+finalDownloadProgress+"%";
                   downloadProg.setText(str);
               }
           });

            try {

                //Pause each time through the for loop for 1 second.
                // We also update our ‘Download Progress’ percentage in the for loop.
                // This makes it look like our ‘Download Progress’ percentage is
                // increasing by 10% each second.
                Thread.sleep(1000);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        runOnUiThread(new Runnable(){
            @Override
            public void run() {
                downloadProg.setText("");
                startButton.setText("Start");
                downloading = false;
            }
        });
    }

    //We want our ‘file download’ to start when we click the ‘Start’ button.
    // So we need to now define a startDownload() method (see Figure M1-2).
    public void startDownload(View view){
        if (downloading){
            return;
        }
        downloading = true;
        stopThread = false;
        //The next step is to actually create and run our thread (see Figure M1-5).
        // Since we want our thread to start running when we press the ‘Start’ button,
        // we want to put this code in the startDownload() method.
        ExampleRunnable runnable = new ExampleRunnable();
        new Thread(runnable).start();
    }

    public void stopDownload(View view){
        stopThread = true;
    }

}