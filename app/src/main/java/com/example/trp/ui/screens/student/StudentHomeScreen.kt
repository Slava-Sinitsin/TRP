package com.example.trp.ui.screens.student

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.example.trp.ui.theme.TRPTheme

@Composable
fun StudentHomeScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text(
            "Student home",
            Modifier.align(Alignment.Center),
            fontSize = 40.sp,
            color = TRPTheme.colors.primaryText
        )
    }
}