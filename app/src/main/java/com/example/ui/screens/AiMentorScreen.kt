package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
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
fun AiMentorScreen(viewModel: NextHireViewModel) {
    val messages by viewModel.chatMessages.collectAsState()
    val isChatLoading by viewModel.isChatLoading.collectAsState()
    val profile by viewModel.userProfile.collectAsState()
    val isDark = isSystemInDarkTheme()

    var inputText by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    val promptSuggestions = listOf(
        "Generate 30-Day ${profile?.degree ?: "B.Tech"} Placement Roadmap 🚀",
        "How to crack Amazon & Google SDE-1 Interviews?",
        "Top 10 Java & DSA Questions for TCS Digital",
        "How to transition from BCA/BBA to High-Paying Tech Jobs?"
    )

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent)
    ) {
        // Mentor Header Bar with Frosted Glass Overlay
        Surface(
            color = if (isDark) GlassSurfaceDark else GlassSurfaceLight,
            modifier = Modifier
                .fillMaxWidth()
                .border(BorderStroke(1.dp, if (isDark) GlassBorderSubtleDark else GlassBorderSubtleLight))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(Indigo600),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Psychology,
                        contentDescription = "AI Mentor",
                        tint = Color.White
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "NextHire AI Career Mentor",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = if (isDark) DarkTextPrimary else Slate900
                    )
                    Text(
                        text = "Customized for ${profile?.degree ?: "College"} Students • Gemini AI",
                        style = MaterialTheme.typography.bodySmall,
                        color = if (isDark) DarkTextSecondary else Slate500
                    )
                }
            }
        }

        // Quick Prompt Suggestions
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(promptSuggestions) { prompt ->
                SuggestionChip(
                    onClick = {
                        inputText = prompt
                        viewModel.sendChatMessage(prompt)
                        inputText = ""
                    },
                    label = { Text(prompt, fontSize = 12.sp, fontWeight = FontWeight.SemiBold) },
                    border = SuggestionChipDefaults.suggestionChipBorder(
                        borderColor = if (isDark) GlassBorderDark else GlassBorderLight,
                        enabled = true
                    ),
                    colors = SuggestionChipDefaults.suggestionChipColors(
                        containerColor = if (isDark) GlassSurfaceDarkLow else GlassSurfaceLightLow,
                        labelColor = if (isDark) DarkTextPrimary else Slate900
                    )
                )
            }
        }

        // Messages List
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(messages) { msg ->
                val isUser = msg.sender == "USER"
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = if (isUser) Alignment.CenterEnd else Alignment.CenterStart
                ) {
                    Surface(
                        shape = RoundedCornerShape(
                            topStart = 20.dp,
                            topEnd = 20.dp,
                            bottomStart = if (isUser) 20.dp else 4.dp,
                            bottomEnd = if (isUser) 4.dp else 20.dp
                        ),
                        color = if (isUser) Indigo600 else if (isDark) GlassSurfaceDark else GlassSurfaceLight,
                        border = BorderStroke(1.dp, if (isUser) Indigo500 else if (isDark) GlassBorderDark else GlassBorderLight),
                        modifier = Modifier.widthIn(max = 300.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(
                                text = if (isUser) "You" else "NextHire AI Mentor 🤖",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = if (isUser) Color.White.copy(alpha = 0.85f) else Indigo600
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = msg.messageText,
                                style = MaterialTheme.typography.bodyMedium,
                                color = if (isUser) Color.White else if (isDark) DarkTextPrimary else Slate900
                            )
                        }
                    }
                }
            }

            if (isChatLoading) {
                item {
                    Row(
                        modifier = Modifier.padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = Indigo600,
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "NextHire AI is analyzing your career question...",
                            style = MaterialTheme.typography.bodySmall,
                            color = if (isDark) DarkTextSecondary else Slate500
                        )
                    }
                }
            }
        }

        // Input Field Container
        Surface(
            color = if (isDark) GlassSurfaceDark else GlassSurfaceLight,
            modifier = Modifier
                .fillMaxWidth()
                .border(BorderStroke(1.dp, if (isDark) GlassBorderDark else GlassBorderLight))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    placeholder = { Text("Ask your AI Career Mentor...") },
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp),
                    shape = RoundedCornerShape(24.dp),
                    maxLines = 3
                )

                IconButton(
                    onClick = {
                        if (inputText.isNotBlank()) {
                            val msg = inputText
                            inputText = ""
                            viewModel.sendChatMessage(msg)
                        }
                    },
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(Indigo600)
                ) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "Send",
                        tint = Color.White
                    )
                }
            }
        }
    }
}

