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
import com.myapplication.healthylife.databinding.FoodListBinding;
import com.myapplication.healthylife.local.DatabaseHelper;
import com.myapplication.healthylife.model.Diet;

import java.io.Serializable;
import java.util.ArrayList;

public class DietRecViewAdapter extends RecyclerView.Adapter<DietRecViewAdapter.ViewHolder> {

    private ArrayList<Diet> diets = new ArrayList<>();
    private Activity activity;
    private DatabaseHelper db;

    public DietRecViewAdapter(Activity activity, Context context) {
        this.activity = activity;
        this.db = new DatabaseHelper(context);
    }

    @NonNull
    @Override
    public DietRecViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(FoodListBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DietRecViewAdapter.ViewHolder holder, int position) {
        holder.bind(diets.get(position));
    }

    @Override
    public int getItemCount() {
        return diets.size();
    }

    public void setDiets(ArrayList<Diet> diets){
        this.diets = diets;
        notifyDataSetChanged();
    }

    protected class ViewHolder extends RecyclerView.ViewHolder{

        FoodListBinding binding;

        public ViewHolder(@NonNull FoodListBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }

        public void bind(Diet diet){
            binding.image.setImageResource(diet.getImage());
            binding.tvName.setText(diet.getName());
            binding.tvDescription.setText(diet.getDescription());

            binding.parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("DietData", (Serializable) diet);
                    Navigation.findNavController(activity, R.id.fragmentContainer).navigate(R.id.action_dietRecommendFragment_to_DietDetailFragment, bundle);
                }
            });
        }
    }
}
