package com.myapplication.healthylife.fragments.tablayoutviewpager2.fitness;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.gson.Gson;
import com.myapplication.healthylife.BaseActivity;
import com.myapplication.healthylife.R;
import com.myapplication.healthylife.databinding.FragmentMainFitnessBinding;
import com.myapplication.healthylife.local.AppPrefs;
import com.myapplication.healthylife.model.User;
import com.myapplication.healthylife.viewmodel.CommunicateViewModel;


public class MainFitnessFragment extends Fragment {
    FragmentMainFitnessBinding binding;
    NavController navController;
    SharedPreferences sharedPreferences;
    User user;
    private CommunicateViewModel viewModel;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMainFitnessBinding.inflate(getLayoutInflater());
        sharedPreferences = AppPrefs.getInstance(getContext());
        user = BaseActivity.getUserData();
        viewModel = new ViewModelProvider(getActivity()).get(CommunicateViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setUserDataToViews();

        viewModel.isUpdated.observe(getViewLifecycleOwner(), isUpdated -> {
            if (isUpdated) {
                user = BaseActivity.getUserData();

                setUserDataToViews();
            }
        });



    }

    private void setUserDataToViews() {
        binding.tvHeight.setText(String.valueOf(user.getHeight()));
        binding.tvWeight.setText(String.valueOf(user.getWeight()));
        binding.tvBmi.setText(String.valueOf(user.getBmi()));
        binding.tvType.setText(getType());
    }

    private String getType()    {
        double bmi = user.getBmi();
        String type = "";
        if (bmi >= 35) {
            type = "Obesity II";
        }else if(bmi >= 30 && bmi <= 34.9) {
            type = "Obesity I";
        }else if(bmi >= 25 && bmi <= 29.9)  {
            type = "Overweight";
        }else if(bmi >= 18.5 && bmi <= 24.9)    {
            type = "Normal";
        }else   {
            type = "Underweight";
        }
        return type;
    }

    @Override
    public void onStart() {
        super.onStart();
        navController = Navigation.findNavController(getActivity(), R.id.fragmentContainer);

        binding.btnExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_mainFragment_to_fitnessFragment);
            }
        });

        binding.btnTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_mainFragment_to_timerFragment);
            }
        });

        binding.btnStandup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_mainFragment_to_standUpNoti);
            }
        });

        binding.btnBreathe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_mainFragment_to_breathe);
            }
        });

        binding.btnStat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_mainFragment_to_statFragment);
            }
        });
    }
}