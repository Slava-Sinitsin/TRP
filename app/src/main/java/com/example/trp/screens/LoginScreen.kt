package com.example.trp.screens

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.trp.R
import com.example.trp.data.AuthRequest
import com.example.trp.data.User
import com.example.trp.network.ApiService
import com.example.trp.ui.theme.TRPTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject

@Composable
fun LoginScreen(navController: NavHostController) {
    var logValue by remember { mutableStateOf("android_student") }
    var passValue by remember { mutableStateOf("rebustubus") }
    var isLogged by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf("") }
    var messageVisibility by remember { mutableStateOf(false) }
    var user by remember { mutableStateOf(User()) }
    LaunchedEffect(isLogged) {
        if (isLogged) {
            navController.popBackStack()
            navController.navigate("WelcomeScreen")
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(TRPTheme.colors.primaryBackground),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Message(
            message = message,
            messageVisibility = messageVisibility
        )
        MailField(value = logValue,
            newValue = { newValue ->
                logValue = newValue
            },
            onMessageVisibilityChange = { newMessageVisibility ->
                messageVisibility = newMessageVisibility
            })
        PassField(value = passValue,
            newValue = { newValue ->
                passValue = newValue
            },
            onMessageVisibilityChange = { newMessageVisibility ->
                messageVisibility = newMessageVisibility
            })
        ConfirmButton(
            logValue = logValue,
            passValue = passValue,
            onMessageChange = { newMessage, newIsMessageVisible ->
                message = newMessage
                messageVisibility = newIsMessageVisible
            },
            onLoggedChange = { newIsLogged ->
                isLogged = newIsLogged
            },
            onUserChange = { newUser ->
                user = newUser
            }
        )
        Spacer(modifier = Modifier.size(70.dp))
    }
}

@Composable
fun Message(
    message: String,
    messageVisibility: Boolean
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(50.dp)
            .padding(vertical = 5.dp, horizontal = 100.dp)
    ) {
        AnimatedVisibility(
            visible = messageVisibility,
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
                    message,
                    color = TRPTheme.colors.errorColor,
                    modifier = Modifier.padding(horizontal = animateDpAsState(if (message.isNotEmpty()) 16.dp else 0.dp).value),
                    fontSize = 15.sp
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MailField(
    value: String,
    newValue: (String) -> Unit,
    onMessageVisibilityChange: (Boolean) -> Unit
) {
    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp, horizontal = 50.dp),
        textStyle = TextStyle.Default.copy(fontSize = 20.sp),
        value = value,
        onValueChange = {
            newValue(it)
            onMessageVisibilityChange(false)
        },
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
            Icon(imageVector = Icons.Default.Email, contentDescription = "Email Icon")
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PassField(
    value: String,
    newValue: (String) -> Unit,
    onMessageVisibilityChange: (Boolean) -> Unit
) {
    var showPassword by remember { mutableStateOf(false) }
    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp, horizontal = 50.dp),
        textStyle = TextStyle.Default.copy(fontSize = 20.sp),
        value = value,
        onValueChange = {
            newValue(it)
            onMessageVisibilityChange(false)
        },
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
        visualTransformation = if (showPassword) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        },
        trailingIcon = {
            if (showPassword) {
                IconButton(onClick = { showPassword = false }) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.pass_visibility_off_24),
                        contentDescription = "Pass Vis ON"
                    )
                }
            } else {
                IconButton(onClick = { showPassword = true }) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.pass_visibility_on_24),
                        contentDescription = "Pass Vis OFF"
                    )
                }
            }
        }
    )
}

@Composable
fun ConfirmButton(
    logValue: String,
    passValue: String,
    onMessageChange: (String, Boolean) -> Unit,
    onLoggedChange: (Boolean) -> Unit,
    onUserChange: (User) -> Unit
) {
    Button(
        onClick = {
            CoroutineScope(Dispatchers.IO).launch {
                val response = ApiService.userAPI.auth(
                    AuthRequest(
                        logValue,
                        passValue
                    )
                )
                val errorMessage = response.errorBody()?.string()?.let { errorBody ->
                    JSONObject(errorBody).getString("authenticationError")
                }
                val isMessageVisible = errorMessage?.isNotEmpty() ?: false
                onMessageChange(errorMessage ?: "", isMessageVisible)
                val user = response.body()
                if (user != null) {
                    onUserChange(user)
                }
                user?.let {
                    onLoggedChange(user.token != null)
                }
            }
        },
        modifier = Modifier.padding(5.dp),
        colors = ButtonDefaults.buttonColors(TRPTheme.colors.MyYellow)
    ) {
        Text(text = "Confirm", color = TRPTheme.colors.secondaryText)
    }
}