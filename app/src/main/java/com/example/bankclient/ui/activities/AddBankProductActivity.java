package com.example.bankclient.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bankclient.R;
import com.example.bankclient.repository.DatabaseHelper;
import com.example.bankclient.ui.models.BankProduct;
import com.example.bankclient.ui.models.UsedBankProduct;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AddBankProductActivity extends AppCompatActivity {
    String[] products;
    AutoCompleteTextView autoCompleteTextView;
    ArrayAdapter<String> arrayAdapter;
    ArrayList<BankProduct> bankProducts;
    DatabaseHelper db;
    BankProduct currentProduct;
    TextView date, percentage;
    EditText sum, startDate;
    Button submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bank_product);
        db = new DatabaseHelper(AddBankProductActivity.this);
        bankProducts = new ArrayList<>();
        storeDataInArray();
        List<String> titles = bankProducts.stream()
                .map(BankProduct::getTitle)
                .collect(Collectors.toList());
        products = new String[titles.size()];
        for (int i = 0; i < titles.size(); i++) products[i] = titles.get(i);

        autoCompleteTextView = findViewById(R.id.bp_autoComplete);
        date = findViewById(R.id.date);

        sum = findViewById(R.id.sum);
        startDate = findViewById(R.id.startDate);

        percentage = findViewById(R.id.percentage);

        submit = findViewById(R.id.addButton);


        arrayAdapter = new ArrayAdapter<String>(this, R.layout.bp_dropdown_item, products);
        autoCompleteTextView.setAdapter(arrayAdapter);
        autoCompleteTextView.setOnItemClickListener((adapterView, view, i, l) -> {
            String item = adapterView.getItemAtPosition(i).toString();
            Cursor cursor = db.readBankByTitle(item);
            if (cursor.getCount()==0){
                Toast.makeText(this, "No Data", Toast.LENGTH_SHORT).show();
            }else {
                while (cursor.moveToNext()){
                    currentProduct = new BankProduct(
                            cursor.getString(0),
                            cursor.getString(1),
                            cursor.getString(2),
                            cursor.getString(3),
                            Boolean.valueOf(cursor.getString(4)));
                    break;
                }
            }
            date.setText(currentProduct.getTime());
            percentage.setText(currentProduct.getPercentage());
        });

        submit.setOnClickListener(view -> {
            Intent intent = new Intent();
            UsedBankProduct addProduct = new UsedBankProduct(
                    currentProduct,
                    String.valueOf(sum.getText()),
                    String.valueOf(startDate.getText()));
            intent.putExtra("product", addProduct);
            setResult(RESULT_OK, intent);
            finish();
        });
    }

    void storeDataInArray(){
        Cursor cursor = db.readAllBank();
        if (cursor.getCount()==0){
            Toast.makeText(this, "No Data", Toast.LENGTH_SHORT).show();
        }else {
            while (cursor.moveToNext()){
                bankProducts.add(new BankProduct(
                        cursor.getString(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        Boolean.valueOf(cursor.getString(4))));
            }

        }
    }
}