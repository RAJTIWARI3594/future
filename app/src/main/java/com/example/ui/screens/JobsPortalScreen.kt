package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
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
import com.example.data.model.JobPost
import com.example.ui.theme.*
import com.example.ui.viewmodel.NextHireViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobsPortalScreen(viewModel: NextHireViewModel) {
    val jobs by viewModel.allJobs.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    var selectedDegreeFilter by remember { mutableStateOf("All") }
    var selectedTypeFilter by remember { mutableStateOf("All") }
    val isDark = isSystemInDarkTheme()

    val degreeFilters = listOf("All", "B.Tech", "BCA", "BBA", "MBA")
    val typeFilters = listOf("All", "Full-Time", "Internship", "Remote")

    var selectedJobForDetail by remember { mutableStateOf<JobPost?>(null) }

    val filteredJobs = jobs.filter { job ->
        val matchesSearch = job.title.contains(searchQuery, ignoreCase = true) ||
                job.company.contains(searchQuery, ignoreCase = true) ||
                job.description.contains(searchQuery, ignoreCase = true)
        val matchesDegree = selectedDegreeFilter == "All" || job.eligibleDegrees.contains(selectedDegreeFilter, ignoreCase = true)
        val matchesType = selectedTypeFilter == "All" || job.type.equals(selectedTypeFilter, ignoreCase = true)
        matchesSearch && matchesDegree && matchesType
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent)
    ) {
        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Search Jobs, Internships, Companies...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Indigo600) },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { searchQuery = "" }) {
                        Icon(Icons.Default.Clear, contentDescription = null)
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            shape = RoundedCornerShape(20.dp),
            singleLine = true
        )

        // Filters Row
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                Text(
                    text = "Degree:",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isDark) DarkTextSecondary else Slate700,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
            items(degreeFilters) { deg ->
                FilterChip(
                    selected = selectedDegreeFilter == deg,
                    onClick = { selectedDegreeFilter = deg },
                    label = { Text(deg, fontSize = 12.sp, fontWeight = FontWeight.SemiBold) },
                    border = FilterChipDefaults.filterChipBorder(
                        borderColor = if (selectedDegreeFilter == deg) Indigo600 else if (isDark) GlassBorderDark else GlassBorderLight,
                        enabled = true,
                        selected = selectedDegreeFilter == deg
                    ),
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Indigo600.copy(alpha = 0.2f),
                        containerColor = if (isDark) GlassSurfaceDarkLow else GlassSurfaceLightLow,
                        labelColor = if (isDark) DarkTextPrimary else Slate900
                    )
                )
            }
        }

        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                Text(
                    text = "Type:",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isDark) DarkTextSecondary else Slate700,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
            items(typeFilters) { type ->
                FilterChip(
                    selected = selectedTypeFilter == type,
                    onClick = { selectedTypeFilter = type },
                    label = { Text(type, fontSize = 12.sp, fontWeight = FontWeight.SemiBold) },
                    border = FilterChipDefaults.filterChipBorder(
                        borderColor = if (selectedTypeFilter == type) Indigo600 else if (isDark) GlassBorderDark else GlassBorderLight,
                        enabled = true,
                        selected = selectedTypeFilter == type
                    ),
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Indigo600.copy(alpha = 0.2f),
                        containerColor = if (isDark) GlassSurfaceDarkLow else GlassSurfaceLightLow,
                        labelColor = if (isDark) DarkTextPrimary else Slate900
                    )
                )
            }
        }

        // Jobs List
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Text(
                    text = "Available Openings (${filteredJobs.size})",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = if (isDark) DarkTextPrimary else Slate900
                )
            }

            items(filteredJobs) { job ->
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
                                text = job.company,
                                fontWeight = FontWeight.Bold,
                                color = Indigo600,
                                fontSize = 13.sp
                            )
                            Text(
                                text = job.postedDate,
                                fontSize = 11.sp,
                                color = if (isDark) DarkTextSecondary else Slate500
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = job.title,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = if (isDark) DarkTextPrimary else Slate900
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = job.salaryOrStipend,
                                fontWeight = FontWeight.Bold,
                                color = Emerald500,
                                fontSize = 14.sp
                            )
                            Text(
                                text = "• ${job.location}",
                                fontSize = 12.sp,
                                color = if (isDark) DarkTextSecondary else Slate500
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Eligible: ${job.eligibleDegrees}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Indigo600
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            OutlinedButton(
                                onClick = { selectedJobForDetail = job },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(42.dp),
                                shape = RoundedCornerShape(12.dp),
                                border = BorderStroke(1.dp, if (isDark) GlassBorderDark else GlassBorderLight)
                            ) {
                                Text("Job Details", color = if (isDark) DarkTextPrimary else Slate900)
                            }

                            GlassButton(
                                onClick = {
                                    viewModel.applyForJob(job)
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(42.dp)
                            ) {
                                Text("One-Click Apply", fontWeight = FontWeight.Bold, color = Color.White)
                            }
                        }
                    }
                }
            }
        }
    }

    // Job Detail Dialog
    selectedJobForDetail?.let { job ->
        AlertDialog(
            onDismissRequest = { selectedJobForDetail = null },
            containerColor = if (isDark) GlassSurfaceDark else GlassSurfaceLight,
            title = { Text(job.title, color = if (isDark) DarkTextPrimary else Slate900) },
            text = {
                Column {
                    Text(
                        text = "${job.company} • ${job.location}",
                        fontWeight = FontWeight.Bold,
                        color = Indigo600
                    )
                    Text(
                        text = "Salary / Stipend: ${job.salaryOrStipend}",
                        color = Emerald500,
                        fontWeight = FontWeight.Bold
                    )
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 10.dp),
                        color = if (isDark) GlassBorderSubtleDark else GlassBorderSubtleLight
                    )
                    Text(
                        text = "JOB DESCRIPTION:",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = if (isDark) DarkTextPrimary else Slate900
                    )
                    Text(
                        text = job.description,
                        fontSize = 13.sp,
                        color = if (isDark) DarkTextSecondary else Slate700
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "REQUIREMENTS:",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = if (isDark) DarkTextPrimary else Slate900
                    )
                    Text(
                        text = job.requirements,
                        fontSize = 13.sp,
                        color = if (isDark) DarkTextSecondary else Slate700
                    )
                }
            },
            confirmButton = {
                GlassButton(onClick = {
                    viewModel.applyForJob(job)
                    selectedJobForDetail = null
                }) {
                    Text("Apply Now", color = Color.White, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { selectedJobForDetail = null }) {
                    Text("Close", color = Indigo600)
                }
            }
        )
    }
}

