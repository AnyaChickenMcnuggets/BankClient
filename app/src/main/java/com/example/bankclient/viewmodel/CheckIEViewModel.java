package com.example.bankclient.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bankclient.model.IncomeExpense;

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
