package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.FerryBooking
import com.example.data.FerryDatabase
import com.example.data.FerryRepository
import com.example.network.GeminiHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.random.Random

class FerryViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: FerryRepository

    init {
        val database = FerryDatabase.getDatabase(application)
        repository = FerryRepository(database.ferryBookingDao())
    }

    val pastBookings: StateFlow<List<FerryBooking>> = repository.allBookings
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _isEnglish = MutableStateFlow(false)
    val isEnglish = _isEnglish.asStateFlow()

    private val _selectedRegion = MutableStateFlow("台東")
    val selectedRegion = _selectedRegion.asStateFlow()

    private val _departurePorts = MutableStateFlow(listOf("台東富岡港"))
    val departurePorts = _departurePorts.asStateFlow()

    private val _selectedDeparture = MutableStateFlow("台東富岡港")
    val selectedDeparture = _selectedDeparture.asStateFlow()

    private val _destinationPorts = MutableStateFlow(listOf("綠島南寮港", "蘭嶼開元港"))
    val destinationPorts = _destinationPorts.asStateFlow()

    private val _selectedDestination = MutableStateFlow("綠島南寮港")
    val selectedDestination = _selectedDestination.asStateFlow()

    private val _passengerName = MutableStateFlow("")
    val passengerName = _passengerName.asStateFlow()

    private val _isConcession = MutableStateFlow(false)
    val isConcession = _isConcession.asStateFlow()

    private val _selectedSeat = MutableStateFlow<String?>(null)
    val selectedSeat = _selectedSeat.asStateFlow()

    private val _occupiedSeats = MutableStateFlow(setOf("2D", "1C", "4A"))
    val occupiedSeats = _occupiedSeats.asStateFlow()

    private val _waveHeight = MutableStateFlow("1.2 公尺 / m")
    val waveHeight = _waveHeight.asStateFlow()

    private val _windSpeed = MutableStateFlow("12 節 / knots")
    val windSpeed = _windSpeed.asStateFlow()

    private val _visibility = MutableStateFlow("10 公里 / km")
    val visibility = _visibility.asStateFlow()

    private val _safetyStars = MutableStateFlow("★★★★☆")
    val safetyStars = _safetyStars.asStateFlow()

    private val _safetyNote = MutableStateFlow("海象平靜，適航航次順暢。")
    val safetyNote = _safetyNote.asStateFlow()

    private val _activeBooking = MutableStateFlow<FerryBooking?>(null)
    val activeBooking = _activeBooking.asStateFlow()

    private val _isAiLoading = MutableStateFlow(false)
    val isAiLoading = _isAiLoading.asStateFlow()

    private val _aiResponse = MutableStateFlow<String?>(null)
    val aiResponse = _aiResponse.asStateFlow()

    private val _feedbackMessage = MutableStateFlow<String?>(null)
    val feedbackMessage = _feedbackMessage.asStateFlow()

    init {
        updatePortsAndWeather()
    }

    fun toggleLanguage() {
        _isEnglish.value = !_isEnglish.value
        updateWeatherTexts()
    }

    fun selectRegion(region: String) {
        _selectedRegion.value = region
        updatePortsAndWeather()
        _selectedSeat.value = null
        _activeBooking.value = null
        _aiResponse.value = null
    }

    fun selectDeparture(port: String) {
        _selectedDeparture.value = port
        updateDestinationsForDeparture(port)
        regenerateOccupiedSeats()
        updateWeatherForRoute()
    }

    fun selectDestination(port: String) {
        _selectedDestination.value = port
        regenerateOccupiedSeats()
        updateWeatherForRoute()
    }

    fun swapPorts() {
        val currentDep = _selectedDeparture.value
        val currentDest = _selectedDestination.value
        
        // Swap them dynamically
        _selectedDeparture.value = currentDest
        _selectedDestination.value = currentDep
        regenerateOccupiedSeats()
        updateWeatherForRoute()
    }

    fun onPassengerNameChange(name: String) {
        _passengerName.value = name
    }

    fun onConcessionToggle(enabled: Boolean) {
        _isConcession.value = enabled
    }

    fun selectSeat(seat: String) {
        if (_occupiedSeats.value.contains(seat)) {
            _feedbackMessage.value = if (_isEnglish.value) "Seat $seat is occupied." else "座位 $seat 已被售出預訂。"
            return
        }
        _selectedSeat.value = if (_selectedSeat.value == seat) null else seat
    }

    fun clearFeedbackMessage() {
        _feedbackMessage.value = null
    }

    fun getTicketPrice(): Int {
        val base = getBasePrice()
        return if (_isConcession.value) (base * 0.5f).toInt() else base
    }

    private fun getBasePrice(): Int {
        val dep = _selectedDeparture.value
        val dest = _selectedDestination.value
        return when {
            dep.contains("台東") || dest.contains("台東") -> {
                if (dep.contains("蘭嶼") || dest.contains("蘭嶼")) 1200 else 600
            }
            dep.contains("東港") || dest.contains("東港") -> 410
            dep.contains("後壁湖") || dest.contains("後壁湖") -> 1200
            dep.contains("高雄") || dest.contains("高雄") -> 980
            dep.contains("嘉義") || dest.contains("嘉義") -> 800
            else -> 500
        }
    }

    private fun updatePortsAndWeather() {
        when (_selectedRegion.value) {
            "台東" -> {
                _departurePorts.value = listOf("台東富岡港", "綠島南寮港", "蘭嶼開元港")
                _selectedDeparture.value = "台東富岡港"
                _destinationPorts.value = listOf("綠島南寮港", "蘭嶼開元港")
                _selectedDestination.value = "綠島南寮港"
            }
            "屏東" -> {
                _departurePorts.value = listOf("東港漁港碼頭", "墾丁後壁湖港")
                _selectedDeparture.value = "東港漁港碼頭"
                _destinationPorts.value = listOf("小琉球白沙港", "小琉球大福港", "蘭嶼開元港")
                _selectedDestination.value = "小琉球白沙港"
            }
            "高雄" -> {
                _departurePorts.value = listOf("高雄港一號碼頭", "澎湖馬公港")
                _selectedDeparture.value = "高雄港一號碼頭"
                _destinationPorts.value = listOf("澎湖馬公港")
                _selectedDestination.value = "澎湖馬公港"
            }
            "嘉義" -> {
                _departurePorts.value = listOf("嘉義布袋港", "澎湖馬公港")
                _selectedDeparture.value = "嘉義布袋港"
                _destinationPorts.value = listOf("澎湖馬公港")
                _selectedDestination.value = "澎湖馬公港"
            }
        }
        regenerateOccupiedSeats()
        updateWeatherForRoute()
    }

    private fun updateDestinationsForDeparture(departure: String) {
        when {
            departure.contains("台東富岡") -> {
                _destinationPorts.value = listOf("綠島南寮港", "蘭嶼開元港")
                _selectedDestination.value = "綠島南寮港"
            }
            departure.contains("綠島南寮") -> {
                _destinationPorts.value = listOf("台東富岡港")
                _selectedDestination.value = "台東富岡港"
            }
            departure.contains("蘭嶼開元") -> {
                _destinationPorts.value = listOf("台東富岡港", "墾丁後壁湖港")
                _selectedDestination.value = "台東富岡港"
            }
            departure.contains("東港") -> {
                _destinationPorts.value = listOf("小琉球白沙港", "小琉球大福港")
                _selectedDestination.value = "小琉球白沙港"
            }
            departure.contains("小琉球") -> {
                _destinationPorts.value = listOf("東港漁港碼頭")
                _selectedDestination.value = "東港漁港碼頭"
            }
            departure.contains("後壁湖") -> {
                _destinationPorts.value = listOf("蘭嶼開元港")
                _selectedDestination.value = "蘭嶼開元港"
            }
            departure.contains("高雄") -> {
                _destinationPorts.value = listOf("澎湖馬公港")
                _selectedDestination.value = "澎湖馬公港"
            }
            departure.contains("澎湖馬公") -> {
                _destinationPorts.value = if (_selectedRegion.value == "高雄") listOf("高雄港一號碼頭") else listOf("嘉義布袋港")
                _selectedDestination.value = _destinationPorts.value.first()
            }
            departure.contains("嘉義") -> {
                _destinationPorts.value = listOf("澎湖馬公港")
                _selectedDestination.value = "澎湖馬公港"
            }
        }
    }

    private fun regenerateOccupiedSeats() {
        val allSeats = listOf(
            "1A", "1B", "1C", "1D",
            "2A", "2B", "2C", "2D",
            "3A", "3B", "3C", "3D",
            "4A", "4B", "4C", "4D"
        )
        val count = Random.nextInt(3, 7)
        val occupied = allSeats.shuffled().take(count).toSet()
        _occupiedSeats.value = occupied
    }

    private fun updateWeatherForRoute() {
        val dep = _selectedDeparture.value
        val dest = _selectedDestination.value
        
        when {
            dep.contains("台東") || dest.contains("台東") -> {
                _waveHeight.value = "1.5 公尺 / m"
                _windSpeed.value = "15 節 / knots"
                _visibility.value = "12 公里 / km"
                _safetyStars.value = "★★★★☆"
            }
            dep.contains("東港") || dest.contains("東港") || dep.contains("小琉球") || dest.contains("小琉球") -> {
                _waveHeight.value = "0.5 公尺 / m"
                _windSpeed.value = "8 節 / knots"
                _visibility.value = "15 公里 / km"
                _safetyStars.value = "★★★★★"
            }
            dep.contains("後壁湖") || dest.contains("後壁湖") -> {
                _waveHeight.value = "2.1 公尺 / m"
                _windSpeed.value = "20 節 / knots"
                _visibility.value = "9 公里 / km"
                _safetyStars.value = "★★★☆☆"
            }
            dep.contains("高雄") || dest.contains("高雄") || dep.contains("嘉義") || dest.contains("嘉義") || dep.contains("澎湖") || dest.contains("澎湖") -> {
                _waveHeight.value = "1.1 公尺 / m"
                _windSpeed.value = "11 節 / knots"
                _visibility.value = "10 公里 / km"
                _safetyStars.value = "★★★★☆"
            }
            else -> {
                _waveHeight.value = "0.8 公尺"
                _windSpeed.value = "10 節"
                _visibility.value = "14 公里"
                _safetyStars.value = "★★★★☆"
            }
        }
        updateWeatherTexts()
    }

    private fun updateWeatherTexts() {
        val dep = _selectedDeparture.value
        val isEng = _isEnglish.value
        when {
            dep.contains("台東") || dep.contains("綠島") || dep.contains("蘭嶼") -> {
                _safetyNote.value = if (isEng) {
                    "Pacific Kuroshio route: currents change rapidly. Motion sickness medicine recommended 30 min prior."
                } else {
                    "太平洋黑潮航線：風浪可能較大，建議易暈船旅客前半小時服用暈船藥。"
                }
            }
            dep.contains("東港") || dep.contains("小琉球") -> {
                _safetyNote.value = if (isEng) {
                    "Inner coast strait: calm waters, short distance (25-30m), very smooth and stable voyage."
                } else {
                    "內海海峽平穩：水深平緩，航程極短（僅25-30分鐘），極適宜全家出遊。"
                }
            }
            dep.contains("後壁湖") -> {
                _safetyNote.value = if (isEng) {
                    "Bashi Channel convergence: strong swells may exceed 2.0m. Keep secure and stay seated."
                } else {
                    "巴士海峽湧浪交匯：強風長浪可能超過 2.0公尺，航行中請遵循安全配置就座。"
                }
            }
            dep.contains("高雄") || dep.contains("嘉義") || dep.contains("澎湖") -> {
                _safetyNote.value = if (isEng) {
                    "Taiwan Strait open passage: safe navigation. Bring government photo IDs for real-identity check."
                } else {
                    "台灣海峽跨海長程航線：視野深遠。請攜帶國民身分證件配合海巡署實名制碼頭檢查。"
                }
            }
        }
    }

    fun bookTicket() {
        val passenger = _passengerName.value.trim()
        val seat = _selectedSeat.value
        val isEng = _isEnglish.value

        if (passenger.isEmpty()) {
            _feedbackMessage.value = if (isEng) "Please enter actual passenger name!" else "請輸入搭船乘客人真實姓名公務申報！"
            return
        }
        if (seat == null) {
            _feedbackMessage.value = if (isEng) "Please tap to select an available seat!" else "請從智慧客艙中點選搭船對號座位！"
            return
        }

        val codeSub = Random.nextInt(100000, 999999)
        val code = "BOF-$codeSub"

        val booking = FerryBooking(
            departure = _selectedDeparture.value,
            destination = _selectedDestination.value,
            passengerName = passenger,
            seatNumber = seat,
            price = getTicketPrice(),
            isConcession = _isConcession.value,
            bookingCode = code
        )

        viewModelScope.launch {
            try {
                repository.insert(booking)
                _activeBooking.value = booking
                _feedbackMessage.value = if (isEng) "Booking Successful! Saved in secure system." else "劃位預訂票務成功！請保留以下電子乘船特考航行證。"
                
                val updatedOccupied = _occupiedSeats.value.toMutableSet()
                updatedOccupied.add(seat)
                _occupiedSeats.value = updatedOccupied
                _selectedSeat.value = null
            } catch (e: Exception) {
                _feedbackMessage.value = if (isEng) "System Booking Error: ${e.message}" else "劃座預訂失敗：${e.message}"
            }
        }
    }

    fun askGeminiAI() {
        val isEng = _isEnglish.value
        val passenger = _passengerName.value.trim().ifEmpty { if (isEng) "Valued Passenger" else "尊榮旅人" }
        val seat = _selectedSeat.value ?: _activeBooking.value?.seatNumber ?: "1A"
        
        _isAiLoading.value = true
        _aiResponse.value = null

        viewModelScope.launch {
            val response = GeminiHelper.generatePassageNotes(
                departure = _selectedDeparture.value,
                destination = _selectedDestination.value,
                passengerName = passenger,
                seatNumber = seat,
                waveHeight = _waveHeight.value,
                windSpeed = _windSpeed.value,
                isConcession = _isConcession.value,
                isEnglish = isEng
            )
            _aiResponse.value = response
            _isAiLoading.value = false
        }
    }

    fun deleteBookingRecord(booking: FerryBooking) {
        viewModelScope.launch {
            repository.delete(booking)
            if (_activeBooking.value?.id == booking.id) {
                _activeBooking.value = null
            }
        }
    }

    fun clearAllRecords() {
        viewModelScope.launch {
            repository.clearAll()
            _activeBooking.value = null
        }
    }
}
