package com.example.deliveryapp.ui.customer

import android.content.Context
import android.location.Geocoder
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.deliveryapp.R
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import com.example.deliveryapp.data.models.OrderTracking
import com.example.deliveryapp.data.repositories.OrderRepository
import com.example.deliveryapp.viewmodels.OrderViewModel
//import com.google.android.gms.location.FusedLocationProviderClient
//import com.google.maps.android.compose.CameraPositionState
import kotlinx.coroutines.launch
import java.util.Locale
import com.example.deliveryapp.data.models.Order
import com.example.deliveryapp.data.repositories.CustomerRepository
import com.example.deliveryapp.viewmodels.CustomerViewModel
import androidx.compose.ui.Alignment

fun CustomerHomePageScreenRoute(): String{
    return "customerHomepage"
}

@Composable
fun CustomerHomepageScreen(navController: NavController, customerID: String){
    CustomerHomepage(navController = navController, customerID = customerID)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerHomepage(navController: NavController, customerID: String) {
    //mvvm Customer
    val customerRepository = CustomerRepository()
    val customerViewModel = CustomerViewModel(customerRepository)
    customerViewModel.getCustomerById(customerID)

    val customer by customerViewModel.customer.observeAsState()

    //mvvm Order
    val orderRepository = OrderRepository()
    val orderViewModel = OrderViewModel(orderRepository)
    val statusList = listOf("Đang vận chuyển")
    orderViewModel.fetchOrders(customerID, statusList)
    val orderStatus by orderViewModel.orders.observeAsState()

    TopBarWithNavigationDrawer(
        navController = navController,
        customer = customer?.name ?: "",
        hasOrder = orderStatus != null,
        bottomBar = { BottomBar(navController, 0) }
    ) { modifier ->
        when {
            orderStatus == null -> {
                // Hiển thị vòng tròn tải trong khi đang fetch dữ liệu
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    LinearProgressIndicator()
                }
            }
            orderStatus!!.isNotEmpty() -> {
                CustomerHomepageWithOrder(
                    navController = navController,
                    customerID = customerID,
                    orderList = orderStatus!!,
                    modifier = modifier
                )
            }
            else -> {
                CustomerHomepageWhitoutOrder(navController = navController, modifier = modifier)
            }
        }
    }


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerHomepageWithOrder(navController: NavController, customerID: String, orderList: List<Order>, modifier: Modifier = Modifier) {
    var isBottomSheetVisible by remember { mutableStateOf(false) }
    var selectedItem by remember { mutableStateOf<Order?>(null) }
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    Column(
        verticalArrangement = Arrangement.Top,
        modifier = modifier.fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(orderList) { item ->
                CustomerHomepageItemWithOrder(item, modifier = Modifier) {
                    selectedItem = item
                    isBottomSheetVisible = true
                }
            }
        }
    }

    if (isBottomSheetVisible && selectedItem != null) {
        OrderTrackingDetailSheet(
            bottomSheetState = bottomSheetState,
            orderTrackingItem = selectedItem!!,
            customerID = customerID
        ){
            isBottomSheetVisible = false
            selectedItem = null
        }
    }

    LaunchedEffect(isBottomSheetVisible) {
        if (isBottomSheetVisible) {
            bottomSheetState.show()
        } else {
            bottomSheetState.hide()
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CustomerHomepageItemWithOrder(orderTrackingItem: Order, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Column(
        modifier = modifier
            .clickable {
                onClick()
            }
            .border(1.dp, color = Color.LightGray)
            .clip(shape = RoundedCornerShape(10.dp))
            .padding(top = 10.dp, start = 20.dp, end = 10.dp)
            .fillMaxWidth()
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(
                text = "Mã đơn hàng: ${orderTrackingItem.orderIdShow}",
                style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Medium),
                modifier = Modifier.padding(vertical = 15.dp)
            )
        }

        Divider(color = Color.LightGray, thickness = 0.3.dp)

        // Row chứa địa chỉ gửi và dự kiến giao
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)) {
            OrderTrackingItemColumn(
                iconResId = R.drawable.box_out,
                title = "Địa chỉ gửi",
                content = orderTrackingItem.pickupAddress ?: "",
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(16.dp))
            OrderTrackingItemColumn(
                iconResId = R.drawable.date_icon,
                title = "Dự kiến giao",
                content = orderTrackingItem.timeStamp ?: "",
                modifier = Modifier.weight(1f)
            )
        }

        // Row chứa địa chỉ nhận và trạng thái
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)) {
            OrderTrackingItemColumn(
                iconResId = R.drawable.box_in,
                title = "Địa chỉ nhận",
                content = orderTrackingItem.deliveryAddress ?: "",
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(16.dp))
            OrderTrackingItemColumn(
                iconResId = R.drawable.status_icon,
                title = "Trạng thái",
                content = orderTrackingItem.status ?: "",
                modifier = Modifier
                    .weight(1f)
                    .padding(bottom = 5.dp)
            )
        }

    }
}

@Composable
fun OrderTrackingItemColumn(
    modifier: Modifier = Modifier, // Ensure this parameter is included
    iconResId: Int? = null,
    title: String,
    content: String
) {
    Row(modifier = modifier,
        verticalAlignment = Alignment.CenterVertically) {
        iconResId?.let {
            Image(
                painter = painterResource(id = it),
                contentDescription = title,
                modifier = Modifier.size(20.dp)
            )
        }
        Column {
            Spacer(modifier = Modifier.width(4.dp)) // Add spacing between icon and text
            Text(title, style = TextStyle(
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSecondary
            ), modifier = Modifier.padding(start = 10.dp, bottom = 5.dp))
            Text(content, style = TextStyle(
                fontSize = 13.sp,
            ), modifier = Modifier.padding(start = 10.dp))
        }
    }
}


@Composable
fun CustomerHomepageWhitoutOrder(navController: NavController, modifier: Modifier){
    Box{
        MapScreen(LocalContext.current)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderTrackingDetailSheet(bottomSheetState: SheetState, customerID: String, orderTrackingItem: Order, hideSheet: () -> Unit){
    val scope = rememberCoroutineScope()
    ModalBottomSheet(
        sheetState = bottomSheetState,
        containerColor = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(top = 15.dp),
        onDismissRequest = {
            scope.launch {
                bottomSheetState.hide()
            }.invokeOnCompletion {
                hideSheet()
            }
        }
    ) {
        BottomSheetContent(orderTrackingItem = orderTrackingItem, customerID = customerID)
    }
}

@Composable
fun BottomSheetContent(orderTrackingItem: Order, customerID: String){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(14.dp)
            .background(color = Color.White)
    ) {
        Text("Theo dõi đơn hàng",
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium)
        Text(
            text = "Mã đơn hàng: ${orderTrackingItem.orderIdShow}",
            style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Medium),
            modifier = Modifier.padding(vertical = 15.dp)
        )
        Row(horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 20.dp)) {
            OrderTrackingDetailItemColumn(
                iconResId = R.drawable.box_out,
                title = "Địa chỉ gửi",
                content = orderTrackingItem.pickupAddress ?: ""
            )
            //    Spacer(modifier = Modifier.weight(1f))
            OrderTrackingDetailItemColumn(
                iconResId = R.drawable.box_in,
                title = "Địa chỉ nhận",
                content = orderTrackingItem.deliveryAddress ?: ""
            )
        }
        Spacer(modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(color = Color.LightGray))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            OrderTrackingDetailItemColumn(
                title = "Khách hàng",
                content = customerID
            )
            OrderTrackingDetailItemColumn(
                title = "Trọng lượng",
                content = orderTrackingItem.weight + "kg"
            )
            OrderTrackingDetailItemColumn(
                title = "Chi phí",
                content = orderTrackingItem.totalAmount ?: ""
            )
        }
        Spacer(modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(color = Color.LightGray))

        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Image(painter = painterResource(R.drawable.shipper_welcome), "",
                    modifier = Modifier.size(35.dp))
                Column {
                    Text(orderTrackingItem.driver ?: "",
                        style = TextStyle(fontSize = 15.sp))
                    Text("Tài xế",
                        style = TextStyle(fontSize = 13.sp,
                            fontWeight = FontWeight.Light))
                }
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.MailOutline, "")
                Icon(Icons.Default.Phone, "")
            }
        }
    }

}

@Composable
fun OrderTrackingDetailItemColumn(
    modifier: Modifier = Modifier, // Ensure this parameter is included
    iconResId: Int? = null,
    title: String,
    content: String
) {
    Row(modifier = modifier,
        verticalAlignment = Alignment.CenterVertically) {
        iconResId?.let {
            Image(
                painter = painterResource(id = it),
                contentDescription = title,
                modifier = Modifier.size(25.dp)
            )
        }
        Column {
            Spacer(modifier = Modifier.width(4.dp))
            Text(title, style = TextStyle(
                fontSize = 13.sp,
                color = Color.LightGray
            ), modifier = Modifier.padding(start = 10.dp, bottom = 5.dp))
            Text(content, style = TextStyle(
                fontSize = 15.sp,
            ), modifier = Modifier.padding(start = 10.dp))
        }
    }
}






