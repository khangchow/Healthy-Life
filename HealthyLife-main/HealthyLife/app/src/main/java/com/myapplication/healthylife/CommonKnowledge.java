package com.myapplication.healthylife;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Calendar;

public class CommonKnowledge extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_common_knowledge, container, false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button btnLearnMore = (Button) getActivity().findViewById(R.id.btnMoreBMI);
        btnLearnMore.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("https://en.wikipedia.org/wiki/Body_mass_index"));
                getActivity().startActivity(i);
            }
        });

        AlarmManager alarmManager = (AlarmManager) getActivity(). getSystemService(Context.ALARM_SERVICE);
        Intent notificationIntent = new Intent(getActivity(), AlarmReceiver2.class);
        PendingIntent broadcast = PendingIntent.getBroadcast(getActivity(), 100, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar cal = Calendar.getInstance();

        Switch simpleSwitch = (Switch) view.findViewById(R.id.switch1);
        SharedPreferences sharedPrefs = getActivity().getPreferences(Context.MODE_PRIVATE);
        simpleSwitch.setChecked(sharedPrefs.getBoolean("BMI", false));
        simpleSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)  {
                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 4*60*60, broadcast);
                }
                else{
                    alarmManager.cancel(broadcast);
                }
            }
        });
        simpleSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(simpleSwitch.isChecked())  {
                    SharedPreferences.Editor editor = sharedPrefs.edit();
                    editor.putBoolean("BMI", true);
                    editor.commit();
                }
                else{
                    SharedPreferences.Editor editor = sharedPrefs.edit();
                    editor.putBoolean("BMI", false);
                    editor.commit();                }
            }
        });

    }
}