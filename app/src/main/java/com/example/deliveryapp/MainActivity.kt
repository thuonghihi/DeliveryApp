package com.example.deliveryapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.deliveryapp.ui.authentication.ForgotPassword
import com.example.deliveryapp.ui.authentication.ForgotPasswordScreen
import com.example.deliveryapp.ui.authentication.LoginScreen
import com.example.deliveryapp.ui.authentication.OtpScreen
import com.example.deliveryapp.ui.authentication.RegisterScreen
import com.example.deliveryapp.ui.authentication.WelcomeScreen
import com.example.deliveryapp.ui.theme.DeliveryAppTheme
import androidx.compose.material3.Surface
import com.example.deliveryapp.ui.customer.CustomerAddReceiverPointScreen
import com.example.deliveryapp.ui.customer.CustomerAddSenderPointScreen
//import com.example.deliveryapp.ui.customer.CustomerAddReceiverPointScreen
//import com.example.deliveryapp.ui.customer.CustomerAddSenderPointScreen
import com.example.deliveryapp.ui.customer.CustomerHomePageScreenRoute
import com.example.deliveryapp.ui.customer.CustomerHomepageScreen
import com.example.deliveryapp.ui.customer.CustomerOrderTrackingDetailScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Surface(modifier = Modifier.fillMaxSize()) {
//               MainApp()
//                Text("abc vcl")
                LoginScreen(navController = rememberNavController())
            }
        }
    }
}

@Composable
fun MainApp(){
    val navController = rememberNavController()
    DeliveryAppTheme {
        NavHost(navController = navController, startDestination = "welcome"){
            composable("welcome"){ WelcomeScreen(navController) }

            composable("login?numberPhone={numberPhone}",
                arguments = listOf(
                    navArgument(name = "numberPhone"){
                        type = NavType.StringType
                        nullable = true
                    }
                )
            ){ backStackEntry ->
                val numberPhone = backStackEntry.arguments?.getString("numberPhone")
                LoginScreen(navController, numberPhone = numberPhone) }

            composable("register"){ RegisterScreen(navController) }

            composable("forgotPassword"){ ForgotPasswordScreen(navController) }

            composable("otpScreen"){ OtpScreen(navController) }

            composable("customerHomepage/{customerID}",
                arguments = listOf(
                    navArgument(name = "customerID"){
                        type = NavType.StringType
                    })
                ){ backStackEntry ->
                val customerID = backStackEntry.arguments?.getString("customerID")
                requireNotNull(customerID)
                CustomerHomepageScreen(navController, customerID = customerID) }

            composable("customerAddSenderPoint"){ CustomerAddSenderPointScreen(navController) }

            composable("customerAddReceiverPoint"){ CustomerAddReceiverPointScreen(navController) }
            composable("orderTrackingDetail"){ CustomerOrderTrackingDetailScreen(navController) }

//            composable("productDetail/{productId}",
//                arguments = listOf(
//                    navArgument(name = "productId"){
//                        type = NavType.StringType
//                    })
//            ){backStackEntry ->
//                val productId = backStackEntry.arguments?.getString("productId")
//                ProductDetailScreen(navController = navController, productId = productId)
//            }

//            composable("addressDetail?addressId={addressId}",
//                arguments = listOf(
//                    navArgument(name = "addressId"){
//                        type = NavType?.StringType
//                        nullable = true
//                    }
//                )
//            ){backStackEntry ->
//                val addressId = backStackEntry.arguments?.getString("addressId")
//                AddressDetailScreen(navController = navController, addressId = addressId)
//            }

//            composable("productDetail/{productId}",
//                arguments = listOf(
//                    navArgument(name = "productId"){
//                        type = NavType.StringType
//                    })
//            ){backStackEntry ->
//                val productId = backStackEntry.arguments?.getString("productId")
//                ProductDetailScreen(navController = navController, productId = productId)
//            }
//
//            //Bắt buộc nhận cả 2 giá trị
//            composable("checkout/{cartId}/{customerId}",
//                arguments = listOf(
//                    navArgument(name = "cartId"){
//                        type = NavType.StringType
//                    },
//                    navArgument(name = "customerId"){
//                        type = NavType.StringType
//                    }
//                )
//            ){backStackEntry ->
//                backStackEntry.arguments.let {
//                    val cartId = it?.getString("cartId")
//                    val customerId = it?.getString("customerId")
//                    requireNotNull(cartId)
//                    requireNotNull(customerId)
//                    CheckoutScreen(cartId = cartId,
//                        customerId = customerId,
//                        navController = navController)
//                }
//            }
//
//            composable("checkoutSuccess"){
//                CheckoutSuccessScreen(navController)
//            }
//
//            composable("myAccount"){
//                MyAccountScreen(navController = navController)
//            }
//
//            composable("addressDetail?addressId={addressId}",
//                arguments = listOf(
//                    navArgument(name = "addressId"){
//                        type = NavType?.StringType
//                        nullable = true
//                    }
//                )
//            ){backStackEntry ->
//                val addressId = backStackEntry.arguments?.getString("addressId")
//                AddressDetailScreen(navController = navController, addressId = addressId)
//            }
        }
    }
}
