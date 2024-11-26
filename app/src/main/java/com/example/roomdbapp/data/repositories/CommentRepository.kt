package com.example.roomdbapp.data.repositories

import android.content.Context
import com.example.roomdbapp.data.datasources.Services
import com.example.roomdbapp.data.models.Comment
import com.example.roomdbapp.db_utils.CommentDatabase
import com.example.roomdbapp.utils.Util
import com.example.roomdbapp.utils.ViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class CommentRepository @Inject constructor(
    private val service: Services,
    private val userDB: CommentDatabase
) {

    suspend fun fetchCommentFromApi(mContext: Context): Flow<ViewState<List<Comment>>> {
        return flow {
            if (Util.isNetworkAvailable(mContext)) {
                val comment = service.fetchComment()
                if (!comment.comments.isNullOrEmpty()) {
                    userDB.userDao().insertCommentswithUser(comment.comments)
                    var result = userDB.userDao().getAllComments()
                    if (!result.isNullOrEmpty()) {
                        emit(ViewState.success(result))
                    }
                }
            } else {
                var result = userDB.userDao().getAllComments()
                if (!result.isNullOrEmpty()) {
                    emit(ViewState.success(result))
                }
            }
        }.flowOn(Dispatchers.IO)
    }
    fun updateCommment(mContext: Context,comment: String,id :Int) : Flow<ViewState<Any>>{
        return flow {
        var result = userDB.userDao().updateComments(comment,id)
        if (result != null) {
            emit(ViewState.success(result))
        }
        }.flowOn(Dispatchers.IO)
    }

    fun deleteCommment(id :Int) : Flow<ViewState<Any>>{
        return flow {
        var result = userDB.userDao().deleteCommentById(id)
        if (result != null) {
            emit(ViewState.success(result))
        }
        }.flowOn(Dispatchers.IO)
    }
}

