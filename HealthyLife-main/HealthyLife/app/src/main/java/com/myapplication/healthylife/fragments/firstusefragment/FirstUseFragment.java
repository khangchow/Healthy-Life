package com.myapplication.healthylife.fragments.firstusefragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.gson.Gson;
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
import com.myapplication.healthylife.utils.ExerciseUtils;
import com.myapplication.healthylife.utils.KeyboardUtils;
import com.myapplication.healthylife.utils.ScrollUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FirstUseFragment extends Fragment {
    private FragmentFirstUseBinding binding;
    private NavController navController;

    private SharedPreferences sharedPreferences;
    private ArrayList<Exercise> exercises = new ArrayList<>();
    private ArrayList<Diet> diets = new ArrayList<>();
    private ArrayList<Dish> dishes = new ArrayList<>();
    private DatabaseHelper db;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    private SimpleDateFormat dateTimeSdf = new SimpleDateFormat("dd/MM/yyyy, kk:mm:ss");
    private Date date;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        sharedPreferences = AppPrefs.getInstance(getContext());
        db = new DatabaseHelper(getContext());

        binding = FragmentFirstUseBinding.inflate(getLayoutInflater());

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listenFocus();

    }

    private void listenFocus() {
        binding.etName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b)  {
                    Log.d("POS", binding.scrollview.getVerticalScrollbarPosition()+" "+ binding.scrollview.getBottom());
                    binding.scrollview.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ScrollUtils.scrollToView(binding.scrollview, view);
                        }
                    }, 500);

                }else   {
                    KeyboardUtils.hideKeyboard(view);
                }
            }
        });

        binding.etWeight.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b)  {
                    binding.scrollview.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ScrollUtils.scrollToView(binding.scrollview, view);
                        }
                    }, 500);
                }
                else   {
                    KeyboardUtils.hideKeyboard(view);
                }
            }
        });

        binding.etHeight.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b)  {
                    binding.scrollview.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ScrollUtils.scrollToView(binding.scrollview, view);
                        }
                    }, 500);
                }else   {
                    KeyboardUtils.hideKeyboard(view);
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        navController = Navigation.findNavController(getActivity(), R.id.fragmentContainer);
        binding.btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!binding.etName.getText().toString().equals("") && validateString(binding.etName.getText().toString())) {
                    if (!binding.etHeight.getText().toString().equals("")
                            && validateFloat(binding.etHeight.getText().toString())
                            && (Float.valueOf(binding.etHeight.getText().toString()) >= 10  && Float.valueOf(binding.etHeight.getText().toString()) <= 300))    {
                        if (!binding.etWeight.getText().toString().equals("")
                                && validateFloat(binding.etWeight.getText().toString())
                                && (Float.valueOf(binding.etWeight.getText().toString()) >= 1 && Float.valueOf(binding.etWeight.getText().toString()) <= 600))    {
                            setUpDataForNewUser();
                            navController.navigate(R.id.action_firstUseFragment_to_mainFragment);
                        }else   {
                            focusEditText(binding.etWeight);
                            Toast.makeText(getActivity(), "Invalid Weight", Toast.LENGTH_SHORT).show();
                        }
                    }else   {
                        focusEditText(binding.etHeight);
                        Toast.makeText(getActivity(), "Invalid Height", Toast.LENGTH_SHORT).show();
                    }
                }else   {
                    focusEditText(binding.etName);
                    Toast.makeText(getActivity(), "Invalid Name", Toast.LENGTH_SHORT).show();
                }
            }
        });

        KeyboardUtils.addKeyboardToggleListener(getActivity(), new KeyboardUtils.SoftKeyboardToggleListener()
        {
            @Override
            public void onToggleSoftKeyboard(boolean isVisible)
            {
                if(!isVisible)   {
                    binding.parent.requestFocus();
                }
            }
        });
    }

    private void setUpDataForNewUser()  {
        User user = new User(binding.etName.getText().toString(),
                Float.valueOf(binding.etHeight.getText().toString()),
                Float.valueOf(binding.etWeight.getText().toString()));

        double bmi = Math.round(((user.getWeight()/Math.pow(user.getHeight()/100, 2))*10)/10);
        Log.d("DATA", String.valueOf(bmi));
        user.setBmi(bmi);

        sharedPreferences.edit().putBoolean("isLogout", false).apply();

        sharedPreferences.edit().putString("user", new Gson().toJson(user)).apply();

        date = new Date();
        String now = sdf.format(date);

        sharedPreferences.edit().putString("lastLogin", now).apply();

        exercises = ExerciseUtils.initExercises();
        ExerciseUtils.saveListOfExercisesForNewUser(exercises, bmi);
        // TODO: 10/25/2021 convert Diet and Dish into utils classes 
        DatabaseUtils.saveListofDietForNewUser(diets, bmi);
        DatabaseUtils.saveListofDishForNewUser(dishes);
        db.addStat(new Stat(-1, user.getHeight(), user.getWeight(), user.getBmi(), dateTimeSdf.format(date)));
    }

    private void focusEditText(View view)    {
        view.requestFocus();
        binding.scrollview.postDelayed(new Runnable() {
            @Override
            public void run() {
                ScrollUtils.scrollToView(binding.scrollview, view);
            }
        }, 500);
        KeyboardUtils.openKeyboard(view);
    }

    private Boolean validateString(String str)   {
        Pattern patternString = Pattern.compile("^[a-zA-Z_ÀÁÂÃÈÉÊẾÌÍÒÓÔÕÙÚĂĐĨŨƠàáâãèéêìíòóôõùúăđĩũơƯĂẠẢẤẦẨẪẬẮẰẲẴẶ" + "ẸẺẼỀỀỂưăạảấầẩẫậắằẳẵặẹẻẽềềểếỄỆỈỊỌỎỐỒỔỖỘỚỜỞỠỢỤỦỨỪễệỉịọỏốồổỗộớờởỡợ" + "ụủứừỬỮỰỲỴÝỶỸửữựỳỵỷỹ\\s]+$");
        Matcher matcher = patternString.matcher(str);
        if (matcher.find()) {
            return true;
        }
        return false;
    }

    private Boolean validateFloat(String num) {
        Pattern patternFloat = Pattern.compile("([0-9]*[.])?[0-9]+");
        Matcher matcher = patternFloat.matcher(num);
        if (matcher.find()) {
            return true;
        }
        return false;
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