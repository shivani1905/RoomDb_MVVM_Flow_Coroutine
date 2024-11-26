package com.example.roomdbapp.utils

data class ViewState<out T>(val status: Constants.Status, val data: T?, val message: String?) {

    companion object {

        fun <T> success(data: T?): ViewState<T> {
            return ViewState(Constants.Status.SUCCESS, data, null)
        }

        fun <T> error(msg: String, data: T?): ViewState<T> {
            return ViewState(Constants.Status.ERROR, data, msg)
        }

        fun <T> loading(data: T?): ViewState<T> {
            return ViewState(Constants.Status.LOADING, data, null)
        }
        fun <T> initial(data: T?): ViewState<T> {
            return ViewState(Constants.Status.IDLE, data, null)
        }
    }
}