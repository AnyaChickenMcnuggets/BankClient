package com.example.bankclient.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.Toast;

import com.example.bankclient.R;
import com.example.bankclient.repository.DatabaseHelper;
import com.example.bankclient.ui.fragments.CheckLongExpenseFragment;
import com.example.bankclient.ui.fragments.CheckLongIncomeFragment;
import com.example.bankclient.ui.fragments.CheckShortExpenseFragment;
import com.example.bankclient.ui.fragments.CheckShortIncomeFragment;
import com.example.bankclient.ui.models.IncomeExpense;
import com.example.bankclient.ui.data_transfer.CheckIEViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SelectIncomeExpenseActivity extends AppCompatActivity {

    DatabaseHelper db;
    Button submit, getLongIncome, getShortIncome, getLongExpense, getShortExpense;
    private CheckIEViewModel viewModel;
    ArrayList<IncomeExpense> ieStart = new ArrayList<>();
    String[] sid;
    public SelectIncomeExpenseActivity() {
        super(R.layout.activity_select_income_expense);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(CheckIEViewModel.class);
        if (getIntent().hasExtra("checked")){
            sid = getIntent().getStringArrayExtra("checked");
            db = new DatabaseHelper(SelectIncomeExpenseActivity.this);
            for (String id:
                 sid) {
                Cursor cursor = db.readIEById(id);
                if (cursor.getCount()==0){
                    Toast.makeText(this, "No Data", Toast.LENGTH_SHORT).show();
                }else {
                    while (cursor.moveToNext()){
                        ieStart.add(new IncomeExpense(
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
            viewModel.setData(ieStart);
        }else {
            viewModel.setData(new ArrayList<>());
        }
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.fragmentContainerView, CheckLongIncomeFragment.class, null)
                    .commit();
        }

        submit = findViewById(R.id.submit);
        getShortExpense = findViewById(R.id.getShortExpense);
        getLongExpense = findViewById(R.id.getLongExpense);
        getShortIncome = findViewById(R.id.getShortIncome);
        getLongIncome = findViewById(R.id.getLongIncome);

        submit.setOnClickListener(view -> {
            Intent intent = new Intent();
            List<String> ids = viewModel.getList().getValue().stream()
                    .map(IncomeExpense::getId)
                    .collect(Collectors.toList());
            sid = new String[ids.size()];
            for (int i = 0; i < ids.size(); i++) sid[i] = ids.get(i);
            intent.putExtra("checked", sid);
            setResult(RESULT_OK, intent);
            finish();
        });

        getShortExpense.setOnClickListener(view -> {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainerView, CheckShortExpenseFragment.class, null)
                    .setReorderingAllowed(true)
                    .addToBackStack("name1")
                    .commit();
        });
        getLongExpense.setOnClickListener(view -> {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainerView, CheckLongExpenseFragment.class, null)
                    .setReorderingAllowed(true)
                    .addToBackStack("name1")
                    .commit();
        });
        getShortIncome.setOnClickListener(view -> {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainerView, CheckShortIncomeFragment.class, null)
                    .setReorderingAllowed(true)
                    .addToBackStack("name1")
                    .commit();
        });
        getLongIncome.setOnClickListener(view -> {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainerView, CheckLongIncomeFragment.class, null)
                    .setReorderingAllowed(true)
                    .addToBackStack("name1")
                    .commit();
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_CANCEL) this.finish();
        return super.onTouchEvent(event);
    }
}