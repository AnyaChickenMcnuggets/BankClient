package com.example.bankclient.ui.recycler_view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bankclient.R;
import com.example.bankclient.util.interface_helper.ItemClickListener;
import com.example.bankclient.ui.models.IncomeExpense;
import com.example.bankclient.ui.viewmodel.CheckIEViewModel;

import java.util.ArrayList;

public class CheckIEAdapter extends RecyclerView.Adapter<CheckIEAdapter.ViewHolder> {
    private ItemClickListener itemClickListener;
    private Context context;
    private ArrayList<IncomeExpense> ieArrayList, checked;
    CheckIEViewModel viewModel;
    public CheckIEAdapter(Context context, ArrayList<IncomeExpense> ie, CheckIEViewModel vm, ItemClickListener il) {
        this.context = context;
        this.ieArrayList = ie;
        this.viewModel = vm;
        this.checked = viewModel.getList().getValue();
        this.itemClickListener = il;
    }

    @NonNull
    @Override
    public CheckIEAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.check_ie_item_rel, parent, false);

        return new CheckIEAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CheckIEAdapter.ViewHolder holder, int position) {
        holder.titleView.setText(String.valueOf(ieArrayList.get(position).getTitle()));
        if (ieArrayList.get(position).getIncome()){
            holder.sumView.setText(String.valueOf(ieArrayList.get(position).getSum()));
        }else {
            holder.sumView.setText("-"+String.valueOf(ieArrayList.get(position).getSum()));
        }

        if (checked.stream().anyMatch(o -> o.getId().equals(ieArrayList.get(position).getId()))){
            holder.checkBox.setChecked(true);
        }

        holder.itemView.setOnClickListener(view -> {
            holder.checkBox.toggle();

        });
        holder.checkBox.setOnCheckedChangeListener((compoundButton, b) -> {
            if (holder.checkBox.isChecked()){
                checked.add(ieArrayList.get(position));
                itemClickListener.onItemClick(checked);
            }else {
                if (checked.stream().anyMatch(o -> o.getId().equals(ieArrayList.get(position).getId()))){
                    checked.remove(checked.stream().filter(o -> o.getId().equals(ieArrayList.get(position).getId())).findFirst().get());
                    itemClickListener.onItemClick(checked);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return ieArrayList != null ? ieArrayList.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        LinearLayout linearLayout;
        TextView titleView, sumView;
        CheckBox checkBox;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleView = itemView.findViewById(R.id.ie_title);
            sumView = itemView.findViewById(R.id.ie_sum);
            checkBox = itemView.findViewById(R.id.ie_check);
            linearLayout = itemView.findViewById(R.id.item_layout);
        }
    }
}
