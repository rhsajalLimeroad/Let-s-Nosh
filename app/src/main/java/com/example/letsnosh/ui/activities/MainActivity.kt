package com.example.letsnosh.ui.activities

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.letsnosh.viewmodel.DishViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Log.d("kya mila?", "setContent")
            DishApp{ showToast("UI is ready to be updated!") }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}

@Composable
fun DishApp(onApiSuccess: () -> Unit) {
    val dishViewModel: DishViewModel = viewModel()
    val dishes by dishViewModel.dishes.observeAsState(initial = emptyList())

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = {
                Log.d("kya mila?", "clicked")
                dishViewModel.loadDishes()
                if (dishes.isNotEmpty()) {
                    Log.d("kya mila?", "success")
                    onApiSuccess()
                }
            },
            modifier = Modifier.padding(16.dp)
        ) {
            Text("Fetch Dishes")
        }
    }
}