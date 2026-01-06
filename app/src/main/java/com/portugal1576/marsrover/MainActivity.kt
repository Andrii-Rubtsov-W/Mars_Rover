package com.portugal1576.marsrover

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.Modifier
import com.portugal1576.marsrover.presentation.navigation.AppNavigationRoot
import com.portugal1576.marsrover.ui.theme.MarsRoverTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MarsRoverTheme() {
                AppNavigationRoot(modifier = Modifier)
            }
        }
    }
}
