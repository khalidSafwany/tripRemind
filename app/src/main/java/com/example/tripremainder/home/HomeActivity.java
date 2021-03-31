package com.example.tripremainder.home;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.tripremainder.MpFragment;
import com.example.tripremainder.R;
import com.example.tripremainder.auth.Sign_inActivity;
import com.example.tripremainder.history.HistoryFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity implements HomeFragment.OnFragmentItemSelectedListener,
        NavigationView.OnNavigationItemSelectedListener {
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    DrawerLayout drawer;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;
    Toolbar toolbar;
    boolean isSecondryFragmentsActive;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Home");
        navigationView = findViewById(R.id.nested);
        navigationView.setNavigationItemSelectedListener(this);
        isSecondryFragmentsActive = false;

        drawer = findViewById(R.id.drawer);
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(true);
        toggle.syncState();
        fragmentManager = getSupportFragmentManager();
        Fragment fragment;
        fragment = fragmentManager.findFragmentByTag("myFragmentTag");
        if (fragment == null) {
            fragmentTransaction = fragmentManager.beginTransaction();
            fragment =new HomeFragment();
            fragmentTransaction.add(R.id.fragmentContainer,fragment,"myFragmentTag");
            fragmentTransaction.commit();
        }

    }





    @Override
    public void onButtonSelected() {
        Toast.makeText(this, "Start New Activity. (Static Fragment are used)", Toast.LENGTH_SHORT).show();
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainer, new HistoryFragment());
        fragmentTransaction.commit();

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        closeDrawer();

        if (menuItem.getItemId() == R.id.home) {
            loadFragment(new HomeFragment());

            getSupportActionBar().setTitle("UpComingTrips");
            isSecondryFragmentsActive = false;

        }
        if (menuItem.getItemId() == R.id.history) {
           loadFragment(new HistoryFragment());
            getSupportActionBar().setTitle("Trips History");
            isSecondryFragmentsActive = true;

        }
        if (menuItem.getItemId() == R.id.map) {
            loadFragment(new MpFragment());
            getSupportActionBar().setTitle("Map");
            isSecondryFragmentsActive = true;

        }
        if (menuItem.getItemId() == R.id.logout) {
            AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
            builder.setTitle("LogOut Alert");
            builder.setMessage("Are you sure to log out ??");
            builder.setCancelable(false);
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(HomeActivity.this, Sign_inActivity.class);
                    startActivity(intent);
                }
            });
            builder.setNegativeButton("Cancel" , null);
            builder.create();
            builder.show();

        }
        return true;
    }

    private void loadFragment(Fragment secondFragment) {
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainer, secondFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void closeDrawer() {
        drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout layout = (DrawerLayout)findViewById(R.id.drawer);
        if (layout.isDrawerOpen(GravityCompat.START)) {
            layout.closeDrawer(GravityCompat.START);
        } else {
            if(!isSecondryFragmentsActive){
                finishAffinity();
            }
            else{
                isSecondryFragmentsActive = false;
            }
           // super.onBackPressed();
            loadFragment(new HomeFragment());
            getSupportActionBar().setTitle("Home");
        }

    }
    BroadcastReceiver bgshowBroacast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String extra = intent.getStringExtra("BROADCAST");
            if (extra != null) {
                if (extra.equalsIgnoreCase("finishBgShowActivity")) {

                    finish();
                    Log.i("TAG", "onReceive: Bg_show_BroadCast receive from bg_send class ");
                }
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(HomeActivity.this).registerReceiver(bgshowBroacast, new IntentFilter("BG_SHOW_BROADCAST"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(HomeActivity.this).unregisterReceiver(bgshowBroacast);
    }
}