package com.example.android.busbookings.Utils;

import android.app.Application;

import com.androidnetworking.AndroidNetworking;
import com.facebook.FacebookSdk;
import com.razorpay.Checkout;

import io.realm.Realm;

public class BusBookings extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        AndroidNetworking.initialize(this);
        Checkout.preload(this);
    }
}
