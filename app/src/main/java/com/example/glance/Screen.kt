package com.example.glance

sealed class Screen(val route: String){
    object MainScreen: Screen(route = "main_screen")
    object SetScreen: Screen(route = "set_screen")
    object FlashScreen: Screen(route = "flash_screen")
    object Folder: Screen(route = "folder")
    object EditScreen: Screen(route = "edit")

    fun withArgs(vararg arg:String): String{
        return buildString {
            append(route)
            arg.forEach{
                arg->
                append("/$arg")
            }
        }

    }
}

