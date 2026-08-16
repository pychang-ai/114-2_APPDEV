package com.example.marinelifeencyclopediaapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener {

    private static final String TAG = "MainActivityLifecycle";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate called");
        setContentView(R.layout.activity_main);

        findViewById(R.id.btnShark).setOnClickListener(this);
        findViewById(R.id.btnTurtle).setOnClickListener(this);
        findViewById(R.id.btnDolphin).setOnClickListener(this);
        findViewById(R.id.btnOctopus).setOnClickListener(this);

        // 搜尋功能
        EditText etSearch = findViewById(R.id.etSearch);
        findViewById(R.id.btnSearch).setOnClickListener(v -> {
            String keyword = etSearch.getText().toString();
            searchAndOpen(keyword);
        });
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

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btnShark) {
            openDetail("大白鯊", "深海", "高速衝刺獵食", "撕咬獵物", "shark");
        } else if (id == R.id.btnTurtle) {
            openDetail("綠蠵龜", "珊瑚礁", "緩慢划動四肢", "啃食海草", "turtle");
        } else if (id == R.id.btnDolphin) {
            openDetail("瓶鼻海豚", "近海", "躍出水面", "合作圍捕魚群", "dolphin");
        } else if (id == R.id.btnOctopus) {
            openDetail("章魚", "岩礁", "噴射水流推進", "用觸手捕捉獵物", "octopus");
        }
    }

    private void openDetail(String name, String habitat,
                            String move, String eat, String imageName) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("name", name);
        intent.putExtra("habitat", habitat);
        intent.putExtra("move", move);
        intent.putExtra("eat", eat);
        intent.putExtra("image", imageName);
        startActivity(intent);
    }

    private void searchAndOpen(String keyword) {
        if (keyword.contains("鯊")) {
            openDetail("大白鯊", "深海", "高速衝刺獵食", "撕咬獵物", "shark");
        } else if (keyword.contains("龜")) {
            openDetail("綠蠵龜", "珊瑚礁", "緩慢划動四肢", "啃食海草", "turtle");
        } else if (keyword.contains("豚")) {
            openDetail("瓶鼻海豚", "近海", "躍出水面", "合作圍捕魚群", "dolphin");
        } else if (keyword.contains("章")) {
            openDetail("章魚", "岩礁", "噴射水流推進", "用觸手捕捉獵物", "octopus");
        }
    }
}
