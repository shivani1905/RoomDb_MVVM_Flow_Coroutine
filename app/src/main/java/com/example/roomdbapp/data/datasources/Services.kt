package com.example.roomdbapp.data.datasources

import com.example.roomdbapp.data.models.CommentListResponse
import retrofit2.http.GET

interface Services {

    @GET("comments")
    suspend fun fetchComment(): CommentListResponse

}