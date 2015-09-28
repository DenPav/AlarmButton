package com.example.denis.alarmbutton.Fragments;

import android.app.Fragment;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.denis.alarmbutton.App;
import com.example.denis.alarmbutton.DBclasses.DBHelper;
import com.example.denis.alarmbutton.DBclasses.EmailTable;
import com.example.denis.alarmbutton.DBclasses.NumberTable;
import com.example.denis.alarmbutton.R;

import java.util.ArrayList;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;


/**
 * AlarmButton created by Denis Pavlovsky on 07.05.15.
 */
public class SettingsFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private static final String TAG = SettingsFragment.class.getSimpleName();

    private static String [] mails = {"Choose mail..", "gmail.com", "yandex.ru", "yahoo.com", "mail.com", "mail.ru", "ukr.net"};
    private SharedPreferences.Editor editor;
    private EditText nameText;
    private EditText mailText;
    private EditText pass;

    private TextView mailsArray;
    private TextView numberArray;

    private String smtpName;
    public static boolean isOwnMailChecked = false;
    private boolean spinnerMailChosen = false;

    private View v;
    private Spinner spinner;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        editor = sharedPref.edit();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.settings_fragment, container, false);
        v = view;
        Log.w(TAG, "onCreateView Start ");

        nameText = (EditText) view.findViewById(R.id.name);
        mailText = (EditText) view.findViewById(R.id.mail);
        pass = (EditText) view.findViewById(R.id.pass);
        mailText.setEnabled(false);
        pass.setEnabled(false);

        Button button = (Button) view.findViewById(R.id.SaveButton);
        button.setOnClickListener(this);

        spinner = (Spinner) view.findViewById(R.id.choose_mail);
        spinner.setSelection(0);

        ArrayAdapter<String> adapter = new
                ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, mails);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        spinner.setEnabled(false);

        CheckBox ownMailCheckBox = (CheckBox) view.findViewById(R.id.OwnMailCheck);
        ownMailCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub
                if (buttonView.isChecked()) {
                    mailText.setEnabled(true);
                    pass.setEnabled(true);
                    spinner.setEnabled(true);
                    isOwnMailChecked = true;
                } else {
                    mailText.setEnabled(false);
                    pass.setEnabled(false);
                    spinner.setEnabled(false);
                }

            }
        });

        final SeekBar repeatSeekBar = (SeekBar) view.findViewById(R.id.repeatSeekBar);
        repeatSeekBar.setEnabled(false);
        repeatSeekBar.setProgress((int)App.ALARM_INTERVAL);
        repeatSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                App.ALARM_INTERVAL = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        CheckBox repeatCheckBox = (CheckBox) view.findViewById(R.id.repeatCheckBox);
        repeatCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub
                if (buttonView.isChecked()) {
                    repeatSeekBar.setEnabled(true);
                }
                else {
                    repeatSeekBar.setEnabled(false);
                }

            }
        });


        refreshText();

        Button addMailButton = (Button) view.findViewById(R.id.addMailButton);
        addMailButton.setOnClickListener(this);

        Button addNumberButton = (Button) view.findViewById(R.id.addNumberButton);
        addNumberButton.setOnClickListener(this);


        Log.w(TAG, "onCreateView ENDS ");

        return view;
    }

    public void refreshText (){
        if (mailsArray == null){
            mailsArray = (TextView) v.findViewById(R.id.emailArray);
        }
        String finalMails = "";
        for (String i : getMails()) {
            finalMails += i + "\n ";
        }
        mailsArray.setText(finalMails);

        if (numberArray == null){
            numberArray = (TextView) v.findViewById(R.id.numbersArray);
        }
        String finalNumbers = "";
        for (String i : getNumbers()) {
            finalNumbers += i + "\n ";
        }
        numberArray.setText(finalNumbers);
    }

    @Override
    public void onClick(View v) {
        switch ( v.getId()) {
            case R.id.SaveButton:
                Log.w(TAG, "Save button click ");
                String mailAdress = mailText.getText().toString();
                String userPass = pass.getText().toString();
                if (isOwnMailChecked){
                    if (smtpName != null && spinnerMailChosen) {
                        try {
                        InternetAddress mail = new InternetAddress(mailAdress);
                        mail.validate();

                        editor.putString(App.SMTP_NAME, smtpName);
                        editor.putString(App.USER_EMAIL, mailAdress);
                        editor.putString(App.USER_PASS, userPass);
                        editor.apply();
                        App.USE_DEFAULT_MAIL = false;
                        Toast.makeText(getActivity(), "Your email address is accepted. But we don`t recommend you to use it ! ", Toast.LENGTH_LONG).show();
                        } catch (AddressException e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity(), "Sorry your email address is wrong! Try again!", Toast.LENGTH_LONG).show();
                     }
                    }else {

                    Toast.makeText(getActivity(), "Attention! You have entered incorrect data! ", Toast.LENGTH_LONG).show();
                    }
                }

                editor.putString(App.USER_NAME, nameText.getText().toString());

                break;
            case R.id.addMailButton:
                Log.w(TAG, "addMailButton click ");
                    addMailButton();
                break;
            case R.id.addNumberButton:
                Log.w(TAG, "addNumberButton click ");
                addNumberButton();
                break;
        }
    }

    @Override
    public void onStop() {
        editor = null;
        super.onStop();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Log.w(TAG, "Spinner click ");
            switch (position) {
                case 1:
                    smtpName = mails[1];

                    break;
                case 2:
                    smtpName = mails[2];
                    spinnerMailChosen = true;
                    break;
                case 3:
                    smtpName = mails[3];
                    spinnerMailChosen = true;
                    break;
                case 4:
                    smtpName = mails[4];
                    spinnerMailChosen = true;
                    break;
                case 5:
                    smtpName = mails[5];
                    spinnerMailChosen = true;
                    break;
                case 6:
                    smtpName = mails[6];
                    spinnerMailChosen = true;
                    break;
                case 7:
                    smtpName = mails[7];
                    spinnerMailChosen = true;
                    break;
            }

    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Toast.makeText(getActivity(), "Will be used default email address - " + App.DEFAULT_MAIL, Toast.LENGTH_LONG).show();
    }



    private void addMailButton (){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_mail_add, null);
        builder.setView(dialogView)
                .setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        EditText editText = (EditText) dialogView.findViewById(R.id.username);
                        String text = editText.getText().toString();

                        try {
                            InternetAddress mail = new InternetAddress(text);
                            mail.validate();
                            addMailToTable(text);
                            refreshText();
                        } catch (AddressException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), "Sorry email address is wrong! Try again!", Toast.LENGTH_LONG).show();
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                }).create().show();

    }

    private void addNumberButton (){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_number_add, null);
        builder.setView(dialogView)
                .setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        EditText editText = (EditText) dialogView.findViewById(R.id.username);

                        // add phone number validation here !!!

                        addNumberToTable(editText.getText().toString());
                        refreshText();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                }).create().show();
    }

    /**
    Working with BD START
     */
    private void addMailToTable (String value) {
        final ContentValues data = new ContentValues();
        data.put(EmailTable.COLUMN_TEXT, value);

        final long insert = DBHelper.getInstance().getDatabase()
                .insert(EmailTable.TABLE_NAME, null, data);
        if (insert < 0 ){

            Log.e(TAG, "addMailToTable method failed");
            Toast.makeText(getActivity(), "Error occurred!",
                    Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), value + " added",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void addNumberToTable (String value) {
        final ContentValues data = new ContentValues();
        data.put(NumberTable.COLUMN_NUMBER, value);

        final long insert = DBHelper.getInstance().getDatabase()
                .insert(NumberTable.TABLE_NAME, null, data);
        if (insert < 0 ){

            Log.e(TAG, "addNumberToTable method failed");
            Toast.makeText(getActivity(), "Error occurred!",
                    Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), value + " added",
                    Toast.LENGTH_SHORT).show();
        }
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

    /**
     Working with BD END
     */
}
