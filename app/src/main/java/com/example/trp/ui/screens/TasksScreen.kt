package com.example.trp.ui.screens

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.trp.ui.theme.TRPTheme

@Composable
fun TasksScreen() {
    MyLazyColumn()
}

@Composable
fun MyLazyColumn() {
    LazyColumn {
        items(40) { index ->
            ListItem(index)
        }
        item { Spacer(modifier = Modifier.size(100.dp)) }
    }
}

@Composable
fun ListItem(index: Int) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = TRPTheme.colors.cardColor
        )
    ) {
        Text(
            text = "Task ${index + 1}",
            modifier = Modifier.padding(16.dp),
            color = TRPTheme.colors.primaryText,
            fontSize = 25.sp
        )
    }
}