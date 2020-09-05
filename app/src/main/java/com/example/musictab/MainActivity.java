package com.example.musictab;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager.widget.ViewPager.OnAdapterChangeListener;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.mahfa.dnswitch.DayNightSwitch;
import com.mahfa.dnswitch.DayNightSwitchListener;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    Toolbar mtoolbar;
    TabLayout mtablayout;
    TabItem curmusic;
    TabItem allmusic;
    TabItem playlist;
    ViewPager mpager;
    PagerController mpagercontroller;
    DayNightSwitch dayNightSwitch;
    View back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mtablayout = findViewById(R.id.tabLayout);
        curmusic = findViewById(R.id.currentmusic);
        allmusic = findViewById(R.id.allmusic);
        playlist = findViewById(R.id.playlist);
        mpager = findViewById(R.id.viewpager);
        mtoolbar = findViewById(R.id.toolbar);
        //back = findViewById(R.id.view2);
        dayNightSwitch = findViewById(R.id.day_night_switch);
        setSupportActionBar(mtoolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Music Dash");

        mpagercontroller = new PagerController(getSupportFragmentManager(),mtablayout.getTabCount());
        mpager.setAdapter(mpagercontroller);

        dayNightSwitch.setDuration(450);
        dayNightSwitch.setListener(new DayNightSwitchListener() {
            @Override
            public void onSwitch(boolean isNight) {
                if (isNight){
                    Toast.makeText(MainActivity.this,"Dark Mode Selected",Toast.LENGTH_SHORT).show();
                     setTheme(R.style.darkTheme);
                }
                else{
                    Toast.makeText(MainActivity.this,"Light Mode Selected",Toast.LENGTH_SHORT).show();
                    setTheme(R.style.AppTheme);
                }
            }
        });

        //mtablayout.setupWithViewPager(mpager);
        mtablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mpager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        mpager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mtablayout));

    }
}
