package com.example.tripremainder.home.details;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.tripremainder.R;

public class DetailsActivity extends AppCompatActivity {
    TextView tripname;
    TextView startpoint;
    TextView endpoint;
    TextView date;
    TextView time;
    TextView state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        setTitle("Trip Details");
        Intent intent = getIntent();
        tripname=findViewById(R.id.nameOfTrip);
        startpoint=findViewById(R.id.startpoint);
        endpoint=findViewById(R.id.endPoint);
        date=findViewById(R.id.date_txt);
        time=findViewById(R.id.timetxt);
        state=findViewById(R.id.state);
        tripname.setText(intent.getStringExtra("tripname"));
        startpoint.setText(intent.getStringExtra("tripstart"));
        endpoint.setText(intent.getStringExtra("tripend"));
        date.setText(intent.getStringExtra("tripdate"));
        time.setText(intent.getStringExtra("triptime").toString().trim());
        state.setText(intent.getStringExtra("tripstate"));
    }
}