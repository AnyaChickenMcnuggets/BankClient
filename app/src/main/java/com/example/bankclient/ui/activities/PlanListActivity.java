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
import com.example.bankclient.api.API;
import com.example.bankclient.ui.models.PostData;
import com.example.bankclient.ui.recycler_view.PlanAdapter;
import com.example.bankclient.repository.DatabaseHelper;
import com.example.bankclient.util.interface_helper.RecyclerViewInterface;
import com.example.bankclient.ui.models.Plan;
import com.example.bankclient.util.solution.PlotGenerator;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class PlanListActivity extends AppCompatActivity implements RecyclerViewInterface {
    RecyclerView recyclerView;
    TextView addPlan;
    TextView planCount, notUpdatedPlanCount;
    API api;
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
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6),
                        cursor.getString(7),
                        cursor.getString(8)));
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

        peekLayout.setOnClickListener(v -> {
            Intent intent = new Intent(PlanListActivity.this, WatchSolutionActivity.class);
            intent.putExtra("plan_id", plan.getId());
            startActivity(intent);
        });

        deleteLayout.setOnClickListener(v -> {
            DatabaseHelper db = new DatabaseHelper(dialog.getContext());
            dialog.dismiss();
            db.deleteOnePlan(plan.getId());
            onRestart();
        });

        updateLayout.setOnClickListener(v -> {
            db.addWaitingById(plan, String.valueOf(plan.getId()));
            LocalDate dateNow = LocalDateTime.now().toLocalDate().with(TemporalAdjusters.firstDayOfNextMonth());
            Double[][] matrix = PlotGenerator.createMatrix(plan.getPlot(), dateNow);
            String matrixStr = PlotGenerator.generateStringFromMatrix(matrix);
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://192.168.1.5:5050")
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            api = retrofit.create(API.class);
            Call<String> call = api.addMatrix(new PostData(matrixStr,String.valueOf(matrix.length), String.valueOf(matrix[0].length)));
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (!response.isSuccessful()){
                        db.addNoResponseById(plan, String.valueOf(plan.getId()));
                        Toast.makeText(PlanListActivity.this, response.code(), Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    String stringResponse = "";
                    try {
                        JSONObject jsonObject = new JSONObject(response.body());
                        stringResponse = jsonObject.get("text").toString();
                        String solution = PlotGenerator.updatePlot(stringResponse, plan.getPlot(), dateNow);
                        String productSolution = PlotGenerator.updateProductSolution(stringResponse, dateNow);
                        db.addSolutionById(plan, solution, String.valueOf(plan.getId()), productSolution);
                    }catch (JSONException err){
                        Toast.makeText(PlanListActivity.this, "JSONError", Toast.LENGTH_SHORT).show();
                    }
                    onRestart();
                    Toast.makeText(PlanListActivity.this, "Обновлено", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    db.addNoResponseById(plan, String.valueOf(plan.getId()));
                    onRestart();
                    Toast.makeText(PlanListActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            dialog.dismiss();
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }
}