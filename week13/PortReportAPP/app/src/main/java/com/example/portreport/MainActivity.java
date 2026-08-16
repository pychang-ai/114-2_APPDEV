package com.example.portreport;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    // Step 1：宣告 Launcher 欄位
    private ActivityResultLauncher<Intent> reportLauncher;

    private TextView tvLatestReport, tvShipName, tvPort, tvStatus, tvNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 初始化顯示元件
        tvLatestReport = findViewById(R.id.tvLatestReport);
        tvShipName     = findViewById(R.id.tvShipName);
        tvPort         = findViewById(R.id.tvPort);
        tvStatus       = findViewById(R.id.tvStatus);
        tvNote         = findViewById(R.id.tvNote);

        // Step 2：在 onCreate 初始化 Launcher（絕對不能放在按鈕點擊裡）
        reportLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    // 使用者送出了通報
                    Intent data = result.getData();
                    if (data != null) {
                        String shipName = data.getStringExtra("SHIP_NAME");
                        String port     = data.getStringExtra("PORT");
                        String status   = data.getStringExtra("STATUS");
                        String note     = data.getStringExtra("NOTE");

                        // 更新畫面
                        tvLatestReport.setText("最新通報已更新");
                        tvShipName.setText("船名：" + shipName);
                        tvPort.setText("港口：" + port);
                        tvStatus.setText("狀態：" + status);
                        tvNote.setText("備註：" + note);

                        Toast.makeText(this,
                            shipName + " " + status + " 通報成功",
                            Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // 使用者取消，不更新
                    Toast.makeText(this, "通報已取消", Toast.LENGTH_SHORT).show();
                }
            }
        );

        // Step 3：按鈕點擊時啟動表單
        Button btnNewReport = findViewById(R.id.btnNewReport);
        btnNewReport.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ReportActivity.class);
            reportLauncher.launch(intent);  // 用 launcher.launch 取代 startActivity
        });
    }
}
