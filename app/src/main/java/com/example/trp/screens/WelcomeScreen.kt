package com.example.trp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.trp.data.JWTDecoder
import com.example.trp.data.User
import com.example.trp.data.UserDataManager
import com.example.trp.ui.theme.TRPTheme

@Composable
fun WelcomeScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(TRPTheme.colors.primaryBackground),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Column(
            modifier = Modifier
                .padding(5.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                UserDataManager.getUser()
                    .collectAsState(initial = User()).value.login.toString(),
                modifier = Modifier
                    .padding(5.dp),
                color = TRPTheme.colors.primaryText,
                fontSize = 40.sp
            )
            Text(
                UserDataManager.getUser()
                    .collectAsState(initial = User()).value.password.toString(),
                modifier = Modifier
                    .padding(5.dp),
                color = TRPTheme.colors.primaryText,
                fontSize = 40.sp
            )
            Text(
                UserDataManager.getUser()
                    .collectAsState(initial = User()).value.token.toString(),
                modifier = Modifier
                    .padding(5.dp),
                color = TRPTheme.colors.primaryText,
                fontSize = 20.sp
            )
            Text(
                JWTDecoder().decodeToken(
                    UserDataManager.getUser()
                        .collectAsState(initial = User()).value.token.toString()
                ).toString(),
                modifier = Modifier
                    .padding(5.dp),
                color = TRPTheme.colors.primaryText,
                fontSize = 20.sp
            )
        }
    }
}