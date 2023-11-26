package com.example.bankclient.interface_helper;

import android.view.View;

import com.example.bankclient.model.IncomeExpense;

import java.util.ArrayList;

public interface ItemClickListener {
    void onItemClick(ArrayList<IncomeExpense> al);
}
