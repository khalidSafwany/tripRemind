package com.example.tripremainder;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tripremainder.auth.Sign_inActivity;


public class SplashScreen extends AppCompatActivity {
    private static int Splash_screen = 5000;
    Animation tobAnim, bottomAnim;
    ImageView map;
    TextView tripReminder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        tobAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);
        map = findViewById(R.id.img);
        tripReminder = findViewById(R.id.trip_txt);
        map.setAnimation(tobAnim);
        tripReminder.setAnimation(bottomAnim);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreen.this, Sign_inActivity.class);
                startActivity(intent);
                finish();
            }
        },Splash_screen);
    }

}