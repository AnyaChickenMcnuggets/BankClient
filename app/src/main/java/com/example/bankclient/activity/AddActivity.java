package com.example.bankclient.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.bankclient.R;
import com.example.bankclient.database.DatabaseHelper;

import java.sql.Time;
import java.time.LocalDateTime;
import java.util.Date;

public class AddActivity extends AppCompatActivity {

    EditText title;
    Button addButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        title = findViewById(R.id.title_input);
        addButton = findViewById(R.id.add_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseHelper db = new DatabaseHelper(AddActivity.this);
                db.addPlan(
                        title.getText().toString().trim(),
                        LocalDateTime.now().toLocalDate().toString().trim(),
                        "problem",
                        "no response");
            }
        });
    }
}