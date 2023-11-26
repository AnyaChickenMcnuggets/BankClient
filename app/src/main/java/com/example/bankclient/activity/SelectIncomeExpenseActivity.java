package com.example.bankclient.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.example.bankclient.R;
import com.example.bankclient.fragment.CheckLongExpenseFragment;
import com.example.bankclient.fragment.CheckLongIncomeFragment;
import com.example.bankclient.fragment.CheckShortExpenseFragment;
import com.example.bankclient.fragment.CheckShortIncomeFragment;
import com.example.bankclient.fragment.LongExpenseFragment;
import com.example.bankclient.fragment.LongIncomeFragment;
import com.example.bankclient.fragment.ShortIncomeFragment;
import com.example.bankclient.model.IncomeExpense;
import com.example.bankclient.viewmodel.CheckIEViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SelectIncomeExpenseActivity extends AppCompatActivity {


    Button submit, getLongIncome, getShortIncome, getLongExpense, getShortExpense;
    private CheckIEViewModel viewModel;
    public SelectIncomeExpenseActivity() {
        super(R.layout.activity_select_income_expense);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.fragmentContainerView, CheckLongIncomeFragment.class, null)
                    .commit();
        }
        viewModel = new ViewModelProvider(this).get(CheckIEViewModel.class);
        if (viewModel.getList().getValue() == null)
            viewModel.setData(new ArrayList<>());
        submit = findViewById(R.id.submit);
        getShortExpense = findViewById(R.id.getShortExpense);
        getLongExpense = findViewById(R.id.getLongExpense);
        getShortIncome = findViewById(R.id.getShortIncome);
        getLongIncome = findViewById(R.id.getLongIncome);

        submit.setOnClickListener(view -> {
            Intent intent = new Intent();
            List<String> ids = viewModel.getList().getValue().stream()
                    .map(IncomeExpense::getId)
                    .collect(Collectors.toList());
            String[] sid = new String[ids.size()];
            for (int i = 0; i < ids.size(); i++) sid[i] = ids.get(i);
            intent.putExtra("checked", sid);
            setResult(RESULT_OK, intent);
            finish();
        });

        getShortExpense.setOnClickListener(view -> {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainerView, CheckShortExpenseFragment.class, null)
                    .setReorderingAllowed(true)
                    .addToBackStack("name1")
                    .commit();
        });
        getLongExpense.setOnClickListener(view -> {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainerView, CheckLongExpenseFragment.class, null)
                    .setReorderingAllowed(true)
                    .addToBackStack("name1")
                    .commit();
        });
        getShortIncome.setOnClickListener(view -> {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainerView, CheckShortIncomeFragment.class, null)
                    .setReorderingAllowed(true)
                    .addToBackStack("name1")
                    .commit();
        });
        getLongIncome.setOnClickListener(view -> {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainerView, CheckLongIncomeFragment.class, null)
                    .setReorderingAllowed(true)
                    .addToBackStack("name1")
                    .commit();
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_CANCEL) this.finish();
        return super.onTouchEvent(event);
    }
}