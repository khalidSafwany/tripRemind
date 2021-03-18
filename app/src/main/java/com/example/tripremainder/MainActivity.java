package com.example.tripremainder;

import androidx.appcompat.app.AppCompatActivity;

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
        System.out.println("azhar");
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}