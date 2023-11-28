package com.example.bankclient.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.Button;

import com.example.bankclient.R;
import com.example.bankclient.ui.fragments.LongIncomeFragment;
import com.example.bankclient.ui.fragments.ShortIncomeFragment;

public class IncomeActivity extends AppCompatActivity {

    SwitchCompat switchCompat;
    Button addButton;
    public IncomeActivity() {
        super(R.layout.activity_income_list);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.fragmentContainerView, LongIncomeFragment.class, null)
                    .commit();
        }

        addButton = findViewById(R.id.add_button);


        switchCompat = findViewById(R.id.switchIncome);

        switchCompat.setOnCheckedChangeListener((compoundButton, b) -> {
            if (switchCompat.isChecked()){
                switchCompat.setText(switchCompat.getTextOn());
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainerView, ShortIncomeFragment.class, null)
                        .setReorderingAllowed(true)
                        .addToBackStack("name")
                        .commit();
            }
            else {
                switchCompat.setText(switchCompat.getTextOff());
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainerView, LongIncomeFragment.class, null)
                        .setReorderingAllowed(true)
                        .addToBackStack("name1")
                        .commit();
            }
        });
        addButton.setOnClickListener(view -> {
            Intent intent = new Intent(IncomeActivity.this, AddIEActivity.class);
            intent.putExtra("isExpense", false);
            intent.putExtra("isLong", !switchCompat.isChecked());
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
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_CANCEL) this.finish();
        return super.onTouchEvent(event);
    }
}