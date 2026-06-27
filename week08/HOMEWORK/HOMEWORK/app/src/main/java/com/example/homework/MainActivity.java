package com.example.homework;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etSearch = findViewById(R.id.etSearch);

        // 設定監聽器
        findViewById(R.id.btnShark).setOnClickListener(this);
        findViewById(R.id.btnTurtle).setOnClickListener(this);
        findViewById(R.id.btnDolphin).setOnClickListener(this);
        findViewById(R.id.btnOctopus).setOnClickListener(this);

        // 搜尋按鈕
        findViewById(R.id.btnSearch).setOnClickListener(v -> {
            String keyword = etSearch.getText().toString();
            searchAndOpen(keyword);
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btnShark) openDetail("大白鯊", "深海", "高速衝刺", "撕咬獵物", "shark");
        else if (id == R.id.btnTurtle) openDetail("綠蠵龜", "熱帶海域", "划動四肢", "啃食海藻", "turtle");
        else if (id == R.id.btnDolphin) openDetail("瓶鼻海豚", "溫帶近海", "尾鰭擺動", "圍獵魚群", "dolphin");
        else if (id == R.id.btnOctopus) openDetail("擬態章魚", "熱帶岩礁", "噴射推進", "觸手捕捉", "octopus");
    }

    private void openDetail(String name, String habitat, String move, String eat, String imageName) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("name", name);
        intent.putExtra("habitat", habitat);
        intent.putExtra("move", move);
        intent.putExtra("eat", eat);
        intent.putExtra("image", imageName);
        startActivity(intent);
    }

    private void searchAndOpen(String keyword) {
        if (keyword.contains("鯊")) openDetail("大白鯊", "深海", "高速衝刺", "撕咬獵物", "shark");
        else if (keyword.contains("龜")) openDetail("綠蠵龜", "熱帶海域", "划動四肢", "啃食海藻", "turtle");
        // ...以此類推
    }
}