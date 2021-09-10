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
import com.myapplication.healthylife.databinding.DietListBinding;
import com.myapplication.healthylife.local.DatabaseHelper;
import com.myapplication.healthylife.model.Diet;
import com.myapplication.healthylife.recycleviewadapters.DietRecViewAdapter;

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
        return new ViewHolder(DietListBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
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

        DietListBinding binding;

        public ViewHolder(@NonNull DietListBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }

        public void bind(Diet diet){
            binding.RVDietListImage.setImageResource(diet.getImage());
            binding.dietName.setText(diet.getName());
            binding.RVDietContentDescription.setText(diet.getDescription());

            binding.DietListRV.setOnClickListener(new View.OnClickListener() {
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
