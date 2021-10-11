package com.myapplication.healthylife.adapter.recycleview;
import android.view.ViewGroup;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.myapplication.healthylife.R;
import com.myapplication.healthylife.databinding.DishListBinding;
import com.myapplication.healthylife.local.DatabaseHelper;
import com.myapplication.healthylife.model.Dish;

import java.io.Serializable;
import java.util.ArrayList;

public class DishRecViewAdapter extends RecyclerView.Adapter<DishRecViewAdapter.ViewHolder>{
    private ArrayList<Dish> dishes = new ArrayList<>();
    private Activity activity;
    private DatabaseHelper db;
    public DishRecViewAdapter(Activity activity, Context context){
        this.activity = activity;
        this.db = new DatabaseHelper(context);
    }

    @NonNull
    @Override
    public DishRecViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DishRecViewAdapter.ViewHolder(DishListBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DishRecViewAdapter.ViewHolder holder, int position) {
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
                    Navigation.findNavController(activity, R.id.fragmentContainer).navigate(R.id.action_DietDetail_to_DishDetail, bundle);
                }
            });
        }
    }
}
