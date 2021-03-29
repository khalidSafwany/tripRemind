package com.example.tripremainder;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
    private static final int COLOR_BLACK_ARGB = 0xff000000;
    private static final int POLYLINE_STROKE_WIDTH_PX = 12;
    private static final int PATTERN_GAP_LENGTH_PX = 20;
    private static final PatternItem DOT = new Dot();
    private static final PatternItem GAP = new Gap(PATTERN_GAP_LENGTH_PX);
    private static final List<PatternItem> PATTERN_POLYLINE_DOTTED = Arrays.asList(GAP, DOT);
    NewTrip newTrip;
    private RoomDB database;

    private List<NewTrip> tripHistoryList;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = RoomDB.getInstance(getContext());
        tripHistoryList = new ArrayList<>();
        tripHistoryList = database.tripDaos().getHistoryTrips();
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
        location = new LatLng(31, 29);
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
                        .add(

                             // endPoint = (newTrip.getEndPoint();
                              //  new LatLng(newTrip.getStartPoint()., 143.321),
                                new LatLng(-34.747, 145.592),
                                new LatLng(-34.364, 147.891),
                                new LatLng(-33.501, 150.217),
                                new LatLng(-32.306, 149.248),
                                new LatLng(-32.491, 147.309)));
                polyline1.setTag("A");
                // [END maps_poly_activity_add_polyline_set_tag]
                // Style the polyline.
                //  stylePolyline(polyline1);

                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                        new LatLng(-23.684, 133.903),4
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
                        // Toast.makeText(this, "Route type " + polyline.getTag().toString(),Toast.LENGTH_SHORT).show();
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

        switch (type) {
            // If no type is given, allow the API to use the default.
            case "A":
                // Use a custom bitmap as the cap at the start of the line.
                polyline.setStartCap(
                        new CustomCap(
                                BitmapDescriptorFactory.fromResource(R.drawable.ic_history), 10));
                break;
        }

        polyline.setEndCap(new RoundCap());
        polyline.setWidth(POLYLINE_STROKE_WIDTH_PX);
        polyline.setColor(COLOR_BLACK_ARGB);
        polyline.setJointType(JointType.ROUND);
    }

    public LatLng getLocationFromAddress(Context context, String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }

            Address location = address.get(0);
            p1 = new LatLng(location.getLatitude(), location.getLongitude() );

        } catch (IOException ex) {

            ex.printStackTrace();
        }

        return p1;
    }

}
