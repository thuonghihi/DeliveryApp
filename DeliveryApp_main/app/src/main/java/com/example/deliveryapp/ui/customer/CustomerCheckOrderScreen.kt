package com.example.deliveryapp.ui.customer

import android.util.Log
import android.widget.CheckBox
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.ModifierLocalProvider
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.deliveryapp.R
import com.example.deliveryapp.data.models.Order
import com.example.deliveryapp.data.repositories.OrderRepository
import com.example.deliveryapp.ui.authentication.LeadingBasicButton
import com.example.deliveryapp.viewmodels.OrderViewModel
import com.google.gson.Gson

fun CheckOrderScreenRoute(order: Order): String {
    val gson = Gson()
    return "checkOrder/${gson.toJson(order)}"
}


//@Preview(showSystemUi = true, showBackground = true)
//@Composable
//fun previewCustomerCheckOrder(){
//    CustomerCheckOrderScreen(navController = rememberNavController(), "Hà Nội", "Quảng Bình")
//}

@Composable
fun CustomerCheckOrderScreen(navController: NavController, order: Order){
    var hasOrderInformation by remember{
        mutableStateOf(false)
    }
    Scaffold(
        topBar = { TopBar(title = "Xác nhận thông tin", navigationIcon = Icons.Default.ArrowBack){
            navController.popBackStack()
        } }
    ) {innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(innerPadding).background(color = Color(0xFFF7F7F7)),
            verticalArrangement = Arrangement.SpaceBetween
        ) { CustomerCheckOrderInformation(navController = navController, "20.3km", "20.000đ", order)
        }
    }
}

@Composable
fun CustomerCheckOrderInformation(navController: NavController, distance: String, previewPrice: String, order: Order){
    val orderRepository = OrderRepository()
    val orderViewModel = OrderViewModel(orderRepository)
    Column(modifier = Modifier.fillMaxWidth()
        .background(color = Color.White)
        .fillMaxSize()
        .padding(15.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        CheckAddress(order.pickupAddress ?: "", order.deliveryAddress ?: "")
        Column {
            Row(modifier = Modifier.fillMaxWidth().padding(top = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Khoảng cách",
                    style = TextStyle(
                        fontSize = 16.sp
                    )
                )
                Text(distance,
                    style = TextStyle(fontSize = 16.sp,
                        fontWeight = FontWeight.Medium)
                )
            }
            Spacer(modifier = Modifier.fillMaxWidth().height(20.dp))

            Row(modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Thành tiền",
                    style = TextStyle(fontSize = 16.sp)
                )
                Text(previewPrice,
                    style = TextStyle(fontSize = 16.sp,
                        fontWeight = FontWeight.Medium)
                )
            }

            Spacer(modifier = Modifier.fillMaxWidth().height(1.dp).background(color = Color.LightGray))

            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 15.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row (
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.weight(1f)
                ){
                    Text("Tiền mặt", style = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.Medium))
                    Image(painterResource(R.drawable.expendmore), "", modifier = Modifier.size(20.dp))
                }
                Spacer(modifier = Modifier.height(25.dp).width(1.dp).background(color = Color.LightGray))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Ưu đãi", style = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.Medium))
                    Image(painterResource(R.drawable.expendmore), "", modifier = Modifier.size(20.dp))
                }
            }

            Spacer(modifier = Modifier.fillMaxWidth().height(1.dp).background(color = Color.LightGray))

            Row (horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 10.dp)){
                var isCheck by remember {
                    mutableStateOf(false)
                }
                Checkbox(checked = isCheck, onCheckedChange = { isCheck = it }, modifier = Modifier.size(15.dp))
                Text(text = "Người nhận trả phí giao hàng",
                    modifier = Modifier.padding(start = 10.dp),
                    style = TextStyle(fontSize = 14.sp, color = MaterialTheme.colorScheme.onPrimary))
            }
            var isLoading by remember { mutableStateOf(false) }
            LeadingBasicButton("Đặt giao hàng") {
                isLoading = true
                orderViewModel.addOrder(order)

            }
            LoadingModal(isLoading) { isLoading = false }
        }
    }
}

@Composable
fun CheckAddress(pickupLocation: String, deliveryLocation: String){
    Column {
        CheckAddressItem(pickupLocation, R.drawable.from_icon)
        Spacer(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 50.dp, top = 5.dp)
            .height(1.dp)
            .background(color = Color.LightGray))
        CheckAddressItem(deliveryLocation, R.drawable.to_icon)
        }
    }



@Composable
fun CheckAddressItem(text: String, icon: Int){
    Row (
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(painter = painterResource(id = icon),
            contentDescription = "",
            modifier = Modifier.size(30.dp).padding(horizontal = 7.dp).padding(top = 5.dp))
        Text(text = text,
            style = TextStyle(fontSize = 18.sp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 5.dp, start = 10.dp)
            )
    }

}
