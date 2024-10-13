package com.example.deliveryapp.ui.customer

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.deliveryapp.R

fun AddSenderPointScreenRoute(senderPoint: String? = ""): String{
    return if(senderPoint.isNullOrEmpty()) "customerAddSenderPoint" else "customerAddSenderPoint?senderPoint={senderPoint}"
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun previewCustomerAddSenderPoint(){
    CustomerAddSenderPointScreen(navController = rememberNavController())
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerAddSenderPointScreen(navController: NavController){
    var senderText by remember {
        mutableStateOf("")
    }
    Scaffold(
        topBar = { TopBar(title = "Lấy hàng tại", navigationIcon = Icons.Default.ArrowBack){
            navController.popBackStack()
        } }
    ) {innerPadding ->
        CustomerAddPointScreen("Nhập địa chỉ lấy hàng", R.drawable.box_out, modifier = Modifier.padding(innerPadding), senderText){
            senderText = it
        }
    }
}



