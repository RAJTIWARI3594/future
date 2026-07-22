package com.example.data.repository

import com.example.data.local.NextHireDao
import com.example.data.model.*
import com.example.data.remote.GeminiService
import kotlinx.coroutines.flow.Flow

class NextHireRepository(private val dao: NextHireDao) {

    // User Profile
    val userProfile: Flow<UserProfile?> = dao.getUserProfile()
    suspend fun saveUserProfile(profile: UserProfile) = dao.saveUserProfile(profile)

    // Job Posts
    val allJobs: Flow<List<JobPost>> = dao.getAllJobs()
    suspend fun insertJob(job: JobPost) = dao.insertJob(job)
    suspend fun updateJob(job: JobPost) = dao.updateJob(job)
    suspend fun deleteJob(id: Long) = dao.deleteJob(id)

    // Notes
    val allNotes: Flow<List<NoteItem>> = dao.getAllNotes()
    suspend fun insertNote(note: NoteItem) = dao.insertNote(note)
    suspend fun updateNote(note: NoteItem) = dao.updateNote(note)
    suspend fun deleteNote(id: Long) = dao.deleteNote(id)

    // Projects
    val allProjects: Flow<List<ProjectIdea>> = dao.getAllProjects()
    suspend fun insertProject(project: ProjectIdea) = dao.insertProject(project)
    suspend fun updateProject(project: ProjectIdea) = dao.updateProject(project)
    suspend fun deleteProject(id: Long) = dao.deleteProject(id)

    // Quizzes
    val allQuizzes: Flow<List<QuizQuestion>> = dao.getAllQuizzes()
    fun getQuizzesByCategory(category: String) = dao.getQuizzesByCategory(category)
    suspend fun insertQuiz(quiz: QuizQuestion) = dao.insertQuiz(quiz)
    suspend fun updateQuiz(quiz: QuizQuestion) = dao.updateQuiz(quiz)
    suspend fun deleteQuiz(id: Long) = dao.deleteQuiz(id)

    // PYQs
    val allPyqs: Flow<List<PyqPaper>> = dao.getAllPyqs()
    suspend fun insertPyq(pyq: PyqPaper) = dao.insertPyq(pyq)
    suspend fun updatePyq(pyq: PyqPaper) = dao.updatePyq(pyq)
    suspend fun deletePyq(id: Long) = dao.deletePyq(id)

    // Community
    val allCommunityPosts: Flow<List<CommunityPost>> = dao.getAllCommunityPosts()
    suspend fun insertCommunityPost(post: CommunityPost) = dao.insertCommunityPost(post)
    suspend fun likeCommunityPost(id: Long) = dao.likeCommunityPost(id)

    // Announcements
    val allAnnouncements: Flow<List<Announcement>> = dao.getAllAnnouncements()
    suspend fun insertAnnouncement(announcement: Announcement) = dao.insertAnnouncement(announcement)
    suspend fun deleteAnnouncement(id: Long) = dao.deleteAnnouncement(id)

    // Applications
    val userApplications: Flow<List<UserApplication>> = dao.getUserApplications()
    suspend fun applyForJob(jobId: Long, jobTitle: String, company: String) {
        val app = UserApplication(
            jobId = jobId,
            jobTitle = jobTitle,
            company = company,
            appliedDate = "Today",
            status = "Submitted"
        )
        dao.insertApplication(app)
    }

    // Chat
    val chatMessages: Flow<List<ChatMessage>> = dao.getAllChatMessages()
    suspend fun sendChatMessage(userMessage: String, profile: UserProfile?): String {
        // Save user message
        dao.insertChatMessage(ChatMessage(sender = "USER", messageText = userMessage))

        // Get AI response
        val responseText = GeminiService.getCareerAdvice(
            userProfile = profile ?: UserProfile(),
            query = userMessage
        )

        // Save mentor message
        dao.insertChatMessage(ChatMessage(sender = "MENTOR", messageText = responseText))
        return responseText
    }

    // ATS Check
    suspend fun checkAtsResume(resumeText: String, targetJobDesc: String): String {
        return GeminiService.checkAtsResume(resumeText, targetJobDesc)
    }

    // Mock Interview
    suspend fun evaluateMockInterview(question: String, userAnswer: String, role: String): String {
        return GeminiService.evaluateMockInterview(question, userAnswer, role)
    }

    // Code Explanation
    suspend fun explainCodeOrDebug(language: String, codeSnippet: String, userQuery: String): String {
        return GeminiService.explainCodeOrDebug(language, codeSnippet, userQuery)
    }
}
