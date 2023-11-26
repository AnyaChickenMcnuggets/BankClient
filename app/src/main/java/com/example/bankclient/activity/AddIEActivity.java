package com.example.bankclient.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.FragmentManager;

import android.database.Cursor;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bankclient.R;
import com.example.bankclient.database.DatabaseHelper;
import com.example.bankclient.model.BankProduct;
import com.example.bankclient.watcher.MoneyTextWatcher;
import com.google.android.material.textfield.TextInputLayout;

import java.text.DecimalFormat;
import java.time.LocalDateTime;

public class AddIEActivity extends AppCompatActivity {
    EditText titleText, sumText;
    SwitchCompat switchIE;
    CheckBox checkLong;
    Button save;
    AutoCompleteTextView autoComplete;
    ArrayAdapter<String> arrayAdapter;
    String[] periods = {"неделя","месяц","квартал","год"};
    String period = "";
    TextInputLayout textInputLayout;
    DatePicker dateText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ie);

        titleText = findViewById(R.id.editTitle);
        dateText = findViewById(R.id.simpleDatePicker);
        sumText = findViewById(R.id.editSum);
        switchIE = findViewById(R.id.isExpense);
        checkLong = findViewById(R.id.isLong);
        save = findViewById(R.id.save);
        autoComplete = findViewById(R.id.autoComplete);
        sumText.setInputType(InputType.TYPE_CLASS_NUMBER);
        textInputLayout = findViewById(R.id.textInputLayout);


        arrayAdapter = new ArrayAdapter<String>(this, R.layout.bp_dropdown_item, periods);
        autoComplete.setAdapter(arrayAdapter);
        autoComplete.setOnItemClickListener((adapterView, view, i, l) -> {
            period = adapterView.getItemAtPosition(i).toString();

        });
        textInputLayout.setVisibility(View.GONE);
        switchIE.setOnCheckedChangeListener((compoundButton, b) -> {
            if (switchIE.isChecked()) {
                switchIE.setText(switchIE.getTextOn());
            } else {
                switchIE.setText(switchIE.getTextOff());
            }
        });

        checkLong.setOnCheckedChangeListener((compoundButton, b) -> {
            if (checkLong.isChecked()) {
                textInputLayout.setVisibility(View.VISIBLE);
            } else {
                textInputLayout.setVisibility(View.GONE);
            }
        });

        save.setOnClickListener(view -> {
            if (titleText.getText().toString().equals("") ||
                    sumText.getText().toString().equals("") ||
                    (period.equals("") && checkLong.isChecked())){
                Toast.makeText(AddIEActivity.this, "Заполните все поля", Toast.LENGTH_LONG).show();
            }else {
                DatabaseHelper db = new DatabaseHelper(AddIEActivity.this);
                db.addIE(
                        titleText.getText().toString().trim(),
                        formatNumberCurrency(sumText.getText().toString()),
                        dateText.getDayOfMonth() + "." + dateText.getMonth()+"." + dateText.getYear(),
                        checkLong.isChecked() ? "true" : "false",
                        switchIE.isChecked() ? "false" : "true",
                        period);
                finish();
            }

        });
    }
    private static String formatNumberCurrency(String number){
        DecimalFormat formatter = new DecimalFormat("###,###,##0.00");
        return formatter.format(Double.parseDouble(number));
    }
}