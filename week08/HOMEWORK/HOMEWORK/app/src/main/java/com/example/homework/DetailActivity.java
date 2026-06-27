package com.example.homework;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // 接收資料
        String name = getIntent().getStringExtra("name");
        String habitat = getIntent().getStringExtra("habitat");
        String move = getIntent().getStringExtra("move");
        String eat = getIntent().getStringExtra("eat");
        String imageName = getIntent().getStringExtra("image");

        // 顯示文字
        ((TextView) findViewById(R.id.tvName)).setText(name);
        ((TextView) findViewById(R.id.tvHabitat)).setText("棲息地：" + habitat);
        ((TextView) findViewById(R.id.tvMove)).setText("移動方式：" + move);
        ((TextView) findViewById(R.id.tvEat)).setText("覓食方式：" + eat);

        // 動態讀取圖片
        ImageView img = findViewById(R.id.imgCreature);
        int resId = getResources().getIdentifier(imageName, "drawable", getPackageName());
        img.setImageResource(resId);

        // 返回按鈕
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
    }
}