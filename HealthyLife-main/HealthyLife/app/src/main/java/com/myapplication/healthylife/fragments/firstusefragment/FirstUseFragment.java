package com.myapplication.healthylife.fragments.firstusefragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.gson.Gson;
import com.myapplication.healthylife.BaseFragment;
import com.myapplication.healthylife.R;
import com.myapplication.healthylife.databinding.FragmentFirstUseBinding;
import com.myapplication.healthylife.local.AppPrefs;
import com.myapplication.healthylife.local.DatabaseHelper;
import com.myapplication.healthylife.model.Diet;
import com.myapplication.healthylife.model.Dish;
import com.myapplication.healthylife.model.Exercise;
import com.myapplication.healthylife.model.Stat;
import com.myapplication.healthylife.model.User;
import com.myapplication.healthylife.utils.DatabaseUtils;
import com.myapplication.healthylife.utils.DietUtils;
import com.myapplication.healthylife.utils.DishUtils;
import com.myapplication.healthylife.utils.ExerciseUtils;
import com.myapplication.healthylife.utils.KeyboardUtils;
import com.myapplication.healthylife.utils.ScrollUtils;
import com.myapplication.healthylife.utils.StatUtils;
import com.myapplication.healthylife.utils.ValidatorUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FirstUseFragment extends Fragment {
    private FragmentFirstUseBinding binding;
    private NavController navController;

    private SharedPreferences sharedPreferences;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    private SimpleDateFormat dateTimeSdf = new SimpleDateFormat("dd/MM/yyyy, kk:mm:ss");
    private Date date;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        sharedPreferences = AppPrefs.getInstance(getContext());

        binding = FragmentFirstUseBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onStart() {
        super.onStart();
        navController = Navigation.findNavController(getActivity(), R.id.fragmentContainer);
        binding.btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name, height, weight;

                name = binding.etName.getEditText().getText().toString();
                height = binding.etHeight.getEditText().getText().toString();
                weight = binding.etWeight.getEditText().getText().toString();

                if(ValidatorUtils.validateString(name)) {
                    binding.etName.setError(null);
                    if (ValidatorUtils.validateHeight(height))    {
                        binding.etHeight.setError(null);
                        if (ValidatorUtils.validateWeight(weight))    {
                            binding.etWeight.setError(null);
                            setUpDataForNewUser();
                            navController.navigate(R.id.action_firstUseFragment_to_mainFragment);
                        }else   {
                            focusEditText(binding.etWeight);
                            binding.etWeight.setError("Invalid Weight");
                        }
                    }else   {
                        focusEditText(binding.etHeight);
                        binding.etHeight.setError("Invalid Height");
                    }
                }else   {
                    focusEditText(binding.etName);
                    binding.etName.setError("Invalid Name");
                }
            }
        });
    }

    private void setUpDataForNewUser()  {
        User user = new User(binding.etName.getEditText().getText().toString(),
                Float.valueOf(binding.etHeight.getEditText().getText().toString()),
                Float.valueOf(binding.etWeight.getEditText().getText().toString()));

        double bmi = Math.round(((user.getWeight()/Math.pow(user.getHeight()/100, 2))*10)/10);
        Log.d("DATA", String.valueOf(bmi));
        user.setBmi(bmi);

        sharedPreferences.edit().putBoolean("isLogout", false).apply();

        sharedPreferences.edit().putString("user", new Gson().toJson(user)).apply();

        date = new Date();
        String now = sdf.format(date);

        sharedPreferences.edit().putString("lastLogin", now).apply();

        ExerciseUtils.saveListOfExercisesForNewUser(bmi);
        DietUtils.saveListofDietForNewUser(bmi);
        DishUtils.saveListofDishForNewUser();
        StatUtils.addStat(new Stat(-1, user.getHeight(), user.getWeight(), user.getBmi(), dateTimeSdf.format(date)));
    }

    private void focusEditText(View view)    {
        view.requestFocus();

        KeyboardUtils.openKeyboard(view);
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

//    @Override
//    public void onAttach(@NonNull Context context) {
//        super.onAttach(context);
//
//        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
//            @Override
//            public void handleOnBackPressed() {
//                Log.d("BACK", "handleOnBackPressed: ");
//            }
//        };
//        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
//    }
}