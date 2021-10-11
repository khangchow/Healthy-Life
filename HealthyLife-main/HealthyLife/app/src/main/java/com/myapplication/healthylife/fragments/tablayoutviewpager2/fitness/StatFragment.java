package com.myapplication.healthylife.fragments.tablayoutviewpager2.fitness;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.myapplication.healthylife.R;
import com.myapplication.healthylife.databinding.FragmentStatBinding;
import com.myapplication.healthylife.local.DatabaseHelper;
import com.myapplication.healthylife.model.Stat;
import com.myapplication.healthylife.adapter.recycleview.StatRecViewAdapter;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentStatBinding.inflate(getLayoutInflater());
        db = new DatabaseHelper(getContext());

        stats = db.getStatList();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new StatRecViewAdapter(getContext());
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
                            double bmi = Math.round(((weight/Math.pow(height/100, 2))*10)/10);
                            date = new Date();
                            Stat stat = new Stat(-1, height, weight, bmi, dateTimeSdf.format(date));
                            if (db.addStat(stat))   {
                                stats.clear();
                                stats = db.getStatList();
                                adapter.setStat(stats);
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
}