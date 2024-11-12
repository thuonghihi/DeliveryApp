package com.example.driver.ui.driver

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.driver.R

fun DriverHomepageScreenRoute() : String{
    return "driverHomepage"
}

//@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DriverHomepageScreen(driverId: String) {
    MainScreen(driverId = driverId)
}

@Composable
fun MainScreen(driverId: String) {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { BottomAppBar(navController) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("home") { DriverHomeScreen(driverId) }
            composable("income") { DriverIncomeScreen(driverId) }
            composable("notification") { DriverNotificationScreen(driverId) }
            composable("profile") { DriverProfileScreen(driverId) }
        }
    }
}

@Composable
fun BottomAppBar(navController: NavController) {
    val items = listOf(
        BottomNavItem("Trang chủ", "home", R.drawable.home_outlined, R.drawable.home_filled),
        BottomNavItem("Thu nhập", "income", R.drawable.income_outlined, R.drawable.income_filled),
        BottomNavItem("Thông báo", "notification", R.drawable.bell_outlined, R.drawable.bell_filled),
        BottomNavItem("Tôi", "profile", R.drawable.account_outlined, R.drawable.account_filled)
    )

    NavigationBar { // Thay vì BottomNavigation
        val currentBackStackEntry = navController.currentBackStackEntryAsState()
        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(painter =  if(currentBackStackEntry.value?.destination?.route == item.route) painterResource(item.selectedIcon)
                    else painterResource(item.unselectedIcon), contentDescription = item.title, modifier = Modifier.size(24.dp))},
                label = { Text(item.title) },
                selected = currentBackStackEntry.value?.destination?.route == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

// Lớp dữ liệu cho các mục trong BottomAppBar
data class BottomNavItem(val title: String, val route: String, val unselectedIcon: Int, val selectedIcon: Int)
