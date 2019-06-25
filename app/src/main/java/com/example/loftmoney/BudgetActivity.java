package com.example.loftmoney;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import java.util.Objects;

public class BudgetActivity extends AppCompatActivity {

    public static final int REQUEST_CODE = 1001;
    private ItemsAdapter mItemsAdapter;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);

        mItemsAdapter = new ItemsAdapter();

        recyclerView.setAdapter(mItemsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(Objects.requireNonNull(ContextCompat.getDrawable(this, R.drawable.divaider)));
        recyclerView.addItemDecoration(dividerItemDecoration);

        mItemsAdapter.addItem(new Item("Молоко", 70));
        mItemsAdapter.addItem(new Item("Зубная щётка", 70));
        mItemsAdapter.addItem(new Item("Сковорода с антипригарным покрытием", 1670));
        Button openAddScreenButton = findViewById(R.id.open_add_button_screen);
        openAddScreenButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(BudgetActivity.this, AddItemActivity.class), REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, @Nullable final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            Item item = null;
            if (data != null) {
                item = new Item(data.getStringExtra("name"), Integer.parseInt(data.getStringExtra("price")));
            }
            mItemsAdapter.addItem(item);
        }
    }
}
