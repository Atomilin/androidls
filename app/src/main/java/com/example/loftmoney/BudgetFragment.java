package com.example.loftmoney;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.loftmoney.MainActivity.AUTH_TOKEN;

public class BudgetFragment extends Fragment implements  ItemAdapterListener, ActionMode.Callback {

    private static final String PRICE_COLOR = "price_color";
    private static final String TYPE = "type";

    public static final int REQUEST_CODE = 1001;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ItemsAdapter mItemsAdapter;
    private Api mApi;
    private ActionMode mActionMode;

    public BudgetFragment() {
        // Required empty public constructor
    }

    public static BudgetFragment newInstance(FragmentType fragmentType) {
        BudgetFragment fragment = new BudgetFragment();
        Bundle args = new Bundle();
        args.putInt(PRICE_COLOR, fragmentType.getPriceColor());
        args.putString(TYPE, fragmentType.name());
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mApi = ((LoftApp) Objects.requireNonNull(getActivity()).getApplication()).getApi();
    }

    @Override
    public void onStart() {
        super.onStart();
        loadItems();
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View fragmentView = inflater.inflate(R.layout.fragment_budget, container, false);

        RecyclerView recyclerView = fragmentView.findViewById(R.id.recycler_view);

        mSwipeRefreshLayout = fragmentView.findViewById(R.id.refresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if (mActionMode != null) {
                    mActionMode.finish();
                }

                loadItems();
            }
        });

        mItemsAdapter = new ItemsAdapter(Objects.requireNonNull(getArguments()).getInt(PRICE_COLOR));
        mItemsAdapter.setListener(this);

        recyclerView.setAdapter(mItemsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(Objects.requireNonNull(getContext()), DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(Objects.requireNonNull(ContextCompat.getDrawable(getContext(), R.drawable.divaider)));
        recyclerView.addItemDecoration(dividerItemDecoration);


        return fragmentView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {

            final String token = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(AUTH_TOKEN, "");
            final int price = Integer.parseInt(data.getStringExtra("price"));
            final String name = data.getStringExtra("name");
            Call<Status> call = mApi.addItems(new AddItemRequest(price, name, Objects.requireNonNull(getArguments()).getString(TYPE)), token);
            call.enqueue(new Callback<Status>() {
                @Override
                public void onResponse(final Call<Status> call, final Response<Status> response) {
                    loadItems();
                }

                @Override
                public void onFailure(Call<Status> call, Throwable t) {

                }
            });
        }
    }

    private void loadItems(){
        final String token = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(AUTH_TOKEN, "");
        Call<List<Item>> itemsResponseCall = mApi.getItems(Objects.requireNonNull(getArguments()).getString(TYPE), token);
        itemsResponseCall.enqueue(new Callback<List<Item>>() {
            @Override
            public void onResponse(final Call<List<Item>> call, final Response<List<Item>> response) {
                mSwipeRefreshLayout.setRefreshing(false);
                mItemsAdapter.clear();
                List<Item> itemList = response.body();

                for (Item item : Objects.requireNonNull(itemList)){
                    mItemsAdapter.addItem(item);
                }
            }

            @Override
            public void onFailure(Call<List<Item>> call, Throwable t) {
                mSwipeRefreshLayout.setRefreshing(false);

            }
        });
    }

    @Override
    public void onItemClick(Item item, int position) {
        if (mItemsAdapter.isSelected(position)){
            mItemsAdapter.toogleItem(position);
            mItemsAdapter.notifyDataSetChanged();
        }
        mActionMode.setTitle("Выделено: "+ mItemsAdapter.getSelectedItemsIds().size());
        if (mItemsAdapter.getSelectedItemsIds().size() ==  0){
            mActionMode.setTitle("");
            mActionMode.finish();
        }
    }

    @Override
    public void onItemLongClick(Item item, int position) {
        mItemsAdapter.toogleItem(position);
        mItemsAdapter.notifyDataSetChanged();

        if (mActionMode == null) {
            ((AppCompatActivity) Objects.requireNonNull(getActivity())).startSupportActionMode(this);
        }
        mActionMode.setTitle("Выделено: "+ mItemsAdapter.getSelectedItemsIds().size());
        if (mItemsAdapter.getSelectedItemsIds().size() ==  0){
            mActionMode.setTitle("");
            mActionMode.finish();
        }
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        mActionMode = mode;
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        MenuInflater inflater = new MenuInflater(getContext());
        inflater.inflate(R.menu.item_menu_remove, menu);
        return true;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

        if (item.getItemId() == R.id.delete_menu_item) {
            showDialog();
        }

        return false;
    }

    private void showDialog() {
        new AlertDialog.Builder(Objects.requireNonNull(getContext()))
                .setMessage(R.string.remove_confirmation)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        removeItems();
                        mActionMode.finish();

                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mActionMode.finish();
                    }
                }).show();

    }

    private void removeItems() {
        List<Integer> selectedIds = mItemsAdapter.getSelectedItemsIds();
        for (int selectedId : selectedIds) {
            removeItem(selectedId);
        }
    }

    private void removeItem(int selectedId) {
        final String token = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(AUTH_TOKEN, "");
        Call<Status> itemsRemoveCall = mApi.removeItem(selectedId, token);
        itemsRemoveCall.enqueue(new Callback<Status>() {

            @Override
            public void onResponse(final Call<Status> call, final Response<Status> response) {
                loadItems();
            }

            @Override
            public void onFailure(Call<Status> call, Throwable t) {

            }
        });
    }


    @Override
    public void onDestroyActionMode(ActionMode mode) {
        mItemsAdapter.clearSelections();
        mItemsAdapter.notifyDataSetChanged();
        mActionMode = null;
    }
}
