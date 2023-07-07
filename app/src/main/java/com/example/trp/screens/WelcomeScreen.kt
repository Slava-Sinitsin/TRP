package com.example.trp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.trp.data.AuthRequest
import com.example.trp.network.ApiService
import com.example.trp.ui.theme.TRPTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun WelcomeScreen() {
    var okResponse by remember { mutableStateOf("Bad") }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(TRPTheme.colors.primaryBackground),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            okResponse,
            fontSize = 20.sp,
            color = TRPTheme.colors.primaryText
        )
        Box(contentAlignment = Alignment.Center) {
            Button(
                onClick = {
                    CoroutineScope(Dispatchers.IO).launch {
                        val authResponse = ApiService.userAPI.auth(
                            AuthRequest(
                                "android_student",
                                "rebustubus"
                            )
                        )
                        val user = authResponse.body()
                        val helloResponse =
                            ApiService.userAPI.hello("Bearer " + user?.token)
                        okResponse = helloResponse.body()?.message.toString()
                    }
                },
                modifier = Modifier.padding(5.dp),
                colors = ButtonDefaults.buttonColors(TRPTheme.colors.MyYellow)
            ) {
                Text(text = "Send", color = TRPTheme.colors.secondaryText)
            }
        }
    }
}