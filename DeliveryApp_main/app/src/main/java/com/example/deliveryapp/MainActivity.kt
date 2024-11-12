package com.example.deliveryapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.deliveryapp.ui.authentication.ForgotPasswordScreen
import com.example.deliveryapp.ui.authentication.LoginScreen
import com.example.deliveryapp.ui.authentication.OtpScreen
import com.example.deliveryapp.ui.authentication.RegisterScreen
import com.example.deliveryapp.ui.authentication.WelcomeScreen
import com.example.deliveryapp.ui.theme.DeliveryAppTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.deliveryapp.data.models.Order
import com.example.deliveryapp.data.repositories.OrderRepository
import com.example.deliveryapp.ui.customer.CustomerActionScreen
import com.example.deliveryapp.ui.customer.CustomerAddOrderInformationScreen
import com.example.deliveryapp.ui.customer.CustomerCheckOrderScreen
import com.example.deliveryapp.ui.customer.CustomerHomepageScreen
import com.example.deliveryapp.ui.customer.MapScreen
import com.example.deliveryapp.viewmodels.GraphViewModel
import com.google.firebase.FirebaseApp
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.Gson

//import com.mapbox.geojson.Point
//import com.mapbox.maps.extension.compose.MapboxMap
//import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
        enableEdgeToEdge()
        setContent {
            FirebaseApp.initializeApp(this)
            DeliveryAppTheme(false) {
                MainApp()
//                CustomerAddReceiverPointScreen(rememberNavController())
//                CustomerHomepageScreen(rememberNavController(), "customer_id_1")
//                CustomerCheckOrderScreen(rememberNavController(), "a", "b")
            }
        }
    }
}

@Composable
fun MainApp() {
    val graphViewModel: GraphViewModel = viewModel()
    val graphData by graphViewModel.graphData.observeAsState()

    Log.d("graphDataOutModel", graphData.toString())

    val navController = rememberNavController()

    // Điều hướng dựa vào giá trị của graphData
    if (graphData.isNullOrEmpty()) {
        AuthNavGraph(navController = navController, graphViewModel = graphViewModel)
    } else {
        // Giả sử bạn có một CustomerNavGraph khác cho dữ liệu graph
        CustomerNavGraph(navController = navController, graphViewModel = graphViewModel)
    }
}


@Composable
fun AuthNavGraph(navController: NavHostController, graphViewModel: GraphViewModel) {
    NavHost(
        navController = navController,
        startDestination = "welcome"
    ) {
        composable("welcome") { WelcomeScreen(navController) }
        composable(
            "login?numberPhone={numberPhone}",
            arguments = listOf(navArgument("numberPhone") {
                type = NavType.StringType
                nullable = true
            })
        ) { backStackEntry ->
            val numberPhone = backStackEntry.arguments?.getString("numberPhone")
            LoginScreen(navController, numberPhone = numberPhone, graphViewModel = graphViewModel)
        }
        composable("register") { RegisterScreen(navController) }
        composable("forgotPassword") { ForgotPasswordScreen(navController) }
        composable("otpScreen") { OtpScreen(navController) }
    }
}

@Composable
fun CustomerNavGraph(navController: NavHostController, graphViewModel: GraphViewModel) {
    val graphData by graphViewModel.graphData.observeAsState()
    NavHost(
        navController = navController,
        startDestination = "customerHomepage"
    ) {
        composable("customerHomepage") { CustomerHomepageScreen(navController, graphData.toString()) }
        composable("customerActionScreen") { CustomerActionScreen(navController, graphData.toString()) }

        composable(
            "checkOrder/{orderJson}",
            arguments = listOf(
                navArgument("orderJson") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val orderJson = backStackEntry.arguments?.getString("orderJson")
            requireNotNull(orderJson)

            // Deserialize JSON thành đối tượng Order
            val order = Gson().fromJson(orderJson, Order::class.java)

            CustomerCheckOrderScreen(
                navController = navController,
                order = order
            )
        }


        composable("addOrderInfo") { CustomerAddOrderInformationScreen(navController = navController, customerID = graphData.toString()) }

    }
}


