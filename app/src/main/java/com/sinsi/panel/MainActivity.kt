package com.sinsi.panel

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sinsi.panel.ui.navigation.SinsiNavGraph
import com.sinsi.panel.ui.theme.BlackBackground
import com.sinsi.panel.ui.theme.SinsiPanelTheme
import com.sinsi.panel.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SinsiPanelTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = BlackBackground
                ) {
                    val viewModel: MainViewModel = viewModel()
                    SinsiNavGraph(viewModel = viewModel)
                }
            }
        }
    }
}
