package com.example.tripremainder.notification;

import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tripremainder.R;

public class DialogNotification extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_dialog_notification);
        String tripNameNot = getIntent().getStringExtra("event");
        String endPointNot= getIntent().getStringExtra("end");
        int id = getIntent().getIntExtra("tripID",-1);

        //Log.i("TAG", "onCreate: " + s);
        final MediaPlayer mp = MediaPlayer.create(this, R.raw.sample);
        mp.start();
       AlertCustomDialog noteCustomDialog = new AlertCustomDialog(tripNameNot,endPointNot,id);
        noteCustomDialog.show(getSupportFragmentManager(), "DialogTest");
    }
}