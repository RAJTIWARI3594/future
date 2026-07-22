package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.model.*
import com.example.data.repository.NextHireRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class NextHireViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: NextHireRepository

    val userProfile: StateFlow<UserProfile?>
    val allJobs: StateFlow<List<JobPost>>
    val allNotes: StateFlow<List<NoteItem>>
    val allProjects: StateFlow<List<ProjectIdea>>
    val allQuizzes: StateFlow<List<QuizQuestion>>
    val allPyqs: StateFlow<List<PyqPaper>>
    val allCommunityPosts: StateFlow<List<CommunityPost>>
    val allAnnouncements: StateFlow<List<Announcement>>
    val userApplications: StateFlow<List<UserApplication>>
    val chatMessages: StateFlow<List<ChatMessage>>

    val isLoggedIn = MutableStateFlow(true)
    val isAdminMode = MutableStateFlow(false)
    val isDarkMode = MutableStateFlow(true)
    val selectedTab = MutableStateFlow(0) // 0: Dashboard, 1: AI Mentor, 2: Resume & ATS, 3: Practice & Coding, 4: Jobs, 5: Resources, 6: Community, 7: Admin CMS

    // Search and Filter States
    val searchQuery = MutableStateFlow("")
    val selectedDegreeFilter = MutableStateFlow("All") // All, BCA, B.Tech, BBA, MBA

    // AI Feature States
    val isChatLoading = MutableStateFlow(false)
    
    val atsResult = MutableStateFlow("")
    val isAtsLoading = MutableStateFlow(false)

    val mockResult = MutableStateFlow("")
    val isMockLoading = MutableStateFlow(false)

    val codeMentorResult = MutableStateFlow("")
    val isCodeLoading = MutableStateFlow(false)

    init {
        val dao = AppDatabase.getDatabase(application).nextHireDao()
        repository = NextHireRepository(dao)

        userProfile = repository.userProfile.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UserProfile()
        )

        allJobs = repository.allJobs.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        allNotes = repository.allNotes.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        allProjects = repository.allProjects.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        allQuizzes = repository.allQuizzes.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        allPyqs = repository.allPyqs.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        allCommunityPosts = repository.allCommunityPosts.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        allAnnouncements = repository.allAnnouncements.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        userApplications = repository.userApplications.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        chatMessages = repository.chatMessages.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    }

    // User Profile Actions
    fun login(email: String, degree: String) {
        viewModelScope.launch {
            val current = userProfile.value ?: UserProfile()
            val isSuperAdmin = email.trim().equals("admin@nexthire.ai", ignoreCase = true)
            val updated = current.copy(
                email = email,
                degree = degree,
                isAdmin = isSuperAdmin
            )
            repository.saveUserProfile(updated)
            if (isSuperAdmin) isAdminMode.value = true
            isLoggedIn.value = true
        }
    }

    fun updateUserProfile(name: String, targetRole: String, skills: String, college: String, passoutYear: String) {
        viewModelScope.launch {
            val current = userProfile.value ?: UserProfile()
            val updated = current.copy(
                name = name,
                targetRole = targetRole,
                skills = skills,
                college = college,
                passoutYear = passoutYear
            )
            repository.saveUserProfile(updated)
        }
    }

    fun toggleAdminMode() {
        isAdminMode.value = !isAdminMode.value
        if (isAdminMode.value) {
            selectedTab.value = 7 // Navigate to Admin CMS
        }
    }

    // Chat Action
    fun sendChatMessage(message: String) {
        if (message.isBlank()) return
        viewModelScope.launch {
            isChatLoading.value = true
            try {
                repository.sendChatMessage(message, userProfile.value)
            } finally {
                isChatLoading.value = false
            }
        }
    }

    // ATS Check Action
    fun runAtsCheck(resumeText: String, jobDesc: String) {
        if (resumeText.isBlank()) return
        viewModelScope.launch {
            isAtsLoading.value = true
            try {
                val result = repository.checkAtsResume(resumeText, jobDesc)
                atsResult.value = result
                // Increment streak/score
                userProfile.value?.let { p ->
                    repository.saveUserProfile(p.copy(points = p.points + 50, atsScore = 88))
                }
            } finally {
                isAtsLoading.value = false
            }
        }
    }

    // Mock Interview Action
    fun evaluateMockInterview(question: String, answer: String, role: String) {
        if (answer.isBlank()) return
        viewModelScope.launch {
            isMockLoading.value = true
            try {
                val result = repository.evaluateMockInterview(question, answer, role)
                mockResult.value = result
            } finally {
                isMockLoading.value = false
            }
        }
    }

    // Code Explanation Action
    fun runCodeMentor(language: String, code: String, question: String) {
        viewModelScope.launch {
            isCodeLoading.value = true
            try {
                val result = repository.explainCodeOrDebug(language, code, question)
                codeMentorResult.value = result
            } finally {
                isCodeLoading.value = false
            }
        }
    }

    // Application Action
    fun applyForJob(job: JobPost) {
        viewModelScope.launch {
            repository.applyForJob(job.id, job.title, job.company)
            userProfile.value?.let { p ->
                repository.saveUserProfile(p.copy(points = p.points + 100))
            }
        }
    }

    // Community Post
    fun addCommunityPost(content: String) {
        if (content.isBlank()) return
        viewModelScope.launch {
            val prof = userProfile.value ?: UserProfile()
            repository.insertCommunityPost(
                CommunityPost(
                    authorName = prof.name,
                    authorDegree = "${prof.degree} '${prof.passoutYear.takeLast(2)}",
                    content = content
                )
            )
        }
    }

    fun likeCommunityPost(postId: Long) {
        viewModelScope.launch {
            repository.likeCommunityPost(postId)
        }
    }

    // Admin CRUD Operations
    fun addOrUpdateJob(job: JobPost) {
        viewModelScope.launch {
            if (job.id == 0L) repository.insertJob(job) else repository.updateJob(job)
        }
    }

    fun deleteJob(id: Long) {
        viewModelScope.launch { repository.deleteJob(id) }
    }

    fun addOrUpdateNote(note: NoteItem) {
        viewModelScope.launch {
            if (note.id == 0L) repository.insertNote(note) else repository.updateNote(note)
        }
    }

    fun deleteNote(id: Long) {
        viewModelScope.launch { repository.deleteNote(id) }
    }

    fun addOrUpdateProject(project: ProjectIdea) {
        viewModelScope.launch {
            if (project.id == 0L) repository.insertProject(project) else repository.updateProject(project)
        }
    }

    fun deleteProject(id: Long) {
        viewModelScope.launch { repository.deleteProject(id) }
    }

    fun addOrUpdateQuiz(quiz: QuizQuestion) {
        viewModelScope.launch {
            if (quiz.id == 0L) repository.insertQuiz(quiz) else repository.updateQuiz(quiz)
        }
    }

    fun deleteQuiz(id: Long) {
        viewModelScope.launch { repository.deleteQuiz(id) }
    }

    fun addOrUpdatePyq(pyq: PyqPaper) {
        viewModelScope.launch {
            if (pyq.id == 0L) repository.insertPyq(pyq) else repository.updatePyq(pyq)
        }
    }

    fun deletePyq(id: Long) {
        viewModelScope.launch { repository.deletePyq(id) }
    }

    fun addAnnouncement(title: String, body: String, audience: String, highPriority: Boolean) {
        viewModelScope.launch {
            repository.insertAnnouncement(
                Announcement(
                    title = title,
                    body = body,
                    targetAudience = audience,
                    isHighPriority = highPriority
                )
            )
        }
    }

    fun deleteAnnouncement(id: Long) {
        viewModelScope.launch { repository.deleteAnnouncement(id) }
    }
}
