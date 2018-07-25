package com.example.android.busbookings.Activitys;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.android.busbookings.Adapters.MyAdapter;
import com.example.android.busbookings.R;

public class MainActivity extends AppCompatActivity{

    public static String emailID = "";

    TabLayout tabLayout;
    ViewPager viewPager;

    TextView Name,email;
    ImageView imgProfile;

    DrawerLayout drawerLayout;
    ActionBar actionBar;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabLayout = findViewById(R.id.TabLayout);
        viewPager = findViewById(R.id.viewpager);

        emailID = getIntent().getStringExtra("Email");


        tabLayout.addTab(tabLayout.newTab().setText("Search Buses"));
        tabLayout.addTab(tabLayout.newTab().setText("My Bookings"));
        tabLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));

        tabLayout.setTabTextColors(Color.parseColor("#ffffff"), Color.parseColor("#ffffff"));

        MyAdapter fragAdapter = new MyAdapter(getSupportFragmentManager(), this, tabLayout.getTabCount());
        viewPager.setAdapter(fragAdapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }


    public void onBackPressed() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(R.string.app_name);
        builder.setMessage("Do you want to close?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finishAffinity();
                    }
                })
                .setNegativeButton("No", null);
        AlertDialog alert = builder.create();
        alert.show();

    }

}
