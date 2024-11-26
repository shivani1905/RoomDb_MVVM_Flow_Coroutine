package com.example.roomdbapp.data.models
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class CommentListResponse(
    @SerializedName("comments")
    val comments: ArrayList<Comment>,
    @SerializedName("limit")
    val limit: Int,
    @SerializedName("skip")
    val skip: Int,
    @SerializedName("total")
    val total: Int
):Serializable

@Entity(tableName = "comment_table")
data class Comment(
    @SerializedName("body")
    val body: String,
    @SerializedName("id")
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    @SerializedName("likes")
    val likes: Int,
    @SerializedName("postId")
    val postId: Int,
    @Embedded(prefix = "user_")
    @SerializedName("user")
    val user: User
):Serializable

@Entity(tableName = "user_table",
    foreignKeys = [ForeignKey(
        entity = Comment::class,
        parentColumns = ["id"],
        childColumns = ["uId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class User(
    @PrimaryKey(autoGenerate = true)
    @SerializedName("uId")
    var uId: Int = 0,
    @SerializedName("fullName")
    val fullName: String,
    @SerializedName("userId")
    val id: Int,
    @SerializedName("username")
    val username: String
):Serializable