package com.example.glance

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

@Composable
fun Navigation(context: Context){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.MainScreen.route){
        composable(route = Screen.MainScreen.route){
            MainScreen(navController = navController, applicationContext = context)
        }
        composable(route = Screen.SetScreen.route + "/{index}", arguments = listOf(navArgument("index"){
            type = NavType.IntType
            })){

            SetScreen(navController = navController, num1 = it.arguments?.getInt("index")) }
        composable(route = Screen.FlashScreen.route + "/{index}", arguments = listOf(navArgument("index"){
            type = NavType.IntType
        })){

            FlashScreen(navController = navController, num1 = it.arguments?.getInt("index")) }
        }

    }


