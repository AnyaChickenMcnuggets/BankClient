package com.example.bankclient.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
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
import com.example.bankclient.ui.recycler_view.PlanAdapter;
import com.example.bankclient.repository.DatabaseHelper;
import com.example.bankclient.util.interface_helper.RecyclerViewInterface;
import com.example.bankclient.ui.models.Plan;

import java.util.ArrayList;

public class PlanListActivity extends AppCompatActivity implements RecyclerViewInterface {
    RecyclerView recyclerView;
    TextView addPlan;
    TextView planCount, notUpdatedPlanCount;

    DatabaseHelper db;
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

        PlanAdapter adapter = new PlanAdapter(PlanListActivity.this, planArrayList, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(PlanListActivity.this));

        planCount = findViewById(R.id.plan_count);
        planCount.setText(String.valueOf(db.countAllPlans()));

        notUpdatedPlanCount = findViewById(R.id.not_updated_plan_count);
        notUpdatedPlanCount.setText(String.valueOf(db.countNotUpdatedPlans()));

        addPlan.setOnClickListener(view -> {
            Intent intent = new Intent(PlanListActivity.this, AddPlanActivity.class);
            startActivity(intent);
        });

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

    @Override
    public void onItemClick(int position) {
        showPopUp(this, planArrayList.get(position));
    }

    private void showPopUp(Context context, Plan plan) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottom_dialog);

        LinearLayout editLayout = dialog.findViewById(R.id.layoutEdit);
        LinearLayout peekLayout = dialog.findViewById(R.id.layoutPeek);
        LinearLayout deleteLayout = dialog.findViewById(R.id.layoutDelete);
        LinearLayout updateLayout = dialog.findViewById(R.id.layoutUpdate);

        editLayout.setOnClickListener(v -> Toast.makeText(dialog.getContext(), "Изменить", Toast.LENGTH_SHORT).show());

        peekLayout.setOnClickListener(v -> Toast.makeText(dialog.getContext(), "Посмотреть", Toast.LENGTH_SHORT).show());

        deleteLayout.setOnClickListener(v -> {
            DatabaseHelper db = new DatabaseHelper(dialog.getContext());
            dialog.dismiss();
            db.deleteOnePlan(plan.getId());
            onRestart();
        });

        updateLayout.setOnClickListener(v -> Toast.makeText(dialog.getContext(), "Обновить", Toast.LENGTH_SHORT).show());

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }
}