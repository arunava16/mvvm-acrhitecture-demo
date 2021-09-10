package com.arunava.example.tmdbdemo.ui.splash

import com.arunava.example.tmdbdemo.ui.commons.data.ImageConfig

sealed class SplashViewStates

data class ConfigReceived(val imageConfig: ImageConfig) : SplashViewStates()
data class ShowErrorDialog(val errorMsg: String) : SplashViewStates()