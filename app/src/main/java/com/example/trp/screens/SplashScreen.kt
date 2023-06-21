package com.example.trp.screens

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
import androidx.navigation.NavHostController
import com.example.trp.R
import com.example.trp.ui.theme.TRPTheme
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavHostController) {
    val infiniteTransition = rememberInfiniteTransition()
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.3f,
        animationSpec = infiniteRepeatable(
            animation = tween(500, easing = { OvershootInterpolator(2f).getInterpolation(it) }),
            RepeatMode.Reverse
        )
    )
    LaunchedEffect(key1 = true) {
        delay(350)
        navController.popBackStack()
        navController.navigate("LoginScreen")
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(TRPTheme.colors.MyYellow),
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