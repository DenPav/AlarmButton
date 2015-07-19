package com.example.denis.alarmbutton.Fragments;

import android.app.Fragment;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.denis.alarmbutton.App;
import com.example.denis.alarmbutton.DBclasses.DBHelper;
import com.example.denis.alarmbutton.DBclasses.EmailTable;
import com.example.denis.alarmbutton.DBclasses.NumberTable;
import com.example.denis.alarmbutton.R;


/**
 * AlarmButton created by Denis Pavlovsky on 07.05.15.
 */
public class SettingsFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private static final String TAG = SettingsFragment.class.getSimpleName();

    private static String [] mails = {"Choose mail..", "gmail.com", "yandex.ru", "yahoo.com", "mail.com", "mail.ru", "ukr.net"};
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;
    private EditText nameText;
    private EditText mailText;
    private EditText pass;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        editor = sharedPref.edit();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.settings_fragment, container, false);

        Log.w(TAG, "onCreateView Start ");

        nameText = (EditText) view.findViewById(R.id.name);
        mailText = (EditText) view.findViewById(R.id.mail);
        pass = (EditText) view.findViewById(R.id.pass);

        Button button = (Button) view.findViewById(R.id.SaveButton);
        button.setOnClickListener(this);

        final Spinner spinner = (Spinner) view.findViewById(R.id.choose_mail);

        spinner.setSelection(0);

        ArrayAdapter<String> adapter = new
                ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, mails);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(this);

        Log.w(TAG, "onCreateView ENDS ");

        return view;
    }

    @Override
    public void onClick(View v) {
        Log.w(TAG, "Save button click ");

        editor.putString(App.USER_NAME, nameText.getText().toString());
        editor.putString(App.USER_EMAIL, mailText.getText().toString());
        editor.putString(App.USER_PASS, pass.getText().toString());
        editor.apply();
    }

    @Override
    public void onStop() {
        editor = null;
        super.onStop();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Log.w(TAG, "Spinner click ");

        if (editor != null) {
            switch (position) {
                case 1:
                    editor.putString(App.SMTP_NAME, mails[1]);
                    editor.apply();
                    break;
                case 2:
                    editor.putString(App.SMTP_NAME, mails[2]);
                    editor.apply();
                    break;
                case 3:
                    editor.putString(App.SMTP_NAME, mails[3]);
                    editor.apply();
                    break;
                case 4:
                    editor.putString(App.SMTP_NAME, mails[4]);
                    editor.apply();
                    break;
                case 5:
                    editor.putString(App.SMTP_NAME, mails[5]);
                    editor.apply();
                    break;
                case 6:
                    editor.putString(App.SMTP_NAME, mails[6]);
                    editor.apply();
                    break;
                case 7:
                    editor.putString(App.SMTP_NAME, mails[7]);
                    editor.apply();
                    break;
            }
        } else {

        }

        if (position > 0 && position < 8) {
            mailText.setEnabled(true);
            mailText.setInputType(InputType.TYPE_CLASS_TEXT);
            mailText.setFocusable(true);
        }
    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }



    private void addMailButton (){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.dialog_mail_add, null))

                .setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                }).create();
    }

    private void addNumberButton (){

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

    /**
     Working with BD END
     */
}
