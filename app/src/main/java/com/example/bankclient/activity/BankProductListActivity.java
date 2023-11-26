package com.example.bankclient.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.example.bankclient.R;
import com.example.bankclient.fragment.BankProductExpenseFragment;
import com.example.bankclient.fragment.BankProductIncomeFragment;
import com.example.bankclient.fragment.LongIncomeFragment;
import com.example.bankclient.fragment.ShortIncomeFragment;

public class BankProductListActivity extends AppCompatActivity {

    Button getExpense, getIncome;
    public BankProductListActivity() {
        super(R.layout.activity_bank_product_list);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.fragmentContainerView, BankProductExpenseFragment.class, null)
                    .commit();
        }
        getExpense = findViewById(R.id.getExpense);
        getIncome = findViewById(R.id.getIncome);
        getExpense.setOnClickListener(view -> {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainerView, BankProductExpenseFragment.class, null)
                    .setReorderingAllowed(true)
                    .addToBackStack("name1")
                    .commit();
        });
        getIncome.setOnClickListener(view -> {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainerView, BankProductIncomeFragment.class, null)
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