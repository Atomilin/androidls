package com.example.loftmoney;

public enum FragmentType {

    expence (R.color.income_price_color),
    income (R.color.dark_sky_blue);

    FragmentType(int priceColor) {
        this.priceColor = priceColor;
    }

    private int priceColor;

    public int getPriceColor() {
        return priceColor;
    }
}
