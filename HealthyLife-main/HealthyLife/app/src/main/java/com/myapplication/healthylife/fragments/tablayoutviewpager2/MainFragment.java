package com.myapplication.healthylife.fragments.tablayoutviewpager2;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.myapplication.healthylife.R;
import com.myapplication.healthylife.ViewPager2Adapter;
import com.myapplication.healthylife.databinding.FragmentMainBinding;
import com.myapplication.healthylife.viewmodel.CommunicateViewModel;

public class MainFragment extends Fragment {
    private FragmentManager fragmentManager;
    private Lifecycle lifecycle;
    private ViewPager2Adapter adapter;
    private FragmentMainBinding binding;
    private NavController navController;
    private CommunicateViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentManager = getActivity().getSupportFragmentManager();
        lifecycle = getLifecycle();
        binding = FragmentMainBinding.inflate(getLayoutInflater());
        viewModel = new ViewModelProvider(getActivity()).get(CommunicateViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new ViewPager2Adapter(fragmentManager, lifecycle);
        binding.viewPager2.setAdapter(adapter);
        optionFragment();
    }

    private void optionFragment(){
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Home").setIcon(R.drawable.ic_home));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Fitness").setIcon(R.drawable.ic_fitnesss));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Diet").setIcon(R.drawable.ic_diet));

        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.d("TAB", "onTabSelected: "+tab.getPosition());
                binding.viewPager2.setCurrentItem(tab.getPosition());
                if (tab.getPosition() == 0) {
                    viewModel.selectTab(0);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        binding.viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                binding.tabLayout.selectTab(binding.tabLayout.getTabAt(position));
            }
        });

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

    @Override
    public void onStart() {
        super.onStart();
        navController = Navigation.findNavController(getActivity(), R.id.fragmentContainer);

    }


}