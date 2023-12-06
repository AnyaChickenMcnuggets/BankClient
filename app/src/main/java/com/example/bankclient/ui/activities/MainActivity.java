package com.example.bankclient.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.bankclient.R;
import com.example.bankclient.repository.DatabaseHelper;

public class MainActivity extends AppCompatActivity {
    DatabaseHelper db;
    TextView textViewPlansBigStatus;
    Button planButton;
    ImageButton incomeButton, expenseButton, productsButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        planButton = findViewById(R.id.buttonPlansBig);
        planButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, PlanListActivity.class);
            startActivity(intent);
        });
        incomeButton = findViewById(R.id.incomeButton);
        incomeButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, IncomeActivity.class);
            startActivity(intent);
        });
        expenseButton = findViewById(R.id.expenseButton);
        expenseButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, ExpenseActivity.class);
            startActivity(intent);
        });
        productsButton = findViewById(R.id.productsButton);
        productsButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, BankProductListActivity.class);
            startActivity(intent);
        });
        db = new DatabaseHelper(MainActivity.this);
//        db.addStartProduct("Вклад \"Максимальный\"", "24", "13%", "true");
//        db.addStartProduct("Вклад \"Накопительный+\"", "13", "9%", "true");
//        db.addStartProduct("Кредит \"Льготный\"", "12", "24%", "false");
//        db.addStartProduct("Кредит \"для ИТ\"", "60", "22%", "false");
        textViewPlansBigStatus = findViewById(R.id.textViewPlansBigStatus);
        textViewPlansBigStatus.setText(String.valueOf(db.countAllPlans()));
    }
    @Override
    public void onRestart()
    {
        super.onRestart();
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);
    }


}