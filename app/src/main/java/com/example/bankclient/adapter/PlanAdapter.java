package com.example.bankclient.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.bankclient.R;
import com.example.bankclient.model.Plan;

import java.util.ArrayList;
import java.util.List;

public class PlanAdapter extends ArrayAdapter<Plan> {
    private Context context;
    private ArrayList<Plan> planArrayList;
    public PlanAdapter(@NonNull Context context, int resource, @NonNull List<Plan> objects) {
        super(context, resource, objects);
        this.context = context;
        this.planArrayList = new ArrayList<>(objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null){
            LayoutInflater i = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = i.inflate(R.layout.plan_item_rel, null);
        }
        if (planArrayList.size()>0){
            Plan p = planArrayList.get(position);
        }
        return convertView;
    }
}
