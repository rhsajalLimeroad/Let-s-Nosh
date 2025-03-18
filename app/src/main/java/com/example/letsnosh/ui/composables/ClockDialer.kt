package com.example.letsnosh.ui.composables

import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp


@Composable
fun <T> ClockDialer(
    componentWidth: Dp,
    itemSize: Dp,
    visibleItemsCount: Int = 3,
    dataList: List<T>,
    defaultItem: T,
    scaleFactor: Float = 1.5f,
    itemTextStyle: TextStyle,
    defaultTextColor: Color,
    highlightedTextColor: Color,
    onItemChosen: (index: Int, item: T) -> Unit = { _, _ -> }
) {
    val halfItemHeight = with(LocalDensity.current) { itemSize.toPx() / 2f }
    val listScrollState = rememberLazyListState(0)
    var lastChosenIndex by remember { mutableIntStateOf(0) }
    var currentItems by remember { mutableStateOf(dataList) }

    LaunchedEffect(dataList) {
        var startingIndex = dataList.indexOf(defaultItem) - 1
        startingIndex += ((Int.MAX_VALUE / 2) / dataList.size) * dataList.size
        currentItems = dataList
        lastChosenIndex = startingIndex
        listScrollState.scrollToItem(startingIndex)
    }

    LazyColumn(
        modifier = Modifier
            .width(componentWidth)
            .height(itemSize * visibleItemsCount)
            .clipToBounds(),
        state = listScrollState,
        flingBehavior = rememberSnapFlingBehavior(lazyListState = listScrollState)
    ) {
        items(count = Int.MAX_VALUE) { index ->
            val item = currentItems[index % currentItems.size]
            Box(
                modifier = Modifier
                    .height(itemSize)
                    .fillMaxWidth()
                    .onGloballyPositioned { coordinates ->
                        val itemPositionY = coordinates.positionInParent().y - halfItemHeight
                        val containerHalfHeight = (halfItemHeight * visibleItemsCount)
                        val isHighlighted = (itemPositionY > containerHalfHeight - halfItemHeight && itemPositionY < containerHalfHeight + halfItemHeight)
                        val itemIndex = index - 1
                        if (isHighlighted && lastChosenIndex != itemIndex) {
                            onItemChosen(itemIndex % currentItems.size, item)
                            lastChosenIndex = itemIndex
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = item.toString(),
                    style = itemTextStyle,
                    color = if (lastChosenIndex == index) highlightedTextColor else defaultTextColor,
                    fontSize = if (lastChosenIndex == index) itemTextStyle.fontSize * scaleFactor else itemTextStyle.fontSize
                )
            }
        }
    }
}