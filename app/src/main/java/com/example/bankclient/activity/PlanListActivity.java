package com.example.bankclient.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bankclient.R;
import com.example.bankclient.adapter.PlanAdapter;
import com.example.bankclient.database.DatabaseHelper;
import com.example.bankclient.model.Plan;

import java.util.ArrayList;

public class PlanListActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    TextView addPlan;
    TextView planCount;

    DatabaseHelper db;
    ArrayList<String> plan_id, plan_title, plan_date, plan_status, plan_response;
    ArrayList<Plan> planArrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_list);
        addPlan = findViewById(R.id.add_plan);
        recyclerView = findViewById(R.id.listPlan);

        db = new DatabaseHelper(PlanListActivity.this);
        planArrayList = new ArrayList<>();
        storeDataInArray();

        PlanAdapter adapter = new PlanAdapter(PlanListActivity.this, planArrayList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(PlanListActivity.this));
        planCount = findViewById(R.id.plan_count);
        // TODO обращаться к бд за ответами
        planCount.setText(String.valueOf(recyclerView.getAdapter().getItemCount()));
        addPlan.setOnClickListener(view -> {
            Intent intent = new Intent(PlanListActivity.this, AddActivity.class);
            startActivity(intent);
        });

    }
    @Override
    public void onRestart()
    {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }

    void storeDataInArray(){
        Cursor cursor = db.readAllPlans();
        if (cursor.getCount()==0){
            Toast.makeText(PlanListActivity.this, "No Data", Toast.LENGTH_SHORT).show();
        }else {
            while (cursor.moveToNext()){
                planArrayList.add(new Plan(
                        cursor.getString(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4)));
            }

        }
    }

}