package com.example.deliveryapp.ui.customer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.deliveryapp.R
import com.example.deliveryapp.ui.authentication.LeadingBasicButton

fun CheckAddressScreenRoute(senderPoint: String? = ""): String{
    return if(senderPoint.isNullOrEmpty()) "customerAddSenderPoint" else "customerAddSenderPoint?senderPoint={senderPoint}"
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun previewCustomerCheckOrder(){
    CustomerCheckOrderScreen(navController = rememberNavController(), "Hà Nội", "Quảng Bình")
}

@Composable
fun CustomerCheckOrderScreen(navController: NavController, senderPoint: String, receiverPoint: String){
    var hasOrderInformation by remember{
        mutableStateOf(false)
    }
    Scaffold(
        topBar = { TopBar(title = "Giao hàng tại", navigationIcon = Icons.Default.ArrowBack){
            navController.popBackStack()
        } }
    ) {innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(innerPadding).background(color = Color(0xFFF7F7F7)),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            AddAddress(navController = navController, senderPointText = senderPoint, receiverPointText = receiverPoint)
            if(hasOrderInformation){

            }
            else CustomerCheckOrderWithoutOrderInformation(navController = navController, "20.3km", "20.000đ")
        }
    }
}

@Composable
fun CustomerCheckOrderWithOrderInformation(navController: NavController){

}

@Composable
fun CustomerCheckOrderWithoutOrderInformation(navController: NavController, distance: String, previewPrice: String){
    Column(modifier = Modifier.fillMaxWidth().background(color = Color.White).padding(15.dp)) {
        Row(modifier = Modifier.fillMaxWidth().padding(top = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Khoảng cách",
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )
            )
            Text(distance,
                style = TextStyle(fontSize = 18.sp,
                    fontWeight = FontWeight.Medium)
            )
        }
        Spacer(modifier = Modifier.fillMaxWidth().height(20.dp))

        Row(modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Tạm tính",
                style = TextStyle(fontSize = 18.sp,
                    fontWeight = FontWeight.Medium)
            )
            Text(previewPrice,
                style = TextStyle(fontSize = 18.sp,
                    fontWeight = FontWeight.Medium)
            )
        }
        Spacer(modifier = Modifier.fillMaxWidth().height(1.dp).background(color = Color.LightGray))
        LeadingBasicButton("Thêm thông tin đơn hàng") { }
    }
}