package biz.itonline.sporttracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import biz.itonline.sporttracker.presentation.navigation.SportNavigationGraph
import biz.itonline.sporttracker.ui.theme.SportTrackerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SportTrackerTheme {
                val navController = rememberNavController()

                SportNavigationGraph(navController = navController)
            }
        }
    }
}