package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.data.model.*
import com.example.ui.theme.*
import com.example.ui.viewmodel.NextHireViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResourceHubScreen(viewModel: NextHireViewModel) {
    var selectedResourceTab by remember { mutableStateOf(0) } // 0: Roadmaps, 1: Projects, 2: Notes, 3: PYQs, 4: Quizzes
    val resourceTabs = listOf("Roadmaps", "Projects", "Notes", "PYQs", "Quizzes")
    val isDark = isSystemInDarkTheme()

    val notes by viewModel.allNotes.collectAsState()
    val projects by viewModel.allProjects.collectAsState()
    val pyqs by viewModel.allPyqs.collectAsState()
    val quizzes by viewModel.allQuizzes.collectAsState()

    // Quiz State
    var activeQuizQuestion by remember { mutableStateOf<QuizQuestion?>(null) }
    var selectedOption by remember { mutableStateOf<Int?>(null) }
    var quizSubmitted by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent)
    ) {
        ScrollableTabRow(
            selectedTabIndex = selectedResourceTab,
            containerColor = if (isDark) GlassSurfaceDark else GlassSurfaceLight,
            contentColor = Indigo600,
            edgePadding = 16.dp,
            modifier = Modifier.border(BorderStroke(1.dp, if (isDark) GlassBorderDark else GlassBorderLight))
        ) {
            resourceTabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedResourceTab == index,
                    onClick = { selectedResourceTab = index },
                    text = { Text(title, fontWeight = FontWeight.Bold, color = if (selectedResourceTab == index) Indigo600 else if (isDark) DarkTextSecondary else Slate700) }
                )
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            when (selectedResourceTab) {
                0 -> {
                    // Roadmaps
                    item {
                        Text(
                            text = "Degree Stream Career Roadmaps 🗺️",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = if (isDark) DarkTextPrimary else Slate900
                        )
                    }

                    val roadmaps = listOf(
                        Triple("Full-Stack SDE Roadmap", "B.Tech / BCA", "1. Java/Python -> 2. DSA & Systems -> 3. React/Compose -> 4. Microservices & Cloud"),
                        Triple("Data Analyst & ML Roadmap", "BCA / B.Tech", "1. Python & SQL -> 2. Pandas & Statistics -> 3. Tableau/PowerBI -> 4. Machine Learning"),
                        Triple("Product & Business Management", "MBA / BBA", "1. Agile & Scrum -> 2. Financial Modeling -> 3. Product Analytics -> 4. GTM Strategy")
                    )

                    items(roadmaps) { (title, degree, path) ->
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
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = title,
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                        color = if (isDark) DarkTextPrimary else Slate900
                                    )
                                    SuggestionChip(
                                        onClick = {},
                                        label = { Text(degree, fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                                        colors = SuggestionChipDefaults.suggestionChipColors(
                                            containerColor = Indigo600.copy(alpha = 0.15f),
                                            labelColor = Indigo600
                                        )
                                    )
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = path,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = if (isDark) DarkTextSecondary else Slate700
                                )
                            }
                        }
                    }
                }

                1 -> {
                    // Projects
                    item {
                        Text(
                            text = "Industry-Grade Resume Projects 🚀",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = if (isDark) DarkTextPrimary else Slate900
                        )
                    }

                    items(projects) { project ->
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
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = project.title,
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                        color = if (isDark) DarkTextPrimary else Slate900,
                                        modifier = Modifier.weight(1f)
                                    )
                                    SuggestionChip(
                                        onClick = {},
                                        label = { Text(project.difficulty, fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                                        colors = SuggestionChipDefaults.suggestionChipColors(
                                            containerColor = Emerald500.copy(alpha = 0.15f),
                                            labelColor = Emerald500
                                        )
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Tech Stack: ${project.techStack}",
                                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                                    color = Indigo600
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = project.description,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = if (isDark) DarkTextSecondary else Slate700
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Architecture Steps: ${project.architectureSteps}",
                                    fontSize = 11.sp,
                                    color = Emerald500
                                )
                            }
                        }
                    }
                }

                2 -> {
                    // Notes
                    item {
                        Text(
                            text = "Subject Study Notes & Handouts 📚",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = if (isDark) DarkTextPrimary else Slate900
                        )
                    }

                    items(notes) { note ->
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
                                Text(
                                    text = note.title,
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = if (isDark) DarkTextPrimary else Slate900
                                )
                                Text(
                                    text = "${note.subject} • ${note.degree}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Indigo600
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = note.description,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = if (isDark) DarkTextSecondary else Slate700
                                )
                            }
                        }
                    }
                }

                3 -> {
                    // PYQs
                    item {
                        Text(
                            text = "Previous Year Placement Papers (PYQs) 📝",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = if (isDark) DarkTextPrimary else Slate900
                        )
                    }

                    items(pyqs) { pyq ->
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
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = pyq.companyName,
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = Indigo600
                                        )
                                    )
                                    Text(
                                        text = pyq.year,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = if (isDark) DarkTextSecondary else Slate500
                                    )
                                }
                                Text(
                                    text = "Role: ${pyq.role} • ${pyq.questionsCount} Questions",
                                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                                    color = if (isDark) DarkTextPrimary else Slate900
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = pyq.contentSummary,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = if (isDark) DarkTextSecondary else Slate700
                                )
                            }
                        }
                    }
                }

                4 -> {
                    // Quizzes
                    item {
                        Text(
                            text = "Interactive Practice Quizzes ⚡",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = if (isDark) DarkTextPrimary else Slate900
                        )
                    }

                    items(quizzes) { quiz ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(
                                    BorderStroke(1.5.dp, if (isDark) GlassBorderDark else GlassBorderLight),
                                    shape = RoundedCornerShape(24.dp)
                                )
                                .clickable {
                                    activeQuizQuestion = quiz
                                    selectedOption = null
                                    quizSubmitted = false
                                },
                            shape = RoundedCornerShape(24.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isDark) GlassSurfaceDark else GlassSurfaceLight
                            ),
                            elevation = CardDefaults.cardElevation(3.dp)
                        ) {
                            Column(modifier = Modifier.padding(18.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = quiz.category,
                                        fontWeight = FontWeight.Bold,
                                        color = Indigo600,
                                        fontSize = 12.sp
                                    )
                                    Text(
                                        text = "Tap to Solve",
                                        fontSize = 11.sp,
                                        color = Emerald500,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = quiz.questionText,
                                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                                    color = if (isDark) DarkTextPrimary else Slate900
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // Active Quiz Modal Dialog
    activeQuizQuestion?.let { quiz ->
        AlertDialog(
            onDismissRequest = { activeQuizQuestion = null },
            containerColor = if (isDark) GlassSurfaceDark else GlassSurfaceLight,
            title = { Text("${quiz.category} Quiz Challenge", color = if (isDark) DarkTextPrimary else Slate900) },
            text = {
                Column {
                    Text(
                        text = quiz.questionText,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = if (isDark) DarkTextPrimary else Slate900
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    val options = listOf(quiz.optionA, quiz.optionB, quiz.optionC, quiz.optionD)
                    options.forEachIndexed { idx, opt ->
                        val isSelected = selectedOption == idx
                        val isCorrect = idx == quiz.correctOptionIndex

                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clickable {
                                    if (!quizSubmitted) selectedOption = idx
                                },
                            shape = RoundedCornerShape(12.dp),
                            color = when {
                                quizSubmitted && isCorrect -> Emerald500.copy(alpha = 0.2f)
                                quizSubmitted && isSelected && !isCorrect -> MaterialTheme.colorScheme.errorContainer
                                isSelected -> Indigo600.copy(alpha = 0.2f)
                                else -> if (isDark) GlassSurfaceDarkLow else GlassSurfaceLightLow
                            }
                        ) {
                            Text(
                                text = "${('A' + idx)}. $opt",
                                modifier = Modifier.padding(12.dp),
                                fontSize = 14.sp,
                                color = if (isDark) DarkTextPrimary else Slate900
                            )
                        }
                    }

                    if (quizSubmitted) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Explanation: ${quiz.explanation}",
                            fontSize = 12.sp,
                            color = Indigo600
                        )
                    }
                }
            },
            confirmButton = {
                if (!quizSubmitted) {
                    GlassButton(onClick = { quizSubmitted = true }) {
                        Text("Submit Answer", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                } else {
                    GlassButton(onClick = { activeQuizQuestion = null }) {
                        Text("Close", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        )
    }
}

