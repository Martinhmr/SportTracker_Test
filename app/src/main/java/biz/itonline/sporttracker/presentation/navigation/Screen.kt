package biz.itonline.sporttracker.presentation.navigation

sealed class Screen(val route: String) {
    data object List : Screen("list_screen")
    data object Add : Screen("add_screen")
}