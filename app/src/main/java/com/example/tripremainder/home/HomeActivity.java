package com.example.tripremainder.home;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Home");
        navigationView = findViewById(R.id.nested);
        navigationView.setNavigationItemSelectedListener(this);

        drawer = findViewById(R.id.drawer);
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(true);
        toggle.syncState();
        //loadFragment(new HomeFragment());
        // fragmentManager = getSupportFragmentManager();
       // fragmentTransaction = fragmentManager.beginTransaction();
        //fragmentTransaction.add(R.id.fragmentContainer, new HomeFragment());
        //fragmentTransaction.commit();// add the fragment
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

        }
        if (menuItem.getItemId() == R.id.history) {
           loadFragment(new HistoryFragment());
            getSupportActionBar().setTitle("Trips History");

        }
        if (menuItem.getItemId() == R.id.map) {
            getSupportActionBar().setTitle("Map");

        }
        if (menuItem.getItemId() == R.id.logout) {
            AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
            builder.setTitle("LogOut Alert");
            builder.setMessage("Are you sure to log out ??");
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

}