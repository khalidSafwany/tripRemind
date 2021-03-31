package com.example.tripremainder.Connectivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkRequest;

import java.io.IOException;

public class Connectivity {
    public static boolean checkConnection() throws IOException, InterruptedException {

        Process process = Runtime.getRuntime().exec("/system/bin/ping -c 1 8.8.8.8");
        if (process.waitFor() == 0) return true;
        else return false;

    }

}
