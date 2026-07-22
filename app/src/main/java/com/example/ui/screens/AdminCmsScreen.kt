package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
fun AdminCmsScreen(viewModel: NextHireViewModel) {
    val jobs by viewModel.allJobs.collectAsState()
    val notes by viewModel.allNotes.collectAsState()
    val projects by viewModel.allProjects.collectAsState()
    val quizzes by viewModel.allQuizzes.collectAsState()
    val pyqs by viewModel.allPyqs.collectAsState()
    val announcements by viewModel.allAnnouncements.collectAsState()
    val isDark = isSystemInDarkTheme()

    var selectedCmsTab by remember { mutableStateOf(0) } // 0: Jobs, 1: Notes, 2: Projects, 3: Quizzes, 4: Announcements, 5: Analytics
    val cmsTabs = listOf("Jobs & Internships", "Notes", "Projects", "Quizzes", "Announcements", "Analytics")

    // Dialog state for adding Job
    var showAddJobDialog by remember { mutableStateOf(false) }
    var jobTitle by remember { mutableStateOf("") }
    var company by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("Bangalore / Hybrid") }
    var type by remember { mutableStateOf("Full-Time") }
    var salary by remember { mutableStateOf("₹15 - ₹20 LPA") }
    var degrees by remember { mutableStateOf("B.Tech, BCA, MBA") }
    var jobDesc by remember { mutableStateOf("") }

    // Dialog state for adding Announcement
    var showAddAnnoDialog by remember { mutableStateOf(false) }
    var annoTitle by remember { mutableStateOf("") }
    var annoBody by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent)
    ) {
        // Admin Header
        Surface(
            color = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.8f),
            tonalElevation = 4.dp,
            modifier = Modifier.border(BorderStroke(1.dp, MaterialTheme.colorScheme.error.copy(alpha = 0.5f)))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.AdminPanelSettings,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Super Admin CMS Panel 🔐",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                    )
                    Text(
                        text = "Manage jobs, notes, quizzes & announcements live for all students",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onErrorContainer.copy(alpha = 0.8f)
                    )
                }
            }
        }

        ScrollableTabRow(
            selectedTabIndex = selectedCmsTab,
            containerColor = if (isDark) GlassSurfaceDark else GlassSurfaceLight,
            contentColor = Indigo600,
            edgePadding = 16.dp,
            modifier = Modifier.border(BorderStroke(1.dp, if (isDark) GlassBorderDark else GlassBorderLight))
        ) {
            cmsTabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedCmsTab == index,
                    onClick = { selectedCmsTab = index },
                    text = { Text(title, fontWeight = FontWeight.Bold, color = if (selectedCmsTab == index) Indigo600 else if (isDark) DarkTextSecondary else Slate700) }
                )
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            when (selectedCmsTab) {
                0 -> {
                    // Jobs Management
                    item {
                        GlassButton(
                            onClick = { showAddJobDialog = true },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, tint = Color.White)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Post New Job / Internship", fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }

                    items(jobs) { job ->
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
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "${job.company} • ${job.type}",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Indigo600
                                    )
                                    Text(
                                        text = job.title,
                                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                        color = if (isDark) DarkTextPrimary else Slate900
                                    )
                                    Text(
                                        text = job.salaryOrStipend,
                                        fontSize = 12.sp,
                                        color = Emerald500
                                    )
                                }
                                IconButton(onClick = { viewModel.deleteJob(job.id) }) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Delete",
                                        tint = MaterialTheme.colorScheme.error
                                    )
                                }
                            }
                        }
                    }
                }

                1 -> {
                    // Notes
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
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(note.title, fontWeight = FontWeight.Bold, color = if (isDark) DarkTextPrimary else Slate900)
                                    Text("${note.subject} • ${note.degree}", fontSize = 12.sp, color = if (isDark) DarkTextSecondary else Slate500)
                                }
                                IconButton(onClick = { viewModel.deleteNote(note.id) }) {
                                    Icon(Icons.Default.Delete, contentDescription = null, tint = MaterialTheme.colorScheme.error)
                                }
                            }
                        }
                    }
                }

                2 -> {
                    // Projects
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
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(project.title, fontWeight = FontWeight.Bold, color = if (isDark) DarkTextPrimary else Slate900)
                                    Text(project.techStack, fontSize = 12.sp, color = Indigo600)
                                }
                                IconButton(onClick = { viewModel.deleteProject(project.id) }) {
                                    Icon(Icons.Default.Delete, contentDescription = null, tint = MaterialTheme.colorScheme.error)
                                }
                            }
                        }
                    }
                }

                3 -> {
                    // Quizzes
                    items(quizzes) { quiz ->
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
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(quiz.category, fontWeight = FontWeight.Bold, color = Indigo600)
                                    Text(quiz.questionText, fontSize = 13.sp, color = if (isDark) DarkTextPrimary else Slate900)
                                }
                                IconButton(onClick = { viewModel.deleteQuiz(quiz.id) }) {
                                    Icon(Icons.Default.Delete, contentDescription = null, tint = MaterialTheme.colorScheme.error)
                                }
                            }
                        }
                    }
                }

                4 -> {
                    // Announcements
                    item {
                        GlassButton(
                            onClick = { showAddAnnoDialog = true },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                        ) {
                            Icon(Icons.Default.Campaign, contentDescription = null, tint = Color.White)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Send Campus Notification Announcement", fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }

                    items(announcements) { anno ->
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
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(anno.title, fontWeight = FontWeight.Bold, color = if (isDark) DarkTextPrimary else Slate900)
                                    Text(anno.body, fontSize = 12.sp, color = if (isDark) DarkTextSecondary else Slate700)
                                }
                                IconButton(onClick = { viewModel.deleteAnnouncement(anno.id) }) {
                                    Icon(Icons.Default.Delete, contentDescription = null, tint = MaterialTheme.colorScheme.error)
                                }
                            }
                        }
                    }
                }

                5 -> {
                    // CMS Analytics
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
                                Text(
                                    text = "Platform CMS Overview & Stats 📈",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = if (isDark) DarkTextPrimary else Slate900
                                )
                                HorizontalDivider(
                                    modifier = Modifier.padding(vertical = 12.dp),
                                    color = if (isDark) GlassBorderSubtleDark else GlassBorderSubtleLight
                                )
                                Text("• Registered Students: 12,480 (BCA, B.Tech, BBA, MBA)", color = if (isDark) DarkTextPrimary else Slate900)
                                Text("• Active Job & Internship Drives: ${jobs.size}", color = if (isDark) DarkTextPrimary else Slate900)
                                Text("• Total Practice Quizzes Solved: 48,290", color = if (isDark) DarkTextPrimary else Slate900)
                                Text("• Gemini AI ATS Resumes Scanned: 15,920", color = if (isDark) DarkTextPrimary else Slate900)
                                Text("• Active Placements Offered: 342 Students", color = if (isDark) DarkTextPrimary else Slate900)
                            }
                        }
                    }
                }
            }
        }
    }

    // Add Job Dialog
    if (showAddJobDialog) {
        AlertDialog(
            onDismissRequest = { showAddJobDialog = false },
            containerColor = if (isDark) GlassSurfaceDark else GlassSurfaceLight,
            title = { Text("Post New Opening", color = if (isDark) DarkTextPrimary else Slate900) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = jobTitle,
                        onValueChange = { jobTitle = it },
                        label = { Text("Job Title (e.g. SDE Intern)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = company,
                        onValueChange = { company = it },
                        label = { Text("Company Name") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = salary,
                        onValueChange = { salary = it },
                        label = { Text("Salary / Stipend") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = degrees,
                        onValueChange = { degrees = it },
                        label = { Text("Eligible Degrees (e.g. B.Tech, BCA, MBA)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = jobDesc,
                        onValueChange = { jobDesc = it },
                        label = { Text("Short Description") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                GlassButton(onClick = {
                    if (jobTitle.isNotBlank() && company.isNotBlank()) {
                        viewModel.addOrUpdateJob(
                            JobPost(
                                title = jobTitle,
                                company = company,
                                location = location,
                                type = type,
                                salaryOrStipend = salary,
                                eligibleDegrees = degrees,
                                description = if (jobDesc.isBlank()) "Excellent opportunity for graduating students." else jobDesc,
                                requirements = "Strong problem solving and core domain skills.",
                                applyUrl = "https://careers.google.com"
                            )
                        )
                        showAddJobDialog = false
                    }
                }) {
                    Text("Publish Job", color = Color.White, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddJobDialog = false }) {
                    Text("Cancel", color = Indigo600)
                }
            }
        )
    }

    // Add Announcement Dialog
    if (showAddAnnoDialog) {
        AlertDialog(
            onDismissRequest = { showAddAnnoDialog = false },
            containerColor = if (isDark) GlassSurfaceDark else GlassSurfaceLight,
            title = { Text("Send Announcement", color = if (isDark) DarkTextPrimary else Slate900) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = annoTitle,
                        onValueChange = { annoTitle = it },
                        label = { Text("Announcement Title") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = annoBody,
                        onValueChange = { annoBody = it },
                        label = { Text("Message Body") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                GlassButton(onClick = {
                    if (annoTitle.isNotBlank()) {
                        viewModel.addAnnouncement(annoTitle, annoBody, "All Students", true)
                        showAddAnnoDialog = false
                    }
                }) {
                    Text("Publish Announcement", color = Color.White, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddAnnoDialog = false }) {
                    Text("Cancel", color = Indigo600)
                }
            }
        )
    }
}

