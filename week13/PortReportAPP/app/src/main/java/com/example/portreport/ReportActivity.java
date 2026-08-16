package com.example.portreport;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ReportActivity extends AppCompatActivity {

    private EditText etShipName, etPort, etNote;
    private RadioGroup rgStatus;
    private RadioButton rbEnter, rbExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        // 初始化元件
        etShipName = findViewById(R.id.etShipName);
        etPort     = findViewById(R.id.etPort);
        etNote     = findViewById(R.id.etNote);
        rgStatus   = findViewById(R.id.rgStatus);
        rbEnter    = findViewById(R.id.rbEnter);
        rbExit     = findViewById(R.id.rbExit);

        Button btnSubmit = findViewById(R.id.btnSubmit);
        Button btnCancel = findViewById(R.id.btnCancel);

        // 送出通報
        btnSubmit.setOnClickListener(v -> {
            String shipName = etShipName.getText().toString().trim();
            String port     = etPort.getText().toString().trim();

            // 驗證必填欄位
            if (shipName.isEmpty() || port.isEmpty()) {
                Toast.makeText(this, "請填寫船名和港口", Toast.LENGTH_SHORT).show();
                return;
            }

            // 取得選擇的狀態
            String status = rbEnter.isChecked() ? "入港" : "出港";
            String note   = etNote.getText().toString().trim();

            // 打包回傳資料
            Intent result = new Intent();
            result.putExtra("SHIP_NAME", shipName);
            result.putExtra("PORT",      port);
            result.putExtra("STATUS",    status);
            result.putExtra("NOTE",      note.isEmpty() ? "無" : note);

            setResult(Activity.RESULT_OK, result);  // 設定回傳成功
            finish();                                // 關閉表單，觸發回傳
        });

        // 取消
        btnCancel.setOnClickListener(v -> {
            setResult(Activity.RESULT_CANCELED);
            finish();
        });
    }
}
