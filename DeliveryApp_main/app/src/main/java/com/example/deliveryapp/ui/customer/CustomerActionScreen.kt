package com.example.deliveryapp.ui.customer

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.deliveryapp.R
import com.example.deliveryapp.data.models.Order
import com.example.deliveryapp.data.repositories.OrderRepository
import com.example.deliveryapp.viewmodels.OrderViewModel

fun CustomerActionScreenRoute(): String{
    return "customerActionScreen"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerActionScreen(navController: NavController, customerID: String) {
    val orderRepository = OrderRepository()
    val orderViewModel = OrderViewModel(orderRepository)
    val statusList = listOf("Hoàn thành", "Đã hủy")
    orderViewModel.fetchOrders(customerID, statusList)
    val actionStatus by orderViewModel.orders.observeAsState()
    Scaffold (
        topBar = {
            Column {
                TopAppBar(
                    title = {
                        Text("Hoạt động", style = TextStyle(fontWeight = FontWeight.Medium, fontSize = 22.sp))
                    }
                )
                Spacer(modifier = Modifier.fillMaxWidth().height(0.5.dp).background(color = Color.LightGray))
            }
        },
        bottomBar = {
            BottomBar(navController, 1)
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            if(actionStatus != null){
                items(actionStatus!!) { item ->
                    ActionItem(item)
                }
            }
        }
    }
}

@Composable
fun ActionItem(order: Order){
    Column (
        modifier = Modifier.fillMaxSize().padding(10.dp)
    ) {
        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Text(order.timeStamp ?: "",
                style = TextStyle(
                    color = MaterialTheme.colorScheme.onSecondary,
                )
            )
            Box (
                modifier = Modifier.clip(RoundedCornerShape(3.dp))
                    .background(when (order.status) {
                        "Hoàn thành" -> Color(0xFFD2FFD3)
                        "Đã hủy" -> Color(0xFFE5E8E8)
                        else -> Color.White
                    })
                    .padding(3.dp)
            ){
                Text(order.status ?: "", style = TextStyle(
                    color = when (order.status) {
                        "Hoàn thành" -> Color(0xFF3BAC3D)
                        "Đã hủy" -> Color.Black
                        else -> Color.White
                    },
                    fontWeight = FontWeight.Medium,
                    fontSize = 12.sp
                ))
            }
        }
        Row (modifier = Modifier.fillMaxWidth().height(50.dp).padding(top = 7.dp)){
            Row (horizontalArrangement = Arrangement.Start, modifier = Modifier.weight(1f)){
                Image(
                    painterResource(R.drawable.box_out), "",
                    modifier = Modifier.size(45.dp))
                Column (
                    modifier = Modifier.padding(horizontal = 10.dp).fillMaxHeight(),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Row {
                        Image(
                            painterResource(R.drawable.dot_blue), "",
                            modifier = Modifier.size(16.dp).padding(end = 5.dp))
                        Text(order.pickupAddress ?: "",
                            style = TextStyle(),
//                        modifier = Modifier.
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    Row {
                        Image(
                            painterResource(R.drawable.dot_orange), "",
                            modifier = Modifier.size(16.dp).padding(end = 5.dp))
                        Text(order.deliveryAddress ?: "",
                            style = TextStyle(),
//                        modifier = Modifier.
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
            Text(order.totalAmount + "đ" ?: "", style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Medium))
        }
        Row(
            modifier = Modifier.fillMaxWidth(), // Chiếm hết chiều rộng của màn hình
            horizontalArrangement = Arrangement.End
        ){
            Button(
                modifier = Modifier.padding(top = 7.dp).height(32.dp),
                onClick = {},
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFC8D03),
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text("Đặt lại", style = TextStyle(fontSize = 13.sp), fontWeight = FontWeight.Medium)
            }
        }
    }
    Spacer(modifier = Modifier.height(5.dp).fillMaxWidth().background(color = Color.LightGray))
}
