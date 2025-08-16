package com.example.godlytorch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.godlytorch.ui.theme.GodlyTorchTheme

class MainActivity : ComponentActivity() {
    private lateinit var controller: FlashlightController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        controller = FlashlightController(this)
        setContent {
            GodlyTorchTheme {
                Surface {
                    TorchScreen(controller)
                }
            }
        }
    }
}

@Composable
fun TorchScreen(controller: FlashlightController) {
    var isOn by remember { mutableStateOf(false) }
    var warmLevel by remember { mutableStateOf(0) } // 0..6
    var coolLevel by remember { mutableStateOf(0) } // 0..6

    LaunchedEffect(isOn) { controller.toggleTorch(isOn) }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize().padding(24.dp)
    ) {
        Text("GodlyTorch", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(16.dp))
        Switch(checked = isOn, onCheckedChange = { isOn = it })
        Spacer(Modifier.height(24.dp))
        Text("Warm tone level: ${warmLevel+1}/7")
        Slider(
            value = warmLevel.toFloat(),
            onValueChange = {
                warmLevel = it.toInt()
                controller.setLevelFromDiscrete((it.toInt()+1))
            },
            valueRange = 0f..6f,
            steps = 5
        )
        Spacer(Modifier.height(16.dp))
        Text("Cool tone level: ${coolLevel+1}/7")
        Slider(
            value = coolLevel.toFloat(),
            onValueChange = {
                coolLevel = it.toInt()
                controller.setLevelFromDiscrete((it.toInt()+1))
            },
            valueRange = 0f..6f,
            steps = 5
        )
        Spacer(Modifier.height(8.dp))
        Text(
            "Note: Dual‑tone split needs OEM support. Without it, both sliders affect the same torch.",
            style = MaterialTheme.typography.bodySmall
        )
    }
}
