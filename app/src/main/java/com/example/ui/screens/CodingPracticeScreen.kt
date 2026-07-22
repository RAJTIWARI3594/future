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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import com.example.ui.viewmodel.NextHireViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CodingPracticeScreen(viewModel: NextHireViewModel) {
    var selectedLang by remember { mutableStateOf("Java") }
    val languages = listOf("Java", "Python", "C", "C++", "JavaScript", "SQL", "DSA", "Aptitude", "Reasoning", "English")
    val isDark = isSystemInDarkTheme()

    var codeSnippet by remember {
        mutableStateOf(
            """
            // Two Sum Problem in Java
            class Solution {
                public int[] twoSum(int[] nums, int target) {
                    Map<Integer, Integer> map = new HashMap<>();
                    for (int i = 0; i < nums.length; i++) {
                        int complement = target - nums[i];
                        if (map.containsKey(complement)) {
                            return new int[] { map.get(complement), i };
                        }
                        map.put(nums[i], i);
                    }
                    return new int[] {};
                }
            }
            """.trimIndent()
        )
    }

    var codeQuery by remember { mutableStateOf("Explain time & space complexity and check for potential edge cases.") }

    val codeResult by viewModel.codeMentorResult.collectAsState()
    val isCodeLoading by viewModel.isCodeLoading.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent)
    ) {
        // Topic / Language Selector
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(languages) { lang ->
                FilterChip(
                    selected = selectedLang == lang,
                    onClick = {
                        selectedLang = lang
                        codeSnippet = when (lang) {
                            "Python" -> "def two_sum(nums, target):\n    seen = {}\n    for i, num in enumerate(nums):\n        if target - num in seen:\n            return [seen[target - num], i]\n        seen[num] = i\n    return []"
                            "SQL" -> "SELECT e.employee_id, e.name, d.department_name, MAX(e.salary)\nFROM employees e\nJOIN departments d ON e.dept_id = d.id\nGROUP BY d.department_name;"
                            else -> "// Write your $lang solution or question here"
                        }
                    },
                    label = { Text(lang, fontWeight = FontWeight.Bold) },
                    border = FilterChipDefaults.filterChipBorder(
                        borderColor = if (selectedLang == lang) Indigo600 else if (isDark) GlassBorderDark else GlassBorderLight,
                        enabled = true,
                        selected = selectedLang == lang
                    ),
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Indigo600.copy(alpha = 0.2f),
                        containerColor = if (isDark) GlassSurfaceDarkLow else GlassSurfaceLightLow,
                        labelColor = if (isDark) DarkTextPrimary else Slate900
                    )
                )
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "$selectedLang Code Playground & AI Mentor",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = if (isDark) DarkTextPrimary else Slate900
                    )
                    SuggestionChip(
                        onClick = {},
                        label = { Text("LeetCode / MNC Pattern", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                        border = SuggestionChipDefaults.suggestionChipBorder(
                            borderColor = Emerald500.copy(alpha = 0.5f),
                            enabled = true
                        ),
                        colors = SuggestionChipDefaults.suggestionChipColors(
                            containerColor = Emerald500.copy(alpha = 0.15f),
                            labelColor = Emerald500
                        )
                    )
                }
            }

            // Code Editor Box
            item {
                OutlinedTextField(
                    value = codeSnippet,
                    onValueChange = { codeSnippet = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .border(
                            BorderStroke(1.dp, if (isDark) GlassBorderDark else GlassBorderLight),
                            shape = RoundedCornerShape(20.dp)
                        ),
                    shape = RoundedCornerShape(20.dp),
                    textStyle = MaterialTheme.typography.bodyMedium.copy(
                        fontFamily = FontFamily.Monospace,
                        fontSize = 13.sp,
                        color = if (isDark) DarkTextPrimary else Slate900
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = if (isDark) GlassSurfaceDarkLow else GlassSurfaceLightLow,
                        unfocusedContainerColor = if (isDark) GlassSurfaceDarkLow else GlassSurfaceLightLow
                    )
                )
            }

            // Query Input
            item {
                OutlinedTextField(
                    value = codeQuery,
                    onValueChange = { codeQuery = it },
                    label = { Text("Ask AI Mentor about this code") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp)
                )
            }

            // AI Submit Button
            item {
                GlassButton(
                    onClick = { viewModel.runCodeMentor(selectedLang, codeSnippet, codeQuery) },
                    enabled = !isCodeLoading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    if (isCodeLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Analyzing Code with Gemini AI...", fontWeight = FontWeight.Bold)
                    } else {
                        Icon(Icons.Default.Terminal, contentDescription = null, tint = Color.White)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Analyze & Debug with Gemini AI", fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }

            // AI Result Card
            if (codeResult.isNotBlank()) {
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
                                    imageVector = Icons.Default.Lightbulb,
                                    contentDescription = null,
                                    tint = Indigo600,
                                    modifier = Modifier.size(26.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = "Gemini Code Insights",
                                    style = MaterialTheme.typography.titleMedium.copy(
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
                                text = codeResult,
                                style = MaterialTheme.typography.bodyMedium,
                                lineHeight = 22.sp,
                                color = if (isDark) DarkTextPrimary else Slate900
                            )
                        }
                    }
                }
            }
        }
    }
}

