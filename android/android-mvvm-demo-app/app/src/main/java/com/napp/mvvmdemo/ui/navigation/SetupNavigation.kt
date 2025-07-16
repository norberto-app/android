package com.napp.mvvmdemo.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.napp.mvvmdemo.ui.main.MainScreen
import com.napp.mvvmdemo.ui.main.detailview.ItemDetailsScreen
import kotlinx.serialization.Serializable

@Composable
fun NavigationSetup() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Main
    ) {
        composable<Main> {
            MainScreen(
                onItemSelected = { id ->
                    println("Selected ID: $id")
                    navController.navigate(SelectedItem(id))
                }
            )
        }

        composable<SelectedItem> { navBackStackEntry ->
            ItemDetailsScreen(navBackStackEntry.toRoute())
        }
    }
}

@Serializable
object Main

@Serializable
data class SelectedItem(val id: Int)