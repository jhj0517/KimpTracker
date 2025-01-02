package com.librarydevloperjo.cointracker

import androidx.compose.runtime.Composable

@Composable
fun KimpTrackerApp() {
    val navController = rememberNavController()
    KimpTrackerNavHost(
        navController = navController
    )
}

@Composable
fun KimpTrackerNavHost(
    navController: NavHostController
) {
    val activity = (LocalContext.current as Activity)
    NavHost(navController = navController, startDestination = Screen.Kimp.route) {
        composable(route = Screen.Kimp.route) {
            KimpTrackerScreen()
        }
        composable(
            route = Screen.Info.route,
        ) {
            InfoScreen(
            )
        }
    }
}