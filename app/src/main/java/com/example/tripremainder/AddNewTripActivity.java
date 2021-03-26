package com.example.tripremainder;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.InputType;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tripremainder.DataBase.Model.NewTrip;
import com.example.tripremainder.DataBase.RoomDB;
import com.example.tripremainder.home.HomeAdapter;
import com.example.tripremainder.home.HomeList;
import com.example.tripremainder.notification.AlarmBrodcast;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.*;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AddNewTripActivity extends AppCompatActivity{

    static EditText tripNameEditText;
    EditText startLocationEditText;
    EditText endLocationEditText;
    ImageButton calenderButton;
    ImageButton timeButton;
    ImageButton addNoteBtn;
    TextView dateText;
    TextView timeText;
    Spinner tripTypeSpinner;
    Button addTripBtn;
    NewTrip tempHomeList;



    String tripName;
    String startLocation;
    String endLocation;
    String date;
    String time;
    String tripType;
    ArrayList<String>notes;
    PlacesClient placesClient;


    RoomDB database;
    List<NewTrip> dataList = new ArrayList<>();
    private HomeAdapter adapter;
    public static final int Notification_id = 1;
    String timeTonotify;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_trip);
        setTitle("New Trip");
        notes = new ArrayList<>();
        setView();

        Places.initialize(AddNewTripActivity.this,"AIzaSyDNuanqZTnydcYiOF0PjV1MR_f8t_vGv1Q");
        placesClient = Places.createClient(this);


        //Hager code
        database = RoomDB.getInstance(this);
        dataList = database.tripDaos().getUpcomingTrips();


        Intent intent = getIntent();
        tripNameEditText.setText(intent.getStringExtra("trip_name"));
        startLocationEditText.setText(intent.getStringExtra("trip_start_point"));
        endLocationEditText.setText(intent.getStringExtra("trip_end_point"));
        dateText.setText(intent.getStringExtra("trip_date"));
        timeText.setText(intent.getStringExtra("trip_time"));
        ////////////////

    }





    private void setView(){
        tripNameEditText = findViewById(R.id.TripNameTextField);
        startLocationEditText = findViewById(R.id.startPlaceTextField);
        endLocationEditText = findViewById(R.id.endLocationText);
        calenderButton = findViewById(R.id.calenderButton);
        calenderButton.setOnClickListener(v-> showDatePicker());
        dateText = findViewById(R.id.DateTextView);
        timeButton = findViewById(R.id.timeButton);
        timeButton.setOnClickListener(v-> showTimePicker());
        timeText = findViewById(R.id.timeText);
        tripTypeSpinner = findViewById(R.id.tripTypeSpinner);
        addTripBtn = findViewById(R.id.addTripButton);

        addTripBtn.setOnClickListener(v->submit());

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.tripsTypesArray, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tripTypeSpinner.setAdapter(adapter);
/*
        addNoteBtn = findViewById(R.id.addNotesButton);
        addNoteBtn.setOnClickListener(v->{
            showAddNoteDialogue();
        });
*/
        startLocationEditText.setOnClickListener(v->{
            showPlacesAssistant();
        });
    }



    private void showPlacesAssistant(){
        List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS, Place.Field.LAT_LNG, Place.Field.NAME);
        Intent searchIntent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY,fieldList).build(AddNewTripActivity.this);
        startActivityForResult(searchIntent,100);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {

            Place place = Autocomplete.getPlaceFromIntent(data);
            startLocationEditText.setText(place.getAddress());

        }
        else if(resultCode == AutocompleteActivity.RESULT_ERROR)
        {
            Status status = Autocomplete.getStatusFromIntent(data);
            Toast.makeText(getApplicationContext(), status.getStatusMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    // Hager code

    private void submit(){
        if(validate()){
            Toast.makeText(AddNewTripActivity.this," Input Validated",Toast.LENGTH_SHORT).show();
            tempHomeList = new NewTrip();
            tempHomeList.setState(0);
            tempHomeList.setTripName(tripNameEditText.getText().toString().trim());
            tempHomeList.setStartPoint(startLocationEditText.getText().toString().trim());
            tempHomeList.setEndPoint(endLocationEditText.getText().toString().trim());
            tempHomeList.setTripDate(dateText.getText().toString().trim());
            tempHomeList.setTripTime(timeText.getText().toString().trim());
            displayAlert();
            Intent returnIntent = new Intent();
            returnIntent.putExtra("result", (Serializable) tempHomeList);
            setResult(200,returnIntent);
            //finish();
        }

    }

    ///////////////////////////////////


//    private void submit(){
//        if(validate()){
//            Toast.makeText(AddNewTripActivity.this," Input Validated",Toast.LENGTH_SHORT).show();
//            tempHomeList = new HomeList();
//            tempHomeList.setTripName(tripNameEditText.getText().toString());
//            tempHomeList.setStartPoint(startLocationEditText.getText().toString());
//            tempHomeList.setEndPoint(endLocationEditText.getText().toString());
//            tempHomeList.setTripDate(dateText.getText().toString());
//            tempHomeList.setTripTime(timeText.getText().toString());
//            //tempHomeList.setNotes(notes);
//            Intent returnIntent = new Intent();
//            returnIntent.putExtra("result", (Serializable) tempHomeList);
//            setResult(200,returnIntent);
//            finish();
//        }
//
//    }

    private boolean validate(){
        if(!tripNameEditText.getText().toString().matches("")){
            if(!startLocationEditText.getText().toString().matches("")){
                if(!endLocationEditText.getText().toString().matches("")){
                    if(!timeText.getText().toString().matches("")){
                        if(dateText.getText().toString().matches(""))
                        {
                            Toast.makeText(AddNewTripActivity.this,"Missing Trip Date",Toast.LENGTH_SHORT).show();
                            return false;
                        }

                    }else{
                        Toast.makeText(AddNewTripActivity.this,"Missing Trip Time",Toast.LENGTH_SHORT).show();
                        return false;
                    }

                }else{
                    Toast.makeText(AddNewTripActivity.this,"Missing Trip End Location",Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
            else{
                Toast.makeText(AddNewTripActivity.this,"Missing Trip Start Location",Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        else{
            Toast.makeText(AddNewTripActivity.this,"Missing Trip Name",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void showTimePicker(){
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        boolean is24HourFormat = android.text.format.DateFormat.is24HourFormat(AddNewTripActivity.this);

        TimePickerDialog timePickerDialog = new TimePickerDialog(AddNewTripActivity.this, (
                timePicker, hourOfDay, minutes) -> {
            String dateString = hourOfDay+" : "+ minutes;
            timeText.setText(dateString);

        },
                hour,
                minute,
                is24HourFormat);
        timePickerDialog.setTitle("Set your trip start time");
        timePickerDialog.show();

    }


    private void showDatePicker(){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(AddNewTripActivity.this,
                (datePicker, year1, month1, day) -> {
                    String dateString = day + "/" + month+1 + "/" + year;
                    dateText.setText(dateString);
                }, year, month, dayOfMonth);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        datePickerDialog.setTitle("Set your trip date");
        datePickerDialog.show();
    }

/*
    void showAddNoteDialogue(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Note");

// Set up the input
        final EditText input = new EditText(this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

// Set up the buttons
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                notes.add(input.getText().toString());
                Toast.makeText(AddNewTripActivity.this,"Note added",Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    */

    public void setAlarm(String tripName, String date, String time, String endLocation) {

        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getApplicationContext(), AlarmBrodcast.class);

        intent.putExtra("event", tripName);
        intent.putExtra("end", endLocation);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);
        String dateandtime = date + " " + timeTonotify;

        DateFormat formatter = new SimpleDateFormat("d-M-yyyy hh:mm");
        try {
            Date date1 = formatter.parse(dateandtime);
            am.set(AlarmManager.RTC_WAKEUP, date1.getTime(), pendingIntent);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        finish();

    }

    public void displayAlert() {
        // AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        // Intent intent = new Intent(getApplicationContext(), AlarmBrodcast.class);
        // PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);
        //String dateandtime = date + " " + timeTonotify;
        //am.set(AlarmManager.RTC_WAKEUP, date1.getTime(), pendingIntent);
        String value = (tripNameEditText.getText().toString().trim());
        String Sdate = (dateText.getText().toString().trim());
        String Stime = (timeText.getText().toString().trim());
        String endpoint = (endLocationEditText.getText().toString().trim());

        AlertDialog.Builder builder = new AlertDialog.Builder(AddNewTripActivity.this);
        builder.setCancelable(false);
        builder.setTitle("Trip Remind");
        builder.setMessage(value);
        // add the buttons
        builder.setIcon(R.drawable.ic_baseline_calendar_today_24);
        builder.setPositiveButton("Start", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Uri gmmIntentUri = Uri.parse("geo:0,0?q="+endpoint);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });
        builder.setNeutralButton("Snooze", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                setAlarm(value, Sdate, Stime, endpoint);

            }
        });
        builder.setNegativeButton("Cancel", null);
        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}