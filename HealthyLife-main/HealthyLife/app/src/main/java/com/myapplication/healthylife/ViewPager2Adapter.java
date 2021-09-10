package com.myapplication.healthylife;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.myapplication.healthylife.fragments.mainfragments.DietFragment;
import com.myapplication.healthylife.fragments.mainfragments.FitnessFragment;
import com.myapplication.healthylife.fragments.mainfragments.HomeFragment;
import com.myapplication.healthylife.fragments.mainfragments.MainFitnessFragment;

public class ViewPager2Adapter extends FragmentStateAdapter {

    public ViewPager2Adapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position)   {
            case 0:
                return new HomeFragment();

            case 1:
                return new MainFitnessFragment();

            case 2:
                return new DietFragment();

            default:
                return null;

        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
