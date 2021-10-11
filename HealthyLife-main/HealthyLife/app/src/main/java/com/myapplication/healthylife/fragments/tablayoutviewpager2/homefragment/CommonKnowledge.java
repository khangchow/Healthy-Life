package com.myapplication.healthylife.fragments.tablayoutviewpager2.homefragment;

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
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.myapplication.healthylife.alarmreceiver.BMIAlarmReceiver;
import com.myapplication.healthylife.databinding.FragmentCommonKnowledgeBinding;
import com.myapplication.healthylife.local.AppPrefs;

public class CommonKnowledge extends Fragment {
    private FragmentCommonKnowledgeBinding binding;
    private SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        sharedPreferences = AppPrefs.getInstance(getContext());
        binding = FragmentCommonKnowledgeBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.btnMoreBMI.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("https://en.wikipedia.org/wiki/Body_mass_index"));
                getActivity().startActivity(i);
            }
        });

        AlarmManager alarmManager = (AlarmManager) getActivity(). getSystemService(Context.ALARM_SERVICE);
        Intent notificationIntent = new Intent(getActivity(), BMIAlarmReceiver.class);
        PendingIntent broadcast = PendingIntent.getBroadcast(getActivity(), 100, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        binding.switch1.setChecked(sharedPreferences.getBoolean("bmi", false));
        binding.switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)  {
                    sharedPreferences.edit().putBoolean("bmi", true).commit();
                    alarmManager.set(AlarmManager.RTC_WAKEUP,System.currentTimeMillis() + 10000 , broadcast);
                }
                else{
                    sharedPreferences.edit().putBoolean("bmi", false).commit();
                    alarmManager.cancel(broadcast);
                }
            }
        });
    }
}