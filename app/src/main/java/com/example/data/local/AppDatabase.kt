package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.model.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        UserProfile::class,
        JobPost::class,
        NoteItem::class,
        ProjectIdea::class,
        QuizQuestion::class,
        PyqPaper::class,
        CommunityPost::class,
        Announcement::class,
        UserApplication::class,
        ChatMessage::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun nextHireDao(): NextHireDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "nexthire_ai_db"
                )
                    .addCallback(DatabaseCallback())
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    CoroutineScope(Dispatchers.IO).launch {
                        seedDatabase(database.nextHireDao())
                    }
                }
            }
        }

        private suspend fun seedDatabase(dao: NextHireDao) {
            // Seed User
            dao.saveUserProfile(
                UserProfile(
                    id = "current_user",
                    name = "Rahul Sharma",
                    email = "rahul.sharma@college.edu",
                    degree = "B.Tech",
                    college = "IIT / State Tech Institute",
                    passoutYear = "2026",
                    targetRole = "Software Development Engineer (SDE-1)",
                    skills = "Java, Python, Data Structures, SQL, System Design, React, Android",
                    points = 1850,
                    streakDays = 14,
                    atsScore = 85
                )
            )

            // Seed Job Posts
            dao.insertJob(
                JobPost(
                    title = "Associate Software Engineer",
                    company = "Google",
                    location = "Bangalore, India (Hybrid)",
                    type = "Full-Time",
                    salaryOrStipend = "₹22 - ₹28 LPA",
                    eligibleDegrees = "B.Tech, BCA, MCA",
                    description = "Join Google's Core Engineering team to build scalable microservices, low-latency mobile client frameworks, and cloud infrastructure.",
                    requirements = "Proficiency in Java, C++, or Python. Strong foundation in Data Structures, Algorithms, and System Design principles.",
                    applyUrl = "https://careers.google.com",
                    isFeatured = true,
                    postedDate = "2 hours ago",
                    category = "Software Engineering"
                )
            )
            dao.insertJob(
                JobPost(
                    title = "SDE Intern (Summer 2026)",
                    company = "Amazon",
                    location = "Hyderabad / Remote",
                    type = "Internship",
                    salaryOrStipend = "₹80,000 / month",
                    eligibleDegrees = "B.Tech, BCA",
                    description = "6-month summer internship working with Amazon AWS Cloud Services. Opportunity for PPO based on performance.",
                    requirements = "Understanding of Object-Oriented Design, Java/Python, Object storage, and REST APIs.",
                    applyUrl = "https://amazon.jobs",
                    isFeatured = true,
                    postedDate = "Today",
                    category = "Cloud & Backend"
                )
            )
            dao.insertJob(
                JobPost(
                    title = "Management Trainee - Product & Marketing",
                    company = "Deloitte",
                    location = "Gurugram / Mumbai",
                    type = "Full-Time",
                    salaryOrStipend = "₹12 - ₹15 LPA",
                    eligibleDegrees = "MBA, BBA",
                    description = "Strategic role assisting Fortune 500 clients in digital transformation, market research, and agile project delivery.",
                    requirements = "Strong analytical reasoning, financial modeling, presentation skills, and MBA/BBA specialization in Marketing/Finance/Operations.",
                    applyUrl = "https://deloitte.com/careers",
                    isFeatured = false,
                    postedDate = "1 day ago",
                    category = "Product & Business Strategy"
                )
            )

            // Seed Notes
            dao.insertNote(
                NoteItem(
                    title = "Complete DSA Cheat Sheet & Complexity Analysis",
                    subject = "Data Structures & Algorithms",
                    degree = "B.Tech / BCA",
                    description = "Handcrafted notes covering Binary Trees, Graph Traversal (DFS/BFS), Dynamic Programming templates, and Time & Space Complexity formulas.",
                    fileContent = "Comprehensive DSA summary notes including standard interview code patterns in Java & C++."
                )
            )
            dao.insertNote(
                NoteItem(
                    title = "Core DBMS & SQL Queries Master Guide",
                    subject = "Database Management Systems",
                    degree = "BCA / B.Tech",
                    description = "Covers ACID properties, ER diagrams, Normalization (1NF to 3NF/BCNF), Subqueries, Joins, Indexing, and Window Functions.",
                    fileContent = "Includes top 50 SQL interview queries solved step-by-step."
                )
            )
            dao.insertNote(
                NoteItem(
                    title = "MBA Financial Management & Valuation Notes",
                    subject = "Corporate Finance",
                    degree = "MBA / BBA",
                    description = "Essential guide on DCF Valuation, Capital Budgeting, WACC calculation, Balance Sheet ratio analysis, and Working Capital Management.",
                    fileContent = "Formula sheets and real-world case study templates."
                )
            )

            // Seed Projects
            dao.insertProject(
                ProjectIdea(
                    title = "AI-Powered Smart Resume & Portfolio Generator",
                    category = "AI / Full-Stack",
                    difficulty = "Intermediate",
                    techStack = "Kotlin, Jetpack Compose, Gemini API, Room DB",
                    description = "Build a mobile app that extracts career achievements, computes ATS resume scores, and generates customized PDF resumes with AI feedback.",
                    architectureSteps = "1. Parse user input -> 2. Construct prompt -> 3. Send to Gemini API -> 4. Parse JSON score -> 5. Render live preview"
                )
            )
            dao.insertProject(
                ProjectIdea(
                    title = "E-Commerce Microservices Platform",
                    category = "Backend / Cloud",
                    difficulty = "Advanced",
                    techStack = "Java, Spring Boot, Docker, PostgreSQL, Redis",
                    description = "Build scalable microservices for product catalog, order processing, payment gateway integration, and JWT authentication.",
                    architectureSteps = "1. API Gateway setup -> 2. Auth service with JWT -> 3. Order service with Redis cache -> 4. Payment webhook"
                )
            )

            // Seed Quizzes
            dao.insertQuiz(
                QuizQuestion(
                    category = "Java",
                    questionText = "Which keyword is used to prevent method overriding in Java?",
                    optionA = "static",
                    optionB = "final",
                    optionC = "abstract",
                    optionD = "protected",
                    correctOptionIndex = 1,
                    explanation = "The 'final' keyword when applied to a method prevents subclasses from overriding it."
                )
            )
            dao.insertQuiz(
                QuizQuestion(
                    category = "Python",
                    questionText = "What is the time complexity of looking up a key in a Python dictionary?",
                    optionA = "O(n)",
                    optionB = "O(log n)",
                    optionC = "O(1) average",
                    optionD = "O(n log n)",
                    correctOptionIndex = 2,
                    explanation = "Python dictionaries are implemented using hash tables, offering average O(1) time complexity for lookup."
                )
            )
            dao.insertQuiz(
                QuizQuestion(
                    category = "DSA",
                    questionText = "Which data structure is best suited for implementing a Breadth-First Search (BFS) on a graph?",
                    optionA = "Stack",
                    optionB = "Queue",
                    optionC = "Priority Queue",
                    optionD = "Binary Heap",
                    correctOptionIndex = 1,
                    explanation = "BFS explores graph nodes level by level, which is naturally managed using a First-In-First-Out (FIFO) Queue."
                )
            )
            dao.insertQuiz(
                QuizQuestion(
                    category = "Aptitude",
                    questionText = "If a train 150m long crosses a pole in 6 seconds, what is its speed in km/h?",
                    optionA = "60 km/h",
                    optionB = "72 km/h",
                    optionC = "90 km/h",
                    optionD = "100 km/h",
                    correctOptionIndex = 2,
                    explanation = "Speed = Distance / Time = 150m / 6s = 25 m/s. In km/h = 25 * (18 / 5) = 90 km/h."
                )
            )

            // Seed PYQs
            dao.insertPyq(
                PyqPaper(
                    companyName = "TCS Digital",
                    year = "2025 - 2026",
                    role = "System Engineer",
                    questionsCount = 45,
                    contentSummary = "Coding section (DSA + Dynamic Programming), Advanced Quantitative Aptitude, Verbal Reasoning, and SQL Subqueries."
                )
            )
            dao.insertPyq(
                PyqPaper(
                    companyName = "Infosys Power Programmer",
                    year = "2025",
                    role = "Specialist Programmer",
                    questionsCount = 30,
                    contentSummary = "Includes 3 competitive programming problems (Graphs, Trees, Segment Trees) and technical interview transcript."
                )
            )

            // Seed Community
            dao.insertCommunityPost(
                CommunityPost(
                    authorName = "Ananya Roy",
                    authorDegree = "B.Tech CSE '25",
                    content = "Cracked the Amazon SDE-1 campus placement! My key tips: 1. Solve top 150 LeetCode patterns. 2. Prepare 2 deep projects with system design tradeoffs. 3. Practice mock interviews daily on NextHire AI!",
                    likesCount = 84,
                    commentsCount = 19,
                    isPinned = true
                )
            )

            // Seed Announcements
            dao.insertAnnouncement(
                Announcement(
                    title = "TCS NQT 2026 Registration Open!",
                    body = "All final year BCA, B.Tech, BBA students can apply now. Complete PYQs and aptitude quizzes in NextHire AI to boost your preparation score.",
                    date = "Today",
                    isHighPriority = true
                )
            )

            // Initial Chat welcome message
            dao.insertChatMessage(
                ChatMessage(
                    sender = "MENTOR",
                    messageText = "Hello Rahul! 👋 I am your NextHire AI Career Mentor. I can help you with resume optimization, mock interviews, DSA roadmap, and job application strategies. How can I guide you today?"
                )
            )
        }
    }
}
