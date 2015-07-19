package com.example.denis.alarmbutton;


import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.KeyEvent;

import com.example.denis.alarmbutton.DBclasses.DBHelper;
import com.example.denis.alarmbutton.DBclasses.EmailTable;
import com.example.denis.alarmbutton.DBclasses.NumberTable;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;

/**
 * AlarmButton created by Denis Pavlovsky on 29.04.15.
 */
public class AlarmButtonService extends Service
        implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        KeyEvent.Callback {

    private static final String TAG = AlarmButtonService.class.getSimpleName();

    private final static String SENT = "SMS_SENT";

    private Location location;
    private ArrayList<String> numbers;
    private ArrayList<String> emails;
    private String message;
    private GoogleApiClient mGoogleApiClient;

    @Override
    public IBinder onBind(Intent intent) {
        if (intent.getExtras().getBoolean("Alarm")) {
            alarm();
        }
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }


    private void alarm() {

        AlarmTask alarmTask = new AlarmTask();
        alarmTask.execute();

    }

    public ArrayList<String> getNumbers() {
        Log.w(TAG, "getNumbers Method Start ");
        ArrayList<String> numbersArray = new ArrayList<>();
        final Cursor cursor = DBHelper.getInstance().getDatabase()
                .query(NumberTable.TABLE_NAME, // table name
                        null, // columns
                        null, // selection
                        null, // selectionArgs
                        null, // groupBy
                        null, // having
                        null);// orderBy

        final int textIndex = cursor.getColumnIndex(NumberTable.COLUMN_NUMBER);

        while (cursor.moveToNext()) {
            final String value = cursor.getString(textIndex);

            numbersArray.add(value);
        }
        cursor.close();

        Log.w(TAG, "getNumbers Method END ");
        return numbersArray;
    }


    public ArrayList<String> getMails() {

        Log.w(TAG, "getMails Method Start ");

        ArrayList<String> mailsArray = new ArrayList<>();
        final Cursor cursor = DBHelper.getInstance().getDatabase()
                .query(EmailTable.TABLE_NAME, // table name
                        null, // columns
                        null, // selection
                        null, // selectionArgs
                        null, // groupBy
                        null, // having
                        null);// orderBy

        final int textIndex = cursor.getColumnIndex(EmailTable.COLUMN_TEXT);

        while (cursor.moveToNext()) {
            final String value = cursor.getString(textIndex);

            mailsArray.add(value);
        }
        cursor.close();

        Log.w(TAG, "getMails Method End ");
        return mailsArray;
    }


    private void sendSmsWithLocation() {
        Log.w(TAG, "sendSmsWithLocation Method Start ");
        String phoneNumber;
        for (int i = 0; i < numbers.size(); i++) {
            phoneNumber = numbers.get(i);

            PendingIntent pi = PendingIntent.getActivity(this, 0,
                    new Intent(this, MainActivity.class), 0);
            SmsManager sms = SmsManager.getDefault();
            sms.sendTextMessage(phoneNumber, null, message, pi, null);

            registerReceiver(new BroadcastReceiver() {
                @Override
                public void onReceive(Context arg0, Intent arg1) {
                    switch (getResultCode()) {
                        case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                            sendSmsWithLocation();
                            break;
                        case SmsManager.RESULT_ERROR_NO_SERVICE:
                            sendSmsWithLocation();
                            break;
                        case SmsManager.RESULT_ERROR_NULL_PDU:
                            sendSmsWithLocation();
                            break;
                        case SmsManager.RESULT_ERROR_RADIO_OFF:
                            sendSmsWithLocation();
                            break;
                    }
                }
            }, new IntentFilter(SENT));
        }
        Log.w(TAG, "sendSmsWithLocation Method END ");
    }

    private void sendEmailWithLocation() {

        Log.w(TAG, "sendEmailWithLocation Method Start ");

        SharedPreferences sharedPref = getApplicationContext().
                getSharedPreferences(App.PREFERENCES_NAME, Context.MODE_PRIVATE);

        try {
            MailSender sender = new MailSender(getApplicationContext(), sharedPref.getString(App.USER_EMAIL, App.DEFOULT_MAIL),
                    sharedPref.getString(App.USER_PASS, App.DEFOULT_PASS));
            for (int i = 0; i < emails.size(); i++) {
                sender.sendMail(App.MAIL_SUBJECT + sharedPref.getString(App.USER_NAME, "<<Name>>"),
                        message,
                        sharedPref.getString(App.USER_EMAIL, App.DEFOULT_MAIL),
                        emails.get(i));
            }
        } catch (Exception e) {
            Log.e("SendMail", e.getMessage(), e);
        }

        Log.w(TAG, "sendEmailWithLocation Method END ");
    }

    public void getLocation() {

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }


    /**
     * Location CallBacks START
     */

    @Override
    public void onConnected(Bundle bundle) {
        location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }

    /**
     * Location CallBacks END
     */

    @Override
    public void onDestroy() {
        location = null;
        numbers = null;
        emails = null;
        message = null;

        super.onDestroy();
    }

    /**
     * volume key tracking START
     */


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_UP:
                event.startTracking();
                return false;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                event.startTracking();
                return false;
        }
        return false;
    }

    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_UP:
                Log.w(TAG, "KEYCODE_VOLUME_UP ");

                alarm();
                return true;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                Log.w(TAG, "KEYCODE_VOLUME_UP ");

                alarm();
                return true;
        }
        return false;
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return false;
    }

    @Override
    public boolean onKeyMultiple(int keyCode, int count, KeyEvent event) {
        return false;
    }

    /**
     * volume key tracking END
     */

    class AlarmTask extends AsyncTask <String, String, String>{

            @Override
            protected String doInBackground(String... params) {

                Log.w(TAG, "AlarmTask Method STARTS ");
                numbers = getNumbers();

                emails = getMails();

                getLocation();

                if (location != null) {
                    Log.w(TAG, "location != null");
                    message = getString(R.string.SaveMe) + String.valueOf(location.getLatitude()) + getString(R.string.Langtitude)
                            + String.valueOf(location.getLongitude()) + getString(R.string.Logntitude);
                } else {
                    message = "Save me please! ";
                }


                if (!numbers.isEmpty()) {
                    sendSmsWithLocation();
                }
                if (!emails.isEmpty()) {
                    sendEmailWithLocation();
                }

                Log.w(TAG, "AlarmTask Method ENDS ");

                return null;
            }
        }

}
