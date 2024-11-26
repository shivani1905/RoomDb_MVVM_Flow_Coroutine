package com.example.roomdbapp.db_utils

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.roomdbapp.data.models.Comment
import com.example.roomdbapp.data.models.User

@Dao
interface CommentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertComments(note: Comment) : Long

    @Insert
    fun insertComments(note: List<Comment>)

    @Update
    fun update(note: Comment)

    @Delete
    fun delete(note: Comment)

    @Query("DELETE FROM comment_table WHERE id = :userId")
    fun deleteCommentById(userId: Int)

    @Query("select * from comment_table ")
    fun getAllComments(): List<Comment>

    @Query("UPDATE  comment_table SET body = :comment where id = :id ")
    suspend fun updateComments(comment :String,id :Int)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUserResponse(userResponse: ArrayList<Comment>)

    // Insert the list of users (with address embedded)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsers(users: ArrayList<Comment>)

    // Insert a single address
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User) : Long
    

    @Transaction
    suspend fun insertCommentswithUser(comments: ArrayList<Comment>) {
        comments.forEach { user ->
            // Step 1: Insert the user and retrieve the generated ID
            val commentId = insertComments(user) // Change from bulk to single insert for ID retrieval
            user.user.uId = commentId.toInt()

            // Step 2: Insert the address and retrieve its generated ID
            insertUser(user.user)
        }
    }

}