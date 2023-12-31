package com.example.bankclient.ui.recycler_view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bankclient.R;
import com.example.bankclient.util.interface_helper.RecyclerViewInterface;
import com.example.bankclient.ui.models.IncomeExpense;

import java.util.ArrayList;

public class IEAdapter extends RecyclerView.Adapter<IEAdapter.ViewHolder> {
    private final RecyclerViewInterface recyclerViewInterface;
    private Context context;
    private ArrayList<IncomeExpense> ieArrayList;
    public IEAdapter(Context context, ArrayList<IncomeExpense> ie, RecyclerViewInterface recyclerViewInterface) {
        this.recyclerViewInterface = recyclerViewInterface;
        this.context = context;
        this.ieArrayList = ie;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.ie_item_rel, parent, false);

        return new ViewHolder(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull IEAdapter.ViewHolder holder, int position) {
        holder.titleView.setText(String.valueOf(ieArrayList.get(position).getTitle()));
        if (ieArrayList.get(position).getIncome()){
            holder.sumView.setText(String.valueOf(ieArrayList.get(position).getSum()));
        }else {
            holder.sumView.setText("-"+String.valueOf(ieArrayList.get(position).getSum()));
        }
    }

    @Override
    public int getItemCount() {
        return ieArrayList != null ? ieArrayList.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout linearLayout;
        TextView titleView, sumView;
        public ViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            titleView = itemView.findViewById(R.id.ie_title);
            sumView = itemView.findViewById(R.id.ie_sum);

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
