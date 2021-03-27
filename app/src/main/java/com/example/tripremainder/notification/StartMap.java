package com.example.tripremainder.notification;

import android.app.NotificationManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tripremainder.AddNewTripActivity;

public class StartMap extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String endLocation= intent.getExtras().getString("endPoint");
            Toast.makeText(StartMap.this, endLocation, Toast.LENGTH_SHORT).show();
        Log.i("TAG", endLocation.toString());
        Uri gmmIntentUri = Uri.parse("geo:0,0?q="+endLocation.toString());
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
        finish();
        NotificationManager notificationManager=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(AddNewTripActivity.Notification_id);
    }


}
