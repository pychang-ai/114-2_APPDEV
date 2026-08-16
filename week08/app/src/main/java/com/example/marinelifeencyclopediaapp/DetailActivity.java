package com.example.marinelifeencyclopediaapp;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class DetailActivity extends AppCompatActivity {

    private static final String TAG = "DetailActivityLifecycle";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate called");
        setContentView(R.layout.activity_detail);

        // 取得 Intent 傳來的資料
        String name = getIntent().getStringExtra("name");
        String habitat = getIntent().getStringExtra("habitat");
        String move = getIntent().getStringExtra("move");
        String eat = getIntent().getStringExtra("eat");
        String imageName = getIntent().getStringExtra("image");

        // 顯示資料
        ((TextView) findViewById(R.id.tvName)).setText(name);
        ((TextView) findViewById(R.id.tvHabitat)).setText("棲息地：" + habitat);
        ((TextView) findViewById(R.id.tvMove)).setText("移動：" + move);
        ((TextView) findViewById(R.id.tvEat)).setText("覓食：" + eat);

        // 動態載入圖片
        ImageView img = findViewById(R.id.imgCreature);
        int imageRes = getResources().getIdentifier(imageName, "drawable", getPackageName());
        if (imageRes != 0) {
            img.setImageResource(imageRes);
        } else {
            // Fallback to launcher icon if resource not found
            img.setImageResource(R.drawable.ic_launcher_foreground);
        }

        // 返回按鈕
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause called");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart called");
    }
}
