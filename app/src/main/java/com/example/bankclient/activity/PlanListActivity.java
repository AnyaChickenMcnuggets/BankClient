package com.example.bankclient.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bankclient.R;
import com.example.bankclient.adapter.PlanAdapter;
import com.example.bankclient.database.DatabaseHelper;
import com.example.bankclient.model.Plan;

import java.util.ArrayList;

public class PlanListActivity extends AppCompatActivity {
    ListView listPlan;
    TextView addPlan;

    DatabaseHelper db;
    ArrayList<String> plan_id, plan_title, plan_date, plan_status, plan_response;
    ArrayList<Plan> arr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_list);
        addPlan = findViewById(R.id.add_plan);
        listPlan = findViewById(R.id.listPlan);

        db = new DatabaseHelper(PlanListActivity.this);
        arr = new ArrayList<>();
        storeDataInArrays();

        PlanAdapter adapter = new PlanAdapter(this, 0, arr);
        listPlan.setAdapter(adapter);
        listPlan.setOnItemClickListener((parent, view, position, id) -> showPopUp());
        addPlan.setOnClickListener(view -> {
            Intent intent = new Intent(PlanListActivity.this, AddActivity.class);
            startActivity(intent);
        });

    }

    void storeDataInArrays(){
        Cursor cursor = db.readAllPlans();
        if (cursor.getCount()==0){
            Toast.makeText(PlanListActivity.this, "No Data", Toast.LENGTH_SHORT).show();
        }else {
            while (cursor.moveToNext()){
                arr.add(new Plan(
                        cursor.getString(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4)));
            }
        }
    }
    private void showPopUp() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottom_dialog);

        LinearLayout editLayout = dialog.findViewById(R.id.layoutEdit);
        LinearLayout shareLayout = dialog.findViewById(R.id.layoutShare);
        LinearLayout deleteLayout = dialog.findViewById(R.id.layoutDelete);
        LinearLayout updateLayout = dialog.findViewById(R.id.layoutUpdate);

        editLayout.setOnClickListener(v -> Toast.makeText(PlanListActivity.this, "Edit", Toast.LENGTH_SHORT).show());
        shareLayout.setOnClickListener(v -> Toast.makeText(PlanListActivity.this, "Share", Toast.LENGTH_SHORT).show());
        deleteLayout.setOnClickListener(v -> Toast.makeText(PlanListActivity.this, "Delete", Toast.LENGTH_SHORT).show());
        updateLayout.setOnClickListener(v -> Toast.makeText(PlanListActivity.this, "Update", Toast.LENGTH_SHORT).show());
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

}