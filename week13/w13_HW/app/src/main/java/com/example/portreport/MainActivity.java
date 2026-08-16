package com.example.portreport;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    // Step 1：宣告 Launcher 欄位
    private ActivityResultLauncher<Intent> reportLauncher;

    private TextView tvLatestReport, tvShipName, tvPort, tvStatus, tvNote;
    private ListView lvReports;
    private ArrayList<String> reportList;
    private ArrayAdapter<String> adapter;
    private int editingIndex = -1; // 用來標記目前是否在修改某筆記錄

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
        lvReports      = findViewById(R.id.lvReports);

        // 初始化 List 相關元件
        reportList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, reportList);
        lvReports.setAdapter(adapter);

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

                        // 每筆格式：[入港] 萬海601 @ 高雄港
                        String reportStr = "[" + status + "] " + shipName + " @ " + port;
                        
                        if (editingIndex == -1) {
                            // 新增通報
                            reportList.add(reportStr);
                            Toast.makeText(this,
                                shipName + " " + status + " 通報成功",
                                Toast.LENGTH_SHORT).show();
                        } else {
                            // 修改現有通報
                            reportList.set(editingIndex, reportStr);
                            Toast.makeText(this,
                                "已修改：" + shipName,
                                Toast.LENGTH_SHORT).show();
                            editingIndex = -1; // 重置狀態
                        }
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    // 使用者取消，不更新
                    editingIndex = -1; // 如果是修改中途取消，也要重置
                    Toast.makeText(this, "通報已取消", Toast.LENGTH_SHORT).show();
                }
            }
        );

        // Step 3：按鈕點擊時啟動表單
        Button btnNewReport = findViewById(R.id.btnNewReport);
        btnNewReport.setOnClickListener(v -> {
            editingIndex = -1; // 確保是新增模式
            Intent intent = new Intent(MainActivity.this, ReportActivity.class);
            // 傳遞預設港口
            intent.putExtra("DEFAULT_PORT", "高雄港");
            reportLauncher.launch(intent);
        });

        // 刪除最新通報
        Button btnDeleteLatest = findViewById(R.id.btnDeleteLatest);
        btnDeleteLatest.setOnClickListener(v -> {
            tvLatestReport.setText("（尚無通報）");
            tvShipName.setText("船名：");
            tvPort.setText("港口：");
            tvStatus.setText("狀態：");
            tvNote.setText("備註：");
            Toast.makeText(this, "已清除", Toast.LENGTH_SHORT).show();
        });

        // 點擊 ListView 中某筆通報時進行修改
        lvReports.setOnItemClickListener((parent, view, position, id) -> {
            editingIndex = position;
            String reportStr = reportList.get(position);
            
            // 從格式 "[入港] 萬海601 @ 高雄港" 解析出資料
            // 也可以選擇傳遞更完整的資料結構，但題目要求以字串加入 ArrayList
            try {
                int statusEnd = reportStr.indexOf("]");
                String status = reportStr.substring(1, statusEnd);
                int atIndex = reportStr.indexOf(" @ ");
                String shipName = reportStr.substring(statusEnd + 2, atIndex);
                String port = reportStr.substring(atIndex + 3);

                Intent intent = new Intent(MainActivity.this, ReportActivity.class);
                intent.putExtra("SHIP_NAME", shipName);
                intent.putExtra("PORT", port);
                intent.putExtra("STATUS", status);
                reportLauncher.launch(intent);
            } catch (Exception e) {
                Toast.makeText(this, "解析錯誤，無法修改", Toast.LENGTH_SHORT).show();
                editingIndex = -1;
            }
        });
    }
}
