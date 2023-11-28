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
import com.example.bankclient.ui.models.BankProduct;

import java.util.ArrayList;

public class BPAdapter extends RecyclerView.Adapter<BPAdapter.ViewHolder>{
    private final RecyclerViewInterface recyclerViewInterface;
    private Context context;
    private ArrayList<BankProduct> bpArrayList;
    public BPAdapter(Context context, ArrayList<BankProduct> bp, RecyclerViewInterface recyclerViewInterface) {
        this.recyclerViewInterface = recyclerViewInterface;
        this.context = context;
        this.bpArrayList = bp;
    }

    @NonNull
    @Override
    public BPAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.bp_item_rel, parent, false);

        return new BPAdapter.ViewHolder(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull BPAdapter.ViewHolder holder, int position) {
        holder.titleView.setText(String.valueOf(bpArrayList.get(position).getTitle()));
        holder.percentageView.setText(String.valueOf(bpArrayList.get(position).getPercentage()));
        holder.dateView.setText(String.valueOf(bpArrayList.get(position).getTime()));

    }

    @Override
    public int getItemCount() {
        return bpArrayList != null ? bpArrayList.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout linearLayout;
        TextView titleView, dateView, percentageView;
        public ViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            titleView = itemView.findViewById(R.id.bp_title);
            dateView = itemView.findViewById(R.id.bp_date);
            percentageView = itemView.findViewById(R.id.bp_percentage);
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
