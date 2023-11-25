package com.example.bankclient.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.example.bankclient.R;
import com.example.bankclient.database.DatabaseHelper;

import java.time.LocalDateTime;

public class AddIEActivity extends AppCompatActivity {
    EditText titleText, dateText, sumText;
    SwitchCompat switchIE;
    CheckBox checkLong;
    Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ie);

        titleText = findViewById(R.id.editTitle);
        dateText = findViewById(R.id.editDate);
        sumText = findViewById(R.id.editSum);
        switchIE = findViewById(R.id.isExpense);
        checkLong = findViewById(R.id.isLong);
        save = findViewById(R.id.save);

        switchIE.setOnClickListener(view -> {
            if (switchIE.isChecked()){
                switchIE.setText(switchIE.getTextOn());
            }
            else {
                switchIE.setText(switchIE.getTextOff());
            }
        });

        save.setOnClickListener(view -> {
            DatabaseHelper db = new DatabaseHelper(AddIEActivity.this);
            db.addIE(
                    titleText.getText().toString().trim(),
                    sumText.getText().toString(),
                    dateText.getText().toString(),
                    checkLong.isChecked() ? "true" : "false",
                    switchIE.isChecked() ? "false" : "true");
        });
    }
}