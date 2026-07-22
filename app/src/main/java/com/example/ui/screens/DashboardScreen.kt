package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.JobPost
import com.example.ui.theme.*
import com.example.ui.viewmodel.NextHireViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(viewModel: NextHireViewModel) {
    val profile by viewModel.userProfile.collectAsState()
    val announcements by viewModel.allAnnouncements.collectAsState()
    val jobs by viewModel.allJobs.collectAsState()
    val applications by viewModel.userApplications.collectAsState()
    val isDark = isSystemInDarkTheme()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // Hero Header Banner with Glass Overlay
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .border(
                        BorderStroke(1.dp, if (isDark) GlassBorderDark else GlassBorderLight),
                        shape = RoundedCornerShape(28.dp)
                    ),
                shape = RoundedCornerShape(28.dp),
                elevation = CardDefaults.cardElevation(6.dp)
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Image(
                        painter = painterResource(id = R.drawable.img_hero_banner_1784734482636),
                        contentDescription = "Hero Banner",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Color.Black.copy(alpha = 0.85f)
                                    )
                                )
                            )
                    )
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(20.dp)
                    ) {
                        GlassChip(
                            text = "🎓 ${profile?.degree ?: "B.Tech"} Class of ${profile?.passoutYear ?: "2026"}",
                            accentColor = Indigo500,
                            isDark = true
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Welcome back, ${profile?.name ?: "Student"}! 👋",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        )
                        Text(
                            text = "Target Role: ${profile?.targetRole ?: "Software Engineer"}",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.LightGray
                        )
                    }
                }
            }
        }

        // Stats Row
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard(
                    title = "Practice Streak",
                    value = "${profile?.streakDays ?: 12} Days 🔥",
                    modifier = Modifier.weight(1f),
                    isDark = isDark
                )
                StatCard(
                    title = "ATS Resume Score",
                    value = "${profile?.atsScore ?: 85}/100 🎯",
                    modifier = Modifier.weight(1f),
                    isDark = isDark
                )
                StatCard(
                    title = "Career Points",
                    value = "${profile?.points ?: 1250} pts 🌟",
                    modifier = Modifier.weight(1f),
                    isDark = isDark
                )
            }
        }

        // Announcements Banner
        if (announcements.isNotEmpty()) {
            item {
                val latest = announcements.first()
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            BorderStroke(1.dp, if (isDark) GlassBorderDark else GlassBorderLight),
                            shape = RoundedCornerShape(20.dp)
                        ),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (latest.isHighPriority) {
                            Rose500.copy(alpha = 0.2f)
                        } else {
                            if (isDark) GlassSurfaceDark else GlassSurfaceLight
                        }
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Campaign,
                            contentDescription = null,
                            tint = if (latest.isHighPriority) Rose500 else Indigo600,
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = latest.title,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.titleSmall,
                                color = if (isDark) DarkTextPrimary else Slate900
                            )
                            Text(
                                text = latest.body,
                                style = MaterialTheme.typography.bodySmall,
                                color = if (isDark) DarkTextSecondary else Slate700,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }
        }

        // Quick Navigation Hub
        item {
            Text(
                text = "Career Hub Quick Actions",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = if (isDark) DarkTextPrimary else Slate900
            )
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                QuickActionButton(
                    title = "AI Mentor",
                    icon = Icons.Default.Psychology,
                    onClick = { viewModel.selectedTab.value = 1 },
                    modifier = Modifier.weight(1f),
                    isDark = isDark
                )
                QuickActionButton(
                    title = "ATS Checker",
                    icon = Icons.Default.Description,
                    onClick = { viewModel.selectedTab.value = 2 },
                    modifier = Modifier.weight(1f),
                    isDark = isDark
                )
                QuickActionButton(
                    title = "Coding Hub",
                    icon = Icons.Default.Code,
                    onClick = { viewModel.selectedTab.value = 3 },
                    modifier = Modifier.weight(1f),
                    isDark = isDark
                )
                QuickActionButton(
                    title = "Job Portal",
                    icon = Icons.Default.Work,
                    onClick = { viewModel.selectedTab.value = 4 },
                    modifier = Modifier.weight(1f),
                    isDark = isDark
                )
            }
        }

        // Featured Placement Drives
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Featured Campus Placement Drives",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = if (isDark) DarkTextPrimary else Slate900
                )
                TextButton(onClick = { viewModel.selectedTab.value = 4 }) {
                    Text("View All", color = Indigo600, fontWeight = FontWeight.Bold)
                }
            }

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                contentPadding = PaddingValues(vertical = 6.dp)
            ) {
                items(jobs.take(4)) { job ->
                    JobCardItem(
                        job = job,
                        onApply = { viewModel.applyForJob(job) },
                        isDark = isDark
                    )
                }
            }
        }

        // Application Status Tracker
        if (applications.isNotEmpty()) {
            item {
                Text(
                    text = "My Active Applications (${applications.size})",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = if (isDark) DarkTextPrimary else Slate900
                )
                Spacer(modifier = Modifier.height(10.dp))
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    applications.take(3).forEach { app ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(
                                    BorderStroke(1.dp, if (isDark) GlassBorderDark else GlassBorderLight),
                                    shape = RoundedCornerShape(20.dp)
                                ),
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isDark) GlassSurfaceDark else GlassSurfaceLight
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = app.jobTitle,
                                        fontWeight = FontWeight.Bold,
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = if (isDark) DarkTextPrimary else Slate900
                                    )
                                    Text(
                                        text = "${app.company} • Applied ${app.appliedDate}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = if (isDark) DarkTextSecondary else Slate500
                                    )
                                }
                                SuggestionChip(
                                    onClick = {},
                                    label = { Text(app.status, fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                                    colors = SuggestionChipDefaults.suggestionChipColors(
                                        containerColor = Emerald500.copy(alpha = 0.15f),
                                        labelColor = Emerald500
                                    ),
                                    border = SuggestionChipDefaults.suggestionChipBorder(
                                        borderColor = Emerald500.copy(alpha = 0.3f),
                                        enabled = true
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StatCard(title: String, value: String, modifier: Modifier = Modifier, isDark: Boolean = false) {
    Card(
        modifier = modifier
            .border(
                BorderStroke(1.dp, if (isDark) GlassBorderDark else GlassBorderLight),
                shape = RoundedCornerShape(20.dp)
            ),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isDark) GlassSurfaceDark else GlassSurfaceLight
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = title,
                fontSize = 11.sp,
                color = if (isDark) DarkTextSecondary else Slate500,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = if (isDark) DarkTextPrimary else Slate900
            )
        }
    }
}

@Composable
fun QuickActionButton(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isDark: Boolean = false
) {
    Card(
        modifier = modifier
            .clickable { onClick() }
            .border(
                BorderStroke(1.dp, if (isDark) GlassBorderDark else GlassBorderLight),
                shape = RoundedCornerShape(20.dp)
            ),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isDark) GlassSurfaceDark else GlassSurfaceLight
        ),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = Indigo600,
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = title,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
                color = if (isDark) DarkTextPrimary else Slate900
            )
        }
    }
}

@Composable
fun JobCardItem(job: JobPost, onApply: () -> Unit, isDark: Boolean = false) {
    var applied by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .width(260.dp)
            .height(180.dp)
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = job.company,
                        fontWeight = FontWeight.Bold,
                        color = Indigo600,
                        fontSize = 12.sp
                    )
                    GlassChip(
                        text = job.type,
                        isDark = isDark,
                        accentColor = ElectricCyan500
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = job.title,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = if (isDark) DarkTextPrimary else Slate900,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = job.salaryOrStipend,
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                    color = Emerald500
                )
            }

            GlassButton(
                onClick = {
                    applied = true
                    onApply()
                },
                enabled = !applied,
                gradientColors = if (applied) listOf(Emerald500, Emerald500) else listOf(Indigo600, Indigo500),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
            ) {
                Text(if (applied) "Applied ✅" else "One-Click Apply", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

