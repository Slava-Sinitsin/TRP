package com.example.trp.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.trp.ui.theme.TRPTheme
import com.example.trp.ui.viewmodels.LoginScreenViewModel

@Composable
@Suppress("UNCHECKED_CAST")
fun LoginScreen(
    onLoginClick: () -> Unit
) {
    val viewModel = viewModel<LoginScreenViewModel>(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return LoginScreenViewModel() as T
            }
        }
    )
    LaunchedEffect(viewModel.isLogged) {
        if (viewModel.isLogged) {
            onLoginClick()
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(TRPTheme.colors.primaryBackground),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Message(viewModel = viewModel)
        MailField(viewModel = viewModel)
        PassField(viewModel = viewModel)
        ConfirmButton(viewModel = viewModel)
        Spacer(modifier = Modifier.size(70.dp))
    }
}

@Composable
fun Message(viewModel: LoginScreenViewModel) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(50.dp)
            .padding(vertical = 5.dp, horizontal = 100.dp)
    ) {
        AnimatedVisibility(
            visible = viewModel.messageVisibility,
            enter = fadeIn(
                animationSpec = tween(
                    durationMillis = 350,
                    easing = FastOutSlowInEasing
                )
            ),
            exit = fadeOut(
                animationSpec = tween(
                    durationMillis = 350,
                    easing = FastOutSlowInEasing
                )
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(40.dp)
                    .background(
                        color = TRPTheme.colors.secondaryBackground,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .border(
                        width = 1.dp,
                        color = TRPTheme.colors.errorColor,
                        shape = RoundedCornerShape(8.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    viewModel.message,
                    color = TRPTheme.colors.errorColor,
                    modifier = Modifier.padding(
                        horizontal = animateDpAsState(
                            if (viewModel.message.isNotEmpty()) 16.dp
                            else 0.dp
                        ).value
                    ),
                    fontSize = 15.sp
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MailField(viewModel: LoginScreenViewModel) {
    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp, horizontal = 50.dp),
        textStyle = TextStyle.Default.copy(fontSize = 20.sp),
        value = viewModel.logValue,
        onValueChange = { viewModel.updateLogValue(it) },
        placeholder = {
            Text(
                "Email",
                color = TRPTheme.colors.primaryText,
                modifier = Modifier.alpha(0.6f)
            )
        },
        shape = RoundedCornerShape(8.dp),
        colors = TextFieldDefaults.textFieldColors(
            containerColor = TRPTheme.colors.secondaryBackground,
            textColor = TRPTheme.colors.primaryText,
            cursorColor = TRPTheme.colors.primaryText,
            focusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        trailingIcon = {
            Icon(imageVector = Icons.Filled.Email, contentDescription = "Email Icon")
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PassField(viewModel: LoginScreenViewModel) {
    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp, horizontal = 50.dp),
        textStyle = TextStyle.Default.copy(fontSize = 20.sp),
        value = viewModel.passValue,
        onValueChange = { viewModel.updatePassValue(it) },
        placeholder = {
            Text(
                "Password",
                color = TRPTheme.colors.primaryText,
                modifier = Modifier.alpha(0.6f)
            )
        },
        shape = RoundedCornerShape(8.dp),
        colors = TextFieldDefaults.textFieldColors(
            containerColor = TRPTheme.colors.secondaryBackground,
            textColor = TRPTheme.colors.primaryText,
            cursorColor = TRPTheme.colors.primaryText,
            focusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        visualTransformation = if (viewModel.passwordVisibility) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        },
        trailingIcon = {
            IconButton(onClick = { viewModel.showPassword(!viewModel.passwordVisibility) }) {
                val icon = if (viewModel.passwordVisibility) {
                    Icons.Filled.VisibilityOff
                } else {
                    Icons.Filled.Visibility
                }
                Icon(
                    icon,
                    contentDescription = if (viewModel.passwordVisibility) "Pass Vis ON"
                    else "Pass Vis OFF"
                )
            }
        }
    )
}

@Composable
fun ConfirmButton(viewModel: LoginScreenViewModel) {
    Button(
        onClick = { viewModel.getUser() },
        modifier = Modifier.padding(5.dp),
        colors = ButtonDefaults.buttonColors(TRPTheme.colors.MyYellow)
    ) {
        Text(text = "Confirm", color = TRPTheme.colors.secondaryText)
    }
}