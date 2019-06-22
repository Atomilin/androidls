package com.example.loftmoney;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

public class AddItemActivity extends AppCompatActivity {

    private EditText titleEdit, priceEdit;
    private String title, price;
    private Button addbutton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        titleEdit = findViewById(R.id.title_edittext);
        priceEdit = findViewById(R.id.price_edittext);
        addbutton = findViewById(R.id.add);


        TextWatcher generalTextWatcher = new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (titleEdit.getText().hashCode() == s.hashCode()) {
                    title = s.toString().trim();
                    changeButtonTextColor();
                } else if (priceEdit.getText().hashCode() == s.hashCode()) {
                    price = s.toString().trim();
                    changeButtonTextColor();
                }
            }

            private void changeButtonTextColor() {
                if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(price)) {
                    addbutton.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.add_button_text_color));
                } else {
                    addbutton.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.add_button_color_inactive));
                }

            }
        };

        titleEdit.addTextChangedListener(generalTextWatcher);
        priceEdit.addTextChangedListener(generalTextWatcher);

    }

}

