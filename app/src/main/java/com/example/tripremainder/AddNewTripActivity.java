package com.example.tripremainder;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.*;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class AddNewTripActivity extends AppCompatActivity{

    EditText tripNameEditText;
    EditText startLocationEditText;
    EditText endLocationEditText;
    ImageButton calenderButton;
    ImageButton timeButton;
    ImageButton addNoteBtn;
    TextView dateText;
    TextView timeText;
    Spinner tripTypeSpinner;
    Button addTripBtn;



    String tripName;
    String startLocation;
    String endLocation;
    String date;
    String time;
    String tripType;
    ArrayList<String>notes;
    PlacesClient placesClient;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_trip);
        setTitle("New Trip");
        notes = new ArrayList<>();
        setView();

        Places.initialize(AddNewTripActivity.this,"AIzaSyDNuanqZTnydcYiOF0PjV1MR_f8t_vGv1Q");
        placesClient = Places.createClient(this);


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

        addNoteBtn = findViewById(R.id.addNotesButton);
        addNoteBtn.setOnClickListener(v->{
            showAddNoteDialogue();
        });

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
    private void submit(){
        if(validate()){
            Toast.makeText(AddNewTripActivity.this," Input Validated",Toast.LENGTH_SHORT).show();
        }

    }

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

}