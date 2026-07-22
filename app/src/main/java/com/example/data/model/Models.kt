package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfile(
    @PrimaryKey val id: String = "current_user",
    val name: String = "Rahul Sharma",
    val email: String = "rahul.sharma@college.edu",
    val degree: String = "B.Tech", // BCA, B.Tech, BBA, MBA
    val college: String = "IIT / NIT State Campus",
    val passoutYear: String = "2026",
    val targetRole: String = "Software Development Engineer (SDE)",
    val bio: String = "Aspiring Tech Lead passionate about Java, Kotlin, DSA & AI Applications.",
    val skills: String = "Java, Python, Data Structures, SQL, Spring Boot, Android",
    val profilePicUri: String = "",
    val isAdmin: Boolean = false,
    val points: Int = 1250,
    val streakDays: Int = 12,
    val atsScore: Int = 82,
    val resumeSummary: String = "Full-stack enthusiast with experience in building scalable mobile & web apps."
)

@Entity(tableName = "job_posts")
data class JobPost(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val company: String,
    val location: String,
    val type: String, // Full-Time, Internship, Remote
    val salaryOrStipend: String,
    val eligibleDegrees: String, // BCA, B.Tech, BBA, MBA
    val description: String,
    val requirements: String,
    val applyUrl: String,
    val isFeatured: Boolean = false,
    val postedDate: String = "Just Now",
    val category: String = "Software Engineering"
)

@Entity(tableName = "note_items")
data class NoteItem(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val subject: String,
    val degree: String,
    val description: String,
    val fileContent: String,
    val author: String = "NextHire AI Academic Team",
    val date: String = "2026",
    val viewsCount: Int = 1420
)

@Entity(tableName = "project_ideas")
data class ProjectIdea(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val category: String, // Web, Mobile, Data Science, AI, Cloud
    val difficulty: String, // Beginner, Intermediate, Advanced
    val techStack: String,
    val description: String,
    val githubUrl: String = "https://github.com/nexthire-ai/sample-project",
    val architectureSteps: String
)

@Entity(tableName = "quiz_questions")
data class QuizQuestion(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val category: String, // Java, Python, C, C++, JS, SQL, DSA, Aptitude, Reasoning, English
    val questionText: String,
    val optionA: String,
    val optionB: String,
    val optionC: String,
    val optionD: String,
    val correctOptionIndex: Int, // 0, 1, 2, 3
    val explanation: String
)

@Entity(tableName = "pyq_papers")
data class PyqPaper(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val companyName: String, // TCS, Infosys, Wipro, Amazon, Deloitte, Accenture
    val year: String,
    val role: String,
    val questionsCount: Int,
    val downloadUrl: String = "#",
    val contentSummary: String
)

@Entity(tableName = "community_posts")
data class CommunityPost(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val authorName: String,
    val authorDegree: String,
    val content: String,
    val likesCount: Int = 0,
    val commentsCount: Int = 0,
    val timestamp: String = "Recent",
    val isPinned: Boolean = false
)

@Entity(tableName = "announcements")
data class Announcement(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val body: String,
    val date: String = "Today",
    val targetAudience: String = "All Students",
    val isHighPriority: Boolean = false
)

@Entity(tableName = "user_applications")
data class UserApplication(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val jobId: Long,
    val jobTitle: String,
    val company: String,
    val appliedDate: String,
    val status: String = "Submitted" // Submitted, Under Review, Interview Scheduled, Accepted
)

@Entity(tableName = "chat_messages")
data class ChatMessage(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val sender: String, // USER or MENTOR
    val messageText: String,
    val timestamp: Long = System.currentTimeMillis()
)
