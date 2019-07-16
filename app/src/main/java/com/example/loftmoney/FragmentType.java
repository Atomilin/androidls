package com.example.loftmoney;

public enum FragmentType {

    expence (R.color.dark_sky_blue),
    income (R.color.income_price_color);

    FragmentType(int priceColor) {
        this.priceColor = priceColor;
    }

    private int priceColor;

    public int getPriceColor() {
        return priceColor;
    }
}
