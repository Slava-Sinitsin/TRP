package com.example.trp.ui.screens.common

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.trp.R
import com.example.trp.ui.theme.TRPTheme
import com.example.trp.ui.viewmodels.common.SplashScreenViewModel

@Composable
fun SplashScreen(navigate: (destination: String) -> Unit) {
    val viewModel: SplashScreenViewModel = hiltViewModel()
    val infiniteTransition = rememberInfiniteTransition(label = "")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.3f,
        animationSpec = infiniteRepeatable(
            animation = tween(500, easing = { OvershootInterpolator(2f).getInterpolation(it) }),
            RepeatMode.Reverse
        ), label = ""
    )
    LaunchedEffect(viewModel.destination) {
        if (viewModel.destination != "") {
            navigate(viewModel.destination)
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(TRPTheme.colors.myYellow),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            modifier = Modifier.scale(scale),
            painter = painterResource(id = R.drawable.welcomelogo),
            contentDescription = "Splash Screen Logo"
        )
    }
}