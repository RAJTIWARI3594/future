package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import com.example.ui.viewmodel.NextHireViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommunityScreen(viewModel: NextHireViewModel) {
    val posts by viewModel.allCommunityPosts.collectAsState()
    val profile by viewModel.userProfile.collectAsState()
    val isDark = isSystemInDarkTheme()

    var showNewPostDialog by remember { mutableStateOf(false) }
    var newPostContent by remember { mutableStateOf("") }

    var selectedCommunityTab by remember { mutableStateOf(0) } // 0: Forum, 1: Leaderboard & Certificates

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent)
    ) {
        TabRow(
            selectedTabIndex = selectedCommunityTab,
            containerColor = if (isDark) GlassSurfaceDark else GlassSurfaceLight,
            contentColor = Indigo600,
            modifier = Modifier.border(BorderStroke(1.dp, if (isDark) GlassBorderDark else GlassBorderLight))
        ) {
            Tab(
                selected = selectedCommunityTab == 0,
                onClick = { selectedCommunityTab = 0 },
                text = { Text("Student Community", fontWeight = FontWeight.Bold, color = if (selectedCommunityTab == 0) Indigo600 else if (isDark) DarkTextSecondary else Slate700) },
                icon = { Icon(Icons.Default.Forum, contentDescription = null, tint = if (selectedCommunityTab == 0) Indigo600 else if (isDark) DarkTextSecondary else Slate700) }
            )
            Tab(
                selected = selectedCommunityTab == 1,
                onClick = { selectedCommunityTab = 1 },
                text = { Text("Leaderboard & Badges", fontWeight = FontWeight.Bold, color = if (selectedCommunityTab == 1) Indigo600 else if (isDark) DarkTextSecondary else Slate700) },
                icon = { Icon(Icons.Default.EmojiEvents, contentDescription = null, tint = if (selectedCommunityTab == 1) Indigo600 else if (isDark) DarkTextSecondary else Slate700) }
            )
        }

        if (selectedCommunityTab == 0) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    GlassButton(
                        onClick = { showNewPostDialog = true },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                    ) {
                        Icon(Icons.Default.AddComment, contentDescription = null, tint = Color.White)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Post Career Advice or Question", fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }

                items(posts) { post ->
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
                        elevation = CardDefaults.cardElevation(3.dp)
                    ) {
                        Column(modifier = Modifier.padding(18.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(Indigo600),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = post.authorName.take(1),
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = post.authorName,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = if (isDark) DarkTextPrimary else Slate900
                                    )
                                    Text(
                                        text = "${post.authorDegree} • ${post.timestamp}",
                                        fontSize = 11.sp,
                                        color = if (isDark) DarkTextSecondary else Slate500
                                    )
                                }
                                if (post.isPinned) {
                                    Spacer(modifier = Modifier.weight(1f))
                                    Icon(
                                        imageVector = Icons.Default.PushPin,
                                        contentDescription = "Pinned",
                                        tint = Emerald500,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = post.content,
                                style = MaterialTheme.typography.bodyMedium,
                                lineHeight = 22.sp,
                                color = if (isDark) DarkTextPrimary else Slate900
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                TextButton(onClick = { viewModel.likeCommunityPost(post.id) }) {
                                    Icon(Icons.Default.ThumbUp, contentDescription = null, tint = Indigo600, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("${post.likesCount} Likes", color = Indigo600, fontWeight = FontWeight.Bold)
                                }
                                TextButton(onClick = {}) {
                                    Icon(Icons.Default.Comment, contentDescription = null, tint = Indigo600, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("${post.commentsCount} Comments", color = Indigo600, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        } else {
            // Leaderboard
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Text(
                        text = "National College Career Leaderboard 🏆",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = if (isDark) DarkTextPrimary else Slate900
                    )
                }

                val leaderboard = listOf(
                    Triple("1. Ananya Roy", "B.Tech CSE '25 • 2850 pts", "🔥 28 Days"),
                    Triple("2. Rahul Sharma (You)", "${profile?.degree} '26 • ${profile?.points} pts", "🔥 ${profile?.streakDays} Days"),
                    Triple("3. Priya Verma", "BCA '26 • 1620 pts", "🔥 10 Days"),
                    Triple("4. Vikram Singh", "MBA '25 • 1400 pts", "🔥 8 Days")
                )

                items(leaderboard) { (name, info, streak) ->
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
                                Text(name, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = if (isDark) DarkTextPrimary else Slate900)
                                Text(info, fontSize = 12.sp, color = if (isDark) DarkTextSecondary else Slate500)
                            }
                            Text(streak, fontWeight = FontWeight.Bold, color = Emerald500)
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "My NextHire AI Verified Certificates 📜",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = if (isDark) DarkTextPrimary else Slate900
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
                        )
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Verified, contentDescription = null, tint = Indigo600)
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = "Verified Career Readiness Badge",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = if (isDark) DarkTextPrimary else Slate900
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Issued to ${profile?.name} for completing 10+ Coding Practice Sessions and achieving 85%+ ATS Resume Compatibility.",
                                fontSize = 12.sp,
                                color = if (isDark) DarkTextSecondary else Slate700
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            GlassButton(
                                onClick = {},
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(Icons.Default.Download, contentDescription = null, tint = Color.White)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Download Official Certificate PDF", color = Color.White, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }

    if (showNewPostDialog) {
        AlertDialog(
            onDismissRequest = { showNewPostDialog = false },
            containerColor = if (isDark) GlassSurfaceDark else GlassSurfaceLight,
            title = { Text("Create Community Post", color = if (isDark) DarkTextPrimary else Slate900) },
            text = {
                OutlinedTextField(
                    value = newPostContent,
                    onValueChange = { newPostContent = it },
                    placeholder = { Text("Share placement tips, interview experiences, or career questions...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                )
            },
            confirmButton = {
                GlassButton(onClick = {
                    viewModel.addCommunityPost(newPostContent)
                    newPostContent = ""
                    showNewPostDialog = false
                }) {
                    Text("Publish Post", color = Color.White, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showNewPostDialog = false }) {
                    Text("Cancel", color = Indigo600)
                }
            }
        )
    }
}

