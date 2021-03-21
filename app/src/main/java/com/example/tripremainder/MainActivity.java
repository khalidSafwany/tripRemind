package com.example.tripremainder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        System.out.println("start");
        System.out.println("khalid");
    }

    @Override
    protected void onStart() {
        super.onStart();



    }


    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = new Intent(MainActivity.this,AddNewTripActivity.class);
        startActivity(intent);
    }
}

