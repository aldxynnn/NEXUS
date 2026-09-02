package com.nexus.app.data.remote

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

    @POST("auth/register")
    suspend fun register(
        @Body request: UserRegisterRequest
    ): Response<UserResponse>

    @POST("auth/login")
    suspend fun login(
        @Body request: UserLoginRequest
    ): Response<UserResponse>

    @POST("tasks/{user_id}")
    suspend fun createTask(
        @Path("user_id") userId: Int,
        @Body request: TaskCreateRequest
    ): Response<TaskResponse>

    @GET("tasks/{user_id}")
    suspend fun getTasks(
        @Path("user_id") userId: Int
    ): Response<List<TaskResponse>>

    @POST("ai/chat")
    suspend fun chatWithAI(
        @Body request: AIChatRequest
    ): Response<AIChatResponse>

    @POST("ai/plan")
    suspend fun generateAIPlan(
        @Body request: AIPlanRequest
    ): Response<AIPlanResponse>

    @POST("ai/insights")
    suspend fun generateAIInsights(
        @Body request: AIInsightsRequest
    ): Response<AIInsightsResponse>
}