package com.example.tripremainder.notification;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.tripremainder.R;

public class DialogMessageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     //   setContentView(R.layout.activity_dialog_message);
        String tripName = getIntent().getStringExtra("tripName");
        String endPoint= getIntent().getStringExtra("tripLocation");
        int id = getIntent().getIntExtra("tripID",-1);
        Intent intent = new Intent("BG_SHOW_BROADCAST");
        intent.putExtra("BROADCAST", "finishBgShowActivity");
        LocalBroadcastManager.getInstance(DialogMessageActivity.this)
                .sendBroadcast(intent);
        //Log.i("TAG", "onCreate: " + s);
        final MediaPlayer mp = MediaPlayer.create(this, R.raw.sample);
        mp.start();
        AlertCustomDialog noteCustomDialog = new AlertCustomDialog(tripName,endPoint,id);
        noteCustomDialog.show(getSupportFragmentManager(), "DialogTest");
       // this.setFinishOnTouchOutside(true);
    }

}