package com.example.loftmoney;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import static com.example.loftmoney.BudgetFragment.REQUEST_CODE;
import static com.example.loftmoney.R.string.income;
import static com.example.loftmoney.R.string.outcome;

public class BudgetActivity extends AppCompatActivity {

    private Toolbar mToolbar;

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private BudgetViewPagerAdapter mViewPagerAdapter;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget);

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mViewPagerAdapter = new BudgetViewPagerAdapter(getSupportFragmentManager());
        mTabLayout = findViewById(R.id.tab_layout);
        mViewPager = findViewById(R.id.view_pager);

        mViewPager.setAdapter(mViewPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);

        mTabLayout.getTabAt(0).setText(outcome);
        mTabLayout.getTabAt(1).setText(income);

        mTabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.tab_indicator_color));

        FloatingActionButton openAddScreenButton = findViewById(R.id.fab_add_button_screen);
        openAddScreenButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                for (Fragment fragment : fragmentManager.getFragments()){
                    if (fragment.getUserVisibleHint()) {
                        fragment.startActivityForResult(new Intent(BudgetActivity.this, AddItemActivity.class), REQUEST_CODE);
                    }

                }

            }
        });


        /*mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mViewPagerAdapter.notifyDataSetChanged();*/

    }
    static class BudgetViewPagerAdapter extends FragmentPagerAdapter {

        public BudgetViewPagerAdapter(FragmentManager fm) {
            super(fm);

        }

        @Override
        public Fragment getItem(int i) {
            switch (i) {
                case 0:
                    return BudgetFragment.newInstance(FragmentType.income);
                case 1:
                    return BudgetFragment.newInstance(FragmentType.expence);
            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }

        /*@Override
        public CharSequence getPageTitle(int position) {
            return "Хз" + position;
        }*/

    }

}
