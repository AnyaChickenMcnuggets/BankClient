package com.example.bankclient.ui.activities;

import static com.example.bankclient.util.solution.PlotGenerator.createMatrix;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.bankclient.R;
import com.example.bankclient.api.API;
import com.example.bankclient.api.RetrofitService;
import com.example.bankclient.api.SocketConnection;
import com.example.bankclient.repository.DatabaseHelper;
import com.example.bankclient.ui.models.IncomeExpense;
import com.example.bankclient.ui.models.Plan;
import com.example.bankclient.ui.models.PostData;
import com.example.bankclient.ui.models.UsedBankProduct;
import com.example.bankclient.util.solution.PlotGenerator;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class AddPlanActivity extends AppCompatActivity {
    Plan plan;
    API api;
    ImageButton addIE, addMyBank;
    String[] ids;
    Button addButton;
    EditText editSum, editTitle;
    ArrayList<UsedBankProduct> addProduct;
    ActivityResultLauncher<Intent> getResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult o) {
            if (o!=null && o.getResultCode()==RESULT_OK){
                if (o.getData()!=null && o.getData().getStringArrayExtra("checked")!=null){
                    ids = o.getData().getStringArrayExtra("checked");
                }
                if (o.getData()!=null && o.getData().getParcelableExtra("product")!=null){
                    addProduct.add(o.getData().getParcelableExtra("product"));
                }
            }
        }
    });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_plan);
        addMyBank = findViewById(R.id.addMyBank);
        addIE = findViewById(R.id.addShortIncome);
        addProduct = new ArrayList<>();
        addIE.setOnClickListener(view -> {
            Intent intent = new Intent(AddPlanActivity.this, SelectIncomeExpenseActivity.class);
            if (ids != null)
                intent.putExtra("checked", ids);
            getResult.launch(intent);
        });
        addMyBank.setOnClickListener(view -> {
            Intent intent = new Intent(AddPlanActivity.this, AddBankProductActivity.class);
            getResult.launch(intent);
        });
        addButton = findViewById(R.id.addButton);
        editTitle = findViewById(R.id.editTitle);
        editSum = findViewById(R.id.editSum);
        addButton.setOnClickListener(view -> {
            DatabaseHelper db = new DatabaseHelper(AddPlanActivity.this);
            ArrayList<IncomeExpense> ies = PlotGenerator.getIEFromIds(AddPlanActivity.this, ids);
            int startSum = Integer.valueOf(editSum.getText().toString().trim());
            LocalDate dateNow = LocalDateTime.now().toLocalDate().with(TemporalAdjusters.firstDayOfNextMonth());
            String plot = PlotGenerator.generatePlot(ies, addProduct, startSum, dateNow);
            long plan_id = db.addPlan(
                    editTitle.getText().toString().trim(),
                    dateNow.toString().trim(),
                    editSum.getText().toString().trim(),
                    ids,
                    addProduct,
                    "problem",
                    "no response",
                    plot);

            Double[][] matrix = PlotGenerator.createMatrix(plot, dateNow);
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
                        Toast.makeText(AddPlanActivity.this, response.code(), Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    String stringResponse = "";
                    try {
                        JSONObject jsonObject = new JSONObject(response.body());
                        stringResponse = jsonObject.get("text").toString();
                        String solution = PlotGenerator.updatePlot(stringResponse, plot, dateNow);

                        Cursor cursor = db.getPlanById(String.valueOf(plan_id));
                        if (cursor.getCount()==0){
                            Toast.makeText(AddPlanActivity.this, "No Data", Toast.LENGTH_SHORT).show();
                        }else {
                            while (cursor.moveToNext()){
                                plan = new Plan(
                                        cursor.getString(0),
                                        cursor.getString(1),
                                        cursor.getString(2),
                                        cursor.getString(3),
                                        cursor.getString(4),
                                        cursor.getString(5),
                                        cursor.getString(6),
                                        cursor.getString(7));
                            }
                        }
                        db.addSolutionById(plan, solution, String.valueOf(plan_id));
                    }catch (JSONException err){
                        Toast.makeText(AddPlanActivity.this, "JSONError", Toast.LENGTH_SHORT).show();
                    }
                    Toast.makeText(AddPlanActivity.this, "результат уже получен", Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Toast.makeText(AddPlanActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            finish();
        });
    }


}