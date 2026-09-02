package com.nexus.app.data.remote

data class UserRegisterRequest(
    val name: String,
    val email: String,
    val password: String
)

data class UserLoginRequest(
    val email: String,
    val password: String
)

data class UserResponse(
    val id: Int,
    val name: String,
    val email: String
)

data class TaskCreateRequest(
    val title: String,
    val day: String = "Today",
    val time: String = "",
    val category: String = "General"
)

data class TaskResponse(
    val id: Int,
    val title: String,
    val completed: Boolean,
    val day: String,
    val time: String,
    val category: String
)

// =========================
// AI CHAT
// =========================

data class AIChatRequest(
    val message: String
)

data class AIChatResponse(
    val response: String
)

// =========================
// AI PLANNER
// =========================

data class AIPlanRequest(
    val goal: String
)

data class AIPlanTaskResponse(
    val title: String,
    val time: String,
    val duration: String,
    val category: String
)

data class AIPlanResponse(
    val tasks: List<AIPlanTaskResponse>
)

// =========================
// AI INSIGHTS
// =========================

data class AIInsightTaskRequest(
    val title: String,
    val category: String,
    val time: String,
    val completed: Boolean,
    val day: String
)

data class AIInsightsRequest(
    val tasks: List<AIInsightTaskRequest>
)

data class AIInsightCardResponse(
    val title: String,
    val value: String,
    val description: String
)

data class AIInsightsResponse(
    val insights: List<AIInsightCardResponse>
)