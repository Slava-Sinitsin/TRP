package com.example.trp.ui.screens

import androidx.compose.foundation.clickable
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
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.trp.ui.theme.TRPTheme
import com.example.trp.ui.viewmodels.DisciplineScreenViewModel

@Suppress("UNCHECKED_CAST")
@Composable
fun DisciplinesScreen(onDisciplineClick: (index: Int) -> Unit) {
    val viewModel = viewModel<DisciplineScreenViewModel>(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return DisciplineScreenViewModel(onDisciplineClick) as T
            }
        }
    )

    Disciplines(viewModel = viewModel)
}

@Composable
fun Disciplines(viewModel: DisciplineScreenViewModel) {
    LazyColumn {
        items(viewModel.disciplines.size) { index ->
            Discipline(
                viewModel = viewModel,
                index = index
            )
        }
        item { Spacer(modifier = Modifier.size(100.dp)) }
    }
}

@Composable
fun Discipline(
    viewModel: DisciplineScreenViewModel,
    index: Int
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable {
                viewModel.getDiscipline(index).id?.let { id ->
                    viewModel.onDisciplineClick(id)
                }
            },
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = TRPTheme.colors.cardColor
        )
    ) {
        Text(
            text = viewModel.getDiscipline(index).name.toString() + " " +
                    viewModel.getDiscipline(index).id.toString(),
            modifier = Modifier.padding(16.dp),
            color = TRPTheme.colors.primaryText,
            fontSize = 25.sp
        )
    }
}