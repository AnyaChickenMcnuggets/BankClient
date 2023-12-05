package com.example.bankclient.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.Button;

import com.example.bankclient.R;
import com.example.bankclient.ui.fragments.LongExpenseFragment;
import com.example.bankclient.ui.fragments.LongIncomeFragment;
import com.example.bankclient.ui.fragments.ShortExpenseFragment;
import com.example.bankclient.ui.fragments.ShortIncomeFragment;
import com.example.bankclient.ui.recycler_view.PagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class ExpenseActivity extends AppCompatActivity {

    SwitchCompat switchCompat;
    Button addButton;
    PagerAdapter pagerAdapter;

    TabLayout tabLayout;
    ViewPager2 viewPager;
    public ExpenseActivity() {
        super(R.layout.activity_expense_list);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pagerAdapter = new PagerAdapter(this);
        pagerAdapter.addFragment(new LongExpenseFragment(), "Регулярные\nрасходы");
        pagerAdapter.addFragment(new ShortExpenseFragment(), "Разовые\nрасходы");
        addButton = findViewById(R.id.add_button);
        tabLayout = findViewById(R.id.tabMode);
        viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(pagerAdapter);
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText(pagerAdapter.getPageTitle(position))
        ).attach();        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        addButton = findViewById(R.id.add_button);



        addButton.setOnClickListener(view -> {
            Intent intent = new Intent(ExpenseActivity.this, AddIEActivity.class);
            intent.putExtra("isExpense", true);
            intent.putExtra("isLong", pagerAdapter.getPageTitle(viewPager.getCurrentItem()).equals("Регулярные\nрасходы") ? true : false);
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