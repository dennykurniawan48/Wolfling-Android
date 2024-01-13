package com.dennydev.wolfling.component

import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.dennydev.wolfling.navigation.Screen

val items = listOf(
    Screen.HomeScreen,
    Screen.NotificationScreen,
    Screen.ProfileScreen,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomNav(
    navController: NavHostController,
    selectedBottomIndex: String,
    notificationCount: Int,
    onChangeSelectedBottomIndex: (route: String) -> Unit
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: return

    NavigationBar {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = {
                    onChangeSelectedBottomIndex(item.route)
                    navController.navigate(item.route){
                        popUpTo(navController.graph.findStartDestination().id){
                            saveState=true
                        }
                        launchSingleTop=true
                        restoreState = true
                    }
                },
                label = {
                    Text(text = item.title)
                },
                alwaysShowLabel = false,
                icon = {
                    BadgedBox(
                        badge = {
                            if(item.route == Screen.NotificationScreen.route && notificationCount > 0) {
                                Badge{
                                    Text("$notificationCount")
                                }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = if (item.route == selectedBottomIndex) {
                                item.selectedIcon
                            } else item.unselectedIcon,
                            contentDescription = item.title
                        )
                    }
                }
            )
        }
    }
}