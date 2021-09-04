package com.arunava.example.tmdbdemo.ui.splash

sealed class SplashViewStates

object ConfigReceived : SplashViewStates()
data class ShowErrorDialog(val errorMsg: String) : SplashViewStates()