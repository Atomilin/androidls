package com.example.loftmoney;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import static com.example.loftmoney.BudgetFragment.REQUEST_CODE;
import static com.example.loftmoney.R.string.income;
import static com.example.loftmoney.R.string.outcome;

public class BudgetActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    private Toolbar mToolbar;

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private BudgetViewPagerAdapter mViewPagerAdapter;
    private FloatingActionButton mFloatingActionButton;

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

        mViewPager.addOnPageChangeListener(this);

        mTabLayout.setupWithViewPager(mViewPager);

        mTabLayout.getTabAt(0).setText(outcome);
        mTabLayout.getTabAt(1).setText(income);
        mTabLayout.getTabAt(2).setText(R.string.balance);

        mTabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.tab_indicator_color));

        mFloatingActionButton = findViewById(R.id.fab_add_button_screen);
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {

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


    @Override
    public void onSupportActionModeStarted(@NonNull ActionMode mode) {
        super.onSupportActionModeStarted(mode);
        mToolbar.setBackgroundColor(ContextCompat.getColor(this,R.color.dark_grey_blue));
        mTabLayout.setBackgroundColor(ContextCompat.getColor(this,R.color.dark_grey_blue));

        //
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.color_bar_action_mode));
        }
        //

        mFloatingActionButton.hide();
    }

    @Override
    public void onSupportActionModeFinished(@NonNull ActionMode mode) {
        super.onSupportActionModeFinished(mode);
        mToolbar.setBackgroundColor(ContextCompat.getColor(this,R.color.colorPrimary));
        mTabLayout.setBackgroundColor(ContextCompat.getColor(this,R.color.colorPrimary));
        //
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        }
        //

        mFloatingActionButton.show();
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {
        if (i == 2){
            mFloatingActionButton.hide();
        } else {
            mFloatingActionButton.show();
        }

    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    static class BudgetViewPagerAdapter extends FragmentPagerAdapter {

        public BudgetViewPagerAdapter(FragmentManager fm) {
            super(fm);

        }

        @Override
        public Fragment getItem(int i) {
            switch (i) {
                case 0:
                    return BudgetFragment.newInstance(FragmentType.expence);
                case 1:
                    return BudgetFragment.newInstance(FragmentType.income);
                case 2:
                    return BalanceFragment.newInstance();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }

        /*@Override
        public CharSequence getPageTitle(int position) {
            return "Хз" + position;
        }*/

    }

}
