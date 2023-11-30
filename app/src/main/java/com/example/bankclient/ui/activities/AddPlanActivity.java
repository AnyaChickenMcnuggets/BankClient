package com.example.bankclient.ui.activities;

import static com.example.bankclient.util.solution.PlotGenerator.createMatrix;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.bankclient.R;
import com.example.bankclient.repository.DatabaseHelper;
import com.example.bankclient.ui.models.IncomeExpense;
import com.example.bankclient.ui.models.UsedBankProduct;
import com.example.bankclient.util.solution.PlotGenerator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;

public class AddPlanActivity extends AppCompatActivity {

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
            db.addPlan(
                    editTitle.getText().toString().trim(),
                    dateNow.toString().trim(),
                    editSum.getText().toString().trim(),
                    ids,
                    addProduct,
                    "problem",
                    "no response",
                    plot);

            PlotGenerator.createMatrix(plot, dateNow);
            finish();
        });
    }


}