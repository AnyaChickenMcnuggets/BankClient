package com.example.bankclient.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.Button;

import com.example.bankclient.R;
import com.example.bankclient.ui.fragments.LongIncomeFragment;
import com.example.bankclient.ui.fragments.ShortIncomeFragment;
import com.example.bankclient.ui.recycler_view.PagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

public class IncomeActivity extends AppCompatActivity {

    SwitchCompat switchCompat;
    Button addButton;
    PagerAdapter pagerAdapter;
    TabLayout tabLayout;
    ViewPager2 viewPager;
    public IncomeActivity() {
        super(R.layout.activity_income_list);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//
        pagerAdapter = new PagerAdapter(this);
        pagerAdapter.addFragment(new LongIncomeFragment(), "Регулярные\nдоходы");
        pagerAdapter.addFragment(new ShortIncomeFragment(), "Разовые\nдоходы");
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

//        switchCompat = findViewById(R.id.switchIncome);
//
//        switchCompat.setOnCheckedChangeListener((compoundButton, b) -> {
//            if (switchCompat.isChecked()){
//                switchCompat.setText(switchCompat.getTextOn());
//                FragmentManager fragmentManager = getSupportFragmentManager();
//                fragmentManager.beginTransaction()
//                        .replace(R.id.fragmentContainerView, ShortIncomeFragment.class, null)
//                        .setReorderingAllowed(true)
//                        .addToBackStack("name")
//                        .commit();
//            }
//            else {
//                switchCompat.setText(switchCompat.getTextOff());
//                FragmentManager fragmentManager = getSupportFragmentManager();
//                fragmentManager.beginTransaction()
//                        .replace(R.id.fragmentContainerView, LongIncomeFragment.class, null)
//                        .setReorderingAllowed(true)
//                        .addToBackStack("name1")
//                        .commit();
//            }
//        });
        addButton.setOnClickListener(view -> {
            Intent intent = new Intent(IncomeActivity.this, AddIEActivity.class);
            intent.putExtra("isExpense", false);
            intent.putExtra("isLong", pagerAdapter.getPageTitle(viewPager.getCurrentItem()).equals("Регулярные\nдоходы") ? true : false);
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