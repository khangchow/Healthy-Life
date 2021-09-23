package com.myapplication.healthylife;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Calendar;

public class DrinkWaterNoti extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
            // Inflate the layout for this fragment
            View v = inflater.inflate(R.layout.fragment_drink_water_noti, container, false);
            return v;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        AlarmManager alarmManager = (AlarmManager) getActivity(). getSystemService(Context.ALARM_SERVICE);
        Intent notificationIntent = new Intent(getActivity(), AlarmReceiver3.class);
        PendingIntent broadcast = PendingIntent.getBroadcast(getActivity(), 100, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        int ALARM_DELAY_IN_SECOND = 60;
        long alarmTimeAtUTC = System.currentTimeMillis() + ALARM_DELAY_IN_SECOND * 1_000L;

        Switch simpleSwitch = (Switch) view.findViewById(R.id.switch1);
        SharedPreferences sharedPrefs = getActivity().getPreferences(Context.MODE_PRIVATE);
        simpleSwitch.setChecked(sharedPrefs.getBoolean("Water", false));
        simpleSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)  {
                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, alarmTimeAtUTC, 60000, broadcast);
                }
                else{
                    alarmManager.cancel(broadcast);
                }
            }
        });
        simpleSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (simpleSwitch.isChecked()) {
                    SharedPreferences.Editor editor = sharedPrefs.edit();
                    editor.putBoolean("Water", true);
                    editor.commit();
                } else {
                    SharedPreferences.Editor editor = sharedPrefs.edit();
                    editor.putBoolean("Water", false);
                    editor.commit();
                }
            }
        });
    }
}