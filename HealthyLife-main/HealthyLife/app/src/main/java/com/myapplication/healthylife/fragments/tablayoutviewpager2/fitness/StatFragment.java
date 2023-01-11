package com.myapplication.healthylife.fragments.tablayoutviewpager2.fitness;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.myapplication.healthylife.BaseActivity;
import com.myapplication.healthylife.R;
import com.myapplication.healthylife.adapter.recycleview.BMIUpdateListener;
import com.myapplication.healthylife.databinding.FragmentStatBinding;
import com.myapplication.healthylife.local.AppPrefs;
import com.myapplication.healthylife.local.DatabaseHelper;
import com.myapplication.healthylife.model.Stat;
import com.myapplication.healthylife.adapter.recycleview.StatRecViewAdapter;
import com.myapplication.healthylife.model.User;
import com.myapplication.healthylife.utils.DietUtils;
import com.myapplication.healthylife.utils.DishUtils;
import com.myapplication.healthylife.utils.ExerciseUtils;
import com.myapplication.healthylife.viewmodel.CommunicateViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class StatFragment extends Fragment {

    ArrayList<Stat> stats = new ArrayList<>();
    FragmentStatBinding binding;
    DatabaseHelper db;
    StatRecViewAdapter adapter;
    private SimpleDateFormat dateTimeSdf = new SimpleDateFormat("dd/MM/yyyy, kk:mm:ss");
    private Date date;
    private CommunicateViewModel viewModel;
    private SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentStatBinding.inflate(getLayoutInflater());
        db = new DatabaseHelper(getContext());
        viewModel = new ViewModelProvider(getActivity()).get(CommunicateViewModel.class);
        stats = db.getStatList();
        sharedPreferences = AppPrefs.getInstance(getContext());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new StatRecViewAdapter(getContext(), (height, weight) -> {
            double bmi = Math.round(((weight/Math.pow(height/100, 2))*10)/10);
            resetData(height, weight, bmi);
        });
        adapter.setStat(stats);
        binding.statRecView.setAdapter(adapter);
        binding.statRecView.setLayoutManager(new LinearLayoutManager(getActivity()));

        binding.fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.custom_dialog_add);

                EditText etHeight = dialog.findViewById(R.id.etHeight);
                EditText etWeight = dialog.findViewById(R.id.etWeight);
                Button btnAdd = dialog.findViewById(R.id.btnAdd);
                Button btnCancel = dialog.findViewById(R.id.btnCancel);

                btnAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        float height;
                        float weight;

                        if (!etHeight.getText().toString().equals("") && !etWeight.getText().toString().equals("")) {
                            height = Float.valueOf(etHeight.getText().toString());
                            weight = Float.valueOf(etWeight.getText().toString());
                            date = new Date();
                            double bmi = Math.round(((weight/Math.pow(height/100, 2))*10)/10);
                            Stat stat = new Stat(-1, height, weight, bmi, dateTimeSdf.format(date));
                            if (db.addStat(stat))   {
                                stats.clear();
                                stats = db.getStatList();
                                adapter.setStat(stats);
                                resetData(height, weight, bmi);
                                dialog.dismiss();
                            }
                        }else{
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
        });
    }

   private void resetData(float height, float weight, double bmi) {
       User user = BaseActivity.getUserData();
       user.setBmi(bmi);
       user.setHeight(height);
       user.setWeight(weight);
       user.setCaloDiet(0);
       user.setCaloFitness(0);
       sharedPreferences.edit().putString("user", new Gson().toJson(user)).apply();
       ExerciseUtils.saveListOfExercisesForNewUser(bmi);
       DietUtils.saveListofDietForNewUser(bmi);
       DishUtils.saveListofDishForNewUser();
       viewModel.updatedBMI(true);
   }
}