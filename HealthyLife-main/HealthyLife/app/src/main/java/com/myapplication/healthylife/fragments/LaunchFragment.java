package com.myapplication.healthylife.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.myapplication.healthylife.R;
import com.myapplication.healthylife.databinding.FragmentLaunchBinding;
import com.myapplication.healthylife.local.AppPrefs;
import com.myapplication.healthylife.local.DatabaseHelper;
import com.myapplication.healthylife.model.Diet;
import com.myapplication.healthylife.model.Exercise;
import com.myapplication.healthylife.model.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class LaunchFragment extends Fragment {

    private FragmentLaunchBinding binding;
    private Boolean newLogin = true, isLogout = false;
    private NavController navController;
    private SharedPreferences sharedPreferences;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    private Date date;
    private DatabaseHelper db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        sharedPreferences = AppPrefs.getInstance(getContext());
        db = new DatabaseHelper(getContext());
        binding = FragmentLaunchBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                String data = sharedPreferences.getString("user", null);
                date = new Date();
                String now = sdf.format(date);

                String lastLogin = sharedPreferences.getString("lastLogin", null);

                ArrayList<Exercise> list = db.getExerciseList();
                if(lastLogin == null)   {
                    lastLogin = now;
                }

                if (!lastLogin.equals(now)) {
                    User user = new Gson().fromJson(data, User.class);
                    user.setCaloFitness(0);
                    sharedPreferences.edit().putString("user", new Gson().toJson(user)).apply();
                    for (Exercise ex: list) {
                        ex.setFinished(false);
                        Log.d("UNTICK", ex.getName()+ " "+ex.isFinished());
                    }
                }

                db.deleteAllExercises();

                for (Exercise ex: list) {
                    db.addExercise(ex);
                }
                sharedPreferences.edit().putString("lastLogin", now).apply();

                if (data == null) {
                    navController.navigate(R.id.action_launchFragment_to_firstUseFragment);
                }else{

                    navController.navigate(R.id.action_launchFragment_to_mainFragment);
                }

            }
        }, 1000);
    }

    @Override
    public void onStart() {
        super.onStart();
        navController = Navigation.findNavController(getActivity(), R.id.fragmentContainer);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
    }
    @Override
    public void onStop() {
        super.onStop();
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
    }
}