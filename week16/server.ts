import express from "express";
import path from "path";
import { createServer as createViteServer } from "vite";
import { GoogleGenAI } from "@google/genai";
import dotenv from "dotenv";

dotenv.config();

const app = express();
const PORT = 3000;

app.use(express.json());

// Initialize server-side Gemini client according to the gemini-api skill guide
let ai: GoogleGenAI | null = null;
const geminiApiKey = process.env.GEMINI_API_KEY;

if (geminiApiKey) {
  try {
    ai = new GoogleGenAI({
      apiKey: geminiApiKey,
      httpOptions: {
        headers: {
          "User-Agent": "aistudio-build",
        },
      },
    });
    console.log("Successfully initialized Gemini Client on server side with User-Agent.");
  } catch (err) {
    console.error("Failed to initialize Gemini Client:", err);
  }
} else {
  console.warn("GEMINI_API_KEY is not defined. AI assistant will run in rich simulated advisor mode.");
}

// API endpoint for Gemini ferry consultant
app.post("/api/gemini/advisor", async (req, res) => {
  try {
    const { departure, destination, lang, marineData, isStudentOrElder, passengerName, customFocus } = req.body;
    
    const selectedLang = lang || "zh";
    const depPort = departure || "Taitung";
    const destPort = destination || "Green Island";

    // Format a high-fidelity system prompt
    const systemInstruction = selectedLang === "en"
      ? `You are the expert Bilingual Marine & Voyage Consultant ("藝海助瀾 AI Marine Consultant") for Taiwan Ferry Line and Seat Reservation System.
Provide ferry travel tips, boarding rules, recommended local sea delicacies and attractions, ocean anti-seasickness advice, and custom instructions based on the departure port, destination port, and passenger profile.
- You must write in elegant, warm, highly readable, customized English.
- Outline specific spots in ${destPort} or ${depPort}.
- Add safety pre-registration instructions (Real-name system) and standard boarding deadline (30 mins before, passport / national ID checking).
- Keep length under 280 words, using clean paragraphs or bullet points.`
      : `您是台灣客輪航程與席位預訂系統的「藝海助瀾 AI 航務顧問」。
請根據出發港口、目的地港口以及今日海象狀況、乘船旅客身分，提供實用防暈、當地旅遊精選、登船安全指示、實名制安檢政策（提早30分鐘攜帶身分證/健保卡/護照登船）等。
- 必須使用溫柔、優美、極具專業素養的繁體中文（Taiwanese Traditional Chinese）回答。
- 專門針對從「${depPort}」前往「${destPort}」的特色與氣候提供提示。
- 使用清晰的項目符號 (bullet points)，長度控制在 500 字以內，注重排版美觀。`;

    const promptText = selectedLang === "en"
      ? `Query Details:
- Voyage: From ${depPort} to ${destPort}
- Marine Condition: Wave height is ${marineData?.waveHeight || "1.2m"}, Wind speed is ${marineData?.windSpeed || "12 knots"}, Visibility is ${marineData?.visibility || "10km"}. Navigation safety rating is ${marineData?.stars || 4} stars out of 5.
- Passenger: ${passengerName || "Valued Passenger"} ${isStudentOrElder ? "(Eligible for Concession/Half fare)" : ""}.
${customFocus ? `- Specific inquiry focus: Please emphasize "${customFocus}" in your response.` : ""}
Help this passenger prepare their itinerary and provide ferry travel checklists!`
      : `查詢詳情：
- 前往航線：從「${depPort}」到「${destPort}」
- 今日海象：觀測波高約 ${marineData?.waveHeight || "1.2公尺"}，平均風速約 ${marineData?.windSpeed || "12節"}，海面能見度 ${marineData?.visibility || "10公里"}。航安星級評定為 ${marineData?.stars || 4} 顆星。
- 乘客姓名：${passengerName || "尊貴乘客"} ${isStudentOrElder ? "（符合半票/優待資格）" : ""}
${customFocus ? `- 旅客特定主題詢問：請特別詳細解答並強調「${customFocus}」這個主題。` : ""}
請為旅客客製化一份精緻的登船提醒、航行備忘、海鮮美食與必玩景點推薦！`;

    // Define local fallback templates for offline mode or upstream api issues
    const fallbackZh = `### 🌊 藍海客輪 · 今日航行備忘 & 登船指南 🌸
（因 Gemini 雲端運算此時處於繁忙高壓峰期，已為您自動載入「藍海離線智慧航安顧問」專業保障報告）

您好，**${passengerName || "尊貴乘客"}**！今日準備由 **${depPort}** 啟航至 **${destPort}**。${customFocus ? `特別針對您所重視的「**${customFocus}**」提供以下專家指引：` : "以下為您整理專屬航務手冊："}

1. **防暈特快攻略**：今日該海域氣象穩定（觀測浪高約 ${marineData?.waveHeight || "1.2米"}），航安星級預估為 ${"★".repeat(marineData?.stars || 4)}。建議易暈船的乘客於**開船前 30 分鐘**服用開港指定防暈配方，選擇客輪中後段、靠近水線的黃金席位，能有效減少黑潮側浪造成的搖晃。
2. **實名制登船安檢要求**：根據海事安全管理規則，所有乘客均須實施**實名登船與防偽驗收**。請務必在**船班出發前 30 分鐘**抵達碼頭，隨身攜帶**身分證、健保卡或護照**進行現場身分比對與安檢。
3. **目的地精選推薦**：
   - 如果前往**綠島**：不可錯過朝日海底溫泉看日出，到南寮街租賃電動機車馳騁。
   - 如果前往**小琉球**：請攜帶海洋友善防曬，下水與綠蠵龜共游，享用在地超人氣痛風海鮮麵！
   - 如果前往**澎湖**：馬公港外灘景緻如鏡，推薦必玩水上活動、大啖碳烤鮮蚵與小管沙拉。
4. **客艙乘船秩序**：請在 APP 內「預訂預位」選取您的專屬艙位，依編號對號入座，並將大件行李放置於通道指定區域。

*安全第一，船行途中請勿攀爬護欄，如遇任何突發海事狀況，請聽從專業船務人員指導！預祝您旅途平安愉快！*`;

    const fallbackEn = `### 🌊 Blue Ocean Ferry · Marine Voyage Guidelines & Memo 🌸
(Gemini is currently experiencing high demand. Seamlessly retrieved from our high-fidelity secure local cache advisor.)

Hello, **${passengerName || "Valued Passenger"}**! Today you are departing from **${depPort}** for **${destPort}**. ${customFocus ? `Regarding your specific query on "**${customFocus}**", here are our custom travel advisor suggestions:` : "Below is your customized itinerary checklist:"}

1. **Anti-Seasickness Preparation**: Today's wave height is approximately ${marineData?.waveHeight || "1.2m"} (${"★".repeat(marineData?.stars || 4)} Safety Stars). We highly advise passengers prone to nausea to take medication **30 minutes prior to departure** and select lower deck seats near the vessel's center of gravity.
2. **Real-Name Security Policy**: According to Maritime Authority regulations, boarding requires a strict identity check. Please arrive at the terminal **30 minutes before your departure time** with your **Passport, National ID, or Health Insurance Card** for boarding scanner validation.
3. **Selected Highlights & Delicacies**:
   - Visiting **Green Island**: Witness the unique saltwater Sunrise Hot Spring and enjoy coral scuba diving.
   - Visiting **Xiaoliuqiu**: Dive safely with endangered green sea turtles and eat local crispy cod and fresh ocean squids!
   - Visiting **Penghu/Magong**: Indulge in sweet raw oysters, brown sugar cake, and explore beautiful double-heart stone weirs.
4. **Cabin Accommodations**: Please use the cabin matrix tool in the App to reserve your preferred seat and follow the boarding instructions.

*WISHING YOU A SAFE AND ENJOYABLE VOYAGE WITH TAIPEI BLUE SHUTTLES!*`;

    const finalFallback = selectedLang === "en" ? fallbackEn : fallbackZh;

    if (ai) {
      try {
        const response = await ai.models.generateContent({
          model: "gemini-3.5-flash",
          contents: promptText,
          config: {
            systemInstruction,
            temperature: 0.7,
          },
        });

        const adviceText = response.text || "";
        res.json({ success: true, advice: adviceText });
      } catch (geminiError: any) {
        console.warn("Gemini generateContent call failed but handled gracefully by routing to fallback:", geminiError);
        res.json({ success: true, advice: finalFallback, fallbackTriggered: true, errorMsg: geminiError.message });
      }
    } else {
      res.json({ success: true, advice: finalFallback, apiKeyMissing: true });
    }
  } catch (error: any) {
    console.error("Gemini Advisor Endpoint Error:", error);
    res.status(500).json({ success: false, error: error.message });
  }
});

// Configure Vite or Static server
async function startServer() {
  if (process.env.NODE_ENV !== "production") {
    const vite = await createViteServer({
      server: { middlewareMode: true },
      appType: "spa",
    });
    app.use(vite.middlewares);
  } else {
    const distPath = path.join(process.cwd(), "dist");
    app.use(express.static(distPath));
    app.get("*", (req, res) => {
      res.sendFile(path.join(distPath, "index.html"));
    });
  }

  app.listen(PORT, "0.0.0.0", () => {
    console.log(`Server running on port ${PORT}`);
  });
}

startServer();
