package com.myapplication.healthylife.fragments.tablayoutviewpager2.fitness;

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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.myapplication.healthylife.alarmreceiver.StandUpAlarmReceiver;
import com.myapplication.healthylife.databinding.FragmentStandUpNotiBinding;
import com.myapplication.healthylife.local.AppPrefs;

public class StandUpNoti extends Fragment {
    private FragmentStandUpNotiBinding binding;
    private SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentStandUpNotiBinding.inflate(getLayoutInflater());
        sharedPreferences = AppPrefs.getInstance(getContext());
        return binding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        AlarmManager alarmManager = (AlarmManager) getActivity(). getSystemService(Context.ALARM_SERVICE);
        Intent notificationIntent = new Intent(getActivity(), StandUpAlarmReceiver.class);
        PendingIntent broadcast = PendingIntent.getBroadcast(getActivity(), 100, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        binding.switch1.setChecked(sharedPreferences.getBoolean("standUp", false));
        binding.switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)  {
                    sharedPreferences.edit().putBoolean("standUp", true).commit();
                    alarmManager.set(AlarmManager.RTC_WAKEUP,System.currentTimeMillis() + 1800000 , broadcast);
                }
                else{
                    sharedPreferences.edit().putBoolean("standUp", false).commit();
                    alarmManager.cancel(broadcast);
             }
            }
        });
    }

}
