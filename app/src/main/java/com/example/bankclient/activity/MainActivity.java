package com.example.bankclient.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.bankclient.R;
import com.example.bankclient.adapter.PlanAdapter;
import com.example.bankclient.model.Plan;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    Button planButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        planButton = findViewById(R.id.buttonPlansBig);
        planButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, PlanListActivity.class);
            startActivity(intent);
        });

    }



}