package com.example.tripremainder;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bubbles.src.main.java.com.siddharthks.bubbles.DataClass;
import com.bubbles.src.main.java.com.siddharthks.bubbles.FloatingBubblePermissions;
import com.example.tripremainder.Connectivity.Connectivity;

import com.bubbles.*;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.ContactsContract;
import android.speech.RecognizerIntent;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.tripremainder.DataBase.Model.NewTrip;
import com.example.tripremainder.DataBase.RoomDB;
import com.example.tripremainder.home.HomeAdapter;
import com.example.tripremainder.notification.AlarmBrodcast;
import com.example.tripremainder.notification.DialogCast;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.*;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
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
    ImageView calenderButton;
    ImageView timeButton;
    ImageView addNoteBtn;
    TextView dateText;
    TextView timeText;
    Spinner tripTypeSpinner;
    Button addTripBtn;
    Button saveTripBtn;
    NewTrip tempNewTrip;
    NewTrip tripToBeUpdated;
    Toolbar toolbarTop ;
    TextView mTitle ;

    private static String isSyncNeededString = "sync";
    private static boolean isSyncNeeded = false;



    ArrayList<String>notes;
    PlacesClient placesClient;
    FIreBaseConnection connection;


    ImageView calenderButton1;
    ImageView timeButton1;
    TextView dateText1;
    TextView timeText1;

    RoomDB database;
    List<com.example.tripremainder.DataBase.Model.NewTrip> dataList = new ArrayList<>();
    private HomeAdapter adapter;
    public static final int Notification_id = 1;
    String timeTonotify;
    String timeToNotifyRound;

    // The Entry point of the database
    private FirebaseDatabase mFirebaseDatabase;
    // The Database Reference
    private DatabaseReference mDatabaseReference;

    String email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_trip);
        //setTitle("New Trip");
        notes = new ArrayList<>();
        setView();
        toolbarTop = findViewById(R.id.toolbar);
        mTitle = toolbarTop.findViewById(R.id.toolbar_title);
        mTitle.setText("New Trip");
        Places.initialize(AddNewTripActivity.this,"AIzaSyDNuanqZTnydcYiOF0PjV1MR_f8t_vGv1Q");
        placesClient = Places.createClient(this);


        //Database Authantication

        email = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        if(mFirebaseDatabase == null){
            mFirebaseDatabase = FirebaseDatabase.getInstance( );
        }

        //Hager code
        database = RoomDB.getInstance(this);
        dataList = database.tripDaos().getUpcomingTrips(email);


        Intent intent = getIntent();
        tripNameEditText.setText(intent.getStringExtra("trip_name"));
        startLocationEditText.setText(intent.getStringExtra("trip_start_point"));
        endLocationEditText.setText(intent.getStringExtra("trip_end_point"));
        dateText.setText(intent.getStringExtra("trip_date"));
        timeText.setText(intent.getStringExtra("trip_time"));
        ////////////////
        tempNewTrip = new NewTrip();



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
        saveTripBtn = findViewById(R.id.saveBtn);



        calenderButton1 = findViewById(R.id.calenderButton1);
        calenderButton1.setOnClickListener(v-> showDatePicker1());
        dateText1 = findViewById(R.id.DateTextView1);
        timeButton1 = findViewById(R.id.timeButton1);
        timeButton1.setOnClickListener(v-> showTimePicker1());
        timeText1 = findViewById(R.id.timeText1);



        tripTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 1){
                    calenderButton1.setVisibility(View.VISIBLE);
                    dateText1.setVisibility(View.VISIBLE);
                    timeButton1.setVisibility(View.VISIBLE);
                    timeText1.setVisibility(View.VISIBLE);
                }else {
                    calenderButton1.setVisibility(View.GONE);
                    dateText1.setVisibility(View.GONE);
                    timeButton1.setVisibility(View.GONE);
                    timeText1.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



//check if view is for update or for new trip
        checkForUpdateOrNewTrip();



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
            showPlacesAssistant(120);
        });
        endLocationEditText.setOnClickListener(v->{
            showPlacesAssistant(121);
        });
    }


    private void checkForUpdateOrNewTrip(){
        tripToBeUpdated = (NewTrip) getIntent().getSerializableExtra("UpdatedTrip");
        if(tripToBeUpdated == null) {
            saveTripBtn.setVisibility(View.INVISIBLE);
            saveTripBtn.setEnabled(false);
            addTripBtn.setOnClickListener(v->submit());
        }
        else{
            setUpdateView();
            saveTripBtn.setOnClickListener(v->submitAndUpdate());
        }
    }



    private void submitAndUpdate(){
        if(validate()){
            Toast.makeText(AddNewTripActivity.this," Input Validated",Toast.LENGTH_SHORT).show();
            tripToBeUpdated.setTripName(tripNameEditText.getText().toString());
            tripToBeUpdated.setStartPoint(startLocationEditText.getText().toString());
            tripToBeUpdated.setEndPoint(endLocationEditText.getText().toString());
            tripToBeUpdated.setTripDate(dateText.getText().toString());
            tripToBeUpdated.setTripTime(timeText.getText().toString());
            //tempNewTrip.setNotes(notes);

            database.tripDaos().updateTripName(tripToBeUpdated.getId() , tripToBeUpdated.getTripName());
            database.tripDaos().updateTripStartPoint(tripToBeUpdated.getId() , tripToBeUpdated.getStartPoint());
            database.tripDaos().updateTripEndPoint(tripToBeUpdated.getId() , tripToBeUpdated.getEndPoint());
            database.tripDaos().updateTripDate(tripToBeUpdated.getId() , tripToBeUpdated.getTripDate());
            boolean isRound = false;
            if(tripToBeUpdated.getTripBackDate()!=null) {
                isRound = true;
            }
            setAlarm(tripToBeUpdated.getTripName(), tripToBeUpdated.getTripDate(), tripToBeUpdated.getEndPoint(), tripToBeUpdated.getId(), isRound);

            try {
               isSyncNeeded = !Connectivity.checkConnection();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
            if(!isSyncNeeded) {
                NewTrip tempTrip = database.tripDaos().getTripById((tripToBeUpdated.getId()));
                connection = new FIreBaseConnection();
                connection.updateTrip(tempTrip);
            }
            Intent returnIntent = new Intent();
            returnIntent.putExtra(isSyncNeededString,isSyncNeeded);
            returnIntent.putExtra("result", tripToBeUpdated);
            setResult(200,returnIntent);
            finish();
        }

    }


    @Override
    protected void onStart() {
        super.onStart();
        if(tripToBeUpdated!=null) {
            setUpdateView();
        }

    }

    private  void setUpdateView(){
        addTripBtn.setVisibility(View.INVISIBLE);
        addTripBtn.setEnabled(false);
        tripNameEditText.setText(tripToBeUpdated.getTripName());
        startLocationEditText.setText(tripToBeUpdated.getStartPoint());
        endLocationEditText.setText(tripToBeUpdated.getEndPoint());
        timeText.setText(tripToBeUpdated.getTripTime());
        dateText.setText(tripToBeUpdated.getTripDate());
        //notes = tripToBeUpdated.getNotes();
    }



    private void showPlacesAssistant(int code){
        List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS, Place.Field.LAT_LNG, Place.Field.NAME);
        Intent searchIntent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY,fieldList).build(AddNewTripActivity.this);
        startActivityForResult(searchIntent,code);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 120 && resultCode == RESULT_OK) {

            Place place = Autocomplete.getPlaceFromIntent(data);
            startLocationEditText.setText(place.getAddress());
            tempNewTrip.setStartPointlat(place.getLatLng().latitude);
            tempNewTrip.setStartPointLong(place.getLatLng().longitude);
            Log.i("TAG", "onActivityResult: "+ place.getLatLng().latitude);
            Log.i("TAG", "onActivityResult: "+ place.getLatLng().longitude);

        }
        else if (requestCode == 121 && resultCode == RESULT_OK) {

            Place place = Autocomplete.getPlaceFromIntent(data);
            endLocationEditText.setText(place.getAddress());
            tempNewTrip.setEndPointlat(place.getLatLng().latitude);
            tempNewTrip.setEndPointLong(place.getLatLng().longitude);
            Log.i("TAG", "onActivityResult: "+ place.getLatLng().latitude);
            Log.i("TAG", "onActivityResult: "+ place.getLatLng().longitude);

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

            try {
                isSyncNeeded = !Connectivity.checkConnection();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }



            Intent intent = getIntent();
            tempNewTrip.setId(intent.getIntExtra("id" , 0));
            tempNewTrip.setEmail(email);
            tempNewTrip.setState(0);
            tempNewTrip.setDirection(tripTypeSpinner.getSelectedItem().toString());
            tempNewTrip.setStateType("UpComing");
            tempNewTrip.setTripName(tripNameEditText.getText().toString().trim());
            tempNewTrip.setStartPoint(startLocationEditText.getText().toString().trim());
            tempNewTrip.setEndPoint(endLocationEditText.getText().toString().trim());
            tempNewTrip.setTripDate(dateText.getText().toString().trim());
            tempNewTrip.setTripTime(timeText.getText().toString().trim());
            tempNewTrip.setTripBackDate(dateText1.getText().toString().trim());
            tempNewTrip.setTripBackTime(timeText1.getText().toString().trim());

            long id = database.tripDaos().insertTrip(tempNewTrip);
            tempNewTrip.setId((int)id);
            boolean isRound = false;
            if(tempNewTrip.getTripBackDate()!=null) {
                   isRound = true;

            }
            setAlarm(tempNewTrip.getTripName(), tempNewTrip.getTripDate(), tempNewTrip.getEndPoint(), (int) id, isRound);

            if(!isSyncNeeded) {
                NewTrip tempTrip = database.tripDaos().getTripById((int)id);
                connection = new FIreBaseConnection();
                connection.addNewTrip(tempTrip);
            }
         //   connection.addNewTrip(tempNewTrip);
            Intent returnIntent = new Intent();
            returnIntent.putExtra("result", (Serializable) tempNewTrip);
            returnIntent.putExtra(isSyncNeededString,isSyncNeeded);

            setResult(200,returnIntent);
            finish();
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

    private boolean validate() {
        if (!tripNameEditText.getText().toString().matches("")) {
            if (!startLocationEditText.getText().toString().matches("")) {
                if (!endLocationEditText.getText().toString().matches("")) {
                    if (!timeText.getText().toString().matches("")) {
                        if (!dateText.getText().toString().matches("")) {
                            System.out.println(tripTypeSpinner.getSelectedItem().toString());
                            if (tripTypeSpinner.getSelectedItem().toString().matches("Round trip")) {
                                if (!timeText1.getText().toString().matches("")) {
                                    if (dateText1.getText().toString().matches("")) {

                                        Toast.makeText(AddNewTripActivity.this, "Missing Return Trip Date", Toast.LENGTH_SHORT).show();
                                        return false;
                                    }
                                } else {
                                    Toast.makeText(AddNewTripActivity.this, "Missing Return Trip TIME", Toast.LENGTH_SHORT).show();
                                    return false;
                                }
                            }
                            } else {
                                Toast.makeText(AddNewTripActivity.this, "Missing Trip Date", Toast.LENGTH_SHORT).show();
                                return false;
                            }
                        } else {
                            Toast.makeText(AddNewTripActivity.this, "Missing Trip Time", Toast.LENGTH_SHORT).show();
                            return false;
                        }

                    } else {
                    tripNameEditText.setError("Enter End Location");
                        Toast.makeText(AddNewTripActivity.this, "Missing Trip End Location", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                } else {
                startLocationEditText.setError("Enter Start Location");
                    Toast.makeText(AddNewTripActivity.this, "Missing Trip Start Location", Toast.LENGTH_SHORT).show();
                    return false;
                }
            } else {
            tripNameEditText.setError("Enter Trip Name");
                Toast.makeText(AddNewTripActivity.this, "Missing Trip Name", Toast.LENGTH_SHORT).show();
                return false;
            }

        return true;
    }

    private void showTimePicker() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,R.style.MyDialogTheme, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                timeTonotify = i + ":" + i1;
                timeText.setText(timeTonotify);

            }
        }, hour, minute, false);
        timePickerDialog.setTitle("Set your trip start time");
        timePickerDialog.setCancelable(false);
        timePickerDialog.show();

    }

    private void showTimePicker1() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, R.style.MyDialogTheme,new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                timeToNotifyRound = i + ":" + i1;
                timeText1.setText(timeToNotifyRound);

            }
        }, hour, minute, false);
        timePickerDialog.setTitle("Set your trip start time");
        timePickerDialog.setCancelable(false);
        timePickerDialog.show();

    }


            private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,R.style.MyDialogTheme, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                dateText.setText(day + "-" + (month + 1) + "-" + year);
            }
        }, year, month, day);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        datePickerDialog.setTitle("Set your trip date");
        datePickerDialog.setCancelable(false);
        datePickerDialog.show();
    }


    private void showDatePicker1(){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,R.style.MyDialogTheme, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                dateText1.setText(day + "-" + (month + 1) + "-" + year);
            }
        }, year, month, day);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        datePickerDialog.setTitle("Set your trip date");
        datePickerDialog.setCancelable(false);
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

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void setAlarm(String tripName, String date, String endLocation,int tripId, boolean isRound) {

        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getApplicationContext(), DialogCast.class);
        intent.putExtra("event", tripName);
        intent.putExtra("end", endLocation);
        intent.putExtra("id",tripId);
        intent.putExtra("isRound", false);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);
        String dateandtime = date + " " + timeTonotify;
        DateFormat formatter = new SimpleDateFormat("d-M-yyyy hh:mm");
        try {
            Date date1 = formatter.parse(dateandtime);
            am.setExact(AlarmManager.RTC_WAKEUP, date1.getTime(), pendingIntent);

        } catch (ParseException e) {
            e.printStackTrace();
        }
//        if(isRound){
//            AlarmManager am2 = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//            Intent intent2 = new Intent(getApplicationContext(), DialogCast.class);
//            intent2.putExtra("event", tripName);
//            intent2.putExtra("end", endLocation);
//            intent2.putExtra("id",tripId);
//            intent2.putExtra("isRound", true);
//            PendingIntent pendingIntent2 = PendingIntent.getBroadcast(getApplicationContext(), 0, intent2, PendingIntent.FLAG_ONE_SHOT);
//            String dateandtime2 = date + " " + timeToNotifyRound;
//            try {
//                Date date2 = formatter.parse(dateandtime2);
//                am2.setExact(AlarmManager.RTC_WAKEUP, date2.getTime(), pendingIntent2);
//
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//        }



    }


}