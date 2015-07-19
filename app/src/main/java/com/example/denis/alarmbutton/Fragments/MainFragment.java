package com.example.denis.alarmbutton.Fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.denis.alarmbutton.AlarmButtonService;
import com.example.denis.alarmbutton.R;

/**
 * AlarmButton created by Denis Pavlovsky on 07.05.15.
 */
public class MainFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = MainFragment.class.getSimpleName();

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };
    private Button button;
    private Button settingsButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment, container, false);

        button = (Button) view.findViewById(R.id.alarm);
        settingsButton = (Button) view.findViewById(R.id.settings);

        return view;

    }

    @Override
    public void onResume() {
        super.onResume();

        button.setOnClickListener(this);
        settingsButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.alarm:
                Log.w(TAG, "MainFragment alarm click");

                Intent intent = new Intent(getActivity(), AlarmButtonService.class);
                intent.putExtra("Alarm", true);
                getActivity().bindService(intent, serviceConnection, Context.BIND_ABOVE_CLIENT);
                Toast.makeText(getActivity(), R.string.MessageSentToast, Toast.LENGTH_LONG).show();
                break;
            case R.id.settings:
                Log.w(TAG, "MainFragment settings click");

                SettingsFragment settingsFragment = new SettingsFragment();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frameLayout, settingsFragment);
                fragmentTransaction.commit();
                break;
        }
    }
}
