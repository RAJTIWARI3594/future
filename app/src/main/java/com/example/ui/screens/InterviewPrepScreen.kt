package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.Emerald500
import com.example.ui.viewmodel.NextHireViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InterviewPrepScreen(viewModel: NextHireViewModel) {
    val profile by viewModel.userProfile.collectAsState()
    var selectedCategory by remember { mutableStateOf("Technical Interview") }
    val categories = listOf("Technical Interview", "HR Interview", "System Design", "Behavioral (STAR)")

    var targetRole by remember { mutableStateOf(profile?.targetRole ?: "Software Engineer (SDE-1)") }
    var currentQuestion by remember {
        mutableStateOf(
            "Explain the difference between Process and Thread, and how deadlocks can be detected and prevented in an operating system."
        )
    }

    var userAnswer by remember {
        mutableStateOf(
            "A process is an independent executing program with its own memory space, whereas a thread is a lightweight unit of execution within a process sharing memory. Deadlocks occur when 4 conditions hold: Mutual Exclusion, Hold and Wait, No Preemption, and Circular Wait. Prevention involves breaking at least one condition."
        )
    }

    val mockResult by viewModel.mockResult.collectAsState()
    val isMockLoading by viewModel.isMockLoading.collectAsState()

    val sampleQuestions = mapOf(
        "Technical Interview" to listOf(
            "Explain the difference between Process and Thread, and deadlock prevention.",
            "How does garbage collection work in Java/JVM?",
            "What is Database Indexing and how do B-Trees optimize SQL queries?",
            "Explain SOLID principles with code examples."
        ),
        "HR Interview" to listOf(
            "Tell me about yourself and why you want to join our company.",
            "What is your biggest weakness and how are you working to overcome it?",
            "Describe a situation where you had a conflict in a team project.",
            "Where do you see yourself in 5 years?"
        ),
        "System Design" to listOf(
            "How would you design URL Shortener (TinyURL) handling 10M daily requests?",
            "Explain horizontal vs vertical scaling and when to use Redis cache.",
            "How do Microservices communicate synchronously vs asynchronously?"
        ),
        "Behavioral (STAR)" to listOf(
            "Give an example of a time when you had to learn a new technology quickly.",
            "Describe a project deadline pressure you faced and how you prioritized tasks."
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Category Chips
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(categories) { cat ->
                FilterChip(
                    selected = selectedCategory == cat,
                    onClick = {
                        selectedCategory = cat
                        sampleQuestions[cat]?.firstOrNull()?.let { currentQuestion = it }
                    },
                    label = { Text(cat, fontWeight = FontWeight.Bold) }
                )
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    text = "AI Mock Interview Simulator 🎙️",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = "Simulates real MNC technical and HR interview rounds with instant AI scoring and feedback.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Question Selection Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(3.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "INTERVIEW QUESTION",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            IconButton(onClick = {
                                val list = sampleQuestions[selectedCategory] ?: emptyList()
                                if (list.isNotEmpty()) {
                                    currentQuestion = list.random()
                                }
                            }) {
                                Icon(Icons.Default.Refresh, contentDescription = "New Question")
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = currentQuestion,
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            lineHeight = 20.sp
                        )
                    }
                }
            }

            // Student Answer Text Box
            item {
                OutlinedTextField(
                    value = userAnswer,
                    onValueChange = { userAnswer = it },
                    label = { Text("Your Response (Text / Speech Transcript)") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp),
                    shape = RoundedCornerShape(16.dp)
                )
            }

            // Evaluate Button
            item {
                Button(
                    onClick = {
                        viewModel.evaluateMockInterview(currentQuestion, userAnswer, targetRole)
                    },
                    enabled = !isMockLoading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    if (isMockLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("AI Interviewer is evaluating...")
                    } else {
                        Icon(Icons.Default.RecordVoiceOver, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Evaluate Answer with Gemini AI", fontWeight = FontWeight.Bold)
                    }
                }
            }

            // Evaluation Result Card
            if (mockResult.isNotBlank()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Stars,
                                    contentDescription = null,
                                    tint = Emerald500,
                                    modifier = Modifier.size(28.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = "Interview Score & Feedback",
                                    style = MaterialTheme.typography.titleLarge.copy(
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }
                            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))
                            Text(
                                text = mockResult,
                                style = MaterialTheme.typography.bodyMedium,
                                lineHeight = 22.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
