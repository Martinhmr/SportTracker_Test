package biz.itonline.sporttracker.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import biz.itonline.sporttracker.presentation.add.AddSportScreen
import biz.itonline.sporttracker.presentation.add.AddSportUiEvent
import biz.itonline.sporttracker.presentation.add.AddSportViewModel
import biz.itonline.sporttracker.presentation.list.SportListScreen
import biz.itonline.sporttracker.presentation.list.SportListViewModel

@Composable
fun SportNavigationGraph(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Screen.List.route
    ) {
        // --- OBRAZOVKA 1: SEZNAM ---
        composable(route = Screen.List.route) {
            val viewModel = hiltViewModel<SportListViewModel>()
            val state by viewModel.uiState.collectAsStateWithLifecycle()
            val filter by viewModel.filter.collectAsStateWithLifecycle()

            SportListScreen(
                state = state,
                currentFilter = filter,
                onFilterChange = viewModel::onFilterChanged,
                onAddClick = {
                    navController.navigate(Screen.Add.route)
                },
                onDeleteClick = viewModel::onDeleteSport
            )
        }

        // --- OBRAZOVKA 2: PŘIDÁNÍ ---
        composable(route = Screen.Add.route) {
            val viewModel = hiltViewModel<AddSportViewModel>()
            val state by viewModel.uiState.collectAsStateWithLifecycle()

            LaunchedEffect(true) {
                viewModel.uiEvent.collect { event ->
                    when (event) {
                        is AddSportUiEvent.SaveSuccess -> {
                            navController.popBackStack()
                        }
                    }
                }
            }

            AddSportScreen(
                state = state,
                onEvent = viewModel::onEvent,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}