package com.example.myapplication; // *** 這行請改成你自己的 package 名稱 ***

import androidx.appcompat.app.AppCompatActivity;
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

public class MainActivity extends AppCompatActivity {

    private EditText edName;
    private RadioGroup rgProduct, rgAnimal;
    private ImageView imgPreview;
    private TextView tvOrderSummary;

    // 要求 5：用 ID 陣列處理 CheckBox
    private final int[] chkIds = {R.id.chkGift, R.id.chkBag, R.id.chkCard};
    private final CheckBox[] chks = new CheckBox[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        setListeners();
        updateOrder(); // 初始化顯示
    }

    private void initViews() {
        edName = findViewById(R.id.edName);
        rgProduct = findViewById(R.id.rgProduct);
        rgAnimal = findViewById(R.id.rgAnimal);
        imgPreview = findViewById(R.id.imgPreview);
        tvOrderSummary = findViewById(R.id.tvOrderSummary);
    }

    private void setListeners() {
        // 要求 4：EditText 即時更新
        edName.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) { updateOrder(); }
            @Override public void afterTextChanged(Editable s) {}
        });

        // 要求 1 & 3：RadioGroup 監聽與圖片切換
        rgAnimal.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // 完全依照你的命名清單對應圖檔
                if (checkedId == R.id.rbBigShark) {
                    imgPreview.setImageResource(R.drawable.bigshark);
                } else if (checkedId == R.id.rbShark) {
                    imgPreview.setImageResource(R.drawable.shark);
                } else if (checkedId == R.id.rbMega) {
                    imgPreview.setImageResource(R.drawable.mega);
                } else if (checkedId == R.id.rbCatfish) {
                    imgPreview.setImageResource(R.drawable.catfish);
                } else if (checkedId == R.id.rbTurtle) {
                    imgPreview.setImageResource(R.drawable.turtle);
                }
                updateOrder();
            }
        });

        rgProduct.setOnCheckedChangeListener((group, id) -> updateOrder());

        // 要求 2 & 5：CheckBox 迴圈註冊監聽
        CompoundButton.OnCheckedChangeListener chkLis = (buttonView, isChecked) -> updateOrder();
        for (int i = 0; i < chkIds.length; i++) {
            chks[i] = findViewById(chkIds[i]);
            if (chks[i] != null) chks[i].setOnCheckedChangeListener(chkLis);
        }
    }

    private void updateOrder() {
        String name = edName.getText().toString();

        // 取得紀念品選項
        RadioButton rbP = findViewById(rgProduct.getCheckedRadioButtonId());
        String product = (rbP != null) ? rbP.getText().toString() : "";

        // 取得生物圖案選項
        RadioButton rbA = findViewById(rgAnimal.getCheckedRadioButtonId());
        String animal = (rbA != null) ? rbA.getText().toString() : "";

        // 取得加購選項
        StringBuilder addons = new StringBuilder();
        for (CheckBox chk : chks) {
            if (chk != null && chk.isChecked()) {
                addons.append(chk.getText().toString()).append(" ");
            }
        }
        String addonStr = (addons.length() > 0) ? addons.toString() : "無";

        // 要求 6：即時顯示訂購單
        String result = "【海洋生物紀念品訂購單】\n" +
                "顧客姓名：" + (name.isEmpty() ? "(尚未輸入)" : name) + "\n" +
                "選購商品：" + product + "\n" +
                "圖案樣式：" + animal + "\n" +
                "加購項目：" + addonStr;

        tvOrderSummary.setText(result);
    }
}