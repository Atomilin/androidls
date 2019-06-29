package com.example.loftmoney;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.Objects;

public class BudgetFragment extends Fragment {

    private static final String PRICE_COLOR = "price_color";

    public static final int REQUEST_CODE = 1001;

    private ItemsAdapter mItemsAdapter;

    public BudgetFragment() {
        // Required empty public constructor
    }

    public static BudgetFragment newInstance(int priceColor) {
        BudgetFragment fragment = new BudgetFragment();
        Bundle args = new Bundle();
        args.putInt(PRICE_COLOR, priceColor);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_budget, container, false);

        RecyclerView recyclerView = fragmentView.findViewById(R.id.recycler_view);

        mItemsAdapter = new ItemsAdapter(getArguments().getInt(PRICE_COLOR));

        recyclerView.setAdapter(mItemsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(Objects.requireNonNull(ContextCompat.getDrawable(getContext(), R.drawable.divaider)));
        recyclerView.addItemDecoration(dividerItemDecoration);

        mItemsAdapter.addItem(new Item("Молоко", 70));
        mItemsAdapter.addItem(new Item("Зубная щётка", 70));
        mItemsAdapter.addItem(new Item("Сковорода с антипригарным покрытием", 1670));
        Button openAddScreenButton = fragmentView.findViewById(R.id.open_add_button_screen);
        openAddScreenButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getContext(), AddItemActivity.class), REQUEST_CODE);
            }
        });

        return fragmentView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Item item = null;
            if (data != null) {
                item = new Item(data.getStringExtra("name"), Integer.parseInt(data.getStringExtra("price")));
            }
            mItemsAdapter.addItem(item);
        }
    }
}
