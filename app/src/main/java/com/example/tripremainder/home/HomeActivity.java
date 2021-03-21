package com.example.tripremainder.home;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.tripremainder.R;
import com.example.tripremainder.history.HistoryFragment;
import com.google.android.material.navigation.NavigationView;

public class HomeActivity extends AppCompatActivity implements HomeFragment.OnFragmentItemSelectedListener,
        NavigationView.OnNavigationItemSelectedListener {
    HomeFragment homeFragment;
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

        homeFragment = new HomeFragment();
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragmentContainer, homeFragment);
        fragmentTransaction.commit();// add the fragment
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
        return true;
    }

    private void loadFragment(Fragment secondFragment) {
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainer, secondFragment);
        fragmentTransaction.addToBackStack(null);
        getSupportActionBar().setTitle("Home");
        fragmentTransaction.commit();
    }

    private void closeDrawer() {
        drawer.closeDrawer(GravityCompat.START);
    }
}