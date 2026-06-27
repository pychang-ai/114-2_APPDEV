/**
 * @license
 * SPDX-License-Identifier: Apache-2.0
 */

import React, { useState, useEffect } from 'react';
import { 
  Anchor, 
  Ship, 
  Compass, 
  MapPin, 
  ArrowLeftRight, 
  Wind, 
  Eye, 
  Sparkles, 
  CheckCircle2, 
  User, 
  CreditCard, 
  QrCode, 
  Globe, 
  RefreshCw,
  AlertTriangle,
  Waves,
  Check,
  ChevronRight,
  Wifi,
  Battery,
  Calendar,
  Layers,
  Map,
  Activity,
  ArrowRight,
  Bell,
  BellRing,
  Download,
  ExternalLink,
  Clock,
  Moon,
  Send
} from 'lucide-react';

// Ports dataset
interface Port {
  id: string;
  zh: string;
  en: string;
  region: 'Taitung' | 'Pingtung' | 'Kaohsiung' | 'Chiayi';
}

const PORTS: Port[] = [
  { id: "Taitung", zh: "台東富岡港", en: "Taitung Fugang Port", region: "Taitung" },
  { id: "Green Island", zh: "綠島南寮漁港", en: "Green Island Nanliao", region: "Taitung" },
  { id: "Pingtung", zh: "屏東東港", en: "Pingtung Donggang", region: "Pingtung" },
  { id: "Xiaoliuqiu", zh: "小琉球白沙港", en: "Xiaoliuqiu Baisha Port", region: "Pingtung" },
  { id: "Kaohsiung", zh: "高雄港二號碼頭", en: "Kaohsiung Port No.2 Pier", region: "Kaohsiung" },
  { id: "Cijin", zh: "旗津輪渡站", en: "Cijin Ferry Station", region: "Kaohsiung" },
  { id: "Chiayi", zh: "嘉義布袋港", en: "Chiayi Budai Port", region: "Chiayi" },
  { id: "Penghu", zh: "澎湖馬公港", en: "Penghu Magong Port", region: "Chiayi" },
];

export default function App() {
  // Mobile frame ticking clock
  const [currentTime, setCurrentTime] = useState<string>("09:41");

  useEffect(() => {
    const updateTime = () => {
      const now = new Date();
      const hrs = String(now.getHours()).padStart(2, '0');
      const mins = String(now.getMinutes()).padStart(2, '0');
      setCurrentTime(`${hrs}:${mins}`);
    };
    updateTime();
    const timer = setInterval(updateTime, 1000);
    return () => clearInterval(timer);
  }, []);

  // Navigation bottom tabs inside phone container
  const [activeTab, setActiveTab] = useState<'timetables' | 'booking' | 'advisor' | 'tickets'>('timetables');
  
  // Dual language switch
  const [lang, setLang] = useState<'zh' | 'en'>('zh');

  // Departure & Destination Port Selection
  const [departure, setDeparture] = useState<string>("Taitung");
  const [destination, setDestination] = useState<string>("Green Island");

  // Selected seat map state
  const [selectedSeat, setSelectedSeat] = useState<string | null>(null);

  // Passenger state
  const [passengerName, setPassengerName] = useState<string>("");
  const [isConcession, setIsConcession] = useState<boolean>(false);

  // Purchased tickets list (pre-loaded with realistic seaway tickets for immersive presentation)
  const [purchasedTickets, setPurchasedTickets] = useState<Array<{
    ticketId: string;
    vesselName: string;
    vesselNameEn: string;
    departureDate: string;
    departureTime: string;
    fromZh: string;
    toZh: string;
    fromEn: string;
    toEn: string;
    seat: string;
    passengerName: string;
    price: number;
    gate: string;
    isConcession: boolean;
    timestamp: string;
    status: 'Ready' | 'Boarded' | 'Cancelled';
  }>>([
    {
      ticketId: "BOF-774209",
      vesselName: "凱旋8號 Triumph VIII",
      vesselNameEn: "Triumph VIII",
      departureDate: "2026-06-11",
      departureTime: "13:30",
      fromZh: "台東富岡港",
      toZh: "綠島南寮漁港",
      fromEn: "Taitung Fugang Port",
      toEn: "Green Island Nanliao",
      seat: "2B",
      passengerName: "王曉明",
      price: 280,
      gate: "GATE 3A",
      isConcession: true,
      timestamp: "08:12:44",
      status: "Ready"
    },
    {
      ticketId: "BOF-391024",
      vesselName: "高雄之星 Kaohsiung Star",
      vesselNameEn: "Kaohsiung Star",
      departureDate: "2026-06-10",
      departureTime: "15:45",
      fromZh: "高雄港二號碼頭",
      toZh: "旗津輪渡站",
      fromEn: "Kaohsiung Port No.2 Pier",
      toEn: "Cijin Ferry Station",
      seat: "5D",
      passengerName: "王曉明",
      price: 50,
      gate: "GATE 1C",
      isConcession: false,
      timestamp: "14:22:15",
      status: "Boarded"
    }
  ]);

  // Selected ticket for full-bleed modal details overlay
  const [selectedPassId, setSelectedPassId] = useState<string | null>("BOF-774209");

  // Generated Ticket state
  const [generatedTicket, setGeneratedTicket] = useState<{
    ticketId: string;
    route: { fromZh: string; toZh: string; fromEn: string; toEn: string };
    seat: string;
    passengerName: string;
    price: number;
    timestamp: string;
  } | null>(null);

  // Calendar integrated scheduler & notification states
  const [isSuccessModalOpen, setIsSuccessModalOpen] = useState<boolean>(false);
  const [latestBookedTicket, setLatestBookedTicket] = useState<any | null>(null);
  const [reminderScheduledMap, setReminderScheduledMap] = useState<Record<string, boolean>>({});
  const [showSimulatedReminder, setShowSimulatedReminder] = useState<boolean>(false);
  const [simulatedReminderTicket, setSimulatedReminderTicket] = useState<any | null>(null);

  // Custom user scheduled reminder timing options
  const [reminderTimeOption, setReminderTimeOption] = useState<string>("24h"); // "24h", "12h", "3h", "1h", "30m"
  
  // Immersive locked off-screen and simulated lock-screen notification states
  const [isPhoneScreenOff, setIsPhoneScreenOff] = useState<boolean>(false);
  const [isPhoneScreenDarkManual, setIsPhoneScreenDarkManual] = useState<boolean>(false);
  const [isPhoneLockedView, setIsPhoneLockedView] = useState<boolean>(false);
  const [lockScreenMessage, setLockScreenMessage] = useState<string>("");

  // AI Recommendation consultant state
  const [aiLoading, setAiLoading] = useState<boolean>(false);
  const [aiResponse, setAiResponse] = useState<string>("");
  const [aiCustomQuestion, setAiCustomQuestion] = useState<string>("");

  // Trigger base region tab switcher
  const handleRegionTab = (region: 'Taitung' | 'Pingtung' | 'Kaohsiung' | 'Chiayi') => {
    if (region === 'Taitung') {
      setDeparture("Taitung");
      setDestination("Green Island");
    } else if (region === 'Pingtung') {
      setDeparture("Pingtung");
      setDestination("Xiaoliuqiu");
    } else if (region === 'Kaohsiung') {
      setDeparture("Kaohsiung");
      setDestination("Cijin");
    } else if (region === 'Chiayi') {
      setDeparture("Chiayi");
      setDestination("Penghu");
    }
  };

  // Safe one-click swapper
  const handleSwapPorts = () => {
    const temp = departure;
    setDeparture(destination);
    setDestination(temp);
  };

  // Retrieve current active port structures
  const depPortObj = PORTS.find(p => p.id === departure) || PORTS[0];
  const destPortObj = PORTS.find(p => p.id === destination) || PORTS[1];

  // Dynamically calculate marine observation statistics depending on selected harbor
  const getMarineObservation = () => {
    let waveHeight = "1.2m";
    let waveHeightZh = "1.2公尺";
    let windSpeed = "12 knots";
    let windSpeedZh = "12節";
    let visibility = "15 km";
    let visibilityZh = "15公里";
    let stars = 4;
    let descZh = "海面平靜，能見度極佳，台灣藍海客輪全線照常航行。";
    let descEn = "Extremely calm sea. Perfect visibility. All voyages departing as scheduled.";

    if (departure.includes("Taitung") || destination.includes("Taitung")) {
      waveHeight = "2.4m";
      waveHeightZh = "2.4公尺";
      windSpeed = "22 knots";
      windSpeedZh = "22節";
      visibility = "8 km";
      visibilityZh = "8公里";
      stars = 3;
      descZh = "台東至綠島航線受外海黑潮與季風影響，有中度側浪，建議提早服用暈船藥。";
      descEn = "Kuroshio currents create moderate side waves. Taking anti-seasickness advice is recommended.";
    } else if (departure.includes("Chiayi") || destination.includes("Chiayi")) {
      waveHeight = "1.8m";
      waveHeightZh = "1.8公尺";
      windSpeed = "18 knots";
      windSpeedZh = "18節";
      visibility = "10 km";
      visibilityZh = "10公里";
      stars = 4;
      descZh = "今日澎湖水道風勢適中，有輕微波浪起伏，客輪適航度良好。";
      descEn = "Moderate wind gusts through Penghu sea corridor. Safe navigation ratings persist.";
    } else if (departure.includes("Kaohsiung") || destination.includes("Cijin")) {
      waveHeight = "0.4m";
      waveHeightZh = "0.4公尺";
      windSpeed = "6 knots";
      windSpeedZh = "6節";
      visibility = "18 km";
      visibilityZh = "18公里";
      stars = 5;
      descZh = "高雄內海港灣與旗津水道海面靜如明鏡，無顯著浪高，適航體驗絕佳。";
      descEn = "Port of Kaohsiung inner channel resembles flat mirrors. Outstanding sailing status.";
    } else if (departure.includes("Pingtung") || destination.includes("Xiaoliuqiu")) {
      waveHeight = "0.7m";
      waveHeightZh = "0.7公尺";
      windSpeed = "8 knots";
      windSpeedZh = "8節";
      visibility = "20 km";
      visibilityZh = "20公里";
      stars = 5;
      descZh = "屏東大鵬灣與琉球航道澄澈蔚藍，今日水面平滑如鏡，極度適合渡假航行。";
      descEn = "Donggang and Xiaoliuqiu clear transit. Flat, safe water with gorgeous sea breeze.";
    }

    return { waveHeight, waveHeightZh, windSpeed, windSpeedZh, visibility, visibilityZh, stars, descZh, descEn };
  };

  const marine = getMarineObservation();

  // Fare matrix calculator
  const getBasePrice = () => {
    const combined = `${departure}-${destination}`;
    const reciprocal = `${destination}-${departure}`;
    
    if (combined === "Taitung-Green Island" || reciprocal === "Taitung-Green Island") return 560;
    if (combined === "Pingtung-Xiaoliuqiu" || reciprocal === "Pingtung-Xiaoliuqiu") return 380;
    if (combined === "Kaohsiung-Cijin" || reciprocal === "Kaohsiung-Cijin") return 50;
    if (combined === "Chiayi-Penghu" || reciprocal === "Chiayi-Penghu") return 1000;
    
    return 450;
  };

  const basePrice = getBasePrice();
  const calculatedFare = isConcession ? Math.round(basePrice * 0.5) : basePrice;

  // Query server side controller api for real-time Gemini recommendations
  const handleQueryAI = async (customFocus?: string) => {
    setAiLoading(true);
    setAiResponse("");
    try {
      const response = await fetch('/api/gemini/advisor', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          departure: depPortObj.en,
          destination: destPortObj.en,
          lang,
          marineData: {
            waveHeight: marine.waveHeight,
            windSpeed: marine.windSpeed,
            visibility: marine.visibility,
            stars: marine.stars
          },
          isStudentOrElder: isConcession,
          passengerName: passengerName || (lang === "en" ? "Valued Guest" : "尊貴貴賓"),
          customFocus: customFocus || ""
        })
      });

      const data = await response.json();
      if (data.success) {
        setAiResponse(data.advice);
      } else {
        setAiResponse(lang === "en" 
          ? "⚠️ AI System encountered a minor transient latency. Please check your backend is fully initialized or retry shortly!" 
          : "⚠️ AI 系統發生暫時性延遲，請確認伺服器已正常啟動並稍後重試！");
      }
    } catch (e) {
      console.error(e);
      setAiResponse(lang === "en" 
        ? "⚠️ Failed to communicate with fullstack server. Fallback to local expert safety rules." 
        : "⚠️ 無法連線至伺服器，已為您自動載入內地防暈與登船安檢離線手冊！");
    } finally {
      setAiLoading(false);
    }
  };

  // Perform seat selection
  const handleSeatClick = (seatCode: string) => {
    setSelectedSeat(seatCode);
  };

  // Calendar and ICS generation helpers
  const handleDownloadICS = (ticket: any) => {
    if (!ticket) return;
    const dateStr = ticket.departureDate.replace(/-/g, '');
    const timeStr = ticket.departureTime.replace(/:/g, '');
    
    const [h, m] = ticket.departureTime.split(':');
    const startHour = parseInt(h);
    const endHourStr = String((startHour + 1) % 24).padStart(2, '0');
    const endTimeStr = `${endHourStr}${m}`;

    const icsLines = [
      'BEGIN:VCALENDAR',
      'VERSION:2.0',
      'PRODID:-//BlueOcean//SmartCabin//ZH',
      'BEGIN:VEVENT',
      `UID:${ticket.ticketId}@blueocean.ferry`,
      `DTSTAMP:${dateStr}T000000Z`,
      `DTSTART:${dateStr}T${timeStr}00`,
      `DTEND:${dateStr}T${endTimeStr}00`,
      `SUMMARY:🚢 藍海客輪乘船提醒 - ${ticket.vesselName}`,
      `DESCRIPTION:🎫 隨行票卡號碼: ${ticket.ticketId}\\n👤 旅客實名: ${ticket.passengerName}\\n💺 分配座席: ${ticket.seat}\\n🚪 登船閘口: ${ticket.gate}\\n提醒：請於開航前 30 分鐘攜帶身份證或健保卡、護照進行現場實名制安檢核對。`,
      `LOCATION:${ticket.fromZh} ➔ ${ticket.toZh}`,
      'END:VEVENT',
      'END:VCALENDAR'
    ];
    
    const blob = new Blob([icsLines.join('\r\n')], { type: 'text/calendar;charset=utf-8' });
    const url = URL.createObjectURL(blob);
    const link = document.createElement('a');
    link.href = url;
    link.download = `BlueOcean_Pass_${ticket.ticketId}.ics`;
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
    URL.revokeObjectURL(url);
  };

  const getGoogleCalendarLink = (ticket: any) => {
    if (!ticket) return '';
    const dateStr = ticket.departureDate.replace(/-/g, '');
    const timeStr = ticket.departureTime.replace(/:/g, '');
    const [h, m] = ticket.departureTime.split(':');
    const startHour = parseInt(h);
    const endHourStr = String((startHour + 1) % 24).padStart(2, '0');
    const endTimeStr = `${endHourStr}${m}`;

    const text = `🚢 藍海智慧客輪乘船提醒 - ${ticket.vesselName}`;
    const dates = `${dateStr}T${timeStr}00/${dateStr}T${endTimeStr}00`;
    const details = `🎫 隨行票卡號碼: ${ticket.ticketId}\n👤 旅客實名: ${ticket.passengerName}\n💺 分配座席: ${ticket.seat}\n🚪 登船閘口: ${ticket.gate}\n\n提醒：請於開航前 30 分鐘攜帶身份證或健保卡、護照進行現場實名制安檢核對。`;
    const location = `${ticket.fromZh} ➔ ${ticket.toZh}`;

    return `https://calendar.google.com/calendar/render?action=TEMPLATE&text=${encodeURIComponent(text)}&dates=${dates}&details=${encodeURIComponent(details)}&location=${encodeURIComponent(location)}`;
  };

  const triggerRealNotification = (ticket: any, chosenOption: string) => {
    if (typeof window === 'undefined' || !('Notification' in window)) return;
    if (Notification.permission !== 'granted') return;

    let delayText = lang === "zh" ? "開航前 24 小時" : "24 hours before";
    if (chosenOption === "12h") delayText = lang === "zh" ? "開航前 12 小時" : "12 hours before";
    if (chosenOption === "3h") delayText = lang === "zh" ? "開航前 3 小時" : "3 hours before";
    if (chosenOption === "1h") delayText = lang === "zh" ? "開航前 1 小時" : "1 hour before";
    if (chosenOption === "30m") delayText = lang === "zh" ? "開航前 30 分鐘" : "30 mins before";

    const title = lang === "zh" 
      ? `🚢 藍海智慧客輪 · 安檢推播通知` 
      : `🚢 BlueOcean · Security Check Alert`;

    const body = lang === "zh"
      ? `【實名航安提醒 - ${delayText}】親愛的旅客 ${ticket.passengerName}，您預訂的「${ticket.vesselName}」將於明天的 ${ticket.departureTime} 從 ${ticket.fromZh} 出發，座席：${ticket.seat}。請隨身備齊身分證件！`
      : `【Boarding Check - ${delayText}】Dear voyager ${ticket.passengerName}, your vessel "${ticket.vesselNameEn}" departs at ${ticket.departureTime} from ${ticket.fromEn}, cabin seat: ${ticket.seat}.`;

    try {
      new Notification(title, {
        body,
        icon: 'https://cdn-icons-png.flaticon.com/512/3135/3135715.png', // Fallback standard icon
        tag: 'blueocean-reminder',
        silent: false
      });
    } catch (err) {
      console.warn("Notification construct error: ", err);
    }
  };

  const handleLaunchLockScreenSimulation = (ticket: any, chosenOption: string) => {
    if (!ticket) return;
    setSimulatedReminderTicket(ticket);
    
    // 1. Simulates turning OFF the phone screen (making it pure pitch black to represent sleep state)
    setIsPhoneScreenOff(true);
    
    // 2. After 2.3 seconds, the phone screen automatically lights up, showing the iOS/Android style lock screen with the notification card!
    setTimeout(() => {
      setIsPhoneScreenOff(false);
      setIsPhoneLockedView(true);
      setShowSimulatedReminder(true);
      
      // Trigger a real browser notification as well in tandem!
      triggerRealNotification(ticket, chosenOption);
    }, 2500);
  };

  const handleScheduleReminder = (ticket: any, chosenOption: string = "24h") => {
    if (!ticket) return;
    setReminderScheduledMap(prev => ({
      ...prev,
      [ticket.ticketId]: true
    }));

    // Trigger local permission mock or requests
    if (typeof window !== 'undefined' && 'Notification' in window) {
      if (Notification.permission === 'default') {
        Notification.requestPermission().then(perm => {
          if (perm === 'granted') {
            // Permission granted, will trigger on alert
          }
        });
      }
    }
  };

  // Confirms booking and generates receipt card
  const handleConfirmBooking = (e: React.FormEvent) => {
    e.preventDefault();
    if (!passengerName.trim()) {
      alert(lang === "en" ? "Please fill in Passenger's Real Name for Maritime verification!" : "為符合實名客輪登船法規，請輸入旅客證件真實姓名！");
      return;
    }
    if (!selectedSeat) {
      alert(lang === "en" ? "Please allocate a cabin seat slot before proceeding!" : "請先於智慧客艙座位圖中點選保留編號位置！");
      return;
    }

    const ticketCode = `BOF-${Math.floor(100000 + Math.random() * 900000)}`;
    
    // Dynamically assign matching beautiful vessel names & gates depending on harbor choice
    let vesselName = "太平洋1號 Pacific I";
    let vesselNameEn = "Pacific I";
    let gate = "GATE 1A";
    let departureTime = "11:00";

    if (departure.includes("Taitung") || destination.includes("Taitung")) {
      vesselName = "恆星號 Star Rider";
      vesselNameEn = "Star Rider";
      gate = "GATE 3A";
      departureTime = "11:00";
    } else if (departure.includes("Pingtung") || destination.includes("Pingtung")) {
      vesselName = "航港福隆號 Blue Wave";
      vesselNameEn = "Blue Wave";
      gate = "GATE 2B";
      departureTime = "13:30";
    } else if (departure.includes("Kaohsiung") || destination.includes("Kaohsiung")) {
      vesselName = "高雄之星 Kaohsiung Star";
      vesselNameEn = "Kaohsiung Star";
      gate = "GATE 1C";
      departureTime = "14:15";
    } else if (departure.includes("Chiayi") || destination.includes("Chiayi")) {
      vesselName = "凱旋8號 Triumph VIII";
      vesselNameEn = "Triumph VIII";
      gate = "GATE 5A";
      departureTime = "16:15";
    }

    const newTicketItem = {
      ticketId: ticketCode,
      vesselName,
      vesselNameEn,
      departureDate: new Date().toISOString().split('T')[0],
      departureTime,
      fromZh: depPortObj.zh,
      toZh: destPortObj.zh,
      fromEn: depPortObj.en,
      toEn: destPortObj.en,
      seat: selectedSeat,
      passengerName: passengerName,
      price: calculatedFare,
      gate,
      isConcession,
      timestamp: new Date().toLocaleTimeString(),
      status: 'Ready' as const
    };

    setPurchasedTickets(prev => [newTicketItem, ...prev]);
    setSelectedPassId(ticketCode);

    setGeneratedTicket({
      ticketId: ticketCode,
      route: {
        fromZh: depPortObj.zh,
        toZh: destPortObj.zh,
        fromEn: depPortObj.en,
        toEn: destPortObj.en
      },
      seat: selectedSeat,
      passengerName: passengerName,
      price: calculatedFare,
      timestamp: new Date().toLocaleTimeString()
    });

    // Populate success values
    setLatestBookedTicket(newTicketItem);
    setIsSuccessModalOpen(true);
  };

  // Seats simulation matrix
  const columns = ['A', 'B', 'C', 'D', 'E', 'F'];
  const rows = [1, 2, 3, 4, 5, 6];
  const occupiedSeats = ['1C', '2D', '4A', '5E', '3B'];

  return (
    <div id="blue-ocean-root" className="min-h-screen bg-[#060B18] text-slate-100 font-sans antialiased flex flex-col md:py-8 justify-center items-center p-2 md:p-4 transition-colors duration-200">
      
      {/* Decorative ocean currents on desktop background */}
      <div className="absolute top-10 left-10 w-96 h-96 bg-cyan-500/5 rounded-full blur-3xl pointer-events-none hidden lg:block"></div>
      <div className="absolute bottom-10 right-10 w-96 h-96 bg-indigo-500/5 rounded-full blur-3xl pointer-events-none hidden lg:block"></div>

      {/* Main Title Banner above the Phone simulator for pristine web onboarding */}
      <div className="text-center mb-5 max-w-md hidden md:block">
        <h2 className="text-2xl font-black bg-gradient-to-r from-cyan-400 via-sky-300 to-indigo-300 bg-clip-text text-transparent uppercase tracking-wider">
          {lang === "zh" ? "藍海智慧客輪 APP" : "Blue Ocean Ferry App"}
        </h2>
        <p className="text-xs text-slate-400 mt-1">
          {lang === "zh" ? "實時海象觀測 ↔ 智慧對號劃座 ↔ 實名票務安檢" : "Live Marine Weather ↔ Seat Matrix Booking ↔ Secure Passenger Check-in"}
        </p>
      </div>

      {/* 📱 PORTRAIT SMARTPHONE MOCKUP FRAME */}
      <div 
        id="phone-frame-container" 
        className="w-full max-w-[412px] h-[852px] bg-[#0A0F24] rounded-[52px] border-[12px] border-[#1C2541] shadow-[0_25px_60px_-15px_rgba(0,0,0,0.9)] overflow-hidden flex flex-col relative"
      >
        {/* Notch / Dynamic Island */}
        <div className="absolute top-0 inset-x-0 h-7 bg-[#1C2541] flex items-center justify-center z-50 rounded-b-2xl">
          <div className="w-28 h-4.5 bg-black rounded-full flex items-center justify-between px-3">
            <div className="w-1.5 h-1.5 bg-[#1e293b] rounded-full"></div>
            <div className="w-12 h-1 bg-zinc-800 rounded-full"></div>
            <div className="w-2.5 h-2.5 bg-zinc-950 rounded-full border border-zinc-900 flex items-center justify-center">
              <div className="w-1 h-1 bg-blue-900 rounded-full"></div>
            </div>
          </div>
        </div>

        {/* 📶 Real Status Bar with Wi-Fi, signal bars, and ticking clock */}
        <div className="bg-[#0A0F24] h-11 pt-7 px-6 flex justify-between items-center text-[11px] font-mono font-semibold text-slate-300 select-none z-40 shrink-0">
          <span>{currentTime}</span>
          <div className="flex items-center gap-2">
            <span className="text-[9px] font-bold text-cyan-400 tracking-tighter">5G</span>
            <Wifi className="w-3.5 h-3.5 text-slate-300" />
            <Battery className="w-4 h-4 text-emerald-400" />
          </div>
        </div>

        {/* 📲 PHONE APP BAR / HEADER */}
        <header className="border-b border-slate-800 bg-[#0E1533]/90 relative z-30 px-4 py-3.5 flex items-center justify-between shadow-md">
          <div className="flex items-center gap-2.5">
            <div className="bg-gradient-to-tr from-cyan-500 to-indigo-500 p-2 rounded-lg shadow-cyan-950/40 shadow-md shrink-0">
              <Compass className="w-4.5 h-4.5 text-[#0A0F24] animate-[spin_12s_linear_infinite]" />
            </div>
            <div>
              <div className="flex items-center gap-1.5">
                <span className="text-[10px] bg-cyan-950/80 text-cyan-400 font-bold px-1.5 py-0.2 rounded border border-cyan-900">
                  {lang === "zh" ? "實名登船" : "Secure Auth"}
                </span>
                <span className="w-1.5 h-1.5 rounded-full bg-emerald-400 animate-pulse"></span>
              </div>
              <h1 className="text-sm font-bold text-slate-100 tracking-tight">
                {lang === "zh" ? "藍海客輪隨行版" : "Blue Ocean Express"}
              </h1>
            </div>
          </div>

          {/* Bilingual Switch pill button */}
          <button
            onClick={() => setLang(lang === "zh" ? "en" : "zh")}
            className="flex items-center gap-1 bg-[#1A234A] hover:bg-[#25326B] text-cyan-400 hover:text-white px-2.5 py-1.5 rounded-lg transition-all text-2xs font-extrabold border border-[#2B3B7E]"
          >
            <Globe className="w-3 h-3" />
            <span className="leading-none">{lang === "zh" ? "EN" : "繁中"}</span>
          </button>
        </header>

        {/* 🛞 SCROLLABLE APP BODY CONTAINER */}
        <div className="flex-1 overflow-y-auto overflow-x-hidden bg-[#0A0F24] pb-24 relative px-3 pt-3 scroll-smooth">
          
          {/* Interactive Route Selector & Hot Ports (Hidden in Tickets & AI Advisor views as requested) */}
          {activeTab !== 'tickets' && activeTab !== 'advisor' && (
            <>
              {/* Interactive Route Selector */}
              <div className="bg-[#11193B] border border-[#1E2D64] rounded-2xl p-4 mb-3.5 shadow-lg relative overflow-hidden">
                <div className="space-y-3 relative z-10">
                  
                  {/* Departure Dropdown */}
                  <div>
                    <label className="text-[9px] font-bold text-cyan-400 block mb-1 uppercase tracking-wider font-mono">
                      🛫 {lang === "zh" ? "出發首選港口" : "Departure Port"}
                    </label>
                    <div className="relative">
                      <select
                        value={departure}
                        onChange={(e) => setDeparture(e.target.value)}
                        className="w-full bg-[#080E25] border border-[#1E2C61] text-xs text-white rounded-xl py-2 px-3 appearance-none focus:ring-1 focus:ring-cyan-400 cursor-pointer text-slate-100 font-medium"
                      >
                        {PORTS.map((p) => (
                          <option key={`dep-${p.id}`} value={p.id}>
                            {lang === "zh" ? p.zh : p.en}
                          </option>
                        ))}
                      </select>
                      <MapPin className="w-3.5 h-3.5 text-cyan-400 absolute right-3 top-2.5 pointer-events-none" />
                    </div>
                  </div>

                  {/* Bilateral Swift Swap Button (Overlap Center) */}
                  <div className="flex justify-center -my-3 relative -top-0.5">
                    <button
                      onClick={handleSwapPorts}
                      type="button"
                      title="Swap Ports"
                      className="bg-cyan-500 hover:bg-cyan-400 text-slate-900 p-2 rounded-full transition-all duration-300 shadow-md active:scale-95 border-4 border-[#11193B]"
                    >
                      <ArrowLeftRight className="w-3.5 h-3.5 font-bold" />
                    </button>
                  </div>

                  {/* Destination Dropdown */}
                  <div>
                    <label className="text-[9px] font-bold text-sky-400 block mb-1 uppercase tracking-wider font-mono">
                      🛬 {lang === "zh" ? "抵達目的地" : "Destination Port"}
                    </label>
                    <div className="relative">
                      <select
                        value={destination}
                        onChange={(e) => setDestination(e.target.value)}
                        className="w-full bg-[#080E25] border border-[#1E2C61] text-xs text-white rounded-xl py-2 px-3 appearance-none focus:ring-1 focus:ring-sky-400 cursor-pointer text-slate-100 font-medium"
                      >
                        {PORTS.map((p) => (
                          <option key={`dest-${p.id}`} value={p.id}>
                            {lang === "zh" ? p.zh : p.en}
                          </option>
                        ))}
                      </select>
                      <Anchor className="w-3.5 h-3.5 text-sky-400 absolute right-3 top-2.5 pointer-events-none" />
                    </div>
                  </div>

                </div>

                {/* Error banner if identical ports selected */}
                {departure === destination && (
                  <div className="mt-2.5 bg-rose-950/60 border border-rose-800 text-rose-300 rounded-xl p-2.5 flex items-start gap-2 text-[10px] font-mono leading-normal">
                    <AlertTriangle className="w-3.5 h-3.5 shrink-0 text-rose-400 mt-0.5" />
                    <span>
                      {lang === "zh" 
                        ? "提示：出發地與目的地相同。請為客輪選擇不同往返良港。" 
                        : "Warning: Identical locations! Please select distinct ports."}
                    </span>
                  </div>
                )}
              </div>

              {/* Quick Route Segment Picker Row */}
              <div className="bg-[#121A3C]/80 border border-[#1F2C61] rounded-2xl p-3 mb-3.5 shadow-md">
                <div className="flex items-center justify-between mb-2">
                  <span className="text-[10px] text-cyan-400 font-bold uppercase tracking-wider block">
                    🗺️ {lang === "zh" ? "智慧航線熱門港區" : "Taiwan Main Port Hubs"}
                  </span>
                  <span className="text-[8px] bg-slate-900 px-1 py-0.2 rounded text-slate-400 font-mono">2-WAY</span>
                </div>
                
                <div className="grid grid-cols-2 gap-2">
                  {[
                    { region: "Taitung", nameZh: "台東 ↔ 綠島", nameEn: "Taitung ↔ Green" },
                    { region: "Pingtung", nameZh: "屏東 ↔ 小琉球", nameEn: "Donggang ↔ Ryukyu" },
                    { region: "Kaohsiung", nameZh: "高雄 ↔ 旗津", nameEn: "Kaohsiung ↔ Cijin" },
                    { region: "Chiayi", nameZh: "嘉義 ↔ 澎湖", nameEn: "Budai ↔ Penghu" }
                  ].map((tab) => (
                    <button
                      key={tab.region}
                      onClick={() => handleRegionTab(tab.region as any)}
                      className={`text-left p-2.5 rounded-xl border transition-all duration-200 relative overflow-hidden group ${
                        (tab.region === 'Taitung' && departure === 'Taitung') ||
                        (tab.region === 'Pingtung' && departure === 'Pingtung') ||
                        (tab.region === 'Kaohsiung' && departure === 'Kaohsiung') ||
                        (tab.region === 'Chiayi' && departure === 'Chiayi')
                          ? 'bg-[#1D2F64] border-cyan-400 text-cyan-300'
                          : 'bg-[#0E1533]/80 border-[#1A2552] text-slate-300 hover:border-slate-700'
                      }`}
                    >
                      <div className="text-[10px] font-bold truncate">
                        {lang === "zh" ? tab.nameZh : tab.nameEn}
                      </div>
                      <div className="text-[8px] text-neutral-400 font-mono italic mt-0.5">
                        {tab.region.toUpperCase()} LINE
                      </div>
                    </button>
                  ))}
                </div>
              </div>
            </>
          )}

          {/* VIEW TAB 1: Real-time Timetable & Marine advisory */}
          {activeTab === 'timetables' && (
            <div className="space-y-4">
              
              {/* Climate observation board */}
              <div className="bg-[#121A3C]/70 border border-[#1E2B61] rounded-2xl p-4 shadow-md">
                <div className="flex items-center justify-between mb-3">
                  <div className="flex items-center gap-1.5">
                    <Waves className="w-4 h-4 text-cyan-400" />
                    <span className="font-bold text-xs text-slate-200">
                      {lang === "zh" ? "今日海象雷達指標" : "Live Marine Weather"}
                    </span>
                  </div>
                  <span className="text-[8px] bg-[#0A0E22] text-cyan-400 font-mono px-1.5 py-0.5 rounded border border-cyan-900">
                    REALTIME
                  </span>
                </div>

                <div className="grid grid-cols-3 gap-2 mb-3.5">
                  <div className="bg-[#080D21] p-2 rounded-lg text-center border border-slate-800/60">
                    <span className="text-[8px] text-slate-400 block mb-0.5">{lang === "zh" ? "波高" : "Waves"}</span>
                    <span className="text-cyan-300 font-extrabold text-xs font-mono">{lang === "zh" ? marine.waveHeightZh : marine.waveHeight}</span>
                  </div>

                  <div className="bg-[#080D21] p-2 rounded-lg text-center border border-slate-800/60">
                    <span className="text-[8px] text-slate-400 block mb-0.5">{lang === "zh" ? "風速" : "Winds"}</span>
                    <span className="text-teal-300 font-extrabold text-xs font-mono">{lang === "zh" ? marine.windSpeedZh : marine.windSpeed}</span>
                  </div>

                  <div className="bg-[#080D21] p-2 rounded-lg text-center border border-slate-800/60">
                    <span className="text-[8px] text-slate-400 block mb-0.5">{lang === "zh" ? "能見度" : "Visibility"}</span>
                    <span className="text-indigo-300 font-extrabold text-xs font-mono">{lang === "zh" ? marine.visibilityZh : marine.visibility}</span>
                  </div>
                </div>

                <div className="bg-[#080D21] p-3 rounded-xl border border-[#25336E]/30 text-[10px] leading-relaxed">
                  <div className="flex justify-between items-center mb-1">
                    <span className="text-slate-400 font-semibold">{lang === "zh" ? "適航評等" : "Safety Rating"}</span>
                    <div className="flex items-center gap-0.5">
                      {[...Array(5)].map((_, i) => (
                        <span key={i} className={`text-xs ${i < marine.stars ? "text-yellow-400" : "text-slate-700"}`}>★</span>
                      ))}
                    </div>
                  </div>
                  <p className="text-rose-300 font-mono text-[9px] flex items-start gap-1">
                    <AlertTriangle className="w-3 h-3 shrink-0 text-rose-400 mt-0.5" />
                    <span>{lang === "zh" ? marine.descZh : marine.descEn}</span>
                  </p>
                </div>
              </div>

              {/* Passenger timetables container */}
              <div className="bg-[#121A3C]/70 border border-[#1E2B61] rounded-2xl p-4 shadow-md">
                <div className="mb-2.5 flex justify-between items-center">
                  <span className="font-bold text-xs text-slate-200">{lang === "zh" ? "實時客輪班表" : "Ferry Schedules"}</span>
                  <span className="text-[9px] text-[#34d399] font-mono">LIVE SYNC</span>
                </div>

                <div className="space-y-2">
                  {[
                    { name: "恆星號 Star Rider", time: "08:30", seats: 12, status: "On Time", statusZh: "準點" },
                    { name: "航港福隆號 Blue Wave", time: "11:00", seats: 0, status: "Sold Out", statusZh: "客滿" },
                    { name: "凱旋8號 Triumph VIII", time: "13:30", seats: 24, status: "On Time", statusZh: "準點" },
                    { name: "綠島明星 Island Star", time: "16:15", seats: 2, status: "Alert", statusZh: "即將截止" }
                  ].map((item, id) => (
                    <div key={id} className="bg-[#080E25] p-3 rounded-xl border border-slate-800/80 flex justify-between items-center">
                      <div>
                        <div className="font-bold text-[11px] flex items-center gap-1">
                          <Ship className="w-3 h-3 text-cyan-400" />
                          <span>{item.name}</span>
                        </div>
                        <div className="text-[8px] text-slate-400 mt-0.5 font-mono">
                          {lang === "zh" ? `${depPortObj.zh} → ${destPortObj.zh}` : `${depPortObj.en} → ${destPortObj.en}`}
                        </div>
                      </div>
                      
                      <div className="text-right">
                        <span className="text-[11px] font-mono font-bold text-cyan-300 block">{item.time}</span>
                        {item.seats === 0 ? (
                          <span className="text-[8px] bg-rose-950 text-rose-400 px-1 py-0.2 rounded mt-0.5 inline-block font-mono">
                            {lang === "zh" ? "已客滿" : "SOLD OUT"}
                          </span>
                        ) : item.status === "Alert" ? (
                          <span className="text-[8px] bg-amber-900 text-amber-300 px-1 py-0.2 rounded mt-0.5 inline-block font-mono animate-pulse">
                            {lang === "zh" ? "僅剩 2 席" : "2 SEATS"}
                          </span>
                        ) : (
                          <span className="text-[8px] bg-emerald-950 text-emerald-400 px-1 py-0.2 rounded mt-0.5 inline-block font-mono">
                            {lang === "zh" ? `剩 ${item.seats} 席` : `${item.seats} SEATS`}
                          </span>
                        )}
                      </div>
                    </div>
                  ))}
                </div>

                <p className="text-[8px] text-slate-500 font-mono mt-3 text-center leading-normal">
                  * {lang === "zh" ? "登船核銷口將於開船前 30 分鐘關閉報到，請備妥身份證明。" : "Boarding processes close precisely 30 mins before departure."}
                </p>
              </div>

            </div>
          )}

          {/* VIEW TAB 2: Secure Reservation Ticket Book */}
          {activeTab === 'booking' && (
            <div className="space-y-4">
              
              {/* Interactive Cabin Seat Grid */}
              <div className="bg-[#121A3C]/70 border border-[#1E2B61] rounded-2xl p-4 shadow-md">
                
                <div className="mb-3.5 border-b border-slate-800 pb-2 flex justify-between items-center">
                  <div>
                    <h3 className="font-bold text-xs text-white">
                      {lang === "zh" ? "💺 客艙黃金席位劃劃對號" : "💺 Allocate Seat"}
                    </h3>
                    <p className="text-[8px] text-slate-400 mt-0.5">Please press on an unoccupied slot</p>
                  </div>
                  <span className="text-[9px] bg-slate-900 px-1.5 py-0.5 rounded text-cyan-400 font-mono">CLASS A</span>
                </div>

                {/* Seat Color legend indicator */}
                <div className="flex justify-between items-center bg-[#080D21] p-2 rounded-xl mb-3.5 text-[8px] font-mono">
                  <div className="flex items-center gap-1">
                    <span className="w-2.5 h-2.5 rounded-sm bg-slate-700 block"></span>
                    <span>{lang === "zh" ? "可預選" : "Free"}</span>
                  </div>
                  <div className="flex items-center gap-1">
                    <span className="w-2.5 h-2.5 rounded-sm bg-rose-950 border border-rose-800 block"></span>
                    <span>{lang === "zh" ? "已佔用" : "Full"}</span>
                  </div>
                  <div className="flex items-center gap-1">
                    <span className="w-2.5 h-2.5 rounded-sm bg-cyan-400 block animate-pulse"></span>
                    <span>{lang === "zh" ? "您選定" : "Selected"}</span>
                  </div>
                </div>

                {/* Ship Cabin layout drawing */}
                <div className="bg-[#080E25] p-3 rounded-2xl border border-[#1D2B5E] flex flex-col items-center">
                  <div className="w-full bg-gradient-to-r from-cyan-950 to-blue-900/60 rounded-t-xl py-1 text-[8px] text-center font-mono font-bold tracking-widest text-cyan-200 mb-4 border border-cyan-800/40">
                     ⚓ {lang === "zh" ? "客輪前艙 (FRONT BOW)" : "VESSEL FRONT CABIN"}
                  </div>

                  <div className="space-y-2 w-full max-w-[280px]">
                    {rows.map((row) => (
                      <div key={`se-${row}`} className="flex items-center justify-between gap-1.5">
                        <span className="text-[8px] font-mono text-slate-500 w-3 text-center">{row}</span>
                        <div className="flex-1 grid grid-cols-6 gap-1.5">
                          {columns.map((col) => {
                            const seatCode = `${row}${col}`;
                            const isOccupied = occupiedSeats.includes(seatCode);
                            const isSelected = selectedSeat === seatCode;

                            return (
                              <button
                                key={seatCode}
                                disabled={isOccupied}
                                type="button"
                                onClick={() => handleSeatClick(seatCode)}
                                className={`aspect-square p-1.5 text-[9px] font-extrabold rounded-md transition-all duration-150 ${
                                  isOccupied
                                    ? 'bg-rose-950/40 border border-rose-950 text-rose-500 cursor-not-allowed opacity-30'
                                    : isSelected
                                      ? 'bg-cyan-400 text-slate-950 font-black shadow-md shadow-cyan-400/40 scale-105'
                                      : 'bg-slate-800 hover:bg-slate-700 text-slate-200 border border-slate-700/85'
                                }`}
                              >
                                {seatCode}
                              </button>
                            );
                          })}
                        </div>
                        <span className="text-[8px] font-mono text-slate-500 w-3 text-center">{row}</span>
                      </div>
                    ))}
                  </div>

                  <div className="w-full border-t border-slate-800/80 mt-4 pt-2.5 text-[9px] flex justify-between items-center text-slate-400 font-mono">
                    <span>{lang === "zh" ? "過道寬敞 (Aisle)" : "Lower Deck Aisle Space"}</span>
                    <span className="text-cyan-400 font-bold">
                      {selectedSeat ? `SEAT: ${selectedSeat}` : (lang === "zh" ? "尚未選位" : "PRESS SEAT")}
                    </span>
                  </div>
                </div>

              </div>

              {/* Secure Booking Form */}
              <div className="bg-[#121A3C]/70 border border-[#1E2B61] rounded-2xl p-4 shadow-md">
                <div className="flex items-center gap-1.5 mb-3 border-b border-slate-800 pb-2">
                  <User className="text-cyan-400 w-4 h-4" />
                  <span className="font-bold text-xs text-slate-200">
                    {lang === "zh" ? "實名登記證件核對" : "Secure Gate Registration"}
                  </span>
                </div>

                <form onSubmit={handleConfirmBooking} className="space-y-3">
                  <div>
                    <label className="text-[9px] font-bold text-slate-400 font-mono block mb-1">
                      {lang === "zh" ? "1. 乘船真實中文姓名（須等同身分證件）" : "1. LEGAL PASSPORT NAME FOR CHECK-IN"}
                    </label>
                    <input
                      type="text"
                      required
                      placeholder={lang === "zh" ? "輸入中文真實姓名" : "Enter passport full name..."}
                      value={passengerName}
                      onChange={(e) => setPassengerName(e.target.value)}
                      className="w-full bg-[#080D22] border border-[#1E2C61] text-xs text-white rounded-xl py-2 px-3 focus:ring-1 focus:ring-cyan-500"
                    />
                  </div>

                  <div>
                    <label className="text-[9px] font-bold text-slate-400 font-mono block mb-1">
                      {lang === "zh" ? "2. 已選起訖與席位摘要" : "2. CURRENT TRAVEL ITINERARY"}
                    </label>
                    <div className="bg-[#080E25] p-2.5 rounded-lg border border-slate-800 text-[10px] space-y-1">
                      <div className="flex justify-between">
                        <span className="text-slate-500 font-mono">航段 Section:</span>
                        <span className="font-bold text-cyan-300">
                          {lang === "zh" ? `${depPortObj.zh} ↔ ${destPortObj.zh}` : `${depPortObj.en} ↔ ${destPortObj.en}`}
                        </span>
                      </div>
                      <div className="flex justify-between">
                        <span className="text-slate-500 font-mono">座席 Cabin Seat:</span>
                        <span className="font-mono font-bold text-emerald-400">
                          {selectedSeat ? selectedSeat : (lang === "zh" ? "尚未選取" : "Unselected")}
                        </span>
                      </div>
                    </div>
                  </div>

                  {/* Half Price Toggle */}
                  <div className="bg-[#080E25] p-2.5 rounded-xl border border-slate-800">
                    <label className="flex items-start gap-2 cursor-pointer select-none">
                      <input
                        type="checkbox"
                        checked={isConcession}
                        onChange={(e) => setIsConcession(e.target.checked)}
                        className="mt-0.5 rounded text-cyan-400 bg-[#080E25] border-slate-700 w-3.5 h-3.5 focus:ring-0 focus:ring-offset-0"
                      />
                      <div className="text-[10px] leading-relaxed">
                        <span className="text-yellow-400 font-bold block">
                          {lang === "zh" ? "學生 / 敬老愛心 50% 折扣票" : "Eligible for 50% Concession"}
                        </span>
                        <span className="text-[8px] text-slate-500 block leading-tight">
                          {lang === "zh" ? "乘船請備中華民國學生證或身分身心證明，必檢" : "Passport / Student certificate required at scanners."}
                        </span>
                      </div>
                    </label>
                  </div>

                  <div className="flex justify-between items-center bg-[#080E25]/60 p-2 rounded-xl">
                    <span className="text-[10px] text-slate-400">{lang === "zh" ? "折抵後實付價格：" : "Final Concession Fare:"}</span>
                    <span className="text-emerald-400 font-mono font-bold text-base">NTD ${calculatedFare}</span>
                  </div>

                  <button
                    type="submit"
                    className="w-full bg-[#10B981] hover:bg-[#34D399] text-white font-extrabold rounded-xl py-3 text-xs uppercase tracking-wider shadow-lg shadow-emerald-950/20 flex items-center justify-center gap-1"
                  >
                    <CheckCircle2 className="w-4 h-4" />
                    <span>{lang === "zh" ? "確認並實名登記購票" : "RESERVE SECURE BOARDING PASS"}</span>
                  </button>
                </form>

              </div>

              {/* PDF Boarding Pass view ticket style */}
              {generatedTicket && (
                <div className="bg-[#10B981] text-[#0A0E23] rounded-2xl p-4 shadow-lg relative overflow-hidden">
                  <div className="absolute right-0 top-0 h-12 w-12 bg-gradient-to-bl from-white/20 to-transparent pointer-events-none"></div>
                  
                  <div className="flex justify-between items-center border-b border-[#0A0E23]/15 pb-2.5">
                    <div>
                      <span className="text-[8px] font-mono font-bold uppercase tracking-widest bg-[#0A0E23] text-white px-1.5 py-0.5 rounded leading-none">
                        ELECTRONIC SCAN PASS
                      </span>
                      <h4 className="font-extrabold text-sm tracking-tight mt-1 font-mono">
                        {generatedTicket.ticketId}
                      </h4>
                    </div>
                    <Ship className="w-5 h-5 text-[#0A0E23]" />
                  </div>

                  <div className="my-3 grid grid-cols-2 gap-2 text-[10px] leading-normal font-sans">
                    <div>
                      <span className="block text-[8px] opacity-70 uppercase font-mono tracking-wider">{lang === "zh" ? "航線" : "Voyage"}</span>
                      <span className="font-extrabold">
                        {lang === "zh" ? `${generatedTicket.route.fromZh} → ${generatedTicket.route.toZh}` : `${generatedTicket.route.fromEn} → ${generatedTicket.route.toEn}`}
                      </span>
                    </div>

                    <div>
                      <span className="block text-[8px] opacity-70 uppercase font-mono tracking-wider">{lang === "zh" ? "座席" : "Seat Rows"}</span>
                      <span className="font-mono font-extrabold">{generatedTicket.seat}</span>
                    </div>

                    <div>
                      <span className="block text-[8px] opacity-70 uppercase font-mono tracking-wider">{lang === "zh" ? "旅客證自" : "Passenger"}</span>
                      <span className="font-bold">{generatedTicket.passengerName}</span>
                    </div>

                    <div>
                      <span className="block text-[8px] opacity-70 uppercase font-mono tracking-wider">{lang === "zh" ? "費用" : "Fare paid"}</span>
                      <span className="font-extrabold font-mono">${generatedTicket.price} NTD</span>
                    </div>
                  </div>

                  {/* QR validation */}
                  <div className="bg-white p-3 rounded-xl border border-emerald-800 flex flex-col items-center">
                    <QrCode className="w-20 h-20 text-slate-900" />
                    <span className="text-[8px] text-slate-500 tracking-wider font-mono font-bold mt-1.5">
                      * 台灣航港局實名安檢防偽驗證碼 *
                    </span>
                    <span className="text-[7px] text-slate-400 font-mono">
                      SYNC: {generatedTicket.timestamp}
                    </span>
                  </div>
                </div>
              )}

            </div>
          )}

          {/* VIEW TAB 4: My Boarding Passes - 「藍海隨行券 / My Passes Wallet」 */}
          {activeTab === 'tickets' && (
            <div className="space-y-4">
              {/* List of active passes selector */}
              <div className="bg-[#121A3C]/70 border border-[#1E2B61] rounded-2xl p-3 shadow-md">
                <h3 className="text-[10px] text-cyan-400 font-bold uppercase tracking-wider mb-2 flex items-center gap-1.5 font-mono">
                  <Layers className="w-3.5 h-3.5" />
                  {lang === "zh" ? "我的隨行卡證包" : "My Smart Seaway Passes"}
                </h3>
                
                <div className="space-y-2 max-h-[160px] overflow-y-auto pr-1">
                  {purchasedTickets.length === 0 ? (
                    <div className="py-6 text-center text-slate-500 text-xs">
                      {lang === "zh" ? "⚠️ 尚無已訂票券，請至「預訂劃位」進行席位保留" : "⚠️ No passes found. Go to Ferry Seats to book!"}
                    </div>
                  ) : (
                    purchasedTickets.map((t) => (
                      <button
                        key={t.ticketId}
                        type="button"
                        onClick={() => setSelectedPassId(t.ticketId)}
                        className={`w-full text-left p-2.5 rounded-xl border transition-all text-xs flex justify-between items-center cursor-pointer ${
                          selectedPassId === t.ticketId
                            ? 'bg-[#1D2F64]/95 border-cyan-400 text-white shadow-md shadow-cyan-950/20'
                            : 'bg-[#0E1533]/80 border-[#1A2552] text-slate-300 hover:border-slate-700 hover:bg-[#152044]'
                        }`}
                      >
                        <div className="space-y-1.5 flex-1">
                          {/* Route on top */}
                          <div className="font-extrabold text-white flex items-center gap-1.5 text-xs font-sans">
                            <Anchor className="w-3 h-3 text-cyan-400 shrink-0" />
                            <span>{lang === "zh" ? `${t.fromZh} ➔ ${t.toZh}` : `${t.fromEn} ➔ ${t.toEn}`}</span>
                          </div>
                          
                          {/* Order ID placed cleanly underneath */}
                          <div className="font-extrabold font-mono text-cyan-300 text-[9.5px] flex items-center gap-1.5">
                            <span>{lang === "zh" ? "📋 訂單編號" : "📋 Order ID"}: {t.ticketId}</span>
                            <span className={`text-[7.5px] font-bold px-1.5 py-0.2 rounded ${
                              t.status === 'Ready'
                                ? 'bg-emerald-500/20 text-emerald-400 border border-emerald-500/30'
                                : 'bg-slate-700/30 text-slate-400 border border-slate-700/30'
                            }`}>
                              {t.status === 'Ready' ? (lang === "zh" ? "待核驗" : "READY") : (lang === "zh" ? "已登船" : "BOARDED")}
                            </span>
                          </div>
                        </div>
                        <div className="text-right shrink-0">
                          <div className="font-bold text-slate-200 font-mono text-[11px]">{t.departureTime}</div>
                          <div className="text-[7.5px] font-mono text-slate-500 mt-0.5">{t.departureDate}</div>
                        </div>
                      </button>
                    ))
                  )}
                </div>
              </div>

              {/* Detailed Perforated Ticket Pass Wrapper */}
              {(() => {
                const activePass = purchasedTickets.find(t => t.ticketId === selectedPassId);
                if (!activePass) return null;
                
                return (
                  <div className="bg-gradient-to-b from-[#15204C] to-[#0A102C] border border-cyan-500/30 rounded-2xl p-4 shadow-xl relative overflow-hidden">
                    
                    {/* Glowing Ambient Ambient Glow Behind Ticket */}
                    <div className="absolute -left-10 -top-10 w-32 h-32 bg-cyan-400/5 rounded-full blur-2xl pointer-events-none"></div>
                    
                    {/* Pass Card Logo Indicator */}
                    <div className="flex justify-between items-center border-b border-cyan-500/10 pb-3">
                      <div className="flex items-center gap-2">
                        <div className="bg-cyan-500 text-slate-950 p-1.5 rounded-lg">
                          <Ship className="w-3.5 h-3.5" />
                        </div>
                        <div>
                          <span className="text-[9px] font-bold uppercase text-cyan-400 tracking-wider block font-mono leading-none">
                            {lang === "zh" ? "藍海隨行券 · 登船聯" : "BLUEGO BOARDING PASS"}
                          </span>
                          <span className="text-[7.5px] text-slate-400 block font-mono mt-0.5">TAIWAN SEAWAY SMART PASS wallet</span>
                        </div>
                      </div>
                      
                      <span className={`text-[8px] font-mono font-black px-2 py-0.5 rounded-full ${
                        activePass.status === 'Ready'
                          ? 'bg-emerald-500 text-slate-950'
                          : 'bg-slate-700 text-slate-300'
                      }`}>
                        {activePass.status === 'Ready' ? (lang === "zh" ? "准予登船" : "READY PASS") : (lang === "zh" ? "已核銷" : "VOID")}
                      </span>
                    </div>

                    {/* Ports Voyage Summary */}
                    <div className="my-3 bg-[#080D21]/90 p-3 rounded-xl border border-slate-800 flex items-center justify-between">
                      <div className="w-[42%] text-left">
                        <span className="text-[8px] text-slate-500 uppercase block font-mono tracking-wider">{lang === "zh" ? "始發港口" : "Departure"}</span>
                        <span className="font-extrabold text-xs text-white block mt-0.5 truncate">{activePass.fromZh}</span>
                        <span className="text-[7.5px] text-slate-400 font-mono block truncate">{activePass.fromEn}</span>
                      </div>

                      <div className="flex flex-col items-center justify-center w-[16%]">
                        <Anchor className="w-4 h-4 text-cyan-400" />
                        <div className="w-full h-[1px] bg-cyan-500/30 my-1 relative">
                          <div className="absolute right-0 top-1/2 -translate-y-1/2 w-1 h-1 bg-cyan-400 rounded-full"></div>
                        </div>
                        <span className="text-[6.5px] font-mono text-[#22D3EE] font-bold uppercase">Voyage</span>
                      </div>

                      <div className="w-[42%] text-right">
                        <span className="text-[8px] text-slate-500 uppercase block font-mono tracking-wider">{lang === "zh" ? "目的港口" : "Destination"}</span>
                        <span className="font-extrabold text-xs text-white block mt-0.5 truncate">{activePass.toZh}</span>
                        <span className="text-[7.5px] text-slate-400 font-mono block truncate">{activePass.toEn}</span>
                      </div>
                    </div>

                    {/* Highly requested Order ID directly below voyage route inside pass card block */}
                    <div className="my-2.5 bg-cyan-500/5 border border-[#1E2B61]/60 rounded-xl px-3 py-1.5 flex items-center justify-between">
                      <span className="text-[8.5px] text-slate-400 font-medium font-sans">
                        📋 {lang === "zh" ? "電子通行證訂單編號" : "E-Pass Order ID"}
                      </span>
                      <span className="font-mono font-black text-rose-400 text-[10.5px] tracking-widest bg-slate-950/40 px-2.5 py-0.5 rounded border border-slate-900/50">
                        {activePass.ticketId}
                      </span>
                    </div>

                    {/* Ticket Details Grid */}
                    <div className="grid grid-cols-2 gap-y-3 gap-x-2 text-[10px] bg-[#070D22]/50 p-3 rounded-xl border border-slate-800/60 font-sans">
                      <div>
                        <span className="block text-[8px] text-slate-500 uppercase font-mono tracking-wider">{lang === "zh" ? "客輪班次" : "Ferry Vessel"}</span>
                        <span className="font-bold text-slate-200">{activePass.vesselName}</span>
                      </div>
                      
                      <div>
                        <span className="block text-[8px] text-slate-500 uppercase font-mono tracking-wider">{lang === "zh" ? "出發班期" : "Voyage Time"}</span>
                        <span className="font-mono font-bold text-cyan-300">{activePass.departureDate} @ {activePass.departureTime}</span>
                      </div>

                      <div>
                        <span className="block text-[8px] text-slate-500 uppercase font-mono tracking-wider">{lang === "zh" ? "分配客艙座席" : "Cabin Seat Row"}</span>
                        <span className="font-mono font-black text-emerald-400 text-xs">{activePass.seat}</span>
                      </div>

                      <div>
                        <span className="block text-[8px] text-slate-500 uppercase font-mono tracking-wider">{lang === "zh" ? "中文實名旅客" : "Passenger Card"}</span>
                        <span className="font-bold text-slate-200">{activePass.passengerName}</span>
                        {activePass.isConcession && <span className="text-[7px] text-amber-400 block font-mono">{lang === "zh" ? "(半票/優待票)" : "(Concession)"}</span>}
                      </div>

                      <div>
                        <span className="block text-[8px] text-slate-500 uppercase font-mono tracking-wider">{lang === "zh" ? "客輪登船閘口" : "Terminal Gate"}</span>
                        <span className="font-mono font-bold text-white">{activePass.gate}</span>
                      </div>

                      <div>
                        <span className="block text-[8px] text-slate-500 uppercase font-mono tracking-wider">{lang === "zh" ? "購票實付額" : "Seat Fare Price"}</span>
                        <span className="font-mono font-bold text-emerald-400">NTD ${activePass.price}</span>
                      </div>
                    </div>

                    {/* Perforated Divider Line with wavy tickets cutout */}
                    <div className="relative my-4 flex items-center justify-between pointer-events-none">
                      {/* Left Cutout */}
                      <div className="absolute -left-6 w-4 h-5 bg-[#0A0F24] rounded-r-full border-r border-[#1F2C61] z-10"></div>
                      {/* Right Cutout */}
                      <div className="absolute -right-6 w-4 h-5 bg-[#0A0F24] rounded-l-full border-l border-[#1F2C61] z-10"></div>
                      {/* Dashed line */}
                      <div className="w-full border-b border-dashed border-[#1D2C61] h-0 mx-2"></div>
                    </div>

                    {/* Electronic Barcode / QR validation card */}
                    <div className="bg-[#030715]/95 border border-slate-800 rounded-xl p-3.5 text-center relative overflow-hidden">
                      {/* Laser red scanning overlay bar */}
                      {activePass.status === 'Ready' && (
                        <div className="absolute left-0 right-0 h-0.5 bg-red-500 shadow-[0_0_8px_rgba(239,68,68,0.8)] opacity-70 animate-pulse z-10" style={{ top: '45%' }}></div>
                      )}

                      <div className="bg-white p-2.5 rounded-xl inline-block shadow-inner relative max-w-[150px]">
                        <QrCode className="w-28 h-28 text-slate-950 block" />
                      </div>

                      <div className="mt-2 text-center">
                        <span className="text-[7px] text-slate-500 uppercase font-mono tracking-widest block leading-none">{lang === "zh" ? "台灣海洋航港局安全登船防偽審密簽" : "REAL-NAME ID CHECK · SECURE PASS CODE"}</span>
                        <span className="font-mono font-black text-slate-400 text-[9.5px] tracking-widest block mt-1">SEC-{activePass.ticketId}-MEMBER</span>
                      </div>

                      {/* Travel Rules Warnings */}
                      <div className="mt-3.5 text-left border-t border-slate-900 pt-3 space-y-1.5 text-[8px] leading-normal text-slate-400 font-sans">
                        <p className="flex gap-1">
                          <span className="text-yellow-400 font-bold shrink-0">⚠️ 國內實名制：</span>
                          <span>
                            {lang === "zh" 
                              ? "乘船請務必攜帶身分證、健保卡或護照正本以備航安實名核對驗對證件。" 
                              : "NHI ID, Passport, or Health Card required at security inspection scanner."}
                          </span>
                        </p>
                        <p className="flex gap-1">
                          <span className="text-cyan-400 font-bold shrink-0">⏰ 閘口截止：</span>
                          <span>
                            {lang === "zh" 
                              ? "開航前 30 分鐘關閉閘門進行載重核算登船登記，逾時不候。" 
                              : "Boarding closes exactly 30 minutes before departure for weight and balance calculation."}
                          </span>
                        </p>
                      </div>
                    </div>

                    {/* Synergy Action Button to instantly prompt AI Consultant */}
                    <button
                      type="button"
                      onClick={() => {
                        setPassengerName(activePass.passengerName);
                        // Safe ports mappings to update main selected route
                        const originTerm = activePass.fromEn.includes("Taitung") ? "Taitung" : activePass.fromEn.includes("Pingtung") ? "Pingtung" : activePass.fromEn.includes("Kaohsiung") ? "Kaohsiung" : "Chiayi";
                        const targetTerm = activePass.toEn.includes("Green Island") ? "Green Island" : activePass.toEn.includes("Xiaoliuqiu") ? "Xiaoliuqiu" : activePass.toEn.includes("Cijin") ? "Cijin" : "Penghu";
                        setDeparture(originTerm);
                        setDestination(targetTerm);
                        setActiveTab('advisor');
                        setTimeout(() => {
                          handleQueryAI(lang === "zh" ? "防暈吐特快攻略與座位建議、名產美食景點" : "Seasickness tips and delicacies for this route");
                        }, 250);
                      }}
                      className="w-full mt-3.5 bg-gradient-to-r from-amber-500 to-yellow-400 hover:from-amber-400 hover:to-yellow-300 text-slate-950 font-black rounded-xl py-2.5 px-3 text-[9.5px] transition-all flex items-center justify-center gap-1.5 shadow-md active:scale-95 cursor-pointer font-sans"
                    >
                      <Sparkles className="w-3.5 h-3.5" />
                      <span>
                        {lang === "zh" 
                          ? `諮詢「${activePass.vesselName.split(" ")[0]}」防暈吐與必玩秘笈` 
                          : `Consult Gemini on "${activePass.vesselNameEn}" Safety`}
                      </span>
                    </button>
                  </div>
                );
              })()}
            </div>
          )}

          {/* VIEW TAB 3: Gemini Smart AI Advisor - 「藝海助瀾」 */}
          {activeTab === 'advisor' && (
            <div className="space-y-4">
              
              {/* Gemini Smart AI Consultant Panel */}
              <div className="bg-gradient-to-tr from-[#16214B] to-[#0D1635] border border-amber-500/40 rounded-2xl p-4 shadow-lg relative overflow-hidden">
                <div className="absolute right-0 top-0 text-amber-500/10 pointer-events-none">
                  <Sparkles className="w-16 h-16 animate-pulse" />
                </div>

                <div className="flex items-center gap-2 mb-3 border-b border-[#212E5B]/50 pb-2.5">
                  <div className="bg-amber-400 text-slate-900 p-1.5 rounded-lg">
                    <Sparkles className="w-4 h-4" />
                  </div>
                  <div>
                    <h3 className="font-bold text-xs text-white">
                      {lang === "zh" ? "「藝海助瀾」AI 航務隨行顧問" : "Bilingual Gemini AI Advisor"}
                    </h3>
                    <p className="text-[8px] text-slate-400">Powered by Gemini 3.5-flash · Live Marine Analytics</p>
                  </div>
                </div>

                {/* Selected Active Route Info */}
                <div className="bg-[#080D21]/80 p-2.5 rounded-xl border border-[#1F2C61] flex items-center justify-between mb-3.5">
                  <div className="text-[10px] text-slate-300">
                    <span className="text-[8px] text-amber-400 font-bold block uppercase tracking-wider">{lang === "zh" ? "首選觀測航線" : "Observation Route"}</span>
                    <span className="font-bold">
                      {lang === "zh" ? `${depPortObj.zh} ↔ ${destPortObj.zh}` : `${depPortObj.en} ↔ ${destPortObj.en}`}
                    </span>
                  </div>
                  <div className="text-right">
                    <span className="text-[8px] text-slate-400 block">{lang === "zh" ? "海安評等" : "Safety Rating"}</span>
                    <span className="text-yellow-400 font-mono font-bold text-[10px]">{"★".repeat(marine.stars)} ({marine.stars}/5)</span>
                  </div>
                </div>

                <p className="text-[10px] text-slate-300 leading-normal mb-3">
                  {lang === "zh" 
                    ? "自動為您分析今日起迄海域與防暈要訣，客製化觀光行程、名產美食及實名安檢政策。" 
                    : "Obtain customized weather advisories, terminal boarding checklists and island food guides via Gemini."}
                </p>

                <div className="space-y-3.5">
                  <div>
                    <label className="text-[9px] font-bold text-amber-400 font-mono block mb-1">
                      {lang === "zh" ? "尊稱 / 搭乘姓名" : "Passenger Name"}
                    </label>
                    <input
                      type="text"
                      value={passengerName}
                      onChange={(e) => setPassengerName(e.target.value)}
                      placeholder={lang === "zh" ? "例如：陳小明" : "E.g. Joe Carter"}
                      className="w-full bg-[#080D22] border border-[#273B76] text-white rounded-xl py-2 px-3 text-xs focus:ring-1 focus:ring-amber-400"
                    />
                  </div>

                  {/* Quick Topics Grid */}
                  <div>
                    <label className="text-[9px] font-bold text-slate-400 block mb-1.5 uppercase font-mono tracking-wider">
                      🎯 {lang === "zh" ? "智慧快捷主題諮詢 (點選直接發問)" : "Quick Topic Chips (Tap to consult)"}
                    </label>
                    <div className="grid grid-cols-2 gap-2">
                      {[
                        { titleZh: "🤢 客輪防暈吐秘笈", titleEn: "🤢 Seasickness Tips", queryZh: "防暈吐特快攻略與座位建議", queryEn: "Anti-seasickness advice" },
                        { titleZh: "🦞 本地名產與景點", titleEn: "🦞 Island Delicacies", queryZh: "當地的海鮮名產美食與必玩景點推薦", queryEn: "Island delicacies & attractions" },
                        { titleZh: "🌤️ 海象安全大剖析", titleEn: "🌤️ Wave Weather", queryZh: "今日海象雷達指標與適航評估", queryEn: "Live wave weather details" },
                        { titleZh: "🎫 實名登船安檢要求", titleEn: "🎫 Scanner Policies", queryZh: "登船實名制攜帶證件與安檢時限規定", queryEn: "Id checks & boarding policies" }
                      ].map((item, id) => (
                        <button
                          key={id}
                          type="button"
                          onClick={() => handleQueryAI(lang === "zh" ? item.queryZh : item.queryEn)}
                          className="text-left p-2 rounded-xl bg-[#080D22]/60 hover:bg-[#15234F] border border-[#233367] hover:border-amber-400 transition-all text-[9.5px] leading-tight text-slate-200 cursor-pointer"
                        >
                          <span className="font-semibold block">{lang === "zh" ? item.titleZh : item.titleEn}</span>
                          <span className="text-[7.5px] text-slate-400 block mt-1 font-mono italic">TAP TO CONSULT</span>
                        </button>
                      ))}
                    </div>
                  </div>

                  {/* Custom Input Query Section */}
                  <div className="border-t border-[#1E2D64]/60 pt-3.5 mt-1">
                    <label className="text-[9px] font-bold text-amber-400 block mb-1.5 uppercase font-mono tracking-wider">
                      💬 {lang === "zh" ? "自訂輸入問題探索更多 (由 Gemini 支援)" : "Or ask your custom question (Powered by Gemini)"}
                    </label>
                    <div className="relative flex items-center">
                      <input
                        type="text"
                        value={aiCustomQuestion}
                        onChange={(e) => setAiCustomQuestion(e.target.value)}
                        onKeyDown={(e) => {
                          if (e.key === 'Enter' && aiCustomQuestion.trim() && !aiLoading) {
                            handleQueryAI(aiCustomQuestion);
                          }
                        }}
                        placeholder={lang === "zh" ? "例如：帶寵物搭船有什麼規定？" : "E.g. What about traveling with pets?"}
                        className="w-full bg-[#080D22] border border-[#273B76] text-white rounded-xl py-2 px-3.5 pr-10 text-xs focus:ring-1 focus:ring-amber-400 focus:outline-none placeholder-slate-500 font-sans"
                      />
                      <button
                        type="button"
                        onClick={() => {
                          if (aiCustomQuestion.trim() && !aiLoading) {
                            handleQueryAI(aiCustomQuestion);
                          }
                        }}
                        disabled={aiLoading || !aiCustomQuestion.trim()}
                        className="absolute right-1.5 p-1.5 text-amber-400 hover:text-amber-300 disabled:text-slate-600 transition-all cursor-pointer"
                        title={lang === "zh" ? "傳送詢問" : "Send query"}
                      >
                        <Send className="w-4 h-4" />
                      </button>
                    </div>
                  </div>

                  <button
                    onClick={() => handleQueryAI()}
                    disabled={aiLoading}
                    className="w-full bg-gradient-to-r from-amber-500 to-yellow-400 hover:from-amber-400 hover:to-yellow-300 text-slate-950 font-extrabold rounded-xl py-2.5 px-3 text-xs transition-all flex items-center justify-center gap-1.5 active:scale-95 shadow-md shadow-amber-950/20 cursor-pointer"
                  >
                    {aiLoading ? (
                      <>
                        <RefreshCw className="w-3.5 h-3.5 animate-spin" />
                        <span>{lang === "zh" ? "正在建立加密航務評估..." : "Formulating Itinerary..."}</span>
                      </>
                    ) : (
                      <>
                        <Sparkles className="w-3.5 h-3.5" />
                        <span>{lang === "zh" ? "諮詢通用全方位推薦報告" : "GENERATE ALL-IN-ONE ADVICE REPORT"}</span>
                      </>
                    )}
                  </button>
                </div>

                {/* AI Advice Box */}
                {(aiResponse || aiLoading) && (
                  <div className="mt-3.5 bg-[#060A1D] border border-amber-500/20 rounded-xl p-3 max-h-[300px] overflow-y-auto relative">
                    {aiLoading ? (
                      <div className="py-8 text-center space-y-2 flex flex-col items-center justify-center">
                        <Waves className="w-6 h-6 text-amber-400 animate-bounce" />
                        <span className="text-[10px] text-slate-300 animate-pulse font-mono">
                          {lang === "zh" ? "正在連線 Gemini 智慧海事引擎..." : "Connecting Gemini maritime AI..."}
                        </span>
                      </div>
                    ) : (
                      <div className="text-[10px] text-slate-300 leading-relaxed font-sans space-y-2">
                        {aiResponse.split('\n').map((line, lIdx) => {
                          if (line.startsWith('###')) {
                            return <h4 key={lIdx} className="text-[11px] font-bold text-amber-300 mt-2 border-b border-slate-700 pb-0.5">{line.replace('###', '')}</h4>;
                          } else if (line.startsWith('-') || line.startsWith('*')) {
                            return <li key={lIdx} className="ml-3 list-disc text-slate-300">{line.substring(2)}</li>;
                          } else {
                            return <p key={lIdx}>{line}</p>;
                          }
                        })}
                      </div>
                    )}
                  </div>
                )}
              </div>

            </div>
          )}

        </div>

        {/* Calendar Integration & Success Modal Bottom Sheet */}
        {isSuccessModalOpen && latestBookedTicket && (
          <div className="absolute inset-0 bg-slate-950/90 backdrop-blur-md z-50 flex flex-col justify-end transition-all duration-300">
            {/* Tap backdrop to close */}
            <div className="absolute inset-0 cursor-pointer" onClick={() => { setIsSuccessModalOpen(false); setActiveTab('tickets'); }}></div>
            
            {/* Sheet container */}
            <div className="bg-[#121A3C] border-t border-cyan-500/30 rounded-t-[32px] p-5 shadow-[0_-20px_50px_rgba(0,0,0,0.5)] z-10 max-h-[90%] overflow-y-auto space-y-4 font-sans relative">
              {/* Grab handle bar */}
              <div className="w-12 h-1.5 bg-slate-700 rounded-full mx-auto mb-2"></div>
              
              {/* Header title */}
              <div className="text-center space-y-1">
                <div className="w-12 h-12 bg-emerald-500/15 rounded-full flex items-center justify-center mx-auto text-emerald-400 border border-emerald-500/30 mb-2">
                  <CheckCircle2 className="w-6 h-6 animate-pulse" />
                </div>
                <h3 className="text-sm font-black text-white">
                  {lang === "zh" ? "🎉 席位預訂購票成功！" : "🎉 Booking Successfully Confirmed!"}
                </h3>
                <div className="bg-cyan-500/10 border border-cyan-500/20 rounded-lg py-1 px-3 inline-block mt-1">
                  <p className="text-[10px] text-cyan-300 font-extrabold font-mono">
                    {lang === "zh" ? "📋 您的訂單編號：" : "📋 Booking ID: "}{latestBookedTicket.ticketId}
                  </p>
                </div>
              </div>

              {/* Info Card Summary */}
              <div className="bg-[#0A0F24] rounded-xl p-3 border border-slate-800 text-[10px] space-y-2">
                <div className="flex justify-between">
                  <span className="text-slate-500">{lang === "zh" ? "乘船旅客" : "Passenger"}</span>
                  <span className="font-bold text-slate-200">{latestBookedTicket.passengerName}</span>
                </div>
                <div className="flex justify-between">
                  <span className="text-slate-500">{lang === "zh" ? "客輪船名" : "Ferry Vessel"}</span>
                  <span className="font-bold text-slate-200">{latestBookedTicket.vesselName}</span>
                </div>
                <div className="flex justify-between">
                  <span className="text-slate-500">{lang === "zh" ? "出發班期" : "Departure"}</span>
                  <span className="font-semibold text-cyan-300 font-mono">
                    {latestBookedTicket.departureDate} @ {latestBookedTicket.departureTime}
                  </span>
                </div>
                <div className="flex justify-between">
                  <span className="text-slate-500">{lang === "zh" ? "客艙座席" : "Cabin Seat"}</span>
                  <span className="font-bold text-emerald-400 font-mono">{latestBookedTicket.seat}</span>
                </div>
                <div className="flex justify-between">
                  <span className="text-slate-500">{lang === "zh" ? "搭乘閘口" : "Terminal Gate"}</span>
                  <span className="font-bold text-slate-300 font-mono">{latestBookedTicket.gate}</span>
                </div>
              </div>

              {/* Calendar Interactive Blocks */}
              <div className="space-y-2">
                <span className="text-[9px] text-slate-400 font-bold uppercase tracking-wider block">
                  離開前，您可以：
                </span>
                
                <span className="text-[9px] text-slate-400 font-bold uppercase tracking-wider block">
                  📅 {lang === "zh" ? "新增至個人行事曆" : "Add to Mobile Calendar"}
                </span>
                
                <div className="grid grid-cols-2 gap-2">
                  <a
                    href={getGoogleCalendarLink(latestBookedTicket)}
                    target="_blank"
                    rel="noopener noreferrer"
                    className="flex items-center justify-center gap-1.5 p-2 rounded-xl bg-slate-900 hover:bg-slate-800 border border-slate-700 hover:border-slate-600 transition-all text-[10px] text-slate-200 font-bold"
                  >
                    <ExternalLink className="w-3.5 h-3.5 text-cyan-400" />
                    <span>Google Calendar</span>
                  </a>

                  <button
                    type="button"
                    onClick={() => handleDownloadICS(latestBookedTicket)}
                    className="flex items-center justify-center gap-1.5 p-2 rounded-xl bg-slate-900 hover:bg-slate-800 border border-slate-700 hover:border-slate-600 transition-all text-[10px] text-slate-200 font-bold cursor-pointer"
                  >
                    <Download className="w-3.5 h-3.5 text-cyan-400" />
                    <span>Apple / Outlook</span>
                  </button>
                </div>
              </div>

              {/* Better customizable notification scheduling section */}
              <div className="space-y-3 pt-1 border-t border-slate-800">
                <div className="flex justify-between items-center">
                  <span className="text-[9px] text-slate-400 font-bold uppercase tracking-wider block">
                    🔔 {lang === "zh" ? "自訂智慧實名安檢推播提醒時間" : "Custom Alarm & Check-in Alert Time"}
                  </span>
                  
                  {/* Standard permissions status tag */}
                  <span className="text-[8px] px-1.5 py-0.5 rounded bg-emerald-500/10 text-emerald-400 border border-emerald-500/20 font-mono">
                    {lang === "zh" ? "● 背景通道就緒" : "● PUSH ENGINE RUNNING"}
                  </span>
                </div>

                <div className="bg-slate-950/70 rounded-xl p-3 border border-[#1E2B61]/60 space-y-3">
                  {/* Custom Pill Timing Selector Grid */}
                  <div>
                    <span className="text-[8.5px] text-slate-400 block mb-2">
                      {lang === "zh" ? "⏳ 選擇您預期接收智慧航安提醒的時間：" : "Select when you want to get notified before ship departure:"}
                    </span>
                    
                    <div className="grid grid-cols-5 gap-1">
                      {[
                        { id: "24h", zh: "24小時", en: "24h" },
                        { id: "12h", zh: "12小時", en: "12h" },
                        { id: "3h", zh: "3小時", en: "3h" },
                        { id: "1h", zh: "1小時", en: "1h" },
                        { id: "30m", zh: "30分鐘", en: "30m" }
                      ].map(opt => (
                        <button
                          key={opt.id}
                          type="button"
                          onClick={() => {
                            setReminderTimeOption(opt.id);
                            // Auto trigger alert refresh if already ticked
                            if (reminderScheduledMap[latestBookedTicket.ticketId]) {
                              handleScheduleReminder(latestBookedTicket, opt.id);
                            }
                          }}
                          className={`py-1.5 rounded-lg text-[9px] font-black transition-all ${
                            reminderTimeOption === opt.id 
                              ? 'bg-cyan-500 text-slate-950 shadow-md shadow-cyan-500/10'
                              : 'bg-slate-900 border border-slate-800 text-slate-400 hover:text-slate-200 hover:border-slate-700'
                          }`}
                        >
                          {lang === "zh" ? opt.zh : opt.en}
                        </button>
                      ))}
                    </div>

                    {/* Intelligent suggestion text under selection */}
                    <div className="mt-1.5 text-[8.5px] text-cyan-400/90 font-medium">
                      {lang === "zh" ? (
                        reminderTimeOption === "24h" && "💡 建議值！適合備妥中華民國身分證/健保卡或護照供航安實名核對"
                      ) : "💡 Best to prepare Passport or local IDs for pre-departure verification."}
                      {lang === "zh" ? (
                        reminderTimeOption === "12h" && "💡 最佳準備點！適合調配防暈吐特效藥並進行最後抵達碼頭車班客運規劃"
                      ) : ""}
                      {lang === "zh" ? (
                        reminderTimeOption === "3h" && "💡 快速確認點！適合出發至港口前，最後檢查攜行行李與安檢狀態"
                      ) : ""}
                      {lang === "zh" ? (
                        reminderTimeOption === "1h" && "💡 臨行警告！建議此刻已在港區或辦理報到，準備在開航後半小時內完成安檢"
                      ) : ""}
                      {lang === "zh" ? (
                        reminderTimeOption === "30m" && "⚠️ 臨界危機值！登船閘門於開航前半小時內可能鎖定，不適宜此極端提醒時間"
                      ) : ""}
                    </div>
                  </div>

                  {/* Trigger Scheduling Controls Row */}
                  <div className="flex items-center justify-between pt-2 border-t border-slate-900">
                    <div className="space-y-0.5">
                      <span className="text-[10px] font-bold text-slate-200 block">
                        {lang === "zh" ? "自動排定伺服器推播任務" : "Automated push scheduler"}
                      </span>
                      <span className="text-[8px] text-slate-500 block leading-tight">
                        {lang === "zh" ? "關閉螢幕、甚至未開啟 App 亦可準時響起提醒" : "Receive real alerts background-wide on lockout"}
                      </span>
                    </div>

                    <button
                      type="button"
                      onClick={() => handleScheduleReminder(latestBookedTicket, reminderTimeOption)}
                      className={`px-3 py-1.5 rounded-lg text-[9px] font-black transition-all ${
                        reminderScheduledMap[latestBookedTicket.ticketId]
                          ? 'bg-emerald-500/20 text-emerald-400 border border-emerald-500/30'
                          : 'bg-[#10B981] hover:bg-[#34D399] text-white'
                      }`}
                    >
                      {reminderScheduledMap[latestBookedTicket.ticketId] ? (lang === "zh" ? "✓ 已預排任務" : "✓ Alert Set") : (lang === "zh" ? "立即啟動排程" : "Activate Alarm")}
                    </button>
                  </div>

                  {/* Interactive Multi-Method Notification Testing Playground */}
                  {reminderScheduledMap[latestBookedTicket.ticketId] && (
                    <div className="border-t border-slate-900 pt-2 space-y-2">
                      <div className="flex items-center justify-between">
                        <span className="text-[8.5px] text-emerald-400/95 font-bold flex items-center gap-1">
                          <span className="w-1.5 h-1.5 bg-emerald-400 rounded-full animate-ping"></span>
                          {lang === "zh" ? `📅 已排程，將於出發前 ${reminderTimeOption === "24h" ? "24小時" : reminderTimeOption === "12h" ? "12小時" : reminderTimeOption === "3h" ? "3小時" : reminderTimeOption === "1h" ? "1小時" : "30分鐘"} 送出警告` : `📅 Scheduled queue active!`}
                        </span>
                      </div>

                      {/* Interactive visual test selectors */}
                      <div className="grid grid-cols-2 gap-2">
                        {/* Option A: Standard Browser Notification */}
                        <button
                          type="button"
                          onClick={() => triggerRealNotification(latestBookedTicket, reminderTimeOption)}
                          className="bg-slate-900 hover:bg-slate-800 text-slate-200 border border-slate-800 hover:border-slate-700 text-[8px] py-1.5 px-2 rounded-lg flex items-center justify-center gap-1 transition-all cursor-pointer"
                        >
                          <Bell className="w-3 h-3 text-cyan-400" />
                          <span>{lang === "zh" ? "⚡ 測試網頁真實推播" : "Test Real Push"}</span>
                        </button>

                        {/* Option B: Sleep State and Locked Screen Simulation */}
                        <button
                          type="button"
                          onClick={() => handleLaunchLockScreenSimulation(latestBookedTicket, reminderTimeOption)}
                          className="bg-amber-500 hover:bg-amber-400 text-slate-950 font-black text-[8px] py-1.5 px-2 rounded-lg flex items-center justify-center gap-1 transition-all cursor-pointer animate-pulse shrink-0"
                        >
                          <Clock className="w-3 h-3 text-slate-950" />
                          <span>{lang === "zh" ? "📱 模擬睡前關機鎖屏提醒" : "Simulate Lock Screen Alert"}</span>
                        </button>
                      </div>

                      <p className="text-[7.5px] text-slate-500 leading-normal text-normal text-center bg-slate-950 p-1.5 rounded border border-slate-900">
                        {lang === "zh" 
                          ? "👆 關機鎖屏模擬會將虛擬手機全黑關閉 2.5 秒，接著於鎖定畫面亮起推播通知重現真實狀況！" 
                          : "Simulates mobile phone going off, then waking screen with push bar."}
                      </p>
                    </div>
                  )}
                </div>
              </div>

              {/* Done/Close Button */}
              <button
                type="button"
                onClick={() => {
                  setIsSuccessModalOpen(false);
                  setActiveTab('tickets');
                }}
                className="w-full bg-gradient-to-r from-cyan-500 to-indigo-600 hover:from-cyan-400 hover:to-indigo-500 text-white font-extrabold rounded-xl py-3 text-xs transition-all shadow-md active:scale-95 cursor-pointer block text-center"
              >
                {lang === "zh" ? "確認並進入隨行票卡" : "Done & Open Ticket Pass"}
              </button>
            </div>
          </div>
        )}

        {/* 1. IMMERSIVE PHONE BLACK SCREEN standby/sleep MODE */}
        {isPhoneScreenOff && (
          <div className="absolute inset-0 bg-[#000000]/98 z-[110] flex flex-col items-center justify-center p-6 text-center select-none animate-fadeIn transition-all duration-500">
            {/* Pulsing clock and scanner radar visual */}
            <div className="w-16 h-16 rounded-full bg-zinc-900 border border-zinc-800 flex items-center justify-center mb-6 shadow-2xl relative">
              <Clock className="w-6 h-6 text-slate-600 animate-pulse" />
              <div className="absolute inset-0 rounded-full border border-cyan-500/10 animate-ping"></div>
            </div>
            
            <p className="text-[10px] text-zinc-600 font-mono tracking-widest uppercase block mb-1">
              [ {lang === "zh" ? "螢幕待命休眠中" : "SCREEN STANDBY MODE"} ]
            </p>
            <h3 className="text-xs font-extrabold text-zinc-300 leading-relaxed">
              {lang === "zh" ? "📱 虛擬手機螢幕已關閉" : "📱 Simulated screen turned off"}
            </h3>
            <p className="text-[9px] text-zinc-500 leading-relaxed max-w-[210px] mt-2">
              {lang === "zh" 
                ? `系統已安排在航行出發「前 ${reminderTimeOption === "24h" ? "24小時" : reminderTimeOption === "12h" ? "12小時" : reminderTimeOption === "3h" ? "3小時" : reminderTimeOption === "1h" ? "1小時" : "30分鐘"}」主動發送推播！` 
                : "A Background task is waiting... Screen will wake up with an urgent real-name security check notification in 2 seconds."}
            </p>
            <p className="text-[8px] text-cyan-400 font-bold animate-pulse mt-4 bg-cyan-950/40 px-2 py-1 rounded border border-cyan-950">
              {lang === "zh" ? "⏳ 背景計時器倒數中 2.5 秒..." : "⏳ Dispensing background worker alert..."}
            </p>

            {/* Force wake up preview bypass button */}
            <button
              type="button"
              onClick={() => {
                setIsPhoneScreenOff(false);
                setIsPhoneLockedView(true);
                setShowSimulatedReminder(true);
                if (simulatedReminderTicket) {
                  triggerRealNotification(simulatedReminderTicket, reminderTimeOption);
                }
              }}
              className="mt-8 px-4 py-1.5 rounded-full border border-zinc-800 hover:border-zinc-700 bg-zinc-900 text-zinc-400 hover:text-zinc-200 text-[8.5px] transition-all cursor-pointer"
            >
              {lang === "zh" ? "⚡ 直接強行亮屏測試" : "⚡ Wake and bypass delay"}
            </button>
          </div>
        )}

        {/* 1.5. MANUAL IMMERSIVE PHONE SLEEP/BLACK SCREEN (With infinite persistence for test) */}
        {isPhoneScreenDarkManual && (
          <div 
            onClick={() => setIsPhoneScreenDarkManual(false)}
            className="absolute inset-0 bg-[#000000]/99 z-[115] flex flex-col items-center justify-center p-6 text-center select-none cursor-pointer duration-500"
          >
            {/* Extremely dark digital lock clock */}
            <div className="opacity-10 flex flex-col items-center space-y-1 mb-8">
              <span className="text-[10px] text-zinc-500 font-mono font-black">10:24</span>
              <div className="w-10 h-0.5 bg-zinc-850 rounded"></div>
            </div>

            <div className="w-12 h-12 rounded-full border border-zinc-900/80 bg-zinc-950/50 flex items-center justify-center mb-6 relative">
              <Moon className="w-4 h-4 text-zinc-800 animate-pulse" />
              <div className="absolute inset-0 rounded-full border border-zinc-900/10 animate-ping"></div>
            </div>
            
            <p className="text-[9px] text-[#22D3EE]/20 font-mono tracking-widest uppercase block mb-1">
              [ {lang === "zh" ? "螢幕已熄滅待命" : "SCREEN TURNED OFF"} ]
            </p>
            <h3 className="text-xs font-bold text-zinc-400 leading-relaxed max-w-[210px]">
              {lang === "zh" ? "📱 虛擬手機螢幕已手動關閉" : "Simulated dark sleep state"}
            </h3>
            <p className="text-[9px] text-zinc-600 leading-relaxed max-w-[200px] mt-2">
              {lang === "zh" 
                ? "💡 實名航安通知正完好存留於背景中！再次點擊螢幕任何處（代表重新點亮螢幕），您會發現同一個通知絕對不會消失。"
                : "The check-in notification is preserved safely in background buffer memory. Click anywhere to wake this screen back up!"}
            </p>

            {/* Interactive Wake Up Button */}
            <button
              type="button"
              onClick={(e) => {
                e.stopPropagation();
                setIsPhoneScreenDarkManual(false);
              }}
              className="mt-8 px-5 py-2 rounded-xl bg-cyan-950/40 hover:bg-cyan-900/60 text-cyan-400 hover:text-cyan-300 text-[10px] font-black border border-cyan-800/40 hover:border-cyan-500/50 transition-all cursor-pointer shadow-lg animate-bounce"
            >
              {lang === "zh" ? "💡 點選按此或碰觸任意處「重新開啟/亮屏」" : "💡 Click to wake up screen"}
            </button>
          </div>
        )}

        {/* 2. SIMULATED LOCK SCREEN VIEW */}
        {isPhoneLockedView && (
          <div className="absolute inset-0 bg-[#090B1E]/95 z-[105] flex flex-col justify-between p-6 select-none font-sans overflow-hidden transition-all duration-500">
            {/* Visual lockscreen neon decorations */}
            <div className="absolute -top-12 -left-12 w-48 h-48 rounded-full bg-cyan-950/20 blur-3xl"></div>
            <div className="absolute -bottom-16 -right-16 w-56 h-56 rounded-full bg-indigo-950/30 blur-3xl"></div>

            {/* High-fidelity lock status indicator */}
            <div className="w-full relative z-10 flex flex-col items-center pt-2 space-y-1">
              <div className="w-6 h-6 flex items-center justify-center bg-white/5 rounded-full border border-white/10 shadow-inner">
                <span className="text-[10px] text-white">🔒</span>
              </div>
              
              {/* Lockscreen clock and calendar date */}
              <div className="text-center">
                <p className="text-[10px] text-slate-300 font-bold uppercase tracking-widest">
                  {lang === "zh" ? "6月11日 星期四" : "Thursday, June 11"}
                </p>
                <h1 className="text-4xl font-extrabold tracking-tighter text-white mt-1">
                  10:24
                </h1>
                <p className="text-[7.5px] text-slate-400 font-mono uppercase tracking-widest mt-1">
                  {lang === "zh" ? "蓝海航安實名核對就緒" : "BlueOcean SafePass Engine Running"}
                </p>
              </div>
            </div>

            {/* Lock Screen Push Notification Card Holder */}
            <div className="w-full relative z-10 flex-grow flex flex-col justify-center py-4">
              {showSimulatedReminder && simulatedReminderTicket ? (
                <div 
                  onClick={() => {
                    // Clicking notification unlocks the phone and targets the ticket directly!
                    setIsPhoneLockedView(false);
                    setShowSimulatedReminder(false);
                    setIsSuccessModalOpen(false);
                    setSelectedPassId(simulatedReminderTicket.ticketId);
                    setActiveTab('tickets');
                  }}
                  className="bg-[#121A3C]/95 border border-[#1E2C61]/80 backdrop-blur-md rounded-2xl p-3.5 shadow-[0_15px_30px_rgba(0,0,0,0.6)] flex flex-col gap-2.5 cursor-pointer hover:border-cyan-400/50 transition-all text-slate-100 animate-slideUp relative"
                >
                  {/* Glowing alert line */}
                  <div className="absolute -left-0.5 top-5 bottom-5 w-1 bg-amber-400 rounded-r-lg"></div>

                  <div className="flex items-center justify-between border-b border-white/5 pb-1.5">
                    <div className="flex items-center gap-2">
                      <div className="bg-cyan-500 text-slate-950 p-1.5 rounded-xl shadow-md">
                        <Ship className="w-3 h-3" />
                      </div>
                      <div>
                        <span className="text-[10px] font-black text-white block">
                          {lang === "zh" ? "藍海智慧航安" : "BlueOcean Smart Pass"}
                        </span>
                        <span className="text-[7.5px] text-slate-400 font-mono block">
                          {lang === "zh" ? "實名登船管制系統" : "SYSTEM PUSH"}
                        </span>
                      </div>
                    </div>
                    <div className="flex items-center gap-1.5">
                      <span className="text-[7px] font-mono bg-cyan-400/10 text-cyan-400 px-1 rounded font-black">
                        {reminderTimeOption === "24h" ? (lang === "zh" ? "24小時前" : "24h") : reminderTimeOption === "12h" ? (lang === "zh" ? "12小時前" : "12h") : reminderTimeOption === "3h" ? (lang === "zh" ? "3小時前" : "3h") : reminderTimeOption === "1h" ? (lang === "zh" ? "1小時前" : "1h") : (lang === "zh" ? "30分鐘前" : "30m")}
                      </span>
                      <span className="text-[8.5px] text-slate-400 font-mono">{lang === "zh" ? "現在" : "now"}</span>
                    </div>
                  </div>

                  <div className="space-y-1 text-left">
                    <p className="text-[11px] font-black text-amber-300">
                      {lang === "zh" 
                        ? `✉️ 實名航安檢票通知：明天客輪「${simulatedReminderTicket.vesselName}」搭船重要提醒` 
                        : `✉️ Ferry Check-in Remind: Vessel "${simulatedReminderTicket.vesselNameEn}" voyage alert`}
                    </p>
                    <p className="text-[10px] text-slate-200 leading-normal">
                      {lang === "zh" 
                        ? `親愛的旅客 ${simulatedReminderTicket.passengerName}，您明日 ${simulatedReminderTicket.departureTime} 從 ${simulatedReminderTicket.fromZh} 開往 ${simulatedReminderTicket.toZh} 的客輪座席劃位已分配為「${simulatedReminderTicket.seat}」。`
                        : `Dear traveler ${simulatedReminderTicket.passengerName}, tomorrow departing to ${simulatedReminderTicket.toEn} is assigned seat "${simulatedReminderTicket.seat}".`}
                    </p>

                    <div className="bg-slate-950/60 p-2 rounded-lg border border-slate-900/40 text-[8.5px] text-slate-300 flex items-center justify-between">
                      <span>{lang === "zh" ? "🚪 登船閘口" : "🚪 Gate"}: <strong className="text-white font-mono">{simulatedReminderTicket.gate}</strong></span>
                      <span className="text-cyan-400">{lang === "zh" ? "⚠️ 身分證, 健保卡或護照" : "⚠️ National ID or Passport required"}</span>
                    </div>
                  </div>

                  <div className="text-center text-[8px] text-cyan-300 font-bold bg-white/5 py-1 rounded animate-pulse">
                    ✨ {lang === "zh" ? "👆 點選此通知：直接解鎖並進入隨行票卡" : "✨ TAP TO UNLOCK & GO TO MY TICKETS"}
                  </div>
                </div>
              ) : (
                <div className="text-center text-slate-500 text-[10px] font-mono select-none">
                  [ {lang === "zh" ? "暫無未讀航安通報" : "No newer alert logs"} ]
                </div>
              )}
            </div>

            {/* Lock screen swipe/dismiss action indicator with screen dark simulation option */}
            <div className="w-full relative z-10 flex flex-col items-center space-y-3.5">
              {/* Manual screen dim to test notification persistence */}
              <button
                type="button"
                onClick={(e) => {
                  e.stopPropagation();
                  setIsPhoneScreenDarkManual(true);
                }}
                className="bg-black/50 hover:bg-black/80 border border-white/10 hover:border-cyan-500/30 text-slate-300 hover:text-white px-3 py-1.5 rounded-full text-[9px] font-bold flex items-center justify-center gap-1.5 transition-all cursor-pointer shadow-md"
              >
                <Moon className="w-3 h-3 text-cyan-400 animate-pulse" />
                <span>{lang === "zh" ? "🔌 測試：模擬按電源鍵「手動關閉螢幕」並保留通知" : "🔌 Simulate Power Button (Keep Alerts)"}</span>
              </button>

              <button
                type="button"
                onClick={() => {
                  setIsPhoneLockedView(false);
                  setShowSimulatedReminder(false);
                }}
                className="text-[9.5px] font-extrabold text-slate-400 hover:text-white tracking-widest uppercase flex flex-col items-center gap-1.5 transition-all cursor-pointer"
              >
                <span>{lang === "zh" ? "▲ 按此或往上滑動直接進入 App" : "▲ TAP TO UNLOCK SCREEN"}</span>
                <span className="w-24 h-1 bg-white/20 rounded-full mt-1.5"></span>
              </button>
            </div>
          </div>
        )}

        {/* 3. SIMULATED STANDBY/SLIDEDOWN ALERTS (Shown when phone is unlocked) */}
        {!isPhoneLockedView && !isPhoneScreenOff && showSimulatedReminder && simulatedReminderTicket && (
          <div className="absolute top-3 left-3 right-3 z-[100] pointer-events-none">
            {/* Inline keyframe animation */}
            <style dangerouslySetInnerHTML={{__html: `
              @keyframes pushDown {
                0% { transform: translateY(-120%) scale(0.95); opacity: 0; }
                8% { transform: translateY(0) scale(1.02); opacity: 1; }
                12% { transform: translateY(0) scale(1); opacity: 1; }
                100% { transform: translateY(0); opacity: 1; }
              }
            `}} />
            
            <div 
              style={{ animation: 'pushDown 0.4s cubic-bezier(0.16, 1, 0.3, 1) forwards' }}
              className="bg-[#12183c]/95 border border-[#1E2B61]/80 backdrop-blur-md rounded-2xl p-3.5 shadow-[0_15px_30px_rgba(0,0,0,0.6)] flex flex-col gap-2.5 pointer-events-auto relative text-slate-100 font-sans"
            >
              {/* Notification Header / App Banner Row */}
              <div className="flex items-center justify-between border-b border-white/5 pb-2">
                <div className="flex items-center gap-2">
                  {/* Smartphone app icon simulation with deep gold badge */}
                  <div className="relative">
                    <div className="bg-cyan-500 text-slate-950 p-1.5 rounded-xl shadow-glow">
                      <Ship className="w-3.5 h-3.5" />
                    </div>
                    <div className="absolute -top-1 -right-1 w-2.5 h-2.5 bg-rose-500 rounded-full border border-[#12183c] flex items-center justify-center">
                      <span className="animate-ping absolute inline-flex h-full w-full rounded-full bg-rose-400 opacity-75"></span>
                    </div>
                  </div>
                  <div>
                    <span className="text-[10px] font-black text-white block tracking-wide">
                      {lang === "zh" ? "藍海智慧航安" : "BlueOcean Smart Pass"}
                    </span>
                    <span className="text-[7.5px] text-slate-400 font-mono block">
                      {lang === "zh" ? "實名安檢系統通知" : "SYSTEM MESSAGE"}
                    </span>
                  </div>
                </div>
                
                <div className="flex items-center gap-2">
                  <span className="text-[8px] bg-cyan-500/10 text-cyan-400 border border-cyan-500/20 px-1.5 py-0.2 rounded font-mono uppercase font-black tracking-widest animate-pulse">
                    {lang === "zh" ? "明日出發" : "TOMORROW"}
                  </span>
                  <span className="text-[8.5px] font-medium text-slate-400 font-mono">
                    {lang === "zh" ? "現在" : "now"}
                  </span>
                </div>
              </div>

              {/* Notification Content Body */}
              <div className="space-y-1.5 text-left">
                <div className="flex items-center gap-1.5">
                  <span className="w-1.5 h-1.5 rounded-full bg-amber-400 block animate-ping shrink-0"></span>
                  <p className="text-[11px] font-black text-amber-300">
                    {lang === "zh" 
                      ? `✉️ 乘船安檢提醒：明日「${simulatedReminderTicket.vesselName.split(" ")[0]}」旅客乘船提示`
                      : `✉️ Embarkation alert: "${simulatedReminderTicket.vesselNameEn}" voyage reminder`}
                  </p>
                </div>
                <p className="text-[10px] text-slate-200 leading-relaxed">
                  {lang === "zh" 
                    ? `親愛的旅客 ${simulatedReminderTicket.passengerName} 您好：您訂購的 ${simulatedReminderTicket.fromZh} ➔ ${simulatedReminderTicket.toZh} 客輪班次明日 ${simulatedReminderTicket.departureTime} 準時開航，座席已劃位安排至：`
                    : `Dear voyager ${simulatedReminderTicket.passengerName}, your ferry departing tomorrow at ${simulatedReminderTicket.departureTime} from ${simulatedReminderTicket.fromEn} to ${simulatedReminderTicket.toEn} has allocated seat:`}
                </p>

                {/* Simulated message details snippet badge */}
                <div className="bg-[#080D24] border border-cyan-500/10 rounded-xl p-2.5 my-1 grid grid-cols-3 gap-2 text-center text-slate-300 font-sans">
                  <div>
                    <span className="text-[7.5px] text-slate-500 uppercase font-mono block">{lang === "zh" ? "客艙" : "Cabin"}</span>
                    <span className="text-[10px] font-black font-mono text-emerald-400">{simulatedReminderTicket.seat}</span>
                  </div>
                  <div>
                    <span className="text-[7.5px] text-slate-500 uppercase font-mono block">{lang === "zh" ? "登機門" : "Gate"}</span>
                    <span className="text-[10px] font-bold font-mono text-white">{simulatedReminderTicket.gate}</span>
                  </div>
                  <div>
                    <span className="text-[7.5px] text-slate-500 uppercase font-mono block">{lang === "zh" ? "核驗實名" : "IDs"}</span>
                    <span className="text-[9px] font-bold text-cyan-400 truncate block">{lang === "zh" ? "雙證件" : "Passport"}</span>
                  </div>
                </div>

                <p className="text-[8.5px] text-slate-400 leading-normal bg-slate-900/40 p-2 rounded-lg border border-slate-800">
                  {lang === "zh" 
                    ? "⚠️ 請攜帶身分證、健保卡或護照正本，並於明日航次出發前 30 分鐘抵達閘口進行防偽驗簽。開航前半小時閘門即關閉進行噸位平衡審核。"
                    : "⚠️ Document check: Passport or National ID required. Boarding gates close 30 minutes before departure."}
                </p>
              </div>

              {/* iOS Style Action Buttons */}
              <div className="grid grid-cols-3 gap-2 pt-1 border-t border-white/5">
                <button
                  type="button"
                  onClick={() => {
                    setShowSimulatedReminder(false);
                    setIsSuccessModalOpen(false);
                    // Switch tab to ticket pass
                    setSelectedPassId(simulatedReminderTicket.ticketId);
                    setActiveTab('tickets');
                  }}
                  className="bg-[#1C254B] hover:bg-[#253266] text-white font-extrabold text-[9px] py-1.5 rounded-xl transition-all cursor-pointer text-center flex items-center justify-center gap-1"
                >
                  <Layers className="w-3 h-3 text-cyan-400" />
                  <span>{lang === "zh" ? "開啟票卡" : "Open Pass"}</span>
                </button>

                <button
                  type="button"
                  onClick={() => {
                    setShowSimulatedReminder(false);
                    setIsSuccessModalOpen(false);
                    setActiveTab('advisor');
                    // Query AI advisor after short delay
                    setTimeout(() => {
                      handleQueryAI(lang === "zh" ? "防暈吐特快攻略與座位建議、名產美食景點" : "Seasickness tips and delicacies for this route");
                    }, 250);
                  }}
                  className="bg-gradient-to-r from-amber-500 to-yellow-400 hover:from-amber-400 hover:to-yellow-300 text-slate-950 font-black text-[9px] py-1.5 rounded-xl transition-all cursor-pointer text-center flex items-center justify-center gap-1"
                >
                  <Sparkles className="w-2.5 h-2.5" />
                  <span>{lang === "zh" ? "專家防暈" : "AI advice"}</span>
                </button>

                <button
                  type="button"
                  onClick={() => setShowSimulatedReminder(false)}
                  className="bg-[#1A1F38] hover:bg-[#252C4E] text-slate-400 hover:text-white font-medium text-[9px] py-1.5 rounded-xl transition-all cursor-pointer text-center"
                >
                  {lang === "zh" ? "關閉訊息" : "Dismiss"}
                </button>
              </div>

            </div>
          </div>
        )}

        {/* 📲 PHONE STICKY BOTTOM APP NAV BAR ACCORDING TO USER FLOW */}
        <nav className="absolute bottom-0 inset-x-0 bg-[#0E1533] border-t border-slate-800 py-2.5 pb-6 px-4 flex justify-around items-center z-40 shadow-[0_-8px_20px_rgba(0,0,0,0.4)]">
          <button
            onClick={() => setActiveTab('timetables')}
            className={`flex flex-col items-center gap-1 transition-colors cursor-pointer ${
              activeTab === 'timetables' ? 'text-cyan-400' : 'text-slate-400 hover:text-slate-100'
            }`}
          >
            <Activity className="w-4 h-4" />
            <span className="text-[8px] font-bold">
              {lang === "zh" ? "船班海象" : "Ocean Maps"}
            </span>
          </button>

          <button
            onClick={() => setActiveTab('booking')}
            className={`flex flex-col items-center gap-1 transition-colors cursor-pointer ${
              activeTab === 'booking' ? 'text-cyan-400' : 'text-slate-400 hover:text-slate-100'
            }`}
          >
            <CreditCard className="w-4 h-4" />
            <span className="text-[8px] font-bold">
              {lang === "zh" ? "預訂劃位" : "Ferry Seats"}
            </span>
          </button>

          <button
            onClick={() => setActiveTab('tickets')}
            className={`flex flex-col items-center gap-1 transition-colors cursor-pointer ${
              activeTab === 'tickets' ? 'text-cyan-400' : 'text-slate-400 hover:text-slate-100'
            }`}
          >
            <QrCode className="w-4 h-4" />
            <span className="text-[8px] font-bold">
              {lang === "zh" ? "隨行票卡" : "My Passes"}
            </span>
          </button>

          <button
            onClick={() => setActiveTab('advisor')}
            className={`flex flex-col items-center gap-1 transition-colors cursor-pointer ${
              activeTab === 'advisor' ? 'text-cyan-400 animate-pulse' : 'text-slate-400 hover:text-slate-100'
            }`}
          >
            <Sparkles className="w-4 h-4" />
            <span className="text-[8px] font-bold">
              {lang === "zh" ? "藝海助瀾" : "AI Advisor"}
            </span>
          </button>
        </nav>

        {/* Home swipe indicator line bar */}
        <div className="absolute bottom-1 right-1/2 translate-x-1/2 w-32 h-1 bg-zinc-800 rounded-full z-50"></div>

      </div>

      {/* Mobile-oriented clean minimal footer (without NKUST) */}
      <footer className="mt-6 text-center text-slate-500 text-[10px] font-mono leading-normal">
        <p>© 2026 Taiwan Ferry Booking & Intelligent Marine Service Corp. All rights reserved.</p>
        <p className="mt-0.5 text-slate-600">Bilingual Gemini AI Marine Consultant Integrated System.</p>
      </footer>

    </div>
  );
}
