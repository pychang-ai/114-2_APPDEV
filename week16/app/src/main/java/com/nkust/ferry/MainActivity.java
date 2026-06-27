package com.nkust.ferry;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 藍海客輪航程與席位預訂系統 - MainActivity
 * 國立高雄科技大學 (NKUST) 海事軟體分析多合一期末專題設計
 * Fully Functional Android Jetpack Dual-Language Controller
 */
public class MainActivity extends AppCompatActivity {

    // UI Widgets
    private TextView tvAppTitle, tvAppSubtitle;
    private Spinner spinnerDeparture, spinnerDestination;
    private ImageButton btnSwapPorts;
    private Button btnTabKaohsiung, btnTabPingtung, btnTabTaitung, btnTabChiayi;
    private View cardMarineBg;
    private TextView tvMarineTitle, tvWaveHeight, tvWindSpeed, tvVisibility, tvSafetyStars, tvSafetyNote;
    private TextView tvSeatMapTitle, tvSelectedSeatCount, tvSelectedSeatLabel;
    private TextView tvPassengerTitle, tvTicketPriceLabel;
    private EditText etPassengerName;
    private CheckBox cbConcessionHalf;
    private Button btnBookTicket;
    private Button btnAskAI;
    private ProgressBar progressAI;
    private TextView tvAIResponse;
    private View TicketLayout;
    private TextView tvTicketCode, tvTicketRoute, tvTicketSeat, tvTicketPassenger, tvTicketPrice;
    private ImageView ivQrPlaceholder;
    private Button btnLangToggle;

    // App state
    private boolean isEnglishMode = false;
    private String selectedDeparture = "Taitung";
    private String selectedDestination = "Green Island";
    private int selectedSeatIndex = -1; // -1 means none
    private List<String> currentSeats = new ArrayList<>();
    private final List<Button> seatButtons = new ArrayList<>();

    // Ports list
    private static final String[] PORTS_ZH = {"台東富岡港", "綠島南寮漁港", "屏東東港", "小琉球白沙港", "高雄港", "旗津輪渡站", "嘉義布袋港", "澎湖馬公港"};
    private static final String[] PORTS_EN = {"Taitung Fugang Port", "Green Island Nanliao", "Pingtung Donggang", "Xiaoliuqiu Baisha", "Kaohsiung Port", "Cijin Ferry Station", "Chiayi Budai Port", "Penghu Magong Port"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Bind layouts
        initViews();
        
        // Setup initial seating model
        generateSeatingData();
        setupSeatGrid();

        // Populate Spinners
        setupSpinners();

        // Listeners for Swap Port Action
        btnSwapPorts.setOnClickListener(v -> swapPorts());

        // Fast region tab segment filters
        btnTabKaohsiung.setOnClickListener(v -> selectRegionIndex(4, 5)); // Kaohsiung <-> Cijin
        btnTabPingtung.setOnClickListener(v -> selectRegionIndex(2, 3));  // Pingtung <-> Xiaoliuqiu
        btnTabTaitung.setOnClickListener(v -> selectRegionIndex(0, 1));   // Taitung <-> Green Island
        btnTabChiayi.setOnClickListener(v -> selectRegionIndex(6, 7));    // Chiayi <-> Penghu/Magong

        // Bilingual Toggle Listener
        btnLangToggle.setOnClickListener(v -> {
            isEnglishMode = !isEnglishMode;
            updateLanguageUI();
        });

        // Booking submission
        btnBookTicket.setOnClickListener(v -> triggerBookingProcess());

        // Call Server-side Gemini AI Advisor proxy
        btnAskAI.setOnClickListener(v -> queryGeminiAdvisor());

        // Initial language sync
        updateLanguageUI();
        updateMarineStatus();
    }

    private void initViews() {
        tvAppTitle = findViewById(R.id.tvAppTitle);
        tvAppSubtitle = findViewById(R.id.tvAppSubtitle);
        spinnerDeparture = findViewById(R.id.spinnerDeparture);
        spinnerDestination = findViewById(R.id.spinnerDestination);
        btnSwapPorts = findViewById(R.id.btnSwapPorts);
        btnTabKaohsiung = findViewById(R.id.btnTabKaohsiung);
        btnTabPingtung = findViewById(R.id.btnTabPingtung);
        btnTabTaitung = findViewById(R.id.btnTabTaitung);
        btnTabChiayi = findViewById(R.id.btnTabChiayi);
        cardMarineBg = findViewById(R.id.cardMarineBg);
        tvMarineTitle = findViewById(R.id.tvMarineTitle);
        tvWaveHeight = findViewById(R.id.tvWaveHeight);
        tvWindSpeed = findViewById(R.id.tvWindSpeed);
        tvVisibility = findViewById(R.id.tvVisibility);
        tvSafetyStars = findViewById(R.id.tvSafetyStars);
        tvSafetyNote = findViewById(R.id.tvSafetyNote);
        tvSeatMapTitle = findViewById(R.id.tvSeatMapTitle);
        tvSelectedSeatCount = findViewById(R.id.tvSelectedSeatCount);
        tvSelectedSeatLabel = findViewById(R.id.tvSelectedSeatLabel);
        tvPassengerTitle = findViewById(R.id.tvPassengerTitle);
        tvTicketPriceLabel = findViewById(R.id.tvTicketPriceLabel);
        etPassengerName = findViewById(R.id.etPassengerName);
        cbConcessionHalf = findViewById(R.id.cbConcessionHalf);
        btnBookTicket = findViewById(R.id.btnBookTicket);
        btnAskAI = findViewById(R.id.btnAskAI);
        progressAI = findViewById(R.id.progressAI);
        tvAIResponse = findViewById(R.id.tvAIResponse);
        TicketLayout = findViewById(R.id.TicketLayout);
        tvTicketCode = findViewById(R.id.tvTicketCode);
        tvTicketRoute = findViewById(R.id.tvTicketRoute);
        tvTicketSeat = findViewById(R.id.tvTicketSeat);
        tvTicketPassenger = findViewById(R.id.tvTicketPassenger);
        tvTicketPrice = findViewById(R.id.tvTicketPrice);
        ivQrPlaceholder = findViewById(R.id.ivQrPlaceholder);
        btnLangToggle = findViewById(R.id.btnLangToggle);
    }

    private void generateSeatingData() {
        currentSeats.clear();
        String[] types = {"A", "B", "C", "D", "E", "F"};
        for (int row = 1; row <= 4; row++) {
            for (String col : types) {
                currentSeats.add(row + col);
            }
        }
    }

    private void setupSpinners() {
        updateSpinnerAdapter();

        spinnerDeparture.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedDeparture = getPortIdAt(position);
                updateMarineStatus();
                updateStaticPricing();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        spinnerDestination.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedDestination = getPortIdAt(position);
                updateMarineStatus();
                updateStaticPricing();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void updateSpinnerAdapter() {
        String[] portsArray = isEnglishMode ? PORTS_EN : PORTS_ZH;
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, portsArray);
        spinnerDeparture.setAdapter(adapter);
        spinnerDestination.setAdapter(adapter);

        // Preselect common combinations
        spinnerDeparture.setSelection(0); // Taitung
        spinnerDestination.setSelection(1); // Green Island
    }

    private String getPortIdAt(int index) {
        if (index < 0 || index >= PORTS_EN.length) return "Unknown";
        return PORTS_EN[index];
    }

    private void selectRegionIndex(int depIdx, int destIdx) {
        spinnerDeparture.setSelection(depIdx);
        spinnerDestination.setSelection(destIdx);
        updateMarineStatus();
        updateStaticPricing();
        String msg = isEnglishMode 
                ? "Fast route switched to: " + PORTS_EN[depIdx] + " ↔ " + PORTS_EN[destIdx]
                : "快速路線切換至：" + PORTS_ZH[depIdx] + " ↔ " + PORTS_ZH[destIdx];
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void swapPorts() {
        int depSel = spinnerDeparture.getSelectedItemPosition();
        int destSel = spinnerDestination.getSelectedItemPosition();
        spinnerDeparture.setSelection(destSel);
        spinnerDestination.setSelection(depSel);
        
        String temp = selectedDeparture;
        selectedDeparture = selectedDestination;
        selectedDestination = temp;

        updateMarineStatus();
        updateStaticPricing();
        
        String msg = isEnglishMode ? "Location Swapped" : "起迄位置已互換";
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void setupSeatGrid() {
        // Find our hardcoded seats in layout XML (e.g. seat0 to seat15)
        for (int i = 0; i < 16; i++) {
            int id = getResources().getIdentifier("seat_" + i, "id", getPackageName());
            if (id != 0) {
                Button btn = findViewById(id);
                if (btn != null) {
                    final int idx = i;
                    seatButtons.add(btn);
                    btn.setOnClickListener(v -> selectSeat(idx));
                }
            }
        }
    }

    private void selectSeat(int index) {
        selectedSeatIndex = index;
        for (int i = 0; i < seatButtons.size(); i++) {
            Button btn = seatButtons.get(i);
            if (i == index) {
                btn.setBackgroundColor(getResources().getColor(android.R.color.holo_green_dark));
                btn.setTextColor(getResources().getColor(android.R.color.white));
            } else {
                btn.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
                btn.setTextColor(getResources().getColor(android.R.color.black));
            }
        }
        
        String seatName = currentSeats.get(index);
        tvSelectedSeatCount.setText(seatName);
        tvSelectedSeatLabel.setText(isEnglishMode ? "Selected CABIN seat" : "已選定客艙座位");
    }

    private void updateMarineStatus() {
        // Dynamically compute wave variables depending on selected origin
        double waveHeightVal = 1.2;
        int windSpeedVal = 12;
        int visibilityVal = 10;
        int starLevel = 4;
        String descZh = "海面平靜，適航航次順暢。";
        String descEn = "Calm sea. Highly suitable for voyage.";

        if (selectedDeparture.contains("Taitung") || selectedDestination.contains("Taitung")) {
            waveHeightVal = 2.4;
            windSpeedVal = 22;
            visibilityVal = 8;
            starLevel = 3;
            descZh = "台東至綠島受外海黑潮側浪影響，注意防暈。";
            descEn = "Affected by offshore Kuroshio side wave. Anti-seasickness advised.";
        } else if (selectedDeparture.contains("Chiayi") || selectedDestination.contains("Chiayi")) {
            waveHeightVal = 1.8;
            windSpeedVal = 18;
            visibilityVal = 9;
            starLevel = 4;
            descZh = "台灣海峽澎湖水道有海流，東北季風強。";
            descEn = "Strong currents in Penghu Channel. Safe sailing.";
        } else if (selectedDeparture.contains("Kaohsiung") || selectedDestination.contains("Kaohsiung")) {
            waveHeightVal = 0.5;
            windSpeedVal = 8;
            visibilityVal = 12;
            starLevel = 5;
            descZh = "內港海象極佳，平穩無風浪。";
            descEn = "Excellent harbor conditions. Very plain and steady.";
        } else if (selectedDeparture.contains("Pingtung") || selectedDestination.contains("Pingtung")) {
            waveHeightVal = 0.8;
            windSpeedVal = 10;
            visibilityVal = 15;
            starLevel = 5;
            descZh = "大鵬灣到琉球水道海面明澈，今日平靜。";
            descEn = "Clear sight to Xiaoliuqiu. Smooth voyage forecast.";
        }

        // Apply to layout XML
        tvWaveHeight.setText(isEnglishMode ? "Waves: " + waveHeightVal + "m" : "浪高：" + waveHeightVal + "公尺");
        tvWindSpeed.setText(isEnglishMode ? "Wind: " + windSpeedVal + " kts" : "風速：" + windSpeedVal + "節");
        tvVisibility.setText(isEnglishMode ? "Visib: " + visibilityVal + "km" : "能見度：" + visibilityVal + "公里");
        
        StringBuilder stars = new StringBuilder();
        for (int i = 0; i < starLevel; i++) stars.append("★");
        tvSafetyStars.setText(stars.toString() + " (" + starLevel + " / 5)");
        tvSafetyNote.setText(isEnglishMode ? descEn : descZh);
    }

    private int getBaseTicketPrice() {
        if (selectedDeparture.contains("Taitung") || selectedDestination.contains("Taitung")) return 560;
        if (selectedDeparture.contains("Chiayi") || selectedDestination.contains("Chiayi")) return 1000;
        if (selectedDeparture.contains("Kaohsiung") || selectedDestination.contains("Kaohsiung")) return 150;
        if (selectedDeparture.contains("Pingtung") || selectedDestination.contains("Pingtung")) return 380;
        return 420;
    }

    private void updateStaticPricing() {
        int base = getBaseTicketPrice();
        if (cbConcessionHalf.isChecked()) {
            base = base / 2;
        }
        tvTicketPriceLabel.setText(isEnglishMode ? "Calculated Fare: NT$" + base : "即時票價試算：NT$" + base);
    }

    private void triggerBookingProcess() {
        String name = etPassengerName.getText().toString().trim();
        if (name.isEmpty()) {
            Toast.makeText(this, isEnglishMode ? "Please input passenger name for security check!" : "請輸入乘客實名制安檢姓名！", Toast.LENGTH_SHORT).show();
            return;
        }
        if (selectedSeatIndex == -1) {
            Toast.makeText(this, isEnglishMode ? "Please select a Cabin Seat!" : "請選取您要的客艙席位！", Toast.LENGTH_SHORT).show();
            return;
        }

        int finalPrice = getBaseTicketPrice();
        if (cbConcessionHalf.isChecked()) finalPrice = finalPrice / 2;

        // Populate tickets
        TicketLayout.setVisibility(View.VISIBLE);
        tvTicketCode.setText("BOF-" + System.currentTimeMillis() % 1000000);
        
        String routeStr = isEnglishMode 
                ? "Route: " + selectedDeparture + " -> " + selectedDestination 
                : "航線：" + selectedDeparture + " ↔ " + selectedDestination;
        tvTicketRoute.setText(routeStr);
        tvTicketSeat.setText((isEnglishMode ? "Seat: " : "座位：") + currentSeats.get(selectedSeatIndex));
        tvTicketPassenger.setText((isEnglishMode ? "Real-Name: " : "實名乘客：") + name);
        tvTicketPrice.setText("NT$" + finalPrice);

        Toast.makeText(this, isEnglishMode ? "Booking success! QR ticket generated." : "訂位成功！專屬安檢乘船驗收電子票證已生成", Toast.LENGTH_LONG).show();
    }

    private void updateLanguageUI() {
        btnLangToggle.setText(isEnglishMode ? "繁體中文" : "English");
        if (isEnglishMode) {
            tvAppTitle.setText("Blue Ocean Ferry App");
            tvAppSubtitle.setText("Taiwan Passenger Ferry Voyage & Seat Booking System");
            btnTabKaohsiung.setText("Kaohsiung");
            btnTabPingtung.setText("Pingtung");
            btnTabTaitung.setText("Taitung");
            btnTabChiayi.setText("Chiayi");
            tvMarineTitle.setText("📈 Today's Marine Forecast");
            tvSeatMapTitle.setText("💺 Cabin Seat Matrix Selection");
            tvPassengerTitle.setText("🎫 Real-Name Secure Reservation");
            cbConcessionHalf.setText("Student / Senior Half Fare (50% Concession)");
            btnBookTicket.setText("CONFIRM CABIN SEATS & RESERVE TICKET");
            btnAskAI.setText("✨ ASK GEMINI TODAY'S VOYAGE & FLIGHT TIPS");
            etPassengerName.setHint("Input full name on Passport");
        } else {
            tvAppTitle.setText("藍海航線 APP");
            tvAppSubtitle.setText("台灣客輪航程與席位預訂系統 — NKUST 海事學術專案");
            btnTabKaohsiung.setText("高雄");
            btnTabPingtung.setText("屏東");
            btnTabTaitung.setText("台東");
            btnTabChiayi.setText("嘉義");
            tvMarineTitle.setText("⛈️ 今日智慧海象觀測學術監控欄");
            tvSeatMapTitle.setText("💺 智慧客艙客機 seat 列席對號劃座");
            tvPassengerTitle.setText("🎫 實名安檢證件核銷申報專欄");
            cbConcessionHalf.setText("符合優待票資格（學生、敬老、身障半價折抵）");
            btnBookTicket.setText("確認劃座位置與核酸認證實名申報預訂票務");
            btnAskAI.setText("✨ 點擊詢問 AI 今日航行備忘 & 行程 (雙語自適應)");
            etPassengerName.setHint("請輸入乘船乘客身分證件相同姓名");
        }

        updateSpinnerAdapter();
        updateMarineStatus();
        updateStaticPricing();
    }

    /**
     * Net core thread to query Express server endpoint to keep consistent with full-stack Node.js behavior
     */
    private void queryGeminiAdvisor() {
        progressAI.setVisibility(View.VISIBLE);
        tvAIResponse.setVisibility(View.VISIBLE);
        tvAIResponse.setText(isEnglishMode ? "Contacting AI Maritime Advisor, please wait..." : "正在密鑰呼叫 藝海助瀾 AI 航務顧問分析海象...");

        new Thread(() -> {
            try {
                // Post object mimicking our express payload spec
                JSONObject payload = new JSONObject();
                payload.put("departure", selectedDeparture);
                payload.put("destination", selectedDestination);
                payload.put("lang", isEnglishMode ? "en" : "zh");
                
                JSONObject mar = new JSONObject();
                double wave = selectedDeparture.contains("Taitung") ? 2.4 : 1.2;
                mar.put("waveHeight", wave);
                mar.put("windSpeed", selectedDeparture.contains("Taitung") ? 22 : 12);
                mar.put("visibility", 10);
                mar.put("stars", selectedDeparture.contains("Taitung") ? 3 : 5);
                payload.put("marineData", mar);

                payload.put("isStudentOrElder", cbConcessionHalf.isChecked());
                payload.put("passengerName", etPassengerName.getText().toString());

                // Point to container network or local server url defined in building config
                URL url = new URL("http://127.0.0.1:3000/api/gemini/advisor");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json; utf-8");
                conn.setRequestProperty("Accept", "application/json");
                conn.setDoOutput(true);
                conn.setConnectTimeout(8000);

                try (OutputStream os = conn.getOutputStream()) {
                    byte[] input = payload.toString().getBytes(StandardCharsets.UTF_8);
                    os.write(input, 0, input.length);
                }

                int code = conn.getResponseCode();
                StringBuilder response = new StringBuilder();
                if (code == 200) {
                    try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                        String responseLine;
                        while ((responseLine = br.readLine()) != null) {
                            response.append(responseLine.trim());
                        }
                    }
                    JSONObject resObj = new JSONObject(response.toString());
                    final String advice = resObj.getString("advice");

                    runOnUiThread(() -> {
                        progressAI.setVisibility(View.GONE);
                        tvAIResponse.setText(advice);
                    });
                } else {
                    throw new Exception("HTTP Error: " + code);
                }
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> {
                    progressAI.setVisibility(View.GONE);
                    // Safe Offline premium fallback
                    String mockAdvice = getOfflineSimulationResponse();
                    tvAIResponse.setText(mockAdvice);
                });
            }
        }).start();
    }

    private String getOfflineSimulationResponse() {
        if (isEnglishMode) {
            return "### [OFFLINE ADVICE MODE ACTIVE]\n" +
                    "Hello! Prepared for route from **" + selectedDeparture + "** to **" + selectedDestination + "**.\n" +
                    "- **Anti-Seasickness**: Today's wave heights represent stable water. Take medicine 30 mins early if you are fragile.\n" +
                    "- **Port highlights**: Taste local sea-oysters and travel safely!\n" +
                    "- **Identity**: Please prepare your official passport or national ID cards for our instant real-name checkpoints.";
        } else {
            return "### 【在地離線即時航安顧問資訊】\n" +
                    "您好！今天為您從 **" + selectedDeparture + "** 往 **" + selectedDestination + "** 規劃的直達客輪班表：\n" +
                    "- **海象評估**：海象良好，請在開船前 30 分鐘於碼頭進行實名制憑證核發、通關安檢。\n" +
                    "- **旅途叮嚀**：離島陽光猛烈，請做好防曬保濕；抵達後必吃深海鮮美管生蠔與石花凍！";
        }
    }
}
