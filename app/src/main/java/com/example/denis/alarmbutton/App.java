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
    public static final String DEFAULT_MAIL = "savemebutton@gmail.com";
    public static final String DEFOULT_PASS = "sdjkfneufhwu";
    public static final String SMTP_NAME = "SMTP_NAME";


    public static boolean USE_DEFAULT_MAIL = true;


    /**
     * Boolean variable which indicates if alarm state is ON or OFF.
     * Depend on this state AlarmButtonService will start or not.
     */
    public static boolean ALARM = false;
    /**
     * Request code for the Alarm Pending Intent
     */
    public static final int ALARM_REQUEST_CODE = 1111;

    /**
     * Interval of alarm Service launch in minutes.
     */
    public static int ALARM_INTERVAL = 5;

    /**
     * Boolean variable which indicates if Alarm Service will be launch repeatedly.
     */
    public static boolean IS_ALARM_REPEATING = true;

    @Override
    public void onCreate() {
        super.onCreate();

        DBHelper.init(this);

        Intent intent = new Intent(this, AlarmButtonService.class);
        startService(intent);
    }

}
