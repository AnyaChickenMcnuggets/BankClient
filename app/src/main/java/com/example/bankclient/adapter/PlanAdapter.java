package com.example.bankclient.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bankclient.R;
import com.example.bankclient.activity.PlanListActivity;
import com.example.bankclient.model.Plan;

import java.util.ArrayList;
import java.util.List;

public class PlanAdapter extends RecyclerView.Adapter<PlanAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Plan> planArrayList;
    private int planArrayListSize;
    public PlanAdapter(Context context, ArrayList<Plan> plans) {
        this.context = context;
        this.planArrayList = plans;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.plan_item_rel, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.titleView.setText(String.valueOf(planArrayList.get(position).getTitle()));
        holder.dateView.setText(String.valueOf(planArrayList.get(position).getDate()));

        holder.statusView.setImageResource(R.drawable.ic_warning);
        holder.responseView.setImageResource(R.drawable.ic_no_data);
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopUp(context);
            }
        });
    }

    @Override
    public int getItemCount() {
        return planArrayList != null ? planArrayList.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout linearLayout;
        TextView titleView, dateView, planCount;
        ImageView statusView, responseView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleView = itemView.findViewById(R.id.plan_title);
            dateView = itemView.findViewById(R.id.plan_date);

            statusView = itemView.findViewById(R.id.plan_status);
            responseView = itemView.findViewById(R.id.plan_response);
            linearLayout = itemView.findViewById(R.id.item_layout);
        }
    }

    private void showPopUp(Context context) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottom_dialog);

        LinearLayout editLayout = dialog.findViewById(R.id.layoutEdit);
        LinearLayout shareLayout = dialog.findViewById(R.id.layoutShare);
        LinearLayout deleteLayout = dialog.findViewById(R.id.layoutDelete);
        LinearLayout updateLayout = dialog.findViewById(R.id.layoutUpdate);

        editLayout.setOnClickListener(v -> Toast.makeText(context, "Edit", Toast.LENGTH_SHORT).show());
        shareLayout.setOnClickListener(v -> Toast.makeText(context, "Share", Toast.LENGTH_SHORT).show());
        deleteLayout.setOnClickListener(v -> Toast.makeText(context, "Delete", Toast.LENGTH_SHORT).show());
        updateLayout.setOnClickListener(v -> Toast.makeText(context, "Update", Toast.LENGTH_SHORT).show());
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }
}
