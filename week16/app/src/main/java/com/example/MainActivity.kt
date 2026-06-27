package com.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.FerryBooking
import com.example.ui.FerryViewModel
import com.example.ui.theme.*
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    viewModel: FerryViewModel = viewModel()
) {
    val context = LocalContext.current
    
    // Explicitly unwrap state value to be 100% robust against delegate resolution anomalies
    val isEng = viewModel.isEnglish.collectAsState().value
    val region = viewModel.selectedRegion.collectAsState().value
    val departurePorts = viewModel.departurePorts.collectAsState().value
    val selectedDep = viewModel.selectedDeparture.collectAsState().value
    val destinationPorts = viewModel.destinationPorts.collectAsState().value
    val selectedDest = viewModel.selectedDestination.collectAsState().value
    
    val passengerName = viewModel.passengerName.collectAsState().value
    val isConcession = viewModel.isConcession.collectAsState().value
    val selectedSeat = viewModel.selectedSeat.collectAsState().value
    val occupiedSeats = viewModel.occupiedSeats.collectAsState().value
    
    val waveHeight = viewModel.waveHeight.collectAsState().value
    val windSpeed = viewModel.windSpeed.collectAsState().value
    val visibility = viewModel.visibility.collectAsState().value
    val safetyStars = viewModel.safetyStars.collectAsState().value
    val safetyNote = viewModel.safetyNote.collectAsState().value

    val activeBooking = viewModel.activeBooking.collectAsState().value
    val isAiLoading = viewModel.isAiLoading.collectAsState().value
    val aiResponse = viewModel.aiResponse.collectAsState().value
    val feedbackMessage = viewModel.feedbackMessage.collectAsState().value
    val pastBookings = viewModel.pastBookings.collectAsState().value

    val keyboardController = LocalSoftwareKeyboardController.current

    var showDepDialog by remember { mutableStateOf(false) }
    var showDestDialog by remember { mutableStateOf(false) }
    var activeTab by rememberSaveable { mutableStateOf(0) }

    LaunchedEffect(feedbackMessage) {
        feedbackMessage?.let { mess ->
            Toast.makeText(context, mess, Toast.LENGTH_LONG).show()
            viewModel.clearFeedbackMessage()
        }
    }

    LaunchedEffect(activeBooking) {
        if (activeBooking != null) {
            activeTab = 2 // Auto-navigate to My Tickets on book success
        }
    }

    val regions = listOf("台東", "屏東", "高雄", "嘉義")
    val regionsEng = mapOf("台東" to "Taitung", "屏東" to "Pingtung", "高雄" to "Kaohsiung", "嘉義" to "Chiayi")

    Scaffold(
        modifier = modifier.fillMaxSize().testTag("app_scaffold"),
        containerColor = OceanDark,
        bottomBar = {
            NavigationBar(
                containerColor = OceanCard,
                contentColor = HighContrastWhite,
                tonalElevation = 8.dp,
                modifier = Modifier.testTag("bottom_nav_bar")
            ) {
                NavigationBarItem(
                    selected = activeTab == 0,
                    onClick = { activeTab = 0 },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.List,
                            contentDescription = "Schedules",
                            tint = if (activeTab == 0) CyberCyan else MutedText
                        )
                    },
                    label = {
                        Text(
                            text = if (isEng) "Schedules" else "即時船班",
                            color = if (activeTab == 0) CyberCyan else MutedText,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = SlateDark
                    ),
                    modifier = Modifier.testTag("nav_sailings_btn")
                )
                NavigationBarItem(
                    selected = activeTab == 1,
                    onClick = { activeTab = 1 },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "Booking",
                            tint = if (activeTab == 1) CyberCyan else MutedText
                        )
                    },
                    label = {
                        Text(
                            text = if (isEng) "Booking" else "預訂預位",
                            color = if (activeTab == 1) CyberCyan else MutedText,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = SlateDark
                    ),
                    modifier = Modifier.testTag("nav_booking_btn")
                )
                NavigationBarItem(
                    selected = activeTab == 2,
                    onClick = { activeTab = 2 },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "My Tickets",
                            tint = if (activeTab == 2) CyberCyan else MutedText
                        )
                    },
                    label = {
                        Text(
                            text = if (isEng) "My Tickets" else "我的船票",
                            color = if (activeTab == 2) CyberCyan else MutedText,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = SlateDark
                    ),
                    modifier = Modifier.testTag("nav_tickets_btn")
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(OceanDark)
        ) {
            if (activeTab == 0) {
                // TAB 0: 即時船班 & 港口導覽
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(OceanDark)
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp)
                        .testTag("scroll_container_sailings"),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // --- 1. Top Header Layout with Spinning Compass ---
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            val infiniteTransition = rememberInfiniteTransition(label = "CompassRotation")
                            val rotationAngle by infiniteTransition.animateFloat(
                                initialValue = 0f,
                                targetValue = 360f,
                                animationSpec = infiniteRepeatable(
                                    animation = tween(12000, easing = LinearEasing),
                                    repeatMode = RepeatMode.Restart
                                ),
                                label = "CompassRotationAngle"
                            )

                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(SlateDark)
                                    .border(1.dp, CyberCyan, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Canvas(
                                    modifier = Modifier
                                        .size(24.dp)
                                        .rotate(rotationAngle)
                                ) {
                                    val strokeWidthVal = 2.dp.toPx()
                                    drawCircle(
                                        color = CyberCyan,
                                        radius = size.width / 2,
                                        style = Stroke(width = strokeWidthVal)
                                    )
                                    val centerX = size.width / 2
                                    val centerY = size.height / 2
                                    val needlePathNorth = androidx.compose.ui.graphics.Path().apply {
                                        moveTo(centerX, 2f)
                                        lineTo(centerX - 4.dp.toPx(), centerY)
                                        lineTo(centerX + 4.dp.toPx(), centerY)
                                        close()
                                    }
                                    drawPath(path = needlePathNorth, color = AlertRed)
                                    val needlePathSouth = androidx.compose.ui.graphics.Path().apply {
                                        moveTo(centerX, size.height - 2f)
                                        lineTo(centerX - 4.dp.toPx(), centerY)
                                        lineTo(centerX + 4.dp.toPx(), centerY)
                                        close()
                                    }
                                    drawPath(path = needlePathSouth, color = MutedText)
                                }
                            }

                            Column {
                                Text(
                                    text = if (isEng) "Blue Ocean Ferry" else "藍海航線 APP",
                                    color = OceanLightBlue,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.testTag("app_title")
                                )
                                Text(
                                    text = if (isEng) "Taiwan Smart Passage & Seating System" else "台灣客輪航程與席位預訂系統",
                                    color = MutedText,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }

                        Button(
                            onClick = { viewModel.toggleLanguage() },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = SlateDark,
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .height(36.dp)
                                .testTag("language_toggle"),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .background(CyberCyan.copy(alpha = 0.2f), RoundedCornerShape(4.dp))
                                        .padding(horizontal = 4.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = "EN/中",
                                        color = CyberCyan,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Text(
                                    text = if (isEng) "中文" else "English",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    // --- 2. Port Segment Quick Switcher Tabs ---
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        regions.forEach { reg ->
                            val isSelected = reg == region
                            val displayName = if (isEng) regionsEng[reg] ?: reg else reg

                            Button(
                                onClick = { viewModel.selectRegion(reg) },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (isSelected) CyberCyan else SlateDark,
                                    contentColor = if (isSelected) OceanDark else Color.White
                                ),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier
                                    .height(42.dp)
                                    .testTag("tab_${reg}"),
                                contentPadding = PaddingValues(horizontal = 16.dp)
                            ) {
                                Text(
                                    text = displayName,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    // --- 3. Marine Observation Weather Board ---
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(2.dp, RoundedCornerShape(16.dp)),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = OceanCard)
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = if (isEng) "⛈️ Port Meteorological Intelligence" else "⛈️ 今日智慧海象觀測",
                                color = OceanLightBlue,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                listOf(
                                    "🌊 " + (if (isEng) "Wave" else "浪高") to waveHeight,
                                    "💨 " + (if (isEng) "Wind" else "風速") to windSpeed,
                                    "👁️ " + (if (isEng) "Visibility" else "能見度") to visibility
                                ).forEach { (label, value) ->
                                    Card(
                                        modifier = Modifier
                                            .weight(1f)
                                            .shadow(1.dp, RoundedCornerShape(8.dp)),
                                        shape = RoundedCornerShape(8.dp),
                                        colors = CardDefaults.cardColors(containerColor = SlateDark)
                                    ) {
                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(8.dp),
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Text(
                                                text = label,
                                                color = MutedText,
                                                fontSize = 10.sp,
                                                textAlign = TextAlign.Center
                                            )
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(
                                                text = value,
                                                color = Color.White,
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                textAlign = TextAlign.Center,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                        }
                                    }
                                }
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = if (isEng) "Safety Rating: " else "航安星級：",
                                    color = MutedText,
                                    fontSize = 11.sp
                                )
                                Text(
                                    text = safetyStars,
                                    color = StarYellow,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Text(
                                text = safetyNote,
                                color = AlertRed,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }

                    // --- 4. Today's Sailings Board (今日即時客運航線班次表) ---
                    Text(
                        text = if (isEng) "🕒 Today's Live Sailings Board" else "🕒 今日即時客運航線班次表",
                        color = CyberCyan,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.fillMaxWidth()
                    )

                    val activeRegionSailingList = when (region) {
                        "台東" -> listOf(
                            Triple("09:30", if (isEng) "Fugang → Green Island" else "富岡 ➔ 綠島南寮", if (isEng) "Green Island Star III" else "綠島之星三號"),
                            Triple("11:15", if (isEng) "Fugang → Orchid Island" else "富岡 ➔ 蘭嶼開元", if (isEng) "Constant Star Ferry" else "恆星客輪"),
                            Triple("13:30", if (isEng) "Fugang → Green Island" else "富岡 ➔ 綠島南寮", if (isEng) "Triumph No.1" else "凱旋一號")
                        )
                        "屏東" -> listOf(
                            Triple("08:00", if (isEng) "Donggang → Xiaoliuqiu" else "東港 ➔ 小琉球大福", if (isEng) "Dafu Ryukyu Line" else "大福琉球客輪"),
                            Triple("10:45", if (isEng) "Donggang → Xiaoliuqiu" else "東港 ➔ 小琉球白沙", if (isEng) "Dongxin Ferry" else "東信客輪"),
                            Triple("14:00", if (isEng) "Donggang → Xiaoliuqiu" else "東港 ➔ 小琉球白沙", if (isEng) "Taifu No.3" else "泰富三號")
                        )
                        "高雄" -> listOf(
                            Triple("09:00", if (isEng) "Kaohsiung → Penghu Magong" else "高雄港 ➔ 澎湖馬公", if (isEng) "Taihua Express" else "台華豪華快輪"),
                            Triple("13:30", if (isEng) "Kaohsiung → Penghu Magong" else "高雄港 ➔ 澎湖馬公", if (isEng) "Penghu Star" else "澎湖之星")
                        )
                        "嘉義" -> listOf(
                            Triple("10:00", if (isEng) "Budai → Penghu Magong" else "布袋港 ➔ 澎湖馬公", if (isEng) "Triumph No.8" else "凱旋八號"),
                            Triple("14:30", if (isEng) "Budai → Penghu Magong" else "布袋港 ➔ 澎湖馬公", if (isEng) "All Stars No.2" else "滿天星二號")
                        )
                        else -> emptyList()
                    }

                    activeRegionSailingList.forEach { (time, route, vessel) ->
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = OceanCard),
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(0.5.dp, MutedBorder, RoundedCornerShape(12.dp))
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(SlateDark)
                                            .border(1.dp, CyberCyan, RoundedCornerShape(8.dp))
                                            .padding(horizontal = 8.dp, vertical = 6.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = time,
                                            color = CyberCyan,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            fontFamily = FontFamily.Monospace
                                        )
                                    }

                                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                        Text(
                                            text = route,
                                            color = Color.White,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = "🚢 $vessel",
                                            color = MutedText,
                                            fontSize = 11.sp
                                        )
                                    }
                                }

                                Button(
                                    onClick = {
                                        if (region == "台東") {
                                            if (route.contains("綠島") || route.contains("Green Island")) {
                                                viewModel.selectDeparture("台東富岡港")
                                                viewModel.selectDestination("綠島南寮港")
                                            } else {
                                                viewModel.selectDeparture("台東富岡港")
                                                viewModel.selectDestination("蘭嶼開元港")
                                            }
                                        } else if (region == "屏東") {
                                            viewModel.selectDeparture("東港漁港碼頭")
                                            viewModel.selectDestination("小琉球白沙港")
                                        } else if (region == "高雄") {
                                            viewModel.selectDeparture("高雄港一號碼頭")
                                            viewModel.selectDestination("澎湖馬公港")
                                        } else if (region == "嘉義") {
                                            viewModel.selectDeparture("嘉義布袋港")
                                            viewModel.selectDestination("澎湖馬公港")
                                        }
                                        activeTab = 1
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = CyberCyan.copy(alpha = 0.15f),
                                        contentColor = CyberCyan
                                    ),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.height(34.dp)
                                ) {
                                    Text(
                                        text = if (isEng) "Book" else "去預訂",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.ExtraBold
                                    )
                                }
                            }
                        }
                    }

                    // --- 5. Offshore Island Ports Guide ( originalmente de InfoTabContent) ---
                    Text(
                        text = if (isEng) "🏝️ Offshore Island Ports Information" else "🏝️ 精選離島港口與通航資訊",
                        color = CyberCyan,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.fillMaxWidth()
                    )

                    val spots = listOf(
                        Triple(
                            if (isEng) "Green Island (Nanliao Port)" else "綠島南寮港",
                            if (isEng) "50 mins from Taitung Fugang" else "從台東富岡港起航，航程約 50 分鐘",
                            if (isEng) "Main gateway to Green Island. Famous for saltwater hot springs, coral snorkeling, volcanic terrain, and local scooter exploration." else "綠島海上玄關，以海底鹽水溫泉、壯麗珊瑚礁潛水及朝日溫泉聞名。每日定期班次，是熱門浮潛聖地。"
                        ),
                        Triple(
                            if (isEng) "Orchid Island (Kaiyuan Port)" else "蘭嶼開元港",
                            if (isEng) "120 mins from Taitung / Kenting" else "從台東富岡或墾丁後壁湖出發，航程約 2 小時",
                            if (isEng) "The native land of the Tao tribe. Explore traditional underground houses, flying fish festivals, and pristine volcanic coastlines." else "達悟族人之鄉，擁有獨特半地下屋文化、拼板舟與飛魚季儀式。開元港坐落於西側，水深湛藍，火山原始地貌保存良好。"
                        ),
                        Triple(
                            if (isEng) "Xiaoliuqiu (Baisha / Dafu Port)" else "小琉球白沙港、大福港",
                            if (isEng) "25 mins from Donggang Pingtung" else "從屏東東港出發，航程僅需 25 分鐘",
                            if (isEng) "Taiwan's unique coral reef island. High density of green sea turtles, year-round warm seawater, and very calm strait voyage." else "台灣唯一珊瑚礁離島，全年受東北季風影響極小，黑潮支流溫暖。為綠蠵龜棲息天堂，白沙港周邊商家林立非常便利。"
                        ),
                        Triple(
                            if (isEng) "Penghu (Magong Port)" else "澎湖馬公港",
                            if (isEng) "90 mins from Kaohsiung / Budai Chiayi" else "從嘉義布袋港或高雄港起航，航程約 90 分鐘",
                            if (isEng) "Historic port famous for regional basalt formations, marine sanctuaries, ocean seafood delicacies, and summer drone fireworks." else "萬年玄武岩海岸與跨海大橋。馬公港是澎湖群島的海運總樞紐，夏季舉辦海洋花火節，海產鮮美、風力水上運動極佳。"
                        )
                    )

                    spots.forEach { (name, route, desc) ->
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = OceanCard),
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(0.5.dp, MutedBorder, RoundedCornerShape(12.dp))
                        ) {
                            Column(
                                modifier = Modifier.padding(14.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = name,
                                        color = Color.White,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(CyberCyan.copy(alpha = 0.15f))
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        textLabel(text = route)
                                    }
                                }
                                Text(
                                    text = desc,
                                    color = MutedText,
                                    fontSize = 11.sp,
                                    lineHeight = 16.sp
                                )
                            }
                        }
                    }

                    // --- 6. Security Inspection & Boarding Regulations ---
                    Text(
                        text = if (isEng) "🎫 Border Inspection & Boarding Regulations" else "🎫 碼頭安檢實名證件核實與乘船條例",
                        color = CyberCyan,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = OceanCard),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(0.5.dp, MutedBorder, RoundedCornerShape(12.dp))
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            listOf(
                                "🪪" to (if (isEng) "Identity Verification" else "身分核對實名制") to (if (isEng) "Every traveler boarding an offshore vessel must present passport/national ID at the gate. Coast Guards inspect details rigorously." else "依據海巡法令規定，每位旅客跨海登船皆須出示身分證正本、護照或健保卡，供港口海巡關卡人員實名查驗辦理登記。"),
                                "🎒" to (if (isEng) "Baggage and Pet Rules" else "隨身行李及寵物登船限制") to (if (isEng) "Max check-in weight is 20kg. Pets must reside in hard pet cages/travel bags; single seat is strictly for humans." else "每人手提行李限 2 件，總重不超過 20 公斤。攜帶寵物搭船必須置於寵物專用硬殼籠箱或提袋內，且不得任其露出佔座。"),
                                "⏰" to (if (isEng) "Boarding Lock Time" else "提早報到登船時間") to (if (isEng) "Check-in counters close 20 mins before departure. Travelers are requested to line up at check-in gate 30-40 mins prior." else "航班開航前 20 分鐘停止辦理取票。建議所有已辦劃座之旅客，於航班起航前 30-40 分鐘提早至碼頭安檢門口刷電子條碼準備登船。")
                            ).forEach { (iconPair, textPair) ->
                                val (icon, title) = iconPair
                                val desc = textPair
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Text(text = icon, fontSize = 16.sp)
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = title,
                                            color = Color.White,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            text = desc,
                                            color = MutedText,
                                            fontSize = 10.sp,
                                            lineHeight = 15.sp
                                        )
                                    }
                                }
                                if (iconPair != "⏰" to (if (isEng) "Boarding Lock Time" else "提早報到登船時間")) {
                                    HorizontalDivider(color = MutedBorder.copy(alpha = 0.5f))
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            } else if (activeTab == 1) {
                // TAB 1: 預訂預位 (Booking Screen)
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(OceanDark)
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp)
                        .testTag("scroll_container_booking"),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = if (isEng) "🎫 Route & Seat Reservation" else "🎫 離島直航客艙座位預訂",
                        color = OceanLightBlue,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.fillMaxWidth().testTag("booking_header_title")
                    )

                    // Region category filter fast tabs
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        regions.forEach { reg ->
                            val isSelected = reg == region
                            val displayName = if (isEng) regionsEng[reg] ?: reg else reg

                            Button(
                                onClick = { viewModel.selectRegion(reg) },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (isSelected) CyberCyan else SlateDark,
                                    contentColor = if (isSelected) OceanDark else Color.White
                                ),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier
                                    .height(42.dp)
                                    .testTag("booking_switcher_${reg}"),
                                contentPadding = PaddingValues(horizontal = 16.dp)
                            ) {
                                Text(
                                    text = displayName,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    // --- select routes dropdown portion ---
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(4.dp, RoundedCornerShape(16.dp)),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = OceanCard)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text(
                                text = if (isEng) "📍 Select Terminal Routes" else "📍 航線起訖港選定 / Routes",
                                color = OceanLightBlue,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )

                            // Departure
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(SlateDark)
                                    .border(1.dp, MutedBorder, RoundedCornerShape(8.dp))
                                    .clickable { showDepDialog = true }
                                    .padding(12.dp)
                                    .testTag("departure_box")
                            ) {
                                Text(
                                    text = if (isEng) "Departure Port" else "登船港 (起點)",
                                    color = MutedText,
                                    fontSize = 10.sp
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = selectedDep,
                                        color = Color.White,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Icon(
                                        imageVector = Icons.Default.KeyboardArrowDown,
                                        contentDescription = "Dropdown",
                                        tint = CyberCyan
                                    )
                                }
                            }

                            // Swap
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(32.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                HorizontalDivider(
                                    color = MutedBorder,
                                    thickness = 1.dp,
                                    modifier = Modifier.fillMaxWidth()
                                )
                                IconButton(
                                    onClick = { viewModel.swapPorts() },
                                    modifier = Modifier
                                        .size(36.dp)
                                        .shadow(2.dp, CircleShape)
                                        .clip(CircleShape)
                                        .background(SlateDark)
                                        .border(1.dp, CyberCyan, CircleShape)
                                        .testTag("swap_ports_button")
                                ) {
                                    Text(
                                        text = "⇅",
                                        color = CyberCyan,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.offset(y = (-1).dp)
                                    )
                                }
                            }

                            // Destination
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(SlateDark)
                                    .border(1.dp, MutedBorder, RoundedCornerShape(8.dp))
                                    .clickable { showDestDialog = true }
                                    .padding(12.dp)
                                    .testTag("destination_box")
                            ) {
                                Text(
                                    text = if (isEng) "Destination Port" else "抵達港 (目的地)",
                                    color = MutedText,
                                    fontSize = 10.sp
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = selectedDest,
                                        color = Color.White,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Icon(
                                        imageVector = Icons.Default.KeyboardArrowDown,
                                        contentDescription = "Dropdown",
                                        tint = CyberCyan
                                    )
                                }
                            }
                        }
                    }

                    // --- Seating Matrix ---
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(2.dp, RoundedCornerShape(16.dp)),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = OceanCard)
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text(
                                text = if (isEng) "💺 Smart Cabin Seat Allocation Matrix" else "💺 智慧客艙客機 seat 列席對號劃座",
                                color = OceanLightBlue,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )

                            val seatRows = listOf("1", "2", "3", "4")
                            val seatCols = listOf("A", "B", "C", "D")

                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                seatRows.forEach { r ->
                                        Row(
                                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            seatCols.forEach { c ->
                                                val seatCode = "$r$c"
                                                val isOccupied = occupiedSeats.contains(seatCode)
                                                val isSelected = selectedSeat == seatCode

                                                Box(
                                                    modifier = Modifier
                                                        .weight(1f)
                                                        .height(42.dp)
                                                        .clip(RoundedCornerShape(8.dp))
                                                        .background(
                                                            when {
                                                                isOccupied -> SlateDark.copy(alpha = 0.5f)
                                                                isSelected -> SuccessfulGreen
                                                                else -> SlateDark
                                                            }
                                                        )
                                                        .border(
                                                            width = 1.dp,
                                                            color = when {
                                                                isOccupied -> Color.Transparent
                                                                isSelected -> CyberCyan
                                                                else -> MutedBorder
                                                            },
                                                            shape = RoundedCornerShape(8.dp)
                                                        )
                                                        .clickable { viewModel.selectSeat(seatCode) }
                                                        .testTag("seat_$seatCode"),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    Text(
                                                        text = seatCode,
                                                        color = if (isOccupied) MutedText.copy(alpha = 0.4f) else Color.White,
                                                        fontWeight = FontWeight.Bold,
                                                        fontSize = 12.sp
                                                    )
                                                    if (isOccupied) {
                                                        Canvas(modifier = Modifier.fillMaxSize()) {
                                                            drawLine(
                                                                color = AlertRed.copy(alpha = 0.6f),
                                                                start = Offset(4f, 4f),
                                                                end = Offset(size.width - 4f, size.height - 4f),
                                                                strokeWidth = 2f
                                                            )
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                }
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                        Box(modifier = Modifier.size(10.dp).clip(RoundedCornerShape(2.dp)).background(SlateDark).border(0.5.dp, MutedBorder))
                                        Text(text = if (isEng) "Free" else "空席", color = MutedText, fontSize = 9.sp)
                                    }
                                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                        Box(modifier = Modifier.size(10.dp).clip(RoundedCornerShape(2.dp)).background(SuccessfulGreen))
                                        Text(text = if (isEng) "Selected" else "選定", color = MutedText, fontSize = 9.sp)
                                    }
                                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                        Box(modifier = Modifier.size(10.dp).clip(RoundedCornerShape(2.dp)).background(SlateDark.copy(alpha = 0.5f)))
                                        Text(text = if (isEng) "Sold" else "已售", color = MutedText, fontSize = 9.sp)
                                    }
                                }

                                Text(
                                    text = if (selectedSeat != null) {
                                        (if (isEng) "Selected: " else "已選座位：") + selectedSeat
                                    } else {
                                        if (isEng) "None" else "未選擇"
                                    },
                                    color = if (selectedSeat != null) SuccessfulGreen else MutedText,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    // --- Real Name passenger verification form ---
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(2.dp, RoundedCornerShape(16.dp)),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = OceanCard)
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text(
                                text = if (isEng) "🎫 Passenger Real-Name Certification" else "🎫 實名安檢證件核銷申報專欄",
                                color = OceanLightBlue,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )

                            OutlinedTextField(
                                value = passengerName,
                                onValueChange = { viewModel.onPassengerNameChange(it) },
                                placeholder = {
                                    Text(
                                        text = if (isEng) "Enter standard ID document name" else "請輸入乘船乘客身分證件相同姓名",
                                        fontSize = 11.sp,
                                        color = MutedText
                                    )
                                },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Person,
                                        contentDescription = "Name Icon",
                                        tint = CyberCyan
                                    )
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("passenger_name_input"),
                                shape = RoundedCornerShape(10.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = SlateDark,
                                    unfocusedContainerColor = SlateDark,
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White,
                                    focusedBorderColor = CyberCyan,
                                    unfocusedBorderColor = MutedBorder
                                ),
                                singleLine = true
                            )

                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = SlateDark.copy(alpha = 0.6f)),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { viewModel.onConcessionToggle(!isConcession) }
                                        .padding(horizontal = 10.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Checkbox(
                                        checked = isConcession,
                                        onCheckedChange = { viewModel.onConcessionToggle(it) },
                                        colors = CheckboxDefaults.colors(
                                            checkedColor = CyberCyan,
                                            uncheckedColor = MutedText
                                        )
                                    )
                                    Column(modifier = Modifier.padding(start = 4.dp)) {
                                        Text(
                                            text = if (isEng) "Apply Concession Discount (Half Price)" else "符合優待票資格（學生、敬老、身障半價折抵）",
                                            color = Color.White,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = if (isEng) "Requires relevant valid photo ID presentation on gate" else "碼頭驗票閘口安檢時需出示相關實名證明文件",
                                            color = MutedText,
                                            fontSize = 8.sp
                                        )
                                    }
                                }
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = if (isEng) "Live Ticket Price: " else "即時票價試算：",
                                    color = Color.White,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = "NT$ " + viewModel.getTicketPrice() + (if (isConcession) " (" + (if (isEng) "Discounted" else "優待票") + ")" else ""),
                                    color = CyberCyan,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    modifier = Modifier.testTag("ticket_price_display")
                                )
                            }

                            Button(
                                onClick = {
                                    keyboardController?.hide()
                                    viewModel.bookTicket()
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                                    .testTag("book_ticket_button"),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = SuccessfulGreen,
                                    contentColor = Color.White
                                ),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = "Confirm Icon",
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Text(
                                        text = if (isEng) "Confirm Seat & Ticket Booking" else "確認劃座位置與核酸認證實名申報預訂票務",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }

                    // --- Gemini AI Sea assistant advice box ---
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(2.dp, RoundedCornerShape(16.dp)),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = OceanCard)
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = { viewModel.askGeminiAI() },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                                    .testTag("ask_ai_button"),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = StarYellow,
                                    contentColor = Color.White
                                ),
                                shape = RoundedCornerShape(12.dp),
                                enabled = !isAiLoading
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Text(
                                        text = "✨",
                                        fontSize = 14.sp
                                    )
                                    Text(
                                        text = if (isEng) "Consult Gemini Navigation Expert" else "✨ 點擊詢問 AI 今日航行備忘 & 行程",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                            if (isAiLoading) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator(
                                        color = StarYellow,
                                        modifier = Modifier
                                            .size(32.dp)
                                            .testTag("ai_loading_spinner")
                                    )
                                }
                            }

                            aiResponse?.let { ans ->
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(OceanDark)
                                        .border(1.dp, StarYellow.copy(alpha = 0.5f), RoundedCornerShape(10.dp))
                                        .padding(12.dp)
                                        .testTag("ai_response_box"),
                                    verticalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Text(
                                            text = "💬",
                                            fontSize = 12.sp
                                        )
                                        Text(
                                            text = if (isEng) "Gemini Sea Voyage Assistant" else "Gemini 智慧航次隨身助理",
                                            color = StarYellow,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                    Text(
                                        text = ans,
                                        color = HighContrastWhite,
                                        fontSize = 11.sp,
                                        lineHeight = 16.sp
                                    )
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            } else {
                // TAB 2: 我的船票 (My Tickets & Passes)
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(OceanDark)
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp)
                        .testTag("scroll_container_my_tickets"),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = if (isEng) "🎟️ My Smart Boarding Passes" else "🎟️ 我的電子船票與登船憑證",
                        color = OceanLightBlue,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.fillMaxWidth().testTag("tickets_header_title")
                    )

                    // Active QR ticket pass
                    if (activeBooking != null) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .shadow(6.dp, RoundedCornerShape(16.dp))
                                .testTag("ticket_pass_card"),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = SuccessfulGreen)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = if (isEng) "🎟️ E-Boarding Pass" else "🎟️ 電子電子乘船特考航行證",
                                        color = Color.White,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(Color.White.copy(alpha = 0.2f))
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            text = if (isEng) "VALID" else "已授權",
                                            color = Color.White,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.ExtraBold
                                        )
                                    }
                                }

                                HorizontalDivider(color = Color.White.copy(alpha = 0.3f), thickness = 1.dp)

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column {
                                        textLabel(if (isEng) "RESERVATION CODE" else "預訂代號")
                                        Text(
                                            text = activeBooking.bookingCode,
                                            color = Color.White,
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.ExtraBold
                                        )
                                    }
                                    Column(horizontalAlignment = Alignment.End) {
                                        textLabel(if (isEng) "CABIN SEAT" else "指定對號客位")
                                        Text(
                                            text = activeBooking.seatNumber,
                                            color = StarYellow,
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.ExtraBold
                                        )
                                    }
                                }

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column {
                                        textLabel(if (isEng) "VOYAGE SECTOR" else "客運航線船段")
                                        Text(
                                            text = "${activeBooking.departure} ➔ ${activeBooking.destination}",
                                            color = Color.White,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    }
                                    Column(horizontalAlignment = Alignment.End) {
                                        textLabel(if (isEng) "FARE PAID" else "已核扣票價")
                                        Text(
                                            text = "NT$ ${activeBooking.price}",
                                            color = Color.White,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.ExtraBold
                                        )
                                    }
                                }

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column {
                                        textLabel(if (isEng) "REAL-NAME VISITOR" else "實名申報乘船旅客")
                                        Text(
                                            text = activeBooking.passengerName,
                                            color = Color.White,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    }
                                    Column(horizontalAlignment = Alignment.End) {
                                        textLabel(if (isEng) "CLASS TYPE" else "乘船票種")
                                        Text(
                                            text = if (activeBooking.isConcession) (if (isEng) "Concession" else "優待折扣票") else (if (isEng) "Adult Standard" else "全額成人票"),
                                            color = Color.White,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    }
                                }

                                // Canvas-based QR Code Mockup simulator
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(130.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Canvas(
                                        modifier = Modifier
                                            .size(105.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(Color.White)
                                            .padding(8.dp)
                                    ) {
                                        val modulesCount = 11
                                        val sizeOfModule = size.width / modulesCount
                                        for (i in 0 until modulesCount) {
                                            for (j in 0 until modulesCount) {
                                                val isAnchor = (i < 3 && j < 3) || (i >= modulesCount - 3 && j < 3) || (i < 3 && j >= modulesCount - 3)
                                                val isRandomFill = !isAnchor && (Math.sin((i * 1.7 + j * 2.3).hashCode().toDouble()) > 0.0)

                                                if (isAnchor || isRandomFill) {
                                                    drawRect(
                                                        color = OceanDark,
                                                        topLeft = Offset(i * sizeOfModule, j * sizeOfModule),
                                                        size = Size(sizeOfModule, sizeOfModule)
                                                    )
                                                }
                                            }
                                        }
                                        drawRect(color = OceanDark, topLeft = Offset(0f, 0f), size = Size(sizeOfModule * 3f, sizeOfModule * 3f), style = Stroke(width = 3f))
                                        drawRect(color = OceanDark, topLeft = Offset((modulesCount - 3) * sizeOfModule, 0f), size = Size(sizeOfModule * 3f, sizeOfModule * 3f), style = Stroke(width = 3f))
                                        drawRect(color = OceanDark, topLeft = Offset(0f, (modulesCount - 3) * sizeOfModule), size = Size(sizeOfModule * 3f, sizeOfModule * 3f), style = Stroke(width = 3f))
                                    }
                                }

                                Text(
                                    text = if (isEng) "※ Please arrive at check-in gate 30 minutes before departure." else "※ 請提早 30 分鐘抵達碼頭海巡安檢口刷碼乘船",
                                    color = Color.White.copy(alpha = 0.9f),
                                    fontSize = 9.sp,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    } else {
                        // Beautiful empty ticket placeholder card
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = OceanCard),
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(0.5.dp, MutedBorder, RoundedCornerShape(16.dp))
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Text(
                                    text = "🎟️",
                                    fontSize = 44.sp,
                                    textAlign = TextAlign.Center
                                )
                                Text(
                                    text = if (isEng) "No Active Boarding Passes Found" else "目前尚無生效中的電子船票",
                                    color = Color.White,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center
                                )
                                Text(
                                    text = if (isEng) {
                                        "You have no voyages booked yet. Please head to the Booking tab to reserve your cabin seat."
                                    } else {
                                        "您目前尚未有即將起航的電子登船關聯。現在即可前往『預訂預位』，兩分鐘快速填單與自選客艙對號！"
                                    },
                                    color = MutedText,
                                    fontSize = 11.sp,
                                    lineHeight = 16.sp,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth(0.9f)
                                )
                                Button(
                                    onClick = { activeTab = 1 },
                                    colors = ButtonDefaults.buttonColors(containerColor = CyberCyan),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(
                                        text = if (isEng) "Go to Bookings" else "前往預訂客位",
                                        color = OceanDark,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }

                    // --- Historic database Bookings list ---
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(2.dp, RoundedCornerShape(16.dp)),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = OceanCard)
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = if (isEng) "📜 Historic Terminal Bookings" else "📜 本機端安全預訂歷史存檔",
                                    color = OceanLightBlue,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )

                                if (pastBookings.isNotEmpty()) {
                                    Text(
                                        text = if (isEng) "Clear All" else "清除全部",
                                        color = AlertRed,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        modifier = Modifier
                                            .clickable { viewModel.clearAllRecords() }
                                            .padding(4.dp)
                                    )
                                }
                            }

                            if (pastBookings.isEmpty()) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(20.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = if (isEng) "No active bookings secured in offline vault." else "目前本機預訂資料庫中無任何票務，請在『預訂預位』頁面中自選座位與申報預約。",
                                        color = MutedText,
                                        fontSize = 11.sp,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            } else {
                                Column(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    pastBookings.take(8).forEach { rec ->
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(SlateDark)
                                                .border(0.5.dp, MutedBorder, RoundedCornerShape(8.dp))
                                                .padding(10.dp),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Column(modifier = Modifier.weight(1f)) {
                                                Row(
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                                ) {
                                                    Text(
                                                        text = rec.passengerName,
                                                        color = Color.White,
                                                        fontSize = 12.sp,
                                                        fontWeight = FontWeight.Bold
                                                    )
                                                    Box(
                                                        modifier = Modifier
                                                            .clip(RoundedCornerShape(3.dp))
                                                            .background(CyberCyan.copy(alpha = 0.2f))
                                                            .padding(horizontal = 4.dp, vertical = 1.dp)
                                                    ) {
                                                        Text(
                                                            text = rec.seatNumber,
                                                            color = CyberCyan,
                                                            fontSize = 8.sp,
                                                            fontWeight = FontWeight.Bold
                                                        )
                                                    }
                                                }
                                                Spacer(modifier = Modifier.height(2.dp))
                                                Text(
                                                    text = "${rec.departure} → ${rec.destination}",
                                                    color = MutedText,
                                                    fontSize = 10.sp,
                                                    maxLines = 1,
                                                    overflow = TextOverflow.Ellipsis
                                                )
                                                Text(
                                                    text = rec.bookingCode,
                                                    color = MutedText,
                                                    fontSize = 8.sp,
                                                    fontFamily = FontFamily.Monospace
                                                )
                                            }

                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                                            ) {
                                                Text(
                                                    text = "NT$ ${rec.price}",
                                                    color = StarYellow,
                                                    fontSize = 12.sp,
                                                    fontWeight = FontWeight.ExtraBold
                                                )
                                                IconButton(
                                                    onClick = { viewModel.deleteBookingRecord(rec) },
                                                    modifier = Modifier.size(24.dp)
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Default.Delete,
                                                        contentDescription = "Delete Record",
                                                        tint = AlertRed.copy(alpha = 0.8f),
                                                        modifier = Modifier.size(16.dp)
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }

    if (showDepDialog) {
        PortSelectionDialog(
            title = if (isEng) "Select Departure Port" else "點選登船起點港口",
            ports = departurePorts,
            selectedPort = selectedDep,
            onSelect = {
                viewModel.selectDeparture(it)
                showDepDialog = false
            },
            onDismiss = { showDepDialog = false }
        )
    }

    if (showDestDialog) {
        PortSelectionDialog(
            title = if (isEng) "Select Destination Port" else "點選抵達目的地港口",
            ports = destinationPorts,
            selectedPort = selectedDest,
            onSelect = {
                viewModel.selectDestination(it)
                showDestDialog = false
            },
            onDismiss = { showDestDialog = false }
        )
    }
}

@Composable
fun InfoTabContent(
    isEng: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(OceanDark)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Title Screen Header
        Text(
            text = if (isEng) "ℹ️ Taiwan Smart Marine & Port Guide" else "ℹ️ 台灣離島客輪智慧航行與安檢導覽",
            color = OceanLightBlue,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth().testTag("info_guide_title"),
            textAlign = TextAlign.Start
        )
        Text(
            text = if (isEng) 
                "Real-time information regarding safety checking, offshore marine regulations, and tourist harbor checkpoints."
            else 
                "提供離島旅客最新海巡安檢口岸規定、實名申報安檢、行李限制及各離島港口導覽資訊。",
            color = MutedText,
            fontSize = 11.sp,
            lineHeight = 16.sp,
            modifier = Modifier.fillMaxWidth()
        )

        // Segment 1: Port Spotlights (港口景點導覽)
        Text(
            text = if (isEng) "🏝️ Offshore Island Ports Information" else "🏝️ 精選離島港口與通航資訊",
            color = CyberCyan,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth()
        )

        val spots = listOf(
            Triple(
                if (isEng) "Green Island (Nanliao Port)" else "綠島南寮港",
                if (isEng) "50 mins from Taitung Fugang" else "從台東富岡港起航，航程約 50 分鐘",
                if (isEng) "Main gateway to Green Island. Famous for saltwater hot springs, coral snorkeling, volcanic terrain, and local scooter exploration." else "綠島海上玄關，以海底鹽水溫泉、壯麗珊瑚礁潛水及朝日溫泉聞名。每日定期班次，是熱門浮潛聖地。"
            ),
            Triple(
                if (isEng) "Orchid Island (Kaiyuan Port)" else "蘭嶼開元港",
                if (isEng) "120 mins from Taitung / Kenting" else "從台東富岡或墾丁後壁湖出發，航程約 2 小時",
                if (isEng) "The native land of the Tao tribe. Explore traditional underground houses, flying fish festivals, and pristine volcanic coastlines." else "達悟族人之鄉，擁有獨特半地下屋文化、拼板舟與飛魚季儀式。開元港坐落於西側，水深湛藍，火山原始地貌保存良好。"
            ),
            Triple(
                if (isEng) "Xiaoliuqiu (Baisha / Dafu Port)" else "小琉球白沙港、大福港",
                if (isEng) "25 mins from Donggang Pingtung" else "從屏東東港出發，航程僅需 25 分鐘",
                if (isEng) "Taiwan's unique coral reef island. High density of green sea turtles, year-round warm seawater, and very calm strait voyage." else "台灣唯一珊瑚礁離島，全年受東北季風影響極小，黑潮支流溫暖。為綠蠵龜棲息天堂，白沙港周邊商家林立非常便利。"
            ),
            Triple(
                if (isEng) "Penghu (Magong Port)" else "澎湖馬公港",
                if (isEng) "90 mins from Kaohsiung / Budai Chiayi" else "從嘉義布袋港或高雄港起航，航程約 90 分鐘",
                if (isEng) "Historic port famous for regional basalt formations, marine sanctuaries, ocean seafood delicacies, and summer drone fireworks." else "萬年玄武岩海岸與跨海大橋。馬公港是澎湖群島的海運總樞紐，夏季舉辦海洋花火節，海產鮮美、風力水上運動極佳。"
            )
        )

        spots.forEach { (name, route, desc) ->
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = OceanCard),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(0.5.dp, MutedBorder, RoundedCornerShape(12.dp))
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = name,
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(CyberCyan.copy(alpha = 0.15f))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            textLabel(text = route)
                        }
                    }
                    Text(
                        text = desc,
                        color = MutedText,
                        fontSize = 11.sp,
                        lineHeight = 16.sp
                    )
                }
            }
        }

        // Segment 2: Safety & Security clearance guidelines (乘船實名制安檢須知)
        Text(
            text = if (isEng) "🎫 Border Inspection & Boarding Regulations" else "🎫 碼頭安檢實名證件核實與乘船條例",
            color = CyberCyan,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth()
        )

        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = OceanCard),
            modifier = Modifier
                .fillMaxWidth()
                .border(0.5.dp, MutedBorder, RoundedCornerShape(12.dp))
        ) {
            Column(
                modifier = Modifier.padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                listOf(
                    "🪪" to (if (isEng) "Identity Verification" else "身分核對實名制") to (if (isEng) "Every traveler boarding an offshore vessel must present passport/national ID at the gate. Coast Guards inspect details rigorously." else "依據海巡法令規定，每位旅客跨海登船皆須出示身分證正本、護照或健保卡，供港口海巡關卡人員實名查驗辦理登記。"),
                    "🎒" to (if (isEng) "Baggage and Pet Rules" else "隨身行李及寵物登船限制") to (if (isEng) "Max check-in weight is 20kg. Pets must reside in hard pet cages/travel bags; single seat is strictly for humans." else "每人手提行李限 2 件，總重不超過 20 公斤。攜帶寵物搭船必須置於寵物專用硬殼籠箱或提袋內，且不得任其露出佔座。"),
                    "⏰" to (if (isEng) "Boarding Lock Time" else "提早報到登船時間") to (if (isEng) "Check-in counters close 20 mins before departure. Travelers are requested to line up at check-in gate 30-40 mins prior." else "航班開航前 20 分鐘停止辦理取票。建議所有已辦劃座之旅客，於航班起航前 30-40 分鐘提早至碼頭安檢門口刷電子條碼準備登船。")
                ).forEach { (iconPair, textPair) ->
                    val (icon, title) = iconPair
                    val desc = textPair
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(text = icon, fontSize = 16.sp)
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = title,
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = desc,
                                color = MutedText,
                                fontSize = 10.sp,
                                lineHeight = 15.sp
                            )
                        }
                    }
                    if (iconPair != "⏰" to (if (isEng) "Boarding Lock Time" else "提早報到登船時間")) {
                        HorizontalDivider(color = MutedBorder.copy(alpha = 0.5f))
                    }
                }
            }
        }

        // Space pad bottom
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun textLabel(text: String) {
    Text(
        text = text,
        color = Color.White.copy(alpha = 0.7f),
        fontSize = 8.sp,
        fontWeight = FontWeight.Bold
    )
}

@Composable
fun PortSelectionDialog(
    title: String,
    ports: List<String>,
    selectedPort: String,
    onSelect: (String) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = OceanCard),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .border(1.dp, CyberCyan, RoundedCornerShape(16.dp))
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = title,
                    color = OceanLightBlue,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )

                HorizontalDivider(color = MutedBorder)

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ports.forEach { port ->
                        val isMatched = port == selectedPort
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isMatched) SlateDark else Color.Transparent)
                                .clickable { onSelect(port) }
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = port,
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = if (isMatched) FontWeight.Bold else FontWeight.Normal
                            )
                            if (isMatched) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Active",
                                    tint = CyberCyan,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text(
                            text = "Cancel",
                            color = MutedText,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }
}
