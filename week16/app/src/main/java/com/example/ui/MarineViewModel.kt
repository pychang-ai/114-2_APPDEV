package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.MarineDatabase
import com.example.data.MarineRepository
import com.example.data.MarineSpecies
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MarineViewModel(application: Application) : AndroidViewModel(application) {
    private val database = MarineDatabase.getDatabase(application)
    private val repository = MarineRepository(database.marineDao())

    // Language configuration
    private val _language = MutableStateFlow(Language.ZH)
    val language: StateFlow<Language> = _language.asStateFlow()

    // Navigation configuration: "HOME", "ENCYCLOPEDIA", "FAVORITES", "TRIVIA"
    private val _currentScreen = MutableStateFlow("HOME")
    val currentScreen: StateFlow<String> = _currentScreen.asStateFlow()

    // Filtering inputs
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow("") // "" means ALL
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    // Active species bottom sheet or detail dialog
    private val _selectedSpecies = MutableStateFlow<MarineSpecies?>(null)
    val selectedSpecies: StateFlow<MarineSpecies?> = _selectedSpecies.asStateFlow()

    // Data lists from database
    val allSpecies = repository.allSpecies
    val favoriteSpecies = repository.favoriteSpecies

    // Combined/Filtered result lists for search
    val filteredSpecies = combine(allSpecies, _searchQuery, _selectedCategory) { list, query, category ->
        list.filter { item ->
            // Filter by Category
            val matchesCategory = category.isEmpty() || item.categoryZh == category || item.categoryEn == category
            
            // Filter by query (handles name in ZH, EN, and Scientific text)
            val matchesQuery = query.isEmpty() ||
                    item.nameZh.contains(query, ignoreCase = true) ||
                    item.nameEn.contains(query, ignoreCase = true) ||
                    item.scientificName.contains(query, ignoreCase = true) ||
                    item.categoryZh.contains(query, ignoreCase = true) ||
                    item.categoryEn.contains(query, ignoreCase = true)

            matchesCategory && matchesQuery
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // Trivia Game States
    private val _currentQuestionIndex = MutableStateFlow(0)
    val currentQuestionIndex = _currentQuestionIndex.asStateFlow()

    private val _triviaScore = MutableStateFlow(0)
    val triviaScore = _triviaScore.asStateFlow()

    private val _selectedAnswerIndex = MutableStateFlow<Int?>(null)
    val selectedAnswerIndex = _selectedAnswerIndex.asStateFlow()

    private val _isAnswered = MutableStateFlow(false)
    val isAnswered = _isAnswered.asStateFlow()

    private val _isTriviaFinished = MutableStateFlow(false)
    val isTriviaFinished = _isTriviaFinished.asStateFlow()

    init {
        viewModelScope.launch {
            // Seed database instantly if first open
            repository.preseedDatabaseIfEmpty()
        }
    }

    fun setLanguage(lang: Language) {
        _language.value = lang
    }

    fun setScreen(screen: String) {
        _currentScreen.value = screen
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setCategory(category: String) {
        _selectedCategory.value = category
    }

    fun selectSpecies(species: MarineSpecies?) {
        _selectedSpecies.value = species
    }

    fun toggleFavorite(species: MarineSpecies) {
        viewModelScope.launch {
            repository.updateFavorite(species.id, !species.isFavorite)
            // also update selectedSpecies if active so the dialog reacts immediately
            if (_selectedSpecies.value?.id == species.id) {
                _selectedSpecies.value = species.copy(isFavorite = !species.isFavorite)
            }
        }
    }

    // Trivia Actions
    fun submitTriviaAnswer(optionIndex: Int) {
        if (_isAnswered.value) return
        _selectedAnswerIndex.value = optionIndex
        _isAnswered.value = true
        val currentQuestion = TriviaData.questions[_currentQuestionIndex.value]
        if (optionIndex == currentQuestion.correctIndex) {
            _triviaScore.value += 1
        }
    }

    fun nextTriviaQuestion() {
        if (_currentQuestionIndex.value < TriviaData.questions.lastIndex) {
            _currentQuestionIndex.value += 1
            _selectedAnswerIndex.value = null
            _isAnswered.value = false
        } else {
            _isTriviaFinished.value = true
        }
    }

    fun restartTrivia() {
        _currentQuestionIndex.value = 0
        _triviaScore.value = 0
        _selectedAnswerIndex.value = null
        _isAnswered.value = false
        _isTriviaFinished.value = false
    }
}
