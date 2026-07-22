package com.example.ui.screens

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import com.example.ui.viewmodel.NextHireViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppContainer(viewModel: NextHireViewModel) {
    val isLoggedIn by viewModel.isLoggedIn.collectAsState()
    val selectedTab by viewModel.selectedTab.collectAsState()
    val isAdminMode by viewModel.isAdminMode.collectAsState()
    val isDarkMode by viewModel.isDarkMode.collectAsState()
    val profile by viewModel.userProfile.collectAsState()

    NextHireTheme(darkTheme = isDarkMode) {
        if (!isLoggedIn) {
            AuthScreen(viewModel = viewModel)
        } else {
            GlassCanvasBackground(isDark = isDarkMode) {
                Scaffold(
                    containerColor = Color.Transparent,
                    topBar = {
                        TopAppBar(
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = if (isDarkMode) GlassSurfaceDark.copy(alpha = 0.85f) else GlassSurfaceLight.copy(alpha = 0.85f)
                            ),
                            modifier = Modifier.border(
                                BorderStroke(
                                    1.dp,
                                    if (isDarkMode) GlassBorderSubtleDark else GlassBorderSubtleLight
                                )
                            ),
                            title = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "NextHire AI",
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    GlassChip(
                                        text = profile?.degree ?: "B.Tech",
                                        accentColor = Indigo600,
                                        isDark = isDarkMode
                                    )
                                }
                            },
                            actions = {
                                // Dark/Light Theme Toggle
                                IconButton(onClick = { viewModel.isDarkMode.value = !isDarkMode }) {
                                    Icon(
                                        imageVector = if (isDarkMode) Icons.Default.LightMode else Icons.Default.DarkMode,
                                        contentDescription = "Toggle Dark Mode",
                                        tint = if (isDarkMode) DarkTextPrimary else Slate900
                                    )
                                }

                                // Super Admin CMS Mode Switcher
                                FilterChip(
                                    selected = isAdminMode,
                                    onClick = { viewModel.toggleAdminMode() },
                                    label = {
                                        Text(
                                            text = if (isAdminMode) "CMS Mode 🔑" else "Admin Access",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    },
                                    leadingIcon = {
                                        Icon(
                                            imageVector = Icons.Default.AdminPanelSettings,
                                            contentDescription = null,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    },
                                    border = FilterChipDefaults.filterChipBorder(
                                        borderColor = if (isDarkMode) GlassBorderDark else GlassBorderLight,
                                        enabled = true,
                                        selected = isAdminMode
                                    ),
                                    colors = FilterChipDefaults.filterChipColors(
                                        containerColor = if (isDarkMode) GlassSurfaceDarkLow else GlassSurfaceLightLow,
                                        selectedContainerColor = Indigo600.copy(alpha = 0.2f)
                                    ),
                                    modifier = Modifier.padding(end = 8.dp)
                                )
                            }
                        )
                    },
                    bottomBar = {
                        NavigationBar(
                            containerColor = if (isDarkMode) GlassSurfaceDark.copy(alpha = 0.9f) else GlassSurfaceLight.copy(alpha = 0.9f),
                            tonalElevation = 0.dp,
                            modifier = Modifier
                                .border(
                                    BorderStroke(
                                        1.dp,
                                        if (isDarkMode) GlassBorderDark else GlassBorderLight
                                    )
                                )
                                .windowInsetsPadding(WindowInsets.navigationBars)
                        ) {
                            val items = listOf(
                                Triple(0, Icons.Default.Dashboard, "Home"),
                                Triple(1, Icons.Default.Psychology, "Mentor"),
                                Triple(2, Icons.Default.Description, "ATS"),
                                Triple(3, Icons.Default.Code, "Coding"),
                                Triple(4, Icons.Default.Work, "Jobs"),
                                Triple(5, Icons.Default.LibraryBooks, "Resources"),
                                Triple(6, Icons.Default.Forum, "Community")
                            )

                            items.forEach { (index, icon, label) ->
                                NavigationBarItem(
                                    selected = selectedTab == index,
                                    onClick = { viewModel.selectedTab.value = index },
                                    icon = { Icon(icon, contentDescription = label) },
                                    label = { Text(label, fontSize = 10.sp, fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal) },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = Indigo600,
                                        selectedTextColor = Indigo600,
                                        indicatorColor = Indigo100.copy(alpha = 0.5f),
                                        unselectedIconColor = if (isDarkMode) DarkTextSecondary else Slate500,
                                        unselectedTextColor = if (isDarkMode) DarkTextSecondary else Slate500
                                    )
                                )
                            }
                        }
                    }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        Crossfade(targetState = selectedTab, label = "TabSwitch") { tab ->
                            when (tab) {
                                0 -> DashboardScreen(viewModel = viewModel)
                                1 -> AiMentorScreen(viewModel = viewModel)
                                2 -> ResumeAtsScreen(viewModel = viewModel)
                                3 -> CodingPracticeScreen(viewModel = viewModel)
                                4 -> JobsPortalScreen(viewModel = viewModel)
                                5 -> ResourceHubScreen(viewModel = viewModel)
                                6 -> CommunityScreen(viewModel = viewModel)
                                7 -> AdminCmsScreen(viewModel = viewModel)
                                else -> DashboardScreen(viewModel = viewModel)
                            }
                        }
                    }
                }
            }
        }
    }
}

