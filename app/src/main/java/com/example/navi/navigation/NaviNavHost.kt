package com.example.navi.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.navi.presentation.component.RadialNavigation
import com.example.navi.presentation.screen.PlaceholderScreen

@Composable
fun NaviNavHost() {
    val navController = rememberNavController()
    var menuExpanded by remember { mutableStateOf(false) }

    val currentRoute = navController
        .currentBackStackEntryAsState()
        .value
        ?.destination
        ?.route

    // Кнопка «Назад» закрывает меню раньше, чем срабатывает навигация.
    BackHandler(enabled = menuExpanded) {
        menuExpanded = false
    }

    Box(modifier = Modifier.fillMaxSize()) {

        NavHost(
            navController = navController,
            startDestination = NaviRoutes.TRACKER,
            modifier = Modifier.fillMaxSize()
        ) {
            composable(NaviRoutes.TRACKER) {
                PlaceholderScreen("Главный трекер")
            }

            composable(NaviRoutes.HABITS) {
                PlaceholderScreen("Управление привычками")
            }

            composable(NaviRoutes.NUTRITION) {
                PlaceholderScreen("Вода и калории")
            }

            composable(NaviRoutes.DIARY) {
                PlaceholderScreen("Личный дневник")
            }

            composable(NaviRoutes.ENVIRONMENT) {
                PlaceholderScreen("Окружающая среда")
            }

            composable(NaviRoutes.SETTINGS) {
                PlaceholderScreen("Настройки")
            }
        }

        // Прозрачная область для закрытия меню нажатием вне него.
        if (menuExpanded) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable {
                        menuExpanded = false
                    }
            )
        }

        RadialNavigation(
            currentRoute = currentRoute,
            expanded = menuExpanded,
            onExpandedChange = { menuExpanded = it },
            onNavigate = { route ->
                if (currentRoute != route) {
                    navController.navigate(route) {
                        popUpTo(
                            navController.graph.findStartDestination().id
                        ) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            },
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}