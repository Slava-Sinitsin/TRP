package com.example.trp.ui.screens.teacher

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.trp.domain.di.ViewModelFactoryProvider
import com.example.trp.ui.components.VerticalNumberPicker
import com.example.trp.ui.components.rememberVerticalPickerState
import com.example.trp.ui.theme.TRPTheme
import com.example.trp.ui.viewmodels.teacher.CreateLabViewModel
import dagger.hilt.android.EntryPointAccessors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateLabScreen(
    disciplineId: Int,
    navController: NavHostController
) {
    val factory = EntryPointAccessors.fromActivity(
        LocalContext.current as Activity,
        ViewModelFactoryProvider::class.java
    ).createLabViewModelFactory()
    val viewModel: CreateLabViewModel = viewModel(
        factory = CreateLabViewModel.provideCreateLabViewModel(
            factory,
            disciplineId
        )
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CreateLabTopBar(
                viewModel = viewModel,
                navController = navController
            )
        }
    ) { scaffoldPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(TRPTheme.colors.primaryBackground)
                .padding(
                    start = 5.dp,
                    top = scaffoldPadding.calculateTopPadding()
                )
        ) {
            TitleField(viewModel = viewModel)
            RatingPicker(viewModel = viewModel)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateLabTopBar(
    viewModel: CreateLabViewModel,
    navController: NavHostController
) {
    TopAppBar(
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = TRPTheme.colors.myYellow,
            titleContentColor = TRPTheme.colors.secondaryText,
        ),
        title = { Text(text = "Create new lab") },
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "BackIconButton"
                )
            }
        },
        actions = {
            IconButton(
                onClick = {
                    viewModel.onApplyButtonClick()
                    navController.popBackStack()
                },
                enabled = viewModel.title.isNotEmpty()
            ) {
                Icon(
                    imageVector = Icons.Filled.Done,
                    contentDescription = "ApplyCreateLabButton",
                )
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TitleField(
    viewModel: CreateLabViewModel
) {
    Text(
        text = "Title",
        color = TRPTheme.colors.primaryText,
        fontSize = 15.sp,
        modifier = Modifier
            .alpha(0.6f)
            .padding(start = 5.dp, top = 10.dp)
    )
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp, horizontal = 5.dp),
        textStyle = TextStyle.Default.copy(fontSize = 15.sp),
        value = viewModel.title,
        onValueChange = { viewModel.updateTitle(it) },
        placeholder = {
            Text(
                "Title",
                color = TRPTheme.colors.primaryText,
                modifier = Modifier.alpha(0.6f),
                fontSize = 15.sp
            )
        },
        shape = RoundedCornerShape(8.dp),
        colors = TextFieldDefaults.textFieldColors(
            containerColor = TRPTheme.colors.secondaryBackground,
            textColor = TRPTheme.colors.primaryText,
            cursorColor = TRPTheme.colors.primaryText,
            focusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            errorIndicatorColor = TRPTheme.colors.errorColor,
            errorCursorColor = TRPTheme.colors.primaryText
        ),
        isError = viewModel.title.isEmpty(),
        singleLine = true
    )
}

@Composable
fun RatingPicker(viewModel: CreateLabViewModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp, start = 5.dp, end = 5.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(TRPTheme.colors.secondaryBackground),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            modifier = Modifier
                .padding(start = 5.dp)
                .alpha(0.6f),
            text = "Max rating",
            color = TRPTheme.colors.primaryText,
            fontSize = 15.sp,
        )
        val values = remember { viewModel.ratingList }
        val state = rememberVerticalPickerState()
        LaunchedEffect(state.selectedItem) {
            viewModel.updateRatingValue(state.selectedItem)
        }
        Box(
            modifier = Modifier
                .padding(start = 200.dp, top = 5.dp, bottom = 5.dp, end = 5.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(TRPTheme.colors.cardButtonColor)
        ) {
            VerticalNumberPicker(
                state = state,
                values = values,
                visibleItemsCount = 3,
                textStyle = TextStyle(fontSize = 15.sp, textAlign = TextAlign.Center),
                startIndex = viewModel.ratingList.indexOf(viewModel.maxRating)
            )
        }
    }
}