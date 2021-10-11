package com.myapplication.healthylife.fragments.tablayoutviewpager2.diet;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.myapplication.healthylife.alarmreceiver.WaterAlarmReceiver;
import com.myapplication.healthylife.databinding.FragmentDrinkWaterNotiBinding;
import com.myapplication.healthylife.local.AppPrefs;

import java.util.Calendar;

public class DrinkWaterNoti extends Fragment {
    private FragmentDrinkWaterNotiBinding binding;
    private SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        sharedPreferences = AppPrefs.getInstance(getContext());
        binding = FragmentDrinkWaterNotiBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        AlarmManager alarmManager = (AlarmManager) getActivity(). getSystemService(Context.ALARM_SERVICE);
        Intent notificationIntent = new Intent(getActivity(), WaterAlarmReceiver.class);
        PendingIntent broadcast = PendingIntent.getBroadcast(getActivity(), 100, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        binding.switchReminder.setChecked(sharedPreferences.getBoolean("water", false));
        binding.switchReminder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)  {
                    sharedPreferences.edit().putBoolean("water", true).commit();
                    alarmManager.set(AlarmManager.RTC_WAKEUP,System.currentTimeMillis() + 10000 , broadcast);
                }
                else{
                    sharedPreferences.edit().putBoolean("water", false).commit();
                    alarmManager.cancel(broadcast);
                }
            }
        });
    }
}