package com.example.marinelifesouvenirshoporderingapp;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener, CompoundButton.OnCheckedChangeListener {

    private EditText edtName;
    private RadioGroup rgType, rgPattern;
    private ImageView imgPattern;
    private TextView tvSummary;
    private int[] chkIds = {R.id.chkGift, R.id.chkBag, R.id.chkCard};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        edtName = findViewById(R.id.edtName);
        rgType = findViewById(R.id.rgType);
        rgPattern = findViewById(R.id.rgPattern);
        imgPattern = findViewById(R.id.imgPattern);
        tvSummary = findViewById(R.id.tvSummary);

        // 1. EditText with TextWatcher
        edtName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateOrderSummary();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // 2 & 3. RadioGroup with OnCheckedChangeListener
        rgType.setOnCheckedChangeListener(this);
        rgPattern.setOnCheckedChangeListener(this);

        // 4. CheckBox registration using for loop + ID array
        for (int id : chkIds) {
            CheckBox chk = findViewById(id);
            chk.setOnCheckedChangeListener(this);
        }

        updateOrderSummary();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (group.getId() == R.id.rgPattern) {
            if (checkedId == R.id.rbShark) {
                imgPattern.setImageResource(R.mipmap.shark);
            } else if (checkedId == R.id.rbDolphin) {
                imgPattern.setImageResource(R.mipmap.dolphin);
            } else if (checkedId == R.id.rbOctopus) {
                imgPattern.setImageResource(R.mipmap.octopus);
            } else if (checkedId == R.id.rbTurtle) {
                imgPattern.setImageResource(R.mipmap.turtle);
            }
        }
        updateOrderSummary();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        updateOrderSummary();
    }

    private void updateOrderSummary() {
        String name = edtName.getText().toString().trim();
        if (name.isEmpty()) name = "(未輸入)";

        String type = "";
        int typeId = rgType.getCheckedRadioButtonId();
        if (typeId != -1) {
            RadioButton rb = findViewById(typeId);
            type = rb.getText().toString();
        } else {
            type = "(未選擇)";
        }

        String pattern = "";
        int patternId = rgPattern.getCheckedRadioButtonId();
        if (patternId != -1) {
            RadioButton rb = findViewById(patternId);
            pattern = rb.getText().toString();
        } else {
            pattern = "(未選擇)";
        }

        StringBuilder addOns = new StringBuilder();
        for (int id : chkIds) {
            CheckBox chk = findViewById(id);
            if (chk.isChecked()) {
                if (addOns.length() > 0) addOns.append(", ");
                addOns.append(chk.getText().toString());
            }
        }
        String addOnStr = addOns.length() > 0 ? addOns.toString() : "無";

        String summary = String.format("姓名：%s\n紀念品：%s\n圖案：%s\n加購項目：%s",
                name, type, pattern, addOnStr);
        tvSummary.setText(summary);
    }
}