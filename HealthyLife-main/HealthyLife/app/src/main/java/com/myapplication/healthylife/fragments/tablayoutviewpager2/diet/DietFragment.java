package com.myapplication.healthylife.fragments.tablayoutviewpager2.diet;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

import com.google.gson.Gson;
import com.myapplication.healthylife.R;
import com.myapplication.healthylife.databinding.FragmentDietBinding;
import com.myapplication.healthylife.databinding.FragmentLaunchBinding;

import com.myapplication.healthylife.local.AppPrefs;
import com.myapplication.healthylife.local.DatabaseHelper;
import com.myapplication.healthylife.model.Diet;
import com.myapplication.healthylife.model.Dish;
import com.myapplication.healthylife.model.User;
import com.myapplication.healthylife.recycleviewadapters.DietRecViewAdapter;
import com.myapplication.healthylife.recycleviewadapters.DishRecSecViewAdapter;
import com.myapplication.healthylife.recycleviewadapters.DishRecViewAdapter;

import java.util.ArrayList;
import java.util.Calendar;

public class DietFragment extends Fragment {

    private FragmentDietBinding binding;
    private NavController navController;
    private DatabaseHelper db;
    private SharedPreferences sharedPreferences;
    private ArrayList<Diet> diets;
    private ArrayList<Dish> dishes;
    private DishRecSecViewAdapter dishRecViewAdapterBreakfast;
    private DishRecSecViewAdapter dishRecViewAdapterLunch;
    private DishRecSecViewAdapter dishRecViewAdapterDinner;
    private Diet AssignedDiet = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        diet = (Diet) getArguments().getSerializable("PickDietData");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDietBinding.inflate(getLayoutInflater());
        db = new DatabaseHelper(getContext());
        sharedPreferences = AppPrefs.getInstance(getContext());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        int index = 0;
        diets=db.getDietList();
        dishes=db.getDishList();
        for(Diet i : diets){
            if(i.isAssigned()){
                AssignedDiet = i;
                break;
            }
            index++;
        }
        if(index == diets.size()){
            binding.LoveDishNotification.setText("No dishes found! " +
                    "Please pick a diet by clicking recommend button and doing further actions.");
        }
        else {
            int Check = AssignDishesToDiet(diets.get(index));
            if (Check == 0)
                binding.LoveDishNotification.setText("Not enough dishes found! " +
                        "Please pick another diet by clicking recommend button and doing further actions.");
            else {

                Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_WEEK);
                ArrayList<Dish> breakfast = new ArrayList<>();
                breakfast.add(diets.get(index).getBreakfast().get(day % diets.get(index).getBreakfast().size()));

                ArrayList<Dish> lunch = new ArrayList<>();
                lunch.add(diets.get(index).getLunch().get(day % diets.get(index).getLunch().size()));

                ArrayList<Dish> dinner = new ArrayList<>();
                dinner.add(diets.get(index).getDinner().get(day % diets.get(index).getDinner().size()));

                dishRecViewAdapterBreakfast = new DishRecSecViewAdapter(getActivity(), getContext());
                dishRecViewAdapterBreakfast.SetDishes(breakfast);
                binding.rvDishDetailTodayBreakfast.setAdapter(dishRecViewAdapterBreakfast);
                binding.rvDishDetailTodayBreakfast.setLayoutManager(new LinearLayoutManager(getContext()));

                dishRecViewAdapterLunch = new DishRecSecViewAdapter(getActivity(), getContext());
                dishRecViewAdapterLunch.SetDishes(lunch);
                binding.rvDishDetailTodayLunch.setAdapter(dishRecViewAdapterLunch);
                binding.rvDishDetailTodayLunch.setLayoutManager(new LinearLayoutManager(getContext()));

                dishRecViewAdapterDinner = new DishRecSecViewAdapter(getActivity(), getContext());
                dishRecViewAdapterDinner.SetDishes(dinner);
                binding.rvDishDetailTodayDinner.setAdapter(dishRecViewAdapterDinner);
                binding.rvDishDetailTodayDinner.setLayoutManager(new LinearLayoutManager(getContext()));
            }
        }
    }

    private int AssignDishesToDiet(Diet diet) {
        for (Dish d : dishes) {
            if((diet.isCarbAllowed() == d.isCarb() || diet.isCarbAllowed() == true) && d.isBreakfast() && (d.isVegan() == diet.isVegan() || !diet.isVegan()) && (diet.isFatAllowed() == d.isFat() || diet.isFatAllowed() == true) && diet.getBreakfast().size()<7)   {
                diet.insertBreakfast(d);
            }
            else if((diet.isCarbAllowed() == d.isCarb() || diet.isCarbAllowed() == true) && d.isLunch() && (d.isVegan() == diet.isVegan() || !diet.isVegan()) && (diet.isFatAllowed() == d.isFat() || diet.isFatAllowed() == true) && diet.getLunch().size()<7)  {
                diet.insertLunch(d);
            }
            else if((diet.isCarbAllowed() == d.isCarb() || diet.isCarbAllowed() == true) && d.isDinner() && (d.isVegan() == diet.isVegan() || !diet.isVegan()) && (diet.isFatAllowed() == d.isFat() || diet.isFatAllowed() == true) && diet.getDinner().size()<7) {
                diet.insertDinner(d);
            }
            if(diet.getBreakfast().size()==7 && diet.getLunch().size()==7 && diet.getDinner().size()==7)
                return 1;
        }
        if(diet.getBreakfast().size()==0 || diet.getLunch().size()==0 || diet.getDinner().size()==0)
            return 0;
        int curBreakfast=diet.getBreakfast().size();
        int curLunch=diet.getLunch().size();
        int curDinner=diet.getDinner().size();
        for (int n = 0; n < 7 ; n++){
            if(diet.getBreakfast().size() == 7){
                break;
            }
            diet.insertBreakfast(diet.getBreakfast().get(diet.getBreakfast().size() - curBreakfast ));
        }
        for (int n = 0; n < 7 ; n++){
            if(diet.getLunch().size() == 7){
                break;
            }
            diet.insertLunch(diet.getLunch().get(diet.getLunch().size() - curLunch ));
        }
        for (int n = 0; n < 7 ; n++){
            if(diet.getDinner().size() == 7){
                break;
            }
            diet.insertLunch(diet.getDinner().get(diet.getDinner().size() - curDinner ));
        }
        return 1;
    }


    @Override
    public void onStart() {
        super.onStart();
        navController = Navigation.findNavController(getActivity(), R.id.fragmentContainer);

        binding.RecommendDietButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                navController.navigate(R.id.action_mainFragment_to_dietRecommendFragment);
            }
        });

        binding.DrinkWaterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_mainFragment_to_drinkWater);
            }
        });

        binding.ResetDietButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AssignedDiet != null) {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
                    builder1.setMessage("Your current dietary recommendation will disappear. Do you want to continue?");
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    String data = sharedPreferences.getString("user", null);
                                    User user = new Gson().fromJson(data, User.class);
                                    user.setCaloDiet(0);
                                    sharedPreferences.edit().putString("user", new Gson().toJson(user)).apply();
                                    AssignedDiet.setAssigned(false);
                                    db.editAssignedDiet(AssignedDiet);
                                    navController.navigate(R.id.action_mainFragment_to_mainFragment);
                                }
                            });

                    builder1.setNegativeButton(
                            "No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }
                else{
                    AlertDialog.Builder builder2 = new AlertDialog.Builder(getContext());
                    builder2.setMessage("No dishes found! " +
                            "Please pick a diet by clicking recommend button and doing further actions.");
                    builder2.setCancelable(true);

                    AlertDialog alert12 = builder2.create();
                    alert12.show();
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}