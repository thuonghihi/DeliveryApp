package com.example.driver

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHost
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.driver.ui.authentication.LoginScreen
import com.example.driver.ui.authentication.OtpScreen
import com.example.driver.ui.driver.DialogOnline
import com.example.driver.ui.driver.DriverHomeScreen
import com.example.driver.ui.driver.DriverHomepageScreen
import com.example.driver.ui.theme.DriverTheme
import com.example.driver.viewmodel.GraphViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DriverTheme(false) {
                MainApp()
//                DialogOnline({}, true)
//                DriverHomeScreen()
            }
        }
    }
}

@Composable
fun MainApp(){
    val navController = rememberNavController()
    val graphViewModel: GraphViewModel = viewModel()
    val graphData by graphViewModel.graphData.observeAsState("driver_id_1")
    NavHost(navController = navController, startDestination = "login") {
        composable(
            "login?numberPhone={numberPhone}",
            arguments = listOf(navArgument("numberPhone") {
                type = NavType.StringType
                nullable = true
            })
        ) { backStackEntry ->
            val numberPhone = backStackEntry.arguments?.getString("numberPhone")
            LoginScreen(navController, numberPhone = numberPhone)
        }
        composable(
            "otpScreen/{numberPhone}",
            arguments = listOf(navArgument("numberPhone") {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            val numberPhone = backStackEntry.arguments?.getString("numberPhone")
            requireNotNull(numberPhone)
            OtpScreen(navController, numberPhone, graphViewModel)
        }
        composable("driverHomepage") { DriverHomepageScreen(graphData.toString()) }
    }
}