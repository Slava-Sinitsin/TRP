package com.example.trp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.trp.R
import com.example.trp.ui.theme.MyYellow

@Composable
fun LoginScreen() {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Transparent),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            MailField()
            PassField()
            ConfirmButton()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MailField() {
    var logValue by remember { mutableStateOf("") }
    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp, horizontal = 50.dp),
        value = logValue,
        onValueChange = { logValue = it },
        placeholder = { Text("Email") },
        shape = RoundedCornerShape(8.dp),
        colors = TextFieldDefaults.textFieldColors(
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
fun PassField() {
    var passValue by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp, horizontal = 50.dp),
        value = passValue,
        onValueChange = { passValue = it },
        placeholder = { Text("Password") },
        shape = RoundedCornerShape(8.dp),
        colors = TextFieldDefaults.textFieldColors(
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
fun ConfirmButton() {
    Button(
        onClick = {},
        modifier = Modifier
            .padding(5.dp),
        colors = ButtonDefaults.buttonColors(MyYellow)
    ) {
        Text(text = "Confirm")
    }
}