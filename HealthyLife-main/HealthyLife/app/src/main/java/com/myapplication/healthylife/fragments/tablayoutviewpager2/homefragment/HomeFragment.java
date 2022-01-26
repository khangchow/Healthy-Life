package com.myapplication.healthylife.fragments.tablayoutviewpager2.homefragment;

import static android.content.Context.INPUT_METHOD_SERVICE;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.myapplication.healthylife.BaseActivity;
import com.myapplication.healthylife.R;

import com.myapplication.healthylife.databinding.FragmentHomeBinding;
import com.myapplication.healthylife.local.AppPrefs;
import com.myapplication.healthylife.local.DatabaseHelper;
import com.myapplication.healthylife.model.Diet;
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
import com.myapplication.healthylife.viewmodel.CommunicateViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private NavController navController;
    private SharedPreferences sharedPreferences;
    private DatabaseHelper db;
    private SimpleDateFormat dateTimeSdf = new SimpleDateFormat("dd/MM/yyyy, kk:mm:ss");
    private Date date;
    private User user;
    private CommunicateViewModel viewModel;
    private TextInputLayout etHeight, etWeight;;
    private Button btnAdd, btnCancel;
    private ScrollView scrollview;
    private Diet AssignedDiet = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        sharedPreferences = AppPrefs.getInstance(getContext());
        db = new DatabaseHelper(getContext());
        binding = FragmentHomeBinding.inflate(getLayoutInflater());

        user = BaseActivity.getUserData();

        viewModel = new ViewModelProvider(getActivity()).get(CommunicateViewModel.class);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel.pos.observe(getActivity(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if (integer == 0)   {
                    user = BaseActivity.getUserData();

                    setTableData();
                }
            }
        });

        setTableData();



        if (challengeCompleted())   {
            // TODO: 10/16/2021 reset diet, improve diet detail interface
            displayUpdateHealthStatusDialog();
        }
    }

    private void setTableData() {
        binding.CaloExercise.setText(String.valueOf("-"+user.getCaloFitness()));
        binding.CaloDiet.setText(String.valueOf("+"+user.getCaloDiet()));
        binding.CaloTotal.setText(String.valueOf(user.getCaloDiet()-user.getCaloFitness()));
    }

    private void displayUpdateHealthStatusDialog()  {
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.custom_dialog_refresh);

        etHeight = dialog.findViewById(R.id.etHeight);
        etWeight = dialog.findViewById(R.id.etWeight);
        btnAdd = dialog.findViewById(R.id.btnAdd);
        btnCancel = dialog.findViewById(R.id.btnCancel);
        scrollview = dialog.findViewById(R.id.scrollview);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                float height;
                float weight;

                // TODO: 11/15/2021 validate inputs 
                if (!etHeight.getEditText().getText().toString().equals("") && !etWeight.getEditText().getText().toString().equals(""))   {
                    height = Float.valueOf(etHeight.getEditText().getText().toString());
                    weight = Float.valueOf(etWeight.getEditText().getText().toString());
                    double bmi = Math.round(((weight/Math.pow(height/100, 2))*10)/10);
                    date = new Date();
                    Stat stat = new Stat(-1, height, weight, bmi, dateTimeSdf.format(date));
                    if (db.addStat(stat))   {

                        user.setHeight(height);
                        user.setWeight(weight);
                        user.setBmi(bmi);

                        user.setCaloFitness(0);

                        user.setCaloDiet(0);
                        Log.d("VM", "Dialog: "+user.toString());
                        sharedPreferences.edit().putString("user", new Gson().toJson(user)).apply();
                        db.deleteAllExercises();
                        ExerciseUtils.saveListOfExercisesForNewUser(bmi);
                        Toast.makeText(getActivity(), "Updated BMI successfully! Check out your new excercise list now!", Toast.LENGTH_SHORT).show();

                        viewModel.updatedBMI(true);

                        setTableData();

                        dialog.dismiss();
                    }
                }else   {
                    Toast.makeText(getActivity(), "Please Fill All Information", Toast.LENGTH_SHORT).show();
                }

            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();

        Window window = dialog.getWindow();

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
    }

    private boolean challengeCompleted()   {
        ArrayList<Exercise> exercises = db.getRecommendedExerciseList();
        for (Exercise ex: exercises
             ) {
            if (ex.getProgress() < 14)    {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();
        navController = Navigation.findNavController(getActivity(), R.id.fragmentContainer);

        binding.btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseUtils.deleteUserData();
                sharedPreferences.edit().putString("user", null).apply();

                DietUtils.removeDiets();

                DishUtils.removeDishes();
//                viewModel.logout(true);

                navController.navigate(R.id.action_mainFragment_to_firstUseFragment);
            }
        });
        binding.btnAboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 1/7/2022 test complete challenge
               navController.navigate(R.id.action_mainFragment_to_aboutUs);
            }
        });
        binding.btnAboutDiet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_mainFragment_to_aboutDiet);
            }
        });
        binding.btnAboutFitness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_mainFragment_to_aboutFitness);

            }
        });
        binding.btnCommonInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_mainFragment_to_commonKnowledge);

            }
        });
    }
}