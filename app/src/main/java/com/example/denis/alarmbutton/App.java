package com.example.denis.alarmbutton;

import android.app.Application;
import android.content.Intent;

import com.example.denis.alarmbutton.DBclasses.DBHelper;

/**
 * AlarmButton created by Denis Pavlovsky on 07.05.15.
 */
public class App extends Application {

    public final static String PREFERENCES_NAME = "SHP";
    public final static String MAIL_SUBJECT = "HELP! Save me! I am in danger - ";
    public final static String USER_NAME = "NAME";
    public static final String USER_EMAIL = "USER_EMAIL";
    public static final String USER_PASS = "USER_PASS";
    public static final String DEFOULT_MAIL = "savemebutton@gmail.com";
    public static final String DEFOULT_PASS = "sdjkfneufhwu";
    public static final String SMTP_NAME = "SMTP_NAME";

    public static boolean ALARM = false;
    public static final int ALARM_REQUEST_CODE = 1111;
    public static long ALARM_INTERVAL = 900000;
    public static boolean IS_ALARM_REPEATING = true;

    @Override
    public void onCreate() {
        super.onCreate();

        DBHelper.init(this);

        Intent intent = new Intent(this, AlarmButtonService.class);
        startService(intent);
    }

}
