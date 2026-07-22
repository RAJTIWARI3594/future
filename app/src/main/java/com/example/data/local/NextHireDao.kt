package com.example.data.local

import androidx.room.*
import com.example.data.model.*
import kotlinx.coroutines.flow.Flow

@Dao
interface NextHireDao {

    // User Profile
    @Query("SELECT * FROM user_profile WHERE id = :id")
    fun getUserProfile(id: String = "current_user"): Flow<UserProfile?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveUserProfile(profile: UserProfile)

    // Job Posts
    @Query("SELECT * FROM job_posts ORDER BY id DESC")
    fun getAllJobs(): Flow<List<JobPost>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertJob(job: JobPost): Long

    @Update
    suspend fun updateJob(job: JobPost)

    @Query("DELETE FROM job_posts WHERE id = :id")
    suspend fun deleteJob(id: Long)

    // Notes
    @Query("SELECT * FROM note_items ORDER BY id DESC")
    fun getAllNotes(): Flow<List<NoteItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: NoteItem): Long

    @Update
    suspend fun updateNote(note: NoteItem)

    @Query("DELETE FROM note_items WHERE id = :id")
    suspend fun deleteNote(id: Long)

    // Projects
    @Query("SELECT * FROM project_ideas ORDER BY id DESC")
    fun getAllProjects(): Flow<List<ProjectIdea>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProject(project: ProjectIdea): Long

    @Update
    suspend fun updateProject(project: ProjectIdea)

    @Query("DELETE FROM project_ideas WHERE id = :id")
    suspend fun deleteProject(id: Long)

    // Quizzes
    @Query("SELECT * FROM quiz_questions WHERE category = :category")
    fun getQuizzesByCategory(category: String): Flow<List<QuizQuestion>>

    @Query("SELECT * FROM quiz_questions")
    fun getAllQuizzes(): Flow<List<QuizQuestion>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuiz(quiz: QuizQuestion): Long

    @Update
    suspend fun updateQuiz(quiz: QuizQuestion)

    @Query("DELETE FROM quiz_questions WHERE id = :id")
    suspend fun deleteQuiz(id: Long)

    // PYQs
    @Query("SELECT * FROM pyq_papers ORDER BY id DESC")
    fun getAllPyqs(): Flow<List<PyqPaper>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPyq(pyq: PyqPaper): Long

    @Update
    suspend fun updatePyq(pyq: PyqPaper)

    @Query("DELETE FROM pyq_papers WHERE id = :id")
    suspend fun deletePyq(id: Long)

    // Community Posts
    @Query("SELECT * FROM community_posts ORDER BY isPinned DESC, id DESC")
    fun getAllCommunityPosts(): Flow<List<CommunityPost>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCommunityPost(post: CommunityPost): Long

    @Query("UPDATE community_posts SET likesCount = likesCount + 1 WHERE id = :id")
    suspend fun likeCommunityPost(id: Long)

    // Announcements
    @Query("SELECT * FROM announcements ORDER BY id DESC")
    fun getAllAnnouncements(): Flow<List<Announcement>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnnouncement(announcement: Announcement): Long

    @Query("DELETE FROM announcements WHERE id = :id")
    suspend fun deleteAnnouncement(id: Long)

    // User Applications
    @Query("SELECT * FROM user_applications ORDER BY id DESC")
    fun getUserApplications(): Flow<List<UserApplication>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertApplication(application: UserApplication)

    // Chat Messages
    @Query("SELECT * FROM chat_messages ORDER BY timestamp ASC")
    fun getAllChatMessages(): Flow<List<ChatMessage>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChatMessage(message: ChatMessage)
}
