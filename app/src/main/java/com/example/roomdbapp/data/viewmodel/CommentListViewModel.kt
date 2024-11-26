package com.example.roomdbapp.data.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.roomdbapp.data.models.Comment
import com.example.roomdbapp.data.repositories.CommentRepository
import com.example.roomdbapp.utils.ViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommentListViewModel @Inject constructor(private val userRepository: CommentRepository) :
    ViewModel() {

    private val _getAllComment = MutableStateFlow<ViewState<List<Comment>>>(
        ViewState.initial(null))

    val commentState : MutableStateFlow<ViewState<List<Comment>>>get() = _getAllComment


    fun fetchComments(mContext: Context) {
        _getAllComment.value = ViewState.loading(null) // Set loading state
        viewModelScope.launch {
            try {
                userRepository.fetchCommentFromApi(mContext)
                    .catch {
                        _getAllComment.value = ViewState.error(it.message.toString(), null)
                    }
                    .collect { pokemonsResponseViewState ->
                                    // Perform database operations in the background thread
                                        _getAllComment.value=  ViewState.success(pokemonsResponseViewState.data)
                    }

            } catch (e: Exception) {
                _getAllComment.value = ViewState.error(e.message.toString(), null)
            }
        }
    }

    private val _deleteComment = MutableStateFlow<ViewState<Any>>(
        ViewState.initial(null))

    val deleteCommentState : MutableStateFlow<ViewState<Any>>get() = _deleteComment
    fun deleteUserById(userId: Int) {
        _deleteComment.value = ViewState.loading(null) // Set loading state
        viewModelScope.launch {
            try {
                userRepository.deleteCommment(userId)
                    .catch {
                        _deleteComment.value = ViewState.error(it.message.toString(), null)
                    }
                    .collect { responseViewState ->
                        // Perform database operations in the background thread
                        _deleteComment.value=  ViewState.success(responseViewState.data)
                    }
            } catch (e: Exception) {
                _deleteComment.value = ViewState.error(e.message.toString(), null)
            }
        }
    }

    private val _updateComment = MutableStateFlow<ViewState<Any>>(
        ViewState.initial(null))

    val updateCommentState : MutableStateFlow<ViewState<Any>>get() = _updateComment
    fun updateComments(mContext: Context, comment: String, id: Int) {
        _updateComment.value = ViewState.loading(null) // Set loading state
        viewModelScope.launch {
            try {
                userRepository.updateCommment(mContext,comment,id)
                    .catch {
                        _updateComment.value = ViewState.error(it.message.toString(), null)
                    }
                    .collect { responseViewState ->
                        // Perform database operations in the background thread
                        _updateComment.value=  ViewState.success(responseViewState.data)
                    }
            } catch (e: Exception) {
                _updateComment.value = ViewState.error(e.message.toString(), null)
            }
        }
    }
}