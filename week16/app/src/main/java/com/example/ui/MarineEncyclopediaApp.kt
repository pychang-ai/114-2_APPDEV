package com.example.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.MarineSpecies
import com.example.data.SeedData
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarineEncyclopediaApp(
    viewModel: MarineViewModel = viewModel()
) {
    val lang by viewModel.language.collectAsStateWithLifecycle()
    val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
    val selectedSpecies by viewModel.selectedSpecies.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "Logo",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(28.dp)
                            )
                            Text(
                                text = Localization.get("app_title", lang),
                                color = MaterialTheme.colorScheme.onBackground,
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp,
                                fontFamily = FontFamily.SansSerif
                            )
                        }
                    },
                    actions = {
                        // Language Quick-Selector Toggle (Minimalist Pill Style)
                        Row(
                            modifier = Modifier
                                .padding(end = 12.dp)
                                .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(20.dp))
                                .border(1.dp, MinBorder, RoundedCornerShape(20.dp))
                                .padding(2.dp)
                        ) {
                            Text(
                                text = "中文",
                                color = if (lang == Language.ZH) MinTextPrimary else MinTextMuted,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(if (lang == Language.ZH) MinSecondary else Color.Transparent)
                                    .clickable { viewModel.setLanguage(Language.ZH) }
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                            Text(
                                text = "EN",
                                color = if (lang == Language.EN) MinTextPrimary else MinTextMuted,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(if (lang == Language.EN) MinSecondary else Color.Transparent)
                                    .clickable { viewModel.setLanguage(Language.EN) }
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background,
                        titleContentColor = MaterialTheme.colorScheme.onBackground
                    )
                )
                HorizontalDivider(color = MinBorder, thickness = 1.dp)
            }
        },
        bottomBar = {
            Column {
                HorizontalDivider(color = MinBorder, thickness = 1.dp)
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.background,
                    tonalElevation = 0.dp, // Flat clean style
                    windowInsets = WindowInsets.navigationBars
                ) {
                    val items = listOf(
                        Triple("HOME", Icons.Default.Home, "tab_home"),
                        Triple("ENCYCLOPEDIA", Icons.Default.Search, "tab_encyclopedia"),
                        Triple("FAVORITES", Icons.Default.Favorite, "tab_favorites"),
                        Triple("TRIVIA", Icons.Default.Star, "tab_settings")
                    )

                    items.forEach { (route, icon, labelKey) ->
                        val selected = currentScreen == route
                        NavigationBarItem(
                            selected = selected,
                            onClick = { viewModel.setScreen(route) },
                            icon = {
                                Icon(
                                    imageVector = icon,
                                    contentDescription = Localization.get(labelKey, lang),
                                    tint = if (selected) MaterialTheme.colorScheme.primary else MinTextMuted
                                )
                            },
                            label = {
                                Text(
                                    text = Localization.get(labelKey, lang),
                                    fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                                    fontSize = 11.sp,
                                    color = if (selected) MaterialTheme.colorScheme.primary else MinTextMuted
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                indicatorColor = MinSecondary,
                                selectedIconColor = MaterialTheme.colorScheme.primary,
                                unselectedIconColor = MinTextMuted
                            ),
                            modifier = Modifier.testTag("nav_tab_${route.lowercase()}")
                        )
                    }
                }
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Screen Switcher / Router with slide transitions
            AnimatedContent(
                targetState = currentScreen,
                transitionSpec = {
                    fadeIn(animationSpec = tween(220)) togetherWith fadeOut(animationSpec = tween(180))
                },
                label = "screen_routing"
            ) { target ->
                when (target) {
                    "HOME" -> HomeScreen(viewModel = viewModel, lang = lang)
                    "ENCYCLOPEDIA" -> EncyclopediaScreen(viewModel = viewModel, lang = lang)
                    "FAVORITES" -> FavoritesScreen(viewModel = viewModel, lang = lang)
                    "TRIVIA" -> TriviaScreen(viewModel = viewModel, lang = lang)
                    else -> HomeScreen(viewModel = viewModel, lang = lang)
                }
            }

            // High-fidelity species specification overlay (Bespoke Bottom Sheet modal dialog)
            selectedSpecies?.let { species ->
                SpeciesDetailOverlay(
                    species = species,
                    lang = lang,
                    onDismiss = { viewModel.selectSpecies(null) },
                    onToggleFavorite = { viewModel.toggleFavorite(species) }
                )
            }
        }
    }
}

@Composable
fun HomeScreen(
    viewModel: MarineViewModel,
    lang: Language
) {
    val allSpecies by viewModel.allSpecies.collectAsStateWithLifecycle(initialValue = emptyList())
    val featured = remember(allSpecies) {
        allSpecies.firstOrNull { it.id == 1 } ?: SeedData.speciesList.first()
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 24.dp)
    ) {
        // Welcoming Deep Sea Banner Card (Clean Minimal Gradient Styling)
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .background(
                        Brush.linearGradient(
                            colors = listOf(MinGradientStart, MinGradientEnd)
                        )
                    )
                    .border(1.dp, MinBorder, RoundedCornerShape(24.dp))
                    .padding(24.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = Localization.get("home_welcome", lang),
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = MinTextPrimary,
                        letterSpacing = (-0.5).sp
                    )
                    Text(
                        text = Localization.get("home_subtitle", lang),
                        fontSize = 14.sp,
                        color = MinTextTertiary,
                        lineHeight = 22.sp
                    )
                }
            }
        }

        // Feature marine animal of the day
        item {
            Text(
                text = "✨ " + Localization.get("featured_species", lang),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MinTextPrimary,
                modifier = Modifier.padding(bottom = 4.dp, start = 4.dp)
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { viewModel.selectSpecies(featured) }
                    .testTag("featured_species_card"),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MinBorder)
            ) {
                Column {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .background(Color(0xFFEBF3FC))
                    ) {
                        MarineAnimalIllustration(
                            key = featured.illustrationKey,
                            modifier = Modifier.fillMaxSize()
                        )
                        // Classification Badge
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopStart)
                                .padding(12.dp)
                                .background(Color.White.copy(alpha = 0.95f), RoundedCornerShape(50.dp))
                                .border(1.dp, MinBorder, RoundedCornerShape(50.dp))
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = if (lang == Language.ZH) featured.categoryZh else featured.categoryEn,
                                color = MinPrimary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = if (lang == Language.ZH) featured.nameZh else featured.nameEn,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MinTextPrimary
                                )
                                Text(
                                    text = featured.scientificName,
                                    fontSize = 12.sp,
                                    fontStyle = FontStyle.Italic,
                                    color = MinTextMuted
                                )
                            }
                            IconButton(
                                onClick = { viewModel.toggleFavorite(featured) }
                            ) {
                                Icon(
                                    imageVector = if (featured.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                    contentDescription = "Favorite",
                                    tint = if (featured.isFavorite) Color(0xFFFF4C5E) else MinTextMuted
                                )
                            }
                        }

                        Text(
                            text = if (lang == Language.ZH) featured.descriptionZh else featured.descriptionEn,
                            fontSize = 13.sp,
                            color = MinTextSecondary,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            lineHeight = 18.sp
                        )
                    }
                }
            }
        }

        // Discover/Quick jump Categories Banner Menu
        item {
            Text(
                text = "📁 " + (if (lang == Language.ZH) "依科屬快速分類" else "Quick Categories"),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MinTextPrimary,
                modifier = Modifier.padding(top = 8.dp, start = 4.dp)
            )
        }

        item {
            val categorizations = listOf(
                Pair("海洋哺乳類", "Marine Mammals"),
                Pair("硬骨魚類", "Osteichthyes"),
                Pair("軟骨魚類", "Chondrichthyes"),
                Pair("海洋無脊椎動物", "Marine Invertebrates")
            )

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(categorizations) { cat ->
                    Box(
                        modifier = Modifier
                            .width(160.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(MaterialTheme.colorScheme.surface)
                            .border(1.dp, MinBorder, RoundedCornerShape(16.dp))
                            .clickable {
                                viewModel.setCategory(if (lang == Language.ZH) cat.first else cat.second)
                                viewModel.setScreen("ENCYCLOPEDIA")
                            }
                            .padding(16.dp)
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(
                                text = cat.first,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = MinTextPrimary
                            )
                            Text(
                                text = cat.second,
                                fontSize = 11.sp,
                                color = MinTextMuted,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }
        }

        // Interesting Ecological Tips footer
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(MinSecondary.copy(alpha = 0.3f))
                    .border(1.dp, MinBorder, RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Text(
                    text = Localization.get("home_quick_tips", lang),
                    fontSize = 12.sp,
                    color = MinTextSecondary,
                    lineHeight = 18.sp
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EncyclopediaScreen(
    viewModel: MarineViewModel,
    lang: Language
) {
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val selectedCategory by viewModel.selectedCategory.collectAsStateWithLifecycle()
    val filteredSpecies by viewModel.filteredSpecies.collectAsStateWithLifecycle()

    val categories = listOf(
        "" to "category_all",
        "海洋哺乳類" to "category_mammal",
        "軟骨魚類" to "category_chondrichthyes",
        "硬骨魚類" to "category_osteichthyes",
        "海洋爬蟲類" to "category_reptiles",
        "海洋無脊椎動物" to "category_invertebrates",
        "海洋鳥類" to "category_aves"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        // Search text box
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { viewModel.updateSearchQuery(it) },
            placeholder = {
                Text(
                    text = Localization.get("search_hint", lang),
                    color = MinTextMuted,
                    fontSize = 13.sp
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "SearchIcon",
                    tint = MinPrimary
                )
            },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { viewModel.updateSearchQuery("") }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Clear",
                            tint = MinTextPrimary
                        )
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
                .testTag("species_search_input"),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = MinTextPrimary,
                unfocusedTextColor = MinTextSecondary,
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedBorderColor = MinPrimary,
                unfocusedBorderColor = MinBorder,
                focusedPlaceholderColor = MinTextMuted,
                unfocusedPlaceholderColor = MinTextMuted
            ),
            shape = RoundedCornerShape(30.dp),
            singleLine = true
        )

        // Horizontal Category Selectors list
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(categories) { (catDbKey, localizedKey) ->
                val active = selectedCategory == catDbKey ||
                        (selectedCategory == "Marine Mammals" && catDbKey == "海洋哺乳類") ||
                        (selectedCategory == "Chondrichthyes" && catDbKey == "軟骨魚類") ||
                        (selectedCategory == "Osteichthyes" && catDbKey == "硬骨魚類") ||
                        (selectedCategory == "Reptiles" && catDbKey == "海洋爬蟲類") ||
                        (selectedCategory == "Invertebrates" && catDbKey == "海洋無脊椎動物") ||
                        (selectedCategory == "Marine Aves / Birds" && catDbKey == "海洋鳥類")

                val labelText = Localization.get(localizedKey, lang)

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(if (active) MinPrimary else MaterialTheme.colorScheme.surface)
                        .border(
                            1.dp,
                            if (active) MinPrimary else MinBorder,
                            RoundedCornerShape(20.dp)
                        )
                        .clickable {
                            viewModel.setCategory(catDbKey)
                        }
                        .padding(horizontal = 14.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = labelText,
                        color = if (active) Color.White else MinTextSecondary,
                        fontWeight = if (active) FontWeight.Bold else FontWeight.Normal,
                        fontSize = 12.sp
                    )
                }
            }
        }

        // Marine Species browsing LazyList
        if (filteredSpecies.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "no records",
                        tint = MinTextMuted.copy(alpha = 0.4f),
                        modifier = Modifier.size(64.dp)
                    )
                    Text(
                        text = Localization.get("empty_search", lang),
                        color = MinTextMuted,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                items(filteredSpecies, key = { it.id }) { s ->
                    SpeciesItemCard(
                        species = s,
                        lang = lang,
                        onClick = { viewModel.selectSpecies(s) },
                        onToggleFavorite = { viewModel.toggleFavorite(s) }
                    )
                }
            }
        }
    }
}

@Composable
fun SpeciesItemCard(
    species: MarineSpecies,
    lang: Language,
    onClick: () -> Unit,
    onToggleFavorite: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("species_card_${species.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MinBorder)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Little round thumb preview of Canvas illustration
            Box(
                modifier = Modifier
                    .size(76.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFEBF3FC))
            ) {
                MarineAnimalIllustration(
                    key = species.illustrationKey,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            // Text components
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = if (lang == Language.ZH) species.nameZh else species.nameEn,
                    color = MinTextPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Text(
                    text = species.scientificName,
                    color = MinTextMuted,
                    fontSize = 12.sp,
                    fontStyle = FontStyle.Italic
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = if (lang == Language.ZH) species.categoryZh else species.categoryEn,
                    color = MinPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp
                )
            }

            // Quick Bookmarking Toggle Button
            IconButton(
                onClick = onToggleFavorite,
                modifier = Modifier.testTag("item_bookmark_${species.id}")
            ) {
                Icon(
                    imageVector = if (species.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Favorite Toggle",
                    tint = if (species.isFavorite) Color(0xFFFF4C5E) else MinTextMuted
                )
            }
        }
    }
}

@Composable
fun FavoritesScreen(
    viewModel: MarineViewModel,
    lang: Language
) {
    val favoriteSpecies by viewModel.favoriteSpecies.collectAsStateWithLifecycle(initialValue = emptyList())

    if (favoriteSpecies.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Large styled glowing outline heart
                Icon(
                    imageVector = Icons.Default.FavoriteBorder,
                    contentDescription = "Empty Favorites",
                    tint = Color(0xFFFF4C5E).copy(alpha = 0.35f),
                    modifier = Modifier.size(80.dp)
                )
                Text(
                    text = Localization.get("empty_favorites", lang),
                    color = MinTextSecondary,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    lineHeight = 22.sp
                )
            }
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = "❤️ " + (if (lang == Language.ZH) "已加入離線查看的圖鑑" else "Bookmarked Marine Life"),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MinTextPrimary,
                modifier = Modifier.padding(top = 16.dp, bottom = 12.dp)
            )

            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                items(favoriteSpecies, key = { it.id }) { s ->
                    SpeciesItemCard(
                        species = s,
                        lang = lang,
                        onClick = { viewModel.selectSpecies(s) },
                        onToggleFavorite = { viewModel.toggleFavorite(s) }
                    )
                }
            }
        }
    }
}

@Composable
fun TriviaScreen(
    viewModel: MarineViewModel,
    lang: Language
) {
    val currentQuestionIndex by viewModel.currentQuestionIndex.collectAsStateWithLifecycle()
    val triviaScore by viewModel.triviaScore.collectAsStateWithLifecycle()
    val selectedAnswerIndex by viewModel.selectedAnswerIndex.collectAsStateWithLifecycle()
    val isAnswered by viewModel.isAnswered.collectAsStateWithLifecycle()
    val isTriviaFinished by viewModel.isTriviaFinished.collectAsStateWithLifecycle()

    val totalQuestions = TriviaData.questions.size

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Gamified Quiz Banner
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        Brush.linearGradient(
                            colors = listOf(MinGradientStart, MinGradientEnd)
                        )
                    )
                    .border(1.dp, MinBorder, RoundedCornerShape(20.dp))
                    .padding(18.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = Localization.get("trivia_title", lang),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MinTextPrimary
                    )
                    Text(
                        text = Localization.get("trivia_subtitle", lang),
                        fontSize = 12.sp,
                        color = MinTextTertiary,
                        lineHeight = 16.sp
                    )
                }
            }
        }

        if (!isTriviaFinished) {
            val question = TriviaData.questions[currentQuestionIndex]
            val questionText = if (lang == Language.ZH) question.questionZh else question.questionEn
            val options = if (lang == Language.ZH) question.optionsZh else question.optionsEn
            val explanation = if (lang == Language.ZH) question.explanationZh else question.explanationEn

            // Progress / Score tracking
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${Localization.get("trivia_question", lang)} ${currentQuestionIndex + 1} / $totalQuestions",
                        color = MinPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${Localization.get("trivia_score", lang)}: $triviaScore",
                        color = MinTextPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                LinearProgressIndicator(
                    progress = { (currentQuestionIndex + 1).toFloat() / totalQuestions },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = MinPrimary,
                    trackColor = MinBorder
                )
            }

            // Question Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, MinBorder)
                ) {
                    Text(
                        text = questionText,
                        fontSize = 16.sp,
                        color = MinTextPrimary,
                        fontWeight = FontWeight.SemiBold,
                        lineHeight = 24.sp,
                        modifier = Modifier.padding(20.dp)
                    )
                }
            }

            // Multiple choice options list
            items(options.size) { idx ->
                val optionText = options[idx]
                val borderTone: Color
                val fillTone: Color
                val textTone: Color

                if (isAnswered) {
                    when {
                        idx == question.correctIndex -> {
                            borderTone = Color(0xFF4CAF50)
                            fillTone = Color(0xFFE8F5E9)
                            textTone = Color(0xFF2E7D32)
                        }
                        idx == selectedAnswerIndex -> {
                            borderTone = Color(0xFFF44336)
                            fillTone = Color(0xFFFFEBEE)
                            textTone = Color(0xFFC62828)
                        }
                        else -> {
                            borderTone = MinBorder
                            fillTone = MaterialTheme.colorScheme.surface
                            textTone = MinTextMuted
                        }
                    }
                } else {
                    borderTone = MinBorder
                    fillTone = MaterialTheme.colorScheme.surface
                    textTone = MinTextSecondary
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(fillTone)
                        .border(1.dp, borderTone, RoundedCornerShape(12.dp))
                        .clickable(enabled = !isAnswered) {
                            viewModel.submitTriviaAnswer(idx)
                        }
                        .padding(18.dp)
                        .testTag("trivia_option_$idx")
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // Choice indicator circle
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(borderTone.copy(alpha = 0.1f))
                                .border(1.5.dp, borderTone, RoundedCornerShape(12.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = ('A' + idx).toString(),
                                color = textTone,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp
                            )
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Text(
                            text = optionText,
                            color = textTone,
                            fontSize = 13.sp,
                            lineHeight = 18.sp,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            // Expanded rationale Explanation box (revealed upon answering)
            if (isAnswered) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(MinSecondary.copy(alpha = 0.3f))
                            .border(1.dp, MinBorder, RoundedCornerShape(16.dp))
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = if (selectedAnswerIndex == question.correctIndex) "🎉 Correct! / 答對了！" else "💡 Trivia Fact / 科學小知識",
                            fontWeight = FontWeight.Bold,
                            color = MinTextPrimary,
                            fontSize = 13.sp
                        )
                        Text(
                            text = explanation,
                            color = MinTextSecondary,
                            fontSize = 12.sp,
                            lineHeight = 18.sp
                        )
                    }
                }

                item {
                    Button(
                        onClick = { viewModel.nextTriviaQuestion() },
                        colors = ButtonDefaults.buttonColors(containerColor = MinPrimary),
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .height(48.dp)
                            .testTag("trivia_next_button")
                    ) {
                        Text(
                            text = Localization.get(
                                if (currentQuestionIndex == totalQuestions - 1) "trivia_finish" else "trivia_next",
                                lang
                            ),
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }
            }

        } else {
            // Trivia Completed report summary
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(24.dp))
                        .background(MaterialTheme.colorScheme.surface)
                        .border(1.dp, MinBorder, RoundedCornerShape(24.dp))
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = Localization.get("trivia_result_title", lang),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MinTextPrimary
                    )

                    // Big glowing score badge
                    Box(
                        modifier = Modifier
                            .size(120.dp)
                            .clip(RoundedCornerShape(60.dp))
                            .background(Brush.radialGradient(listOf(MinSecondary.copy(alpha = 0.4f), Color.Transparent)))
                            .border(2.dp, MinPrimary, RoundedCornerShape(60.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "$triviaScore", fontSize = 48.sp, fontWeight = FontWeight.Bold, color = MinPrimary)
                            Text(text = "/ $totalQuestions", fontSize = 14.sp, color = MinTextMuted)
                        }
                    }

                    // Level feedback text based on scores
                    val message = when {
                        triviaScore == totalQuestions -> Localization.get("trivia_level_perfect", lang)
                        triviaScore >= totalQuestions - 2 -> Localization.get("trivia_level_good", lang)
                        else -> Localization.get("trivia_level_rookie", lang)
                    }

                    Text(
                        text = message,
                        fontSize = 14.sp,
                        color = MinTextSecondary,
                        textAlign = TextAlign.Center,
                        lineHeight = 20.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )

                    HorizontalDivider(color = MinBorder, thickness = 1.dp, modifier = Modifier.padding(vertical = 4.dp))

                    Button(
                        onClick = { viewModel.restartTrivia() },
                        colors = ButtonDefaults.buttonColors(containerColor = MinSecondary),
                        border = BorderStroke(1.dp, MinBorder),
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .height(48.dp)
                            .testTag("trivia_restart_button")
                    ) {
                        Text(
                            text = Localization.get("trivia_restart", lang),
                            color = MinTextPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
}

// Gorgeous Detail Overlay mapping (Bespoke Full Dialog Sheet)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpeciesDetailOverlay(
    species: MarineSpecies,
    lang: Language,
    onDismiss: () -> Unit,
    onToggleFavorite: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false, // covers entire screen size class elegantly
            dismissOnBackPress = true,
            dismissOnClickOutside = false
        )
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = if (lang == Language.ZH) species.nameZh else species.nameEn,
                            color = MinTextPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = onDismiss,
                            modifier = Modifier.testTag("detail_back_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back",
                                tint = MinTextPrimary
                            )
                        }
                    },
                    actions = {
                        // Quick toggle button inside dialogue bar
                        IconButton(
                            onClick = onToggleFavorite,
                            modifier = Modifier.testTag("detail_bookmark_toggle")
                        ) {
                            Icon(
                                imageVector = if (species.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = "Favorite",
                                tint = if (species.isFavorite) Color(0xFFFF4C5E) else MinTextMuted
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
                )
            },
            containerColor = MaterialTheme.colorScheme.background
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
            ) {
                // Large animated vector illustration banner
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .background(Color(0xFFEBF3FC))
                ) {
                    MarineAnimalIllustration(
                        key = species.illustrationKey,
                        modifier = Modifier.fillMaxSize()
                    )

                    // Classification tag overlapping
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(16.dp)
                            .background(Color.White.copy(alpha = 0.95f), RoundedCornerShape(50.dp))
                            .border(1.dp, MinBorder, RoundedCornerShape(50.dp))
                            .padding(horizontal = 14.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = if (lang == Language.ZH) species.categoryZh else species.categoryEn,
                            color = MinPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                }

                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Scientific Designation Card
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = if (lang == Language.ZH) species.nameZh else species.nameEn,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = MinTextPrimary
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "${Localization.get("detail_scientific", lang)}: ",
                                color = MinTextMuted,
                                fontSize = 13.sp
                            )
                            Text(
                                text = species.scientificName,
                                color = MinPrimary,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                fontStyle = FontStyle.Italic
                            )
                        }
                    }

                    HorizontalDivider(color = MinBorder, thickness = 1.dp)

                    // Description text block
                    Text(
                        text = if (lang == Language.ZH) species.descriptionZh else species.descriptionEn,
                        fontSize = 14.sp,
                        color = MinTextSecondary,
                        lineHeight = 22.sp,
                        fontFamily = FontFamily.SansSerif
                    )

                    // Compact stats matrix board
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(MaterialTheme.colorScheme.surface)
                            .border(1.dp, MinBorder, RoundedCornerShape(16.dp))
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Title for stats
                        Text(
                            text = if (lang == Language.ZH) "📊 生態調查數據" else "📊 Ecological Profile",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = MinTextPrimary
                        )

                        val stats = listOf(
                            Triple(Localization.get("detail_depth", lang), species.depth, Icons.Default.Info),
                            Triple(Localization.get("detail_size", lang), species.size, Icons.Default.Info),
                            Triple(Localization.get("detail_habitat", lang), if (lang == Language.ZH) species.habitatZh else species.habitatEn, Icons.Default.Info),
                            Triple(Localization.get("detail_diet", lang), if (lang == Language.ZH) species.dietZh else species.dietEn, Icons.Default.Info)
                        )

                        stats.forEach { (label, value, icon) ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.Top
                            ) {
                                Text(
                                    text = "$label：",
                                    color = MinTextTertiary,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.width(90.dp)
                                )
                                Text(
                                    text = value,
                                    color = MinTextSecondary,
                                    fontSize = 13.sp,
                                    lineHeight = 18.sp,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }

                        // Danger Level meters
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${Localization.get("detail_danger", lang)}：",
                                color = MinTextTertiary,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.width(90.dp)
                            )
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                for (i in 1..5) {
                                    Box(
                                        modifier = Modifier
                                            .size(width = 24.dp, height = 8.dp)
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(
                                                if (i <= species.dangerLevel) {
                                                    if (species.dangerLevel >= 4) MinPrimary else MinSecondary
                                                } else {
                                                    MinBorder
                                                }
                                            )
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                val dangerText = when (species.dangerLevel) {
                                    1 -> Localization.get("danger_1", lang)
                                    2 -> Localization.get("danger_2", lang)
                                    3 -> Localization.get("danger_3", lang)
                                    4 -> Localization.get("danger_4", lang)
                                    else -> Localization.get("danger_5", lang)
                                }
                                Text(
                                    text = dangerText,
                                    color = if (species.dangerLevel >= 4) MinTertiary else MinPrimary,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    // Trivia fun-fact glowing bubble block
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(MinGradientStart.copy(alpha = 0.3f), MinGradientEnd.copy(alpha = 0.3f))
                                )
                            )
                            .border(1.dp, MinBorder, RoundedCornerShape(16.dp))
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = "FunFact",
                                tint = MinPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = Localization.get("detail_fun_fact", lang),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = MinTextPrimary
                            )
                        }
                        Text(
                            text = if (lang == Language.ZH) species.funFactZh else species.funFactEn,
                            fontSize = 13.sp,
                            color = MinTextSecondary,
                            lineHeight = 20.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}
