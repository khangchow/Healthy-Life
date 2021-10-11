package com.myapplication.healthylife.fragments.tablayoutviewpager2.diet;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.myapplication.healthylife.R;
import com.myapplication.healthylife.databinding.FragmentDietRecommendBinding;

import com.myapplication.healthylife.local.AppPrefs;
import com.myapplication.healthylife.local.DatabaseHelper;
import com.myapplication.healthylife.model.Diet;
import com.myapplication.healthylife.model.Dish;
import com.myapplication.healthylife.adapter.recycleview.DietRecViewAdapter;

import java.util.ArrayList;

public class DietRecommendFragment extends Fragment {
    private FragmentDietRecommendBinding binding;
    private NavController navController;
    private DatabaseHelper db;
    private ArrayList<Diet> dietList;
    private ArrayList<Dish> dish;
    private SharedPreferences sharedPreferences;
    DietRecViewAdapter dietRecAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDietRecommendBinding.inflate(getLayoutInflater());
        db = new DatabaseHelper(getContext());
        sharedPreferences = AppPrefs.getInstance(getContext());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initDietRecycleView();
    }

    private void initDietRecycleView() {
        dietList = db.getDietList();
        dish = db.getDishList();
        int m = 0;
        for (Diet diet : dietList) {
            for(Dish d:dish) {
                if ((diet.isCarbAllowed() == d.isCarb() || diet.isCarbAllowed()) && d.isBreakfast() && (d.isVegan() == diet.isVegan() || !diet.isVegan()) && (diet.isFatAllowed() == d.isFat() || diet.isFatAllowed()) && diet.getBreakfast().size() < 7) {
                    dietList.get(m).getBreakfast().add(d);
                } else if ((diet.isCarbAllowed() == d.isCarb() || diet.isCarbAllowed()) && d.isLunch() && (d.isVegan() == diet.isVegan() || !diet.isVegan()) && (diet.isFatAllowed() == d.isFat() || diet.isFatAllowed()) && diet.getLunch().size() < 7) {
                    dietList.get(m).getLunch().add(d);
                } else if ((diet.isCarbAllowed() == d.isCarb() || diet.isCarbAllowed()) && d.isDinner() && (d.isVegan() == diet.isVegan() || !diet.isVegan()) && (diet.isFatAllowed() == d.isFat() || diet.isFatAllowed()) && diet.getDinner().size() < 7) {
                    dietList.get(m).getDinner().add(d);
                }
                if (diet.getBreakfast().size() == 7 && diet.getLunch().size() == 7 && diet.getDinner().size() == 7)
                    break;
            }
            ++m;
        }

        dietRecAdapter = new DietRecViewAdapter(getActivity(), getContext());
        dietRecAdapter.setDiets(dietList);
        binding.RVDietRecommend.setAdapter(dietRecAdapter);
        binding.RVDietRecommend.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void onStart() {
        super.onStart();
        navController = Navigation.findNavController(getActivity(), R.id.fragmentContainer);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
