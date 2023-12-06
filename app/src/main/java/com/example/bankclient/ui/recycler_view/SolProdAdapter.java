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
import com.example.bankclient.ui.models.BankProduct;
import com.example.bankclient.ui.models.ProductSolution;
import com.example.bankclient.util.interface_helper.RecyclerViewInterface;

import java.util.ArrayList;

public class SolProdAdapter extends RecyclerView.Adapter<SolProdAdapter.ViewHolder>{
    private final RecyclerViewInterface recyclerViewInterface;
    private Context context;
    private ArrayList<ProductSolution> psArrayList;
    public SolProdAdapter(Context context, ArrayList<ProductSolution> ps, RecyclerViewInterface recyclerViewInterface) {
        this.recyclerViewInterface = recyclerViewInterface;
        this.context = context;
        this.psArrayList = ps;
    }

    @NonNull
    @Override
    public SolProdAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sp_item_rel, parent, false);

        return new SolProdAdapter.ViewHolder(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull SolProdAdapter.ViewHolder holder, int position) {
        holder.titleView.setText(String.valueOf(psArrayList.get(position).getTitle()));
        holder.sumView.setText(String.valueOf(psArrayList.get(position).getSum()));
        holder.dateView.setText(String.valueOf(psArrayList.get(position).getDate()));

    }

    @Override
    public int getItemCount() {
        return psArrayList != null ? psArrayList.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout linearLayout;
        TextView titleView, dateView, sumView;
        public ViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            titleView = itemView.findViewById(R.id.title);
            dateView = itemView.findViewById(R.id.date);
            sumView = itemView.findViewById(R.id.sum);
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
