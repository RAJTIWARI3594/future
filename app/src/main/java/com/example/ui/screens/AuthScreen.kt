package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.*
import com.example.ui.viewmodel.NextHireViewModel

@Composable
fun AuthScreen(viewModel: NextHireViewModel) {
    var isSignUp by remember { mutableStateOf(false) }
    var email by remember { mutableStateOf("rahul.sharma@college.edu") }
    var password by remember { mutableStateOf("password123") }
    var name by remember { mutableStateOf("Rahul Sharma") }
    var selectedDegree by remember { mutableStateOf("B.Tech") }
    val degrees = listOf("B.Tech", "BCA", "BBA", "MBA")
    val isDark = isSystemInDarkTheme()

    GlassCanvasBackground(isDark = isDark) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // App Logo Badge
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.linearGradient(
                            colors = listOf(Indigo600, ElectricCyan500)
                        )
                    )
                    .border(BorderStroke(2.dp, Color.White.copy(alpha = 0.8f)), shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.School,
                    contentDescription = "NextHire Logo",
                    tint = Color.White,
                    modifier = Modifier.size(44.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "NextHire AI",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = Indigo600
                )
            )

            Text(
                text = "AI-Powered Career & Placement Platform for College Students",
                style = MaterialTheme.typography.bodyMedium,
                color = if (isDark) DarkTextSecondary else Slate700,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(28.dp))

            // Frosted Glass Form Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        BorderStroke(1.5.dp, if (isDark) GlassBorderDark else GlassBorderLight),
                        shape = RoundedCornerShape(32.dp)
                    ),
                shape = RoundedCornerShape(32.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isDark) GlassSurfaceDark else GlassSurfaceLight
                ),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                ) {
                    Text(
                        text = if (isSignUp) "Create Student Account" else "Welcome Back",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = if (isDark) DarkTextPrimary else Slate900
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    if (isSignUp) {
                        OutlinedTextField(
                            value = name,
                            onValueChange = { name = it },
                            label = { Text("Full Name") },
                            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = Indigo600) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            singleLine = true
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("College Email ID") },
                        leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = Indigo600) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Password") },
                        leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = Indigo600) },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Select Degree Stream:",
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                        color = if (isDark) DarkTextSecondary else Slate700
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        degrees.forEach { degree ->
                            FilterChip(
                                selected = selectedDegree == degree,
                                onClick = { selectedDegree = degree },
                                label = { Text(degree, fontWeight = FontWeight.Bold) },
                                modifier = Modifier.weight(1f),
                                border = FilterChipDefaults.filterChipBorder(
                                    borderColor = if (selectedDegree == degree) Indigo600 else if (isDark) GlassBorderDark else GlassBorderLight,
                                    enabled = true,
                                    selected = selectedDegree == degree
                                ),
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = Indigo600.copy(alpha = 0.2f),
                                    containerColor = if (isDark) GlassSurfaceDarkLow else GlassSurfaceLightLow
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    GlassButton(
                        onClick = { viewModel.login(email, selectedDegree) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        gradientColors = listOf(Indigo600, Indigo500)
                    ) {
                        Icon(
                            imageVector = if (isSignUp) Icons.Default.PersonAdd else Icons.Default.Login,
                            contentDescription = null,
                            tint = Color.White
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isSignUp) "Sign Up as Student" else "Login to Dashboard",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedButton(
                        onClick = { viewModel.login("google.student@college.edu", selectedDegree) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(1.dp, if (isDark) GlassBorderDark else GlassBorderLight)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = null,
                            tint = Indigo600
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Continue with Google Sign-In", color = if (isDark) DarkTextPrimary else Slate900)
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    TextButton(
                        onClick = { isSignUp = !isSignUp },
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Text(
                            text = if (isSignUp) "Already have an account? Login" else "Don't have an account? Sign Up",
                            color = Indigo600,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 8.dp),
                        color = if (isDark) GlassBorderSubtleDark else GlassBorderSubtleLight
                    )

                    // Super Admin Bypass Option
                    TextButton(
                        onClick = {
                            email = "admin@nexthire.ai"
                            viewModel.login(email, "B.Tech")
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = Icons.Default.AdminPanelSettings,
                            contentDescription = null,
                            tint = Purple500
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Login as Super Admin (CMS Access)",
                            color = Purple500,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

