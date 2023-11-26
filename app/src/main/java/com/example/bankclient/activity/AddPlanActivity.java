package com.example.bankclient.activity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.bankclient.R;
import com.example.bankclient.database.DatabaseHelper;
import com.example.bankclient.model.IncomeExpense;
import com.example.bankclient.model.UsedBankProduct;

import java.time.LocalDateTime;
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
            db.addPlan(
                    editTitle.getText().toString().trim(),
                    LocalDateTime.now().toLocalDate().toString().trim(),
                    editSum.getText().toString().trim(),
                    ids,
                    addProduct,
                    "problem",
                    "no response");
            finish();
        });
    }
}