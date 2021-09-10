package com.myapplication.healthylife.recycleviewadapters;
import android.view.ViewGroup;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.myapplication.healthylife.R;
import com.myapplication.healthylife.databinding.DishListBinding;
import com.myapplication.healthylife.local.DatabaseHelper;
import com.myapplication.healthylife.model.Diet;
import com.myapplication.healthylife.model.Dish;
import com.myapplication.healthylife.recycleviewadapters.DishRecSecViewAdapter;

import java.io.Serializable;
import java.util.ArrayList;

public class DishRecSecViewAdapter extends RecyclerView.Adapter<DishRecSecViewAdapter.ViewHolder>{
    private ArrayList<Dish> dishes = new ArrayList<>();
    private Activity activity;
    private DatabaseHelper db;
    public DishRecSecViewAdapter(Activity activity, Context context){
        this.activity = activity;
        this.db = new DatabaseHelper(context);
    }

    @NonNull
    @Override
    public DishRecSecViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DishRecSecViewAdapter.ViewHolder(DishListBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DishRecSecViewAdapter.ViewHolder holder, int position) {
        holder.bind(dishes.get(position));
    }

    @Override
    public int getItemCount() {
        return dishes.size();
    }
    public void SetDishes(ArrayList<Dish> dishes){
        this.dishes = dishes;
        notifyDataSetChanged();
    }
    protected class ViewHolder extends RecyclerView.ViewHolder{

        DishListBinding binding;

        public ViewHolder(@NonNull DishListBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }

        public void bind(Dish dish){
            binding.dishName.setText(dish.getName());
            binding.RVDishContentDescription.setText(dish.getDescription());
             binding.RVDishListImage.setImageResource(dish.getImage());

            binding.DishListRV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("DishData", (Serializable) dish);
                    Navigation.findNavController(activity, R.id.fragmentContainer).navigate(R.id.action_mainFragment_to_dishDetailFragment, bundle);
                }
            });
        }
    }
}
