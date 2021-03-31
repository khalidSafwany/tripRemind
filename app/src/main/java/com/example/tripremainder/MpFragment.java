package com.example.tripremainder;

import android.content.Context;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.tripremainder.DataBase.Model.NewTrip;
import com.example.tripremainder.DataBase.RoomDB;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CustomCap;
import com.google.android.gms.maps.model.Dot;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.RoundCap;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MpFragment extends Fragment {

    SupportMapFragment mapFragment;
    Marker markerPerth;
    private LatLng location;
    private static final int COLOR_BLACK_ARGB = Color.RED;
    private static final int POLYLINE_STROKE_WIDTH_PX = 12;
    private static final int PATTERN_GAP_LENGTH_PX = 20;
    private static final PatternItem DOT = new Dot();
    private static final PatternItem GAP = new Gap(PATTERN_GAP_LENGTH_PX);
    private static final List<PatternItem> PATTERN_POLYLINE_DOTTED = Arrays.asList(GAP, DOT);
    NewTrip newTrip;
    private RoomDB database;
    private LatLng[] latLngs;
    private LatLng[] latLngp;

    private List<NewTrip> tripHistoryList;
    PolylineOptions polylineOptions ;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = RoomDB.getInstance(getContext());
        polylineOptions =new PolylineOptions();
        tripHistoryList = new ArrayList<>();
        tripHistoryList = database.tripDaos().getHistoryTrips();
        latLngs = new LatLng[tripHistoryList.size()];
        latLngp = new LatLng[tripHistoryList.size()];
        if(!tripHistoryList.isEmpty()){
        for(int i=0;i<tripHistoryList.size();i++){
            LatLng latLng =new LatLng(tripHistoryList.get(i).getStartPointlat(),tripHistoryList.get(i).getStartPointLong());
            LatLng latLng1 =new LatLng(tripHistoryList.get(i).getEndPointlat(),tripHistoryList.get(i).getEndPointLong());

            latLngs[i] = latLng;
            latLngp[i] = latLng1;
           // polylineOptions.add(new LatLng(tripHistoryList.get(i).getStartPointlat(),tripHistoryList.get(i).getStartPointLong()));
        }}
        else{
            Toast.makeText(getContext(),"Missing Trip Location",Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mp, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
       // location = new LatLng(31, 29);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                //  googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                //    @Override
                //   public void onMapClick(LatLng latLng) {
                // MarkerOptions markerOptions = new MarkerOptions();
                //  markerOptions.position(latLng);
                //   markerOptions.title(latLng.latitude +" : "+latLng.longitude);
                //  googleMap.clear();
                Polyline polyline1 = googleMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(latLngs));
              //  Polyline polyline2 = googleMap.addPolyline(polylineOptions);
                polyline1.setTag("A");
                // [END maps_poly_activity_add_polyline_set_tag]
                // Style the polyline.
                  stylePolyline(polyline1);

                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                       new LatLng(30.033333,31.233334),5
                ));
                googleMap.setOnPolylineClickListener(new GoogleMap.OnPolylineClickListener() {
                    @Override
                    public void onPolylineClick(Polyline polyline) {
                        if ((polyline.getPattern() == null) || (!polyline.getPattern().contains(DOT))) {
                            polyline.setPattern(PATTERN_POLYLINE_DOTTED);
                        } else {
                            // The default pattern is a solid stroke.
                            polyline.setPattern(null);
                        }
                    }
                });

                //  googleMap.addMarker(markerOptions);
                // }
                //   });
            }
        });

    }

    private void stylePolyline(Polyline polyline) {
        String type = "";
        // Get the data object stored with the polyline.
        if (polyline.getTag() != null) {
            type = polyline.getTag().toString();
        }


        polyline.setEndCap(new RoundCap());
        polyline.setWidth(POLYLINE_STROKE_WIDTH_PX);
        polyline.setColor(COLOR_BLACK_ARGB);
        polyline.setJointType(JointType.ROUND);
    }



}
