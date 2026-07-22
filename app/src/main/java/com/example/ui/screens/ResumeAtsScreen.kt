package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
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
fun ResumeAtsScreen(viewModel: NextHireViewModel) {
    var selectedTabState by remember { mutableStateOf(0) } // 0: ATS Scanner, 1: Resume Builder
    val profile by viewModel.userProfile.collectAsState()
    val isDark = isSystemInDarkTheme()

    val atsResult by viewModel.atsResult.collectAsState()
    val isAtsLoading by viewModel.isAtsLoading.collectAsState()

    // Resume Input States
    var resumeText by remember {
        mutableStateOf(
            """
            ${profile?.name ?: "Rahul Sharma"} | ${profile?.degree ?: "B.Tech"} Class of ${profile?.passoutYear ?: "2026"}
            Email: ${profile?.email} | College: ${profile?.college}
            
            OBJECTIVE:
            ${profile?.resumeSummary}
            
            SKILLS:
            ${profile?.skills}
            
            PROJECTS:
            1. AI Smart Resume Generator: Built using Kotlin, Jetpack Compose, Room DB, Gemini AI REST API.
            2. E-Commerce Microservices: Built using Java, Spring Boot, Docker, Redis, and PostgreSQL.
            """.trimIndent()
        )
    }

    var targetJobDesc by remember {
        mutableStateOf(
            """
            Software Engineer / SDE-1 Role:
            Looking for candidates proficient in Java/Kotlin, Data Structures, Algorithms, REST APIs, SQL Databases, System Design, and Cloud basics. Experience with Git and Agile development is a plus.
            """.trimIndent()
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent)
    ) {
        // Frosted Glass Tab Row
        TabRow(
            selectedTabIndex = selectedTabState,
            containerColor = if (isDark) GlassSurfaceDark else GlassSurfaceLight,
            contentColor = Indigo600,
            modifier = Modifier.border(BorderStroke(1.dp, if (isDark) GlassBorderDark else GlassBorderLight))
        ) {
            Tab(
                selected = selectedTabState == 0,
                onClick = { selectedTabState = 0 },
                text = { Text("ATS Resume Checker", fontWeight = FontWeight.Bold, color = if (selectedTabState == 0) Indigo600 else if (isDark) DarkTextSecondary else Slate700) },
                icon = { Icon(Icons.Default.Analytics, contentDescription = null, tint = if (selectedTabState == 0) Indigo600 else if (isDark) DarkTextSecondary else Slate700) }
            )
            Tab(
                selected = selectedTabState == 1,
                onClick = { selectedTabState = 1 },
                text = { Text("Resume Builder", fontWeight = FontWeight.Bold, color = if (selectedTabState == 1) Indigo600 else if (isDark) DarkTextSecondary else Slate700) },
                icon = { Icon(Icons.Default.Edit, contentDescription = null, tint = if (selectedTabState == 1) Indigo600 else if (isDark) DarkTextSecondary else Slate700) }
            )
        }

        if (selectedTabState == 0) {
            // ATS Checker Content
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Text(
                        text = "ATS Resume Match Scanner 🎯",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = if (isDark) DarkTextPrimary else Slate900
                    )
                    Text(
                        text = "Scans your resume against the target company's job description using Gemini AI to boost shortlisting odds.",
                        style = MaterialTheme.typography.bodySmall,
                        color = if (isDark) DarkTextSecondary else Slate500
                    )
                }

                item {
                    OutlinedTextField(
                        value = resumeText,
                        onValueChange = { resumeText = it },
                        label = { Text("Your Resume Text / Content") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp),
                        shape = RoundedCornerShape(20.dp)
                    )
                }

                item {
                    OutlinedTextField(
                        value = targetJobDesc,
                        onValueChange = { targetJobDesc = it },
                        label = { Text("Target Job Description") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp),
                        shape = RoundedCornerShape(20.dp)
                    )
                }

                item {
                    GlassButton(
                        onClick = { viewModel.runAtsCheck(resumeText, targetJobDesc) },
                        enabled = !isAtsLoading,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                    ) {
                        if (isAtsLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Scanning Resume with Gemini AI...", fontWeight = FontWeight.Bold)
                        } else {
                            Icon(Icons.Default.Scanner, contentDescription = null, tint = Color.White)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Scan Resume for ATS Match Score", fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }

                if (atsResult.isNotBlank()) {
                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(
                                    BorderStroke(1.dp, if (isDark) GlassBorderDark else GlassBorderLight),
                                    shape = RoundedCornerShape(24.dp)
                                ),
                            shape = RoundedCornerShape(24.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isDark) GlassSurfaceDark else GlassSurfaceLight
                            ),
                            elevation = CardDefaults.cardElevation(4.dp)
                        ) {
                            Column(modifier = Modifier.padding(20.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = null,
                                        tint = Emerald500,
                                        modifier = Modifier.size(28.dp)
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(
                                        text = "ATS AI Scan Results",
                                        style = MaterialTheme.typography.titleLarge.copy(
                                            fontWeight = FontWeight.Bold
                                        ),
                                        color = if (isDark) DarkTextPrimary else Slate900
                                    )
                                }
                                HorizontalDivider(
                                    modifier = Modifier.padding(vertical = 12.dp),
                                    color = if (isDark) GlassBorderSubtleDark else GlassBorderSubtleLight
                                )
                                Text(
                                    text = atsResult,
                                    style = MaterialTheme.typography.bodyMedium,
                                    lineHeight = 22.sp,
                                    color = if (isDark) DarkTextPrimary else Slate900
                                )
                            }
                        }
                    }
                }
            }
        } else {
            // Live Resume Builder
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Text(
                        text = "Interactive Live Resume Preview",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = if (isDark) DarkTextPrimary else Slate900
                    )
                    Text(
                        text = "Formatted according to MNC & Harvard ATS-approved standard.",
                        style = MaterialTheme.typography.bodySmall,
                        color = if (isDark) DarkTextSecondary else Slate500
                    )
                }

                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(
                                BorderStroke(1.5.dp, if (isDark) GlassBorderDark else GlassBorderLight),
                                shape = RoundedCornerShape(24.dp)
                            ),
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isDark) GlassSurfaceDark else GlassSurfaceLight
                        ),
                        elevation = CardDefaults.cardElevation(6.dp)
                    ) {
                        Column(modifier = Modifier.padding(24.dp)) {
                            Text(
                                text = profile?.name?.uppercase() ?: "RAHUL SHARMA",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Indigo600
                                )
                            )
                            Text(
                                text = "${profile?.degree ?: "B.Tech"} Student • ${profile?.email ?: "rahul@college.edu"} • ${profile?.college ?: "State Institute"}",
                                style = MaterialTheme.typography.bodySmall,
                                color = if (isDark) DarkTextSecondary else Slate500
                            )

                            HorizontalDivider(
                                modifier = Modifier.padding(vertical = 12.dp),
                                color = if (isDark) GlassBorderSubtleDark else GlassBorderSubtleLight
                            )

                            Text(
                                text = "CAREER OBJECTIVE",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Indigo600
                                )
                            )
                            Text(
                                text = profile?.resumeSummary ?: "Aspiring SDE with strong computer science fundamentals.",
                                style = MaterialTheme.typography.bodySmall,
                                color = if (isDark) DarkTextPrimary else Slate900
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = "TECHNICAL SKILLS",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Indigo600
                                )
                            )
                            Text(
                                text = profile?.skills ?: "Java, Python, SQL, DSA, Android, Git",
                                style = MaterialTheme.typography.bodySmall,
                                color = if (isDark) DarkTextPrimary else Slate900
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = "FEATURED PROJECTS",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Indigo600
                                )
                            )
                            Text(
                                text = "• NextHire AI Placement App: Built with Jetpack Compose & Gemini API.\n• Microservices Architecture: Java Spring Boot & PostgreSQL.",
                                style = MaterialTheme.typography.bodySmall,
                                color = if (isDark) DarkTextPrimary else Slate900
                            )
                        }
                    }
                }
            }
        }
    }
}

