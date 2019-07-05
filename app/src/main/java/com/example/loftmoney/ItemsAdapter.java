package com.example.loftmoney;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ItemViewHolder> {

    private List<Item> mItemList = new ArrayList<>();
    private int mPriceColor;

    public ItemsAdapter(int mPriceColor) {
        this.mPriceColor = mPriceColor;
    }

    @NonNull
    @Override
    public ItemsAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = View.inflate(viewGroup.getContext(), R.layout.item_view, null);
        TextView priceView = itemView.findViewById(R.id.item_price);
        priceView.setTextColor(itemView.getContext().getResources().getColor(mPriceColor));
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemsAdapter.ItemViewHolder viewHolder, int i) {
        final Item item = mItemList.get(i);
        viewHolder.bindItem(item);

    }

    @Override
    public int getItemCount() {
        return mItemList.size();
    }

    public void addItem(final Item item) {
        mItemList.add(0,item);
        notifyItemInserted(0);
    }

    public void clear() {
        mItemList.clear();
        notifyDataSetChanged();
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView mNameView;
        private TextView mPriceView;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            mNameView = itemView.findViewById(R.id.item_name);
            mPriceView = itemView.findViewById(R.id.item_price);
        }

        public void bindItem(final Item item) {
            mNameView.setText(item.getName());
            mPriceView.setText(mPriceView.getContext().getResources().getString(R.string.price_template, String.valueOf(item.getPrice())));
        }
    }
}
