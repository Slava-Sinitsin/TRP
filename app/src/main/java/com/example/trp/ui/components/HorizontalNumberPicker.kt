package com.example.trp.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Divider
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.trp.ui.theme.TRPTheme
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

class HorizontalPickerState {
    var selectedItem by mutableStateOf("")
}

@Composable
fun rememberHorizontalPickerState() = remember { HorizontalPickerState() }

@Composable
private fun pixelsToDp(pixels: Int) = with(LocalDensity.current) { pixels.toDp() }

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HorizontalNumberPicker(
    modifier: Modifier = Modifier,
    values: List<String>,
    state: HorizontalPickerState = rememberHorizontalPickerState(),
    startIndex: Int = 0,
    visibleItemsCount: Int = 3,
    textStyle: TextStyle = LocalTextStyle.current
) {
    val visibleItemsMiddle = visibleItemsCount / 2
    val listScrollCount = values.size
    val listScrollMiddle = listScrollCount / 2
    val listStartIndex =
        listScrollMiddle - listScrollMiddle % values.size - visibleItemsMiddle + startIndex

    fun getItem(index: Int) = values[index % values.size]
    val listState = rememberLazyListState(initialFirstVisibleItemIndex = listStartIndex + 1)
    val flingBehavior = rememberSnapFlingBehavior(lazyListState = listState)
    val itemWidthPixels = remember { mutableStateOf(0) }
    val itemWidthDp = pixelsToDp(itemWidthPixels.value)
    val fadingEdgeGradient = remember {
        Brush.horizontalGradient(
            0f to Color.Transparent,
            0.5f to Color.Black,
            1f to Color.Transparent
        )
    }
    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemIndex }
            .map { index -> getItem(index + visibleItemsMiddle - 1) }
            .distinctUntilChanged()
            .collect { item -> state.selectedItem = item }
    }
    Box {
        LazyRow(
            modifier = modifier
                .width(itemWidthDp * visibleItemsCount)
                .fadingEdge(fadingEdgeGradient),
            state = listState,
            flingBehavior = flingBehavior,
            horizontalArrangement = Arrangement.Center
        ) {
            item { Spacer(modifier = Modifier.width(itemWidthDp)) }
            items(listScrollCount) { index ->
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .onSizeChanged { size -> itemWidthPixels.value = size.width }
                        .then(Modifier.padding(8.dp)),
                    text = getItem(index),
                    color = TRPTheme.colors.primaryText,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = textStyle
                )
            }
            item { Spacer(modifier = Modifier.width(itemWidthDp)) }
        }
        Divider(
            modifier = Modifier
                .offset(x = itemWidthDp * visibleItemsMiddle)
                .height(40.dp)
                .width(1.dp)
                .padding(vertical = 5.dp),
            color = TRPTheme.colors.myYellow
        )
        Divider(
            modifier = Modifier
                .offset(x = itemWidthDp * (visibleItemsMiddle + 1))
                .height(40.dp)
                .width(1.dp)
                .padding(vertical = 5.dp),
            color = TRPTheme.colors.myYellow
        )
    }
}

private fun Modifier.fadingEdge(brush: Brush) = this
    .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
    .drawWithContent {
        drawContent()
        drawRect(brush = brush, blendMode = BlendMode.DstIn)
    }

