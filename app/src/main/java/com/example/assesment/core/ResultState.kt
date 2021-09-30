package com.example.assesment.core

sealed class ResultState {
    object Loading : ResultState()
    class Error(val message: String) : ResultState()
    object Success : ResultState()
}