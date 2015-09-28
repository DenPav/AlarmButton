package com.example.denis.alarmbutton;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;

import com.example.denis.alarmbutton.Fragments.MainFragment;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();

        MainFragment mainFragment = new MainFragment();
        FragmentTransaction fragmentManager = getFragmentManager().beginTransaction();
        fragmentManager.add(R.id.frameLayout, mainFragment);
        fragmentManager.commit();
    }
}
