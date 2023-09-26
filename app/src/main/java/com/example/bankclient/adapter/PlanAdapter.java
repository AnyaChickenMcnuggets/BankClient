package com.example.bankclient.adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bankclient.R;
import com.example.bankclient.database.DatabaseHelper;
import com.example.bankclient.interface_helper.RecyclerViewInterface;
import com.example.bankclient.model.Plan;

import java.util.ArrayList;

public class PlanAdapter extends RecyclerView.Adapter<PlanAdapter.ViewHolder> {
    private final RecyclerViewInterface recyclerViewInterface;
    private Context context;
    private ArrayList<Plan> planArrayList;
    public PlanAdapter(Context context, ArrayList<Plan> plans, RecyclerViewInterface recyclerViewInterface) {
        this.recyclerViewInterface = recyclerViewInterface;
        this.context = context;
        this.planArrayList = plans;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.plan_item_rel, parent, false);

        return new ViewHolder(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.titleView.setText(String.valueOf(planArrayList.get(position).getTitle()));
        holder.dateView.setText(String.valueOf(planArrayList.get(position).getDate()));

        holder.statusView.setImageResource(R.drawable.ic_warning);
        holder.responseView.setImageResource(R.drawable.ic_no_data);
    }

    @Override
    public int getItemCount() {
        return planArrayList != null ? planArrayList.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout linearLayout;
        TextView titleView, dateView;
        ImageView statusView, responseView;
        public ViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            titleView = itemView.findViewById(R.id.plan_title);
            dateView = itemView.findViewById(R.id.plan_date);

            statusView = itemView.findViewById(R.id.plan_status);
            responseView = itemView.findViewById(R.id.plan_response);
            linearLayout = itemView.findViewById(R.id.item_layout);

            itemView.setOnClickListener(view -> {
                if (recyclerViewInterface!=null){
                    int pos = getAdapterPosition();
                    if (pos!=RecyclerView.NO_POSITION){
                        recyclerViewInterface.onItemClick(pos);
                    }
                }
            });
        }
    }

}
