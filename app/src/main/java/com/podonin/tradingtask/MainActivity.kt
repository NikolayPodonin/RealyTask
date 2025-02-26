package com.podonin.tradingtask

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import cafe.adriel.voyager.navigator.Navigator
import com.podonin.tradingtask.splash_screen.ScreenSplash
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<SharedViewModel>()
    private var splashScreen: SplashScreen? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        splashScreen = installSplashScreen()

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setSplashCloseListener()
        initViewModel()

        setContent {
            Navigator(ScreenSplash())
        }
    }

    private fun setSplashCloseListener() {
        splashScreen?.setOnExitAnimationListener { splashScreenView ->
            lifecycleScope.launch {
                viewModel.splashCloseFlow.collect {
                    splashScreenView.view.alpha = it
                    if (it == 1f) splashScreenView.remove()
                }
            }
        }
    }

    private fun initViewModel() {
        viewModel.init(applicationContext)
    }
}