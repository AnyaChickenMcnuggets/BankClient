package com.example.bankclient.ui.activities;

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
import com.example.bankclient.repository.DatabaseHelper;
import com.example.bankclient.ui.models.IncomeExpense;
import com.example.bankclient.ui.models.UsedBankProduct;

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
            createMatrix(addProduct, ids, editSum.getText().toString().trim());
            finish();
        });
    }

    public Double[][] createMatrix(ArrayList<UsedBankProduct> products, String[] incomeExpenseIds, String nal){
        Double[][] matrix;
        int n,m;
        DatabaseHelper db = new DatabaseHelper(AddPlanActivity.this);
        ArrayList<IncomeExpense> ies = new ArrayList<>();
        for (String id : incomeExpenseIds) {
            Cursor cursor = db.readIEById(id);
            if (cursor.getCount()==0){
                Toast.makeText(AddPlanActivity.this, "ОШИБКА НЕТ ТАКОГО ID", Toast.LENGTH_SHORT).show();
            }else {
                while (cursor.moveToNext()){
                    ies.add(new IncomeExpense(
                            cursor.getString(0),
                            cursor.getString(1),
                            cursor.getString(2),
                            cursor.getString(3),
                            Boolean.valueOf(cursor.getString(4)),
                            Boolean.valueOf(cursor.getString(5)),
                            cursor.getString(6)));
                }

            }
        }

        n = (int) products.stream().filter(g -> g.getIncome()).count() * 12 + 24 * (int) products.stream().filter(g -> !g.getIncome()).count() + 1;
        m =  products.stream().anyMatch(g -> !g.getIncome()) ? 13 + (int) products.stream().filter(g -> !g.getIncome()).count() * 23 : 13 ;
        matrix = new Double[n][m];
        for (UsedBankProduct product : products) {
            if (product.getIncome()){

            }else {

            }
        }

        Double incomeExpenseSum = Double.valueOf(nal);

        for (IncomeExpense incomeExpense : ies) {
            if (incomeExpense.getLong()){
                if (incomeExpense.getIncome())
                    incomeExpenseSum += Double.valueOf(incomeExpense.getSum().split("\\.")[0].replaceAll(",", ""));
                else
                    incomeExpenseSum -= Double.valueOf(incomeExpense.getSum().split("\\.")[0].replaceAll(",", ""));
            }
        }

        // первые 12 по месяцам 100%
        for (int i = 0; i < 12; i++){
            int a = i;
            matrix[0][i] = incomeExpenseSum * (i+1) + ies.stream().filter(g -> !g.getLong() && g.getIncome() && Integer.parseInt(g.getDate().split("\\.")[1]) == a).mapToDouble(o -> Double.valueOf(o.getSum())).sum()
                    + ies.stream().filter(g -> !g.getLong() && !g.getIncome() && Integer.parseInt(g.getDate().split("\\.")[1]) == a).mapToDouble(o -> Double.valueOf(o.getSum())).sum();
        }

        return matrix;
    }
}