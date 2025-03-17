package com.example.letsnosh.helper

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.letsnosh.ui.theme.Blue
import com.example.letsnosh.ui.theme.Orange
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CookingTimeBottomSheet(
    modifier: Modifier = Modifier,
    onDeleteClick: () -> Unit = {},
    onRescheduleClick: (selectedTime: String) -> Unit = {},
    onCookNowClick: () -> Unit = {},
    selectedPeriod: String = "AM",
    onDismissRequest: () -> Unit = {}
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val coroutineScope = rememberCoroutineScope()
    var selectedTime by remember { mutableStateOf("") }

    Scaffold { innerPadding ->
        ModalBottomSheet(
            modifier = modifier
                .padding(innerPadding)
                .widthIn(max = 400.dp),
            onDismissRequest = onDismissRequest,
            sheetState = sheetState,
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
            containerColor = Color.White,
            content = {
                ScheduleCookingTimeContent(
                    onDeleteClick = onDeleteClick,
                    onRescheduleClick = { onRescheduleClick(selectedTime) },
                    onCookNowClick = onCookNowClick,
                    selectedPeriod = selectedPeriod,
                    onCloseClick = {
                        coroutineScope.launch { sheetState.hide() }
                        onDismissRequest()
                    },
                    onTimeSelected = { selectedTime1 ->
                        selectedTime =  selectedTime1
                    }
                )
            }
        )
    }
}


@Composable
fun ScheduleCookingTimeContent(
    onDeleteClick: () -> Unit = {},
    onRescheduleClick: () -> Unit = {},
    onCookNowClick: () -> Unit = {},
    selectedPeriod: String = "AM",
    onCloseClick: () -> Unit = {},
    onTimeSelected: (String) -> Unit = {}
) {
    var selectedHour by remember { mutableIntStateOf(6) }
    var selectedMinute by remember { mutableIntStateOf(30) }
    var selectedPeriodState by remember { mutableStateOf(selectedPeriod) }

    Column(
        modifier = Modifier
            .padding(horizontal = 10.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ScheduleCookingTimeHeader(onCloseClick = onCloseClick)

        ScheduleCookingTimeSelector(
            initialSelectedPeriod = selectedPeriodState,
            onPeriodChange = {
                selectedPeriodState = it
                onTimeSelected("$selectedHour:${String.format("%02d", selectedMinute)} $selectedPeriodState")
            },
            onHourChange = { hour ->
                selectedHour = hour-1
                onTimeSelected("$selectedHour:${String.format("%02d", selectedMinute)} $selectedPeriodState")
            },
            onMinuteChange = { minute ->
                selectedMinute = minute-1
                onTimeSelected("$selectedHour:${String.format("%02d", selectedMinute)} $selectedPeriodState")
            }
        )

        ScheduleCookingTimeActions(
            onDeleteClick = onDeleteClick,
            onRescheduleClick = {
                onRescheduleClick()
                onCloseClick()
            },
            onCookNowClick = onCookNowClick
        )
    }
}

@Composable
fun ScheduleCookingTimeHeader(onCloseClick: () -> Unit) {
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
                .border(1.dp, shape = CircleShape, color = Blue)
                .size(30.dp)
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
fun ScheduleCookingTimeSelector(
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

        AMPMToggle(
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
fun ScheduleCookingTimeActions(
    onDeleteClick: () -> Unit,
    onRescheduleClick: () -> Unit,
    onCookNowClick: () -> Unit
) {
    Row(
        modifier = Modifier.padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Delete Button
        TextButton(onClick = onDeleteClick) {
            Text(
                "Delete",
                color = Color.Red,
                style = MaterialTheme.typography.bodyLarge.copy(
                    textDecoration = TextDecoration.Underline
                )
            )
        }

        // Reschedule Button
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

        // Cook Now Button
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


@Composable
fun AMPMToggle(
    modifier: Modifier = Modifier,
    initialSelected: String = "AM",
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
            containerColor = if (isSelected) Color(0xFFE8ECF8) else Blue,
            contentColor = if (isSelected) Color.White else Blue
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(label, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal)
    }
}