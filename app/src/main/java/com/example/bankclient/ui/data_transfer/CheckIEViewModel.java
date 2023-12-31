package com.example.bankclient.ui.data_transfer;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bankclient.ui.models.IncomeExpense;

import java.util.ArrayList;

public class CheckIEViewModel extends ViewModel {
    private final MutableLiveData<ArrayList<IncomeExpense>> list = new MutableLiveData<>();

    public void setData(ArrayList<IncomeExpense> ie){
        list.setValue(ie);

    }
    public MutableLiveData<ArrayList<IncomeExpense>> getList() {
        return list;
    }
}
