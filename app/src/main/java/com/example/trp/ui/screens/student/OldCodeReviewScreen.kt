package com.example.trp.ui.screens.student

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.chihsuanwu.freescroll.freeScroll
import com.chihsuanwu.freescroll.rememberFreeScrollState
import com.example.trp.data.mappers.tasks.Note
import com.example.trp.domain.di.ViewModelFactoryProvider
import com.example.trp.ui.theme.TRPTheme
import com.example.trp.ui.viewmodels.student.OldCodeReviewScreenViewModel
import dagger.hilt.android.EntryPointAccessors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OldCodeReviewScreen(
    codeReviewId: Int
) {
    val factory = EntryPointAccessors.fromActivity(
        LocalContext.current as Activity,
        ViewModelFactoryProvider::class.java
    ).oldCodeReviewScreenViewModelFactory()
    val viewModel: OldCodeReviewScreenViewModel = viewModel(
        factory = OldCodeReviewScreenViewModel.provideOldCodeReviewScreenViewModel(
            factory,
            codeReviewId
        )
    )
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { scaffoldPadding ->
        LazyColumn(
            modifier = Modifier
                .background(TRPTheme.colors.primaryBackground)
                .padding(start = 5.dp, end = 5.dp, top = scaffoldPadding.calculateTopPadding())
                .fillMaxSize()
        ) {
            item { Spacer(modifier = Modifier.size(5.dp)) }
            item { ReviewField(viewModel = viewModel) }
            item { Spacer(modifier = Modifier.size(10.dp)) }
            item { CommentsField(viewModel = viewModel) }
            item { Spacer(modifier = Modifier.size(100.dp)) }
        }

        if (viewModel.errorMessage.isNotEmpty()) {
            Toast.makeText(LocalContext.current, viewModel.errorMessage, Toast.LENGTH_SHORT).show()
            viewModel.updateErrorMessage("")
        }
    }
}

@Composable
fun ReviewField(viewModel: OldCodeReviewScreenViewModel) {
    val freeScrollState = rememberFreeScrollState()
    val primaryBackground = TRPTheme.colors.primaryBackground.copy(alpha = 0.6f)
    val secondaryBackground = TRPTheme.colors.secondaryBackground.copy(alpha = 0.6f)
    val selectedColor = TRPTheme.colors.okColor.copy(alpha = 0.3f)

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.Transparent,
        shape = RoundedCornerShape(8.dp),
        shadowElevation = 6.dp
    ) {
        Row {
            Column(
                modifier = Modifier
                    .height(400.dp)
                    .weight(0.25f)
                    .background(TRPTheme.colors.cardButtonColor)
                    .verticalScroll(freeScrollState.verticalScrollState)
            ) {
                viewModel.codeList.forEachIndexed { index, _ ->
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 5.dp),
                        text = "${index + 1}",
                        color = TRPTheme.colors.primaryText,
                        textAlign = TextAlign.End,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 15.sp
                    )
                }
            }
            Column(
                modifier = Modifier
                    .height(400.dp)
                    .weight(2f)
                    .background(TRPTheme.colors.secondaryBackground)
                    .freeScroll(freeScrollState)
            ) {
                viewModel.codeList.forEachIndexed { index, item ->
                    Box(
                        modifier = Modifier
                            .background(
                                if (viewModel.codeList[index].second) {
                                    selectedColor
                                } else if (index % 2 == 0) {
                                    primaryBackground
                                } else {
                                    secondaryBackground
                                }
                            )
                    ) {
                        Text(text = item.first, fontFamily = FontFamily.Monospace, fontSize = 15.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun CommentsField(viewModel: OldCodeReviewScreenViewModel) {
    Column {
        Text(
            text = "Comments",
            color = TRPTheme.colors.primaryText,
            fontSize = 15.sp
        )
        Spacer(modifier = Modifier.size(10.dp))
        viewModel.codeReview.notes?.forEach { note ->
            Comment(
                viewModel = viewModel,
                note = note
            )
            Spacer(modifier = Modifier.size(10.dp))
        }
    }
}

@Composable
fun Comment(
    viewModel: OldCodeReviewScreenViewModel,
    note: Note
) {
    Surface(
        color = TRPTheme.colors.secondaryBackground,
        shape = RoundedCornerShape(8.dp),
        shadowElevation = 6.dp
    ) {
        Column(
            modifier = Modifier.padding(5.dp)
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = note.author?.fullName ?: "",
                color = TRPTheme.colors.primaryText.copy(alpha = 0.6f),
                textAlign = if (note.author?.fullName == viewModel.user.username) {
                    TextAlign.End
                } else {
                    TextAlign.Start
                }
            )
            Spacer(modifier = Modifier.size(5.dp))
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = note.message ?: "",
                color = TRPTheme.colors.primaryText,
                textAlign = if (note.author?.fullName == viewModel.user.username) {
                    TextAlign.End
                } else {
                    TextAlign.Start
                }
            )
        }
    }
}