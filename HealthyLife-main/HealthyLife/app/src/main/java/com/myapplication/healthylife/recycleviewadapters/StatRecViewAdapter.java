package com.myapplication.healthylife.recycleviewadapters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.myapplication.healthylife.R;
import com.myapplication.healthylife.databinding.ExerciseListBinding;
import com.myapplication.healthylife.databinding.StatListBinding;
import com.myapplication.healthylife.local.DatabaseHelper;
import com.myapplication.healthylife.model.Exercise;
import com.myapplication.healthylife.model.Stat;

import java.io.Serializable;
import java.util.ArrayList;

public class StatRecViewAdapter extends RecyclerView.Adapter<StatRecViewAdapter.ViewHolder> {
    private ArrayList<Stat> stats = new ArrayList<>();
    private DatabaseHelper db;
    private Context mContext;

    public StatRecViewAdapter(Context context) {
        this.db = new DatabaseHelper(context);
        this.mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(StatListBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(stats.get(position));
    }

    @Override
    public int getItemCount() {
        return stats.size();
    }

    public void setStat(ArrayList<Stat> stats) {
        this.stats = stats;
        notifyDataSetChanged();
    }

    protected class ViewHolder extends RecyclerView.ViewHolder{

        StatListBinding binding;

        public ViewHolder(@NonNull StatListBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }

        public void bind(Stat stat)   {
            binding.tvHeight.setText(String.valueOf(stat.getHeight())+" (cm)");
            binding.tvWeight.setText(String.valueOf(stat.getWeight())+" (kg)");
            binding.tvBmi.setText(String.valueOf(stat.getBmi())+" (kg/m2)");
            binding.tvDate.setText(stat.getDate());

            binding.ibDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setMessage("Do you want to delete this record?")
                            .setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    if (db.deleteStat(stat.getId()))    {
                                        stats.remove(stat);
                                        notifyItemRemoved(getAdapterPosition());
                                        notifyItemRangeChanged(getItemCount(), stats.size());
                                        Toast.makeText(mContext, "Deleted", Toast.LENGTH_SHORT).show();
                                    }else{
                                        Toast.makeText(mContext, "Error", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            })
                            .setPositiveButton("No", null)
                            .show();
                }
            });

            binding.parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(mContext, String.valueOf(stat.getId()), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


}
