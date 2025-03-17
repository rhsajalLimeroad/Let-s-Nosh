package com.example.letsnosh.ui.composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.letsnosh.ui.theme.Blue
import com.example.letsnosh.ui.theme.BluishWhite
import com.example.letsnosh.ui.theme.Orange
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CookingTimeBottomSheet(
    modifier: Modifier = Modifier,
    onDeleteClick: () -> Unit = {},
    onRescheduleClick: (selectedTime: String) -> Unit = {},
    onCookNowClick: () -> Unit = {},
    scheduledMeridian: String = "AM",
    onDismissRequest: () -> Unit = {}
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val coroutineScope = rememberCoroutineScope()
    var scheduledTime by remember { mutableStateOf("") }

    Scaffold { padding ->
        ModalBottomSheet(
            modifier = modifier
                .padding(padding)
                .widthIn(max = 400.dp),
            onDismissRequest = onDismissRequest,
            sheetState = sheetState,
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
            containerColor = Color.White,
            content = {
                BottomSheetContent(
                    onDeleteClick = onDeleteClick,
                    onRescheduleClick = { onRescheduleClick(scheduledTime) },
                    onCookNowClick = onCookNowClick,
                    scheduledMeridian = scheduledMeridian,
                    onCloseClick = {
                        coroutineScope.launch { sheetState.hide() }
                        onDismissRequest()
                    },
                    onTimeSelected = {
                        scheduledTime =  it
                    }
                )
            }
        )
    }
}


@Composable
fun BottomSheetContent(
    onDeleteClick: () -> Unit = {},
    onRescheduleClick: () -> Unit = {},
    onCookNowClick: () -> Unit = {},
    scheduledMeridian: String = "AM",
    onCloseClick: () -> Unit = {},
    onTimeSelected: (String) -> Unit = {}
) {
    var selectedHour by remember { mutableIntStateOf(6) }
    var selectedMinute by remember { mutableIntStateOf(30) }
    val selectedMeridianState by remember { mutableStateOf(scheduledMeridian) }

    Column(
        modifier = Modifier
            .padding(horizontal = 10.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Header(onCloseClick = onCloseClick)

        TimeSelectionView(
            initialSelectedPeriod = selectedMeridianState,
            onPeriodChange = { period -> updateSelectedTime(selectedHour, selectedMinute, period, onTimeSelected) },
            onHourChange = { hour ->
                selectedHour = hour - 1
                updateSelectedTime(selectedHour, selectedMinute, selectedMeridianState, onTimeSelected)
            },
            onMinuteChange = { minute ->
                selectedMinute = minute - 1
                updateSelectedTime(selectedHour, selectedMinute, selectedMeridianState, onTimeSelected)
            }
        )

        Row(
            modifier = Modifier.padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextButton(onClick = onDeleteClick) {
                Text(
                    "Delete",
                    color = Color.Red,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        textDecoration = TextDecoration.Underline
                    )
                )
            }

            OutlinedButton(
                modifier = Modifier.padding(start = 16.dp),
                onClick = onRescheduleClick,
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Orange
                ),
                border = BorderStroke(1.dp, Orange),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Re-schedule")
            }

            Button(
                modifier = Modifier.padding(start = 16.dp),
                onClick = onCookNowClick,
                colors = ButtonDefaults.buttonColors(containerColor = Orange),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Cook Now", color = Color.White)
            }
        }
    }
}

@Composable
fun Header(onCloseClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            modifier = Modifier.padding(start = 16.dp),
            text = "Schedule cooking time",
            style = MaterialTheme.typography.titleMedium,
            color = Blue
        )
        IconButton(
            onClick = onCloseClick,
            modifier = Modifier
                .border(2.dp, shape = CircleShape, color = Blue)
                .size(32.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Close",
                tint = Blue
            )
        }
    }
}

@Composable
fun TimeSelectionView(
    initialSelectedPeriod: String = "AM",
    onPeriodChange: (String) -> Unit = {},
    onHourChange: (Int) -> Unit = {},
    onMinuteChange: (Int) -> Unit = {}
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(40.dp)
    ) {
        TimePicker(
            onHourChange = onHourChange,
            onMinuteChange = onMinuteChange
        )

        MeridianToggle(
            modifier = Modifier,
            initialSelected = initialSelectedPeriod,
            onSelectionChange = onPeriodChange
        )
    }
}


@Composable
fun TimePicker(
    onHourChange: (Int) -> Unit = {},
    onMinuteChange: (Int) -> Unit = {}
) {
    Row(
        modifier = Modifier
            .padding(10.dp)
            .background(
                color = Color(0xFFF1F1F3),
                shape = RoundedCornerShape(8.dp)
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        ClockDialer(
            componentWidth = 95.dp,
            itemSize = 35.dp,
            visibleItemsCount = 3,
            dataList = (1..12).toList(),
            defaultItem = 6,
            scaleFactor = 1.5f,
            itemTextStyle = TextStyle(fontSize = 20.sp),
            defaultTextColor = Blue,
            highlightedTextColor = Blue,
            onItemChosen = { _, item ->
                onHourChange(item)
            }
        )
        Text(
            text = ":",
            style = MaterialTheme.typography.titleLarge,
            color = Blue
        )
        ClockDialer(
            componentWidth = 95.dp,
            itemSize = 40.dp,
            visibleItemsCount = 3,
            dataList = (0..59).toList(),
            defaultItem = 30,
            scaleFactor = 1.5f,
            itemTextStyle = TextStyle(fontSize = 20.sp),
            defaultTextColor = Blue,
            highlightedTextColor = Blue,
            onItemChosen = { _, item ->
                onMinuteChange(item)
            }
        )
    }
}

@Composable
fun MeridianToggle(
    modifier: Modifier = Modifier,
    initialSelected: String = "PM",
    onSelectionChange: (String) -> Unit = {}
) {
    var selected by remember { mutableStateOf(initialSelected) }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        ToggleButton(
            label = "AM",
            isSelected = selected == "AM",
            onClick = {
                selected = "AM"
                onSelectionChange("AM")
            }
        )

        ToggleButton(
            label = "PM",
            isSelected = selected == "PM",
            onClick = {
                selected = "PM"
                onSelectionChange("PM")
            }
        )
    }
}

@Composable
fun ToggleButton(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    TextButton(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) Blue else BluishWhite,
            contentColor = if (isSelected) Color.White else Blue
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            label,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}

private fun updateSelectedTime(hour: Int, minute: Int, period: String, onTimeSelected: (String) -> Unit) {
    val formattedMinute = String.format("%02d", minute)
    onTimeSelected("$hour:$formattedMinute $period")
}