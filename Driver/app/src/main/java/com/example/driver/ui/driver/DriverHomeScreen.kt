package com.example.driver.ui.driver

import android.text.Layout
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchColors
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.example.driver.R
import com.example.driver.data.models.Driver
import com.example.driver.data.models.Location
import com.example.driver.data.models.Order
import com.example.driver.data.repositories.DriverRepository
import com.example.driver.data.repositories.OrderRepository
import com.example.driver.ui.authentication.LeadingBasicButton
import com.example.driver.ui.authentication.LoginScreen
import com.example.driver.viewmodel.DriverViewModel
import com.example.driver.viewmodel.OrderViewModel
import com.mapbox.maps.extension.style.expressions.dsl.generated.pi
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DriverHomeScreen(driverId: String){
    val driverRepository = DriverRepository()
    val driverViewModel = DriverViewModel(driverRepository)
    val orderRepository = OrderRepository()
    val orderViewModel = OrderViewModel(orderRepository)
    val active by driverViewModel.status.observeAsState()
    val auto by driverViewModel.auto.observeAsState()
    val orders by orderViewModel.orders.observeAsState()
//    driverViewModel.getStatus(driverId)
//    driverViewModel.getAuto(driverId)
    var checkStatus by remember { mutableStateOf(true) }
    var checkAuto by remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()
    var openBottomSheet by remember { mutableStateOf(false) }
    var openDialog by remember { mutableStateOf(false) }

    LaunchedEffect (Unit) {
        orderViewModel.fetchOrders(driverId, "assigned")
    }
    LaunchedEffect (checkStatus) {
        driverViewModel.getStatus(driverId)
    }
    LaunchedEffect (checkAuto) {
        driverViewModel.getAuto(driverId)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        MapScreen(LocalContext.current)
        active?.let {
            if(!it){
                Text(
                    text = "Chưa bật hoạt động",
                    style = TextStyle(fontSize = 14.sp, textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.primary),
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .fillMaxWidth()
                        .background(color = Color(0xFFF14852))
                        .padding(10.dp)
                )
            }
            else {}
        }
        Button(
            onClick = {
                openDialog = true
            },
            modifier = Modifier.align(Alignment.TopEnd)
                .padding(top = if (active == true) 5.dp else 40.dp, end = 8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF2B2D30),
                contentColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Row {
                Icon(painter = painterResource(R.drawable.shutdown_icon), "", modifier = Modifier.size(20.dp))
                Text(" Bật/Tắt")
            }
        }
        Log.d("Order12345678", orders.toString())
        if (orders != null) {
            Log.d("order12345678", orders!!.first().toString())
            LaunchedEffect (Unit) {
                openBottomSheet = true
            }
            Log.d("order123456789", "gọi bottom sheet")
            Log.d("bottomsshett", openBottomSheet.toString())
            if (openBottomSheet) {
                BottomSheetHasOrder(
                    title = "Đến điểm lấy hàng",
                    work ="Đã đến điểm lấy hàng",
                    order = orders!!.first(),
                    onDismiss = { openBottomSheet = false }) {}
            }
            Log.d("bottomsshett123", openBottomSheet.toString())
        }

        // Show button to open the bottom sheet if it's not open
//        if (!openBottomSheet) {
//            LeadingBasicButton("Xem thông tin đơn hàng") {
//                scope.launch {
//                    sheetState.show()
//                }
//                openBottomSheet = true
//            }
//        }
        if (openDialog) {
            active?.let {
                DialogOnline(
                    onDismiss = { openDialog = false },
                    active = it,
                    auto = auto!!,
                    onlineChecked = {
                        scope.launch {
                            driverViewModel.changeStatus(driverId, !it)
                            checkStatus = !checkStatus
                            if(it == false) driverViewModel.getAuto(driverId)
                        }
                    },
                    autoChecked = {
                        scope.launch {
                            driverViewModel.changeAuto(driverId, !auto!!)
                            checkAuto = !checkAuto
                        }
                    }
                )
            }
        }
    }
}


@Composable
fun DialogOnline(onDismiss: () -> Unit, active: Boolean, auto: Boolean, onlineChecked: () -> Unit, autoChecked: () -> Unit) {
    Dialog(
        onDismissRequest = { onDismiss() },
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(8.dp))
                .padding(vertical = 5.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 15.dp, vertical = 5.dp)
            ) {
                Row (
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Image(painter = painterResource(if(active) R.drawable.dot_green else R.drawable.dot_red), "",
                        modifier = Modifier.size(10.dp))
                    Text(
                        text = if(active) "Đang hoạt động" else "Chưa bật hoạt động",
                        modifier = Modifier.padding(start = 10.dp),
                        style = TextStyle(fontSize = 15.sp, color = MaterialTheme.colorScheme.onPrimary)
                    )
                }
                Switch(
                    checked = active,
                    onCheckedChange = { onlineChecked()},
                    modifier = Modifier.scale(0.8f),
                    colors = SwitchDefaults.colors(
                        uncheckedTrackColor = Color.LightGray,
                        checkedTrackColor = Color(0xFF67A26C),
                        uncheckedThumbColor = Color.Gray,
                        checkedThumbColor = MaterialTheme.colorScheme.primary,
                        checkedBorderColor = Color.Unspecified,
                        uncheckedBorderColor = Color.Unspecified
                    )
                )
            }

            Spacer(modifier = Modifier.fillMaxWidth().height(7.dp).background(Color.LightGray))

            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 15.dp, vertical = 5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth().weight(1f).padding(vertical = 10.dp)
                ) {
                    Text(
                        text = "Tự động nhận yêu cầu",
                        style = TextStyle(
                            fontSize = 15.sp,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    )
                    if (!active) {
                        Text(
                            text = "Cần bật hoạt động để sử dụng tính năng",
                            color = Color.Red,
                            style = TextStyle(fontSize = 13.sp),
                            modifier = Modifier.padding(top = 5.dp)
                        )
                    }
                }
                Switch(
                    checked = auto,
                    onCheckedChange = { autoChecked()},
                    modifier = Modifier.scale(0.8f),
                    enabled = active,
                    colors = SwitchDefaults.colors(
                        uncheckedTrackColor = Color.LightGray,
                        checkedTrackColor = Color(0xFF67A26C),
                        uncheckedThumbColor = Color.Gray,
                        checkedThumbColor = MaterialTheme.colorScheme.primary,
                        checkedBorderColor = Color.Unspecified,
                        uncheckedBorderColor = Color.Unspecified
                    )
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetHasOrder(title: String,
                        work: String,
                        order: Order,
                        onDismiss: () -> Unit,
                        onClick: () -> Unit){
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState()
    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = {
            scope.launch {
                sheetState.hide()
            }.invokeOnCompletion {
                onDismiss()
            }
        },
        dragHandle = { BottomSheetDefaults.DragHandle(width = 0.dp, height = 0.dp) }
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(title, style = TextStyle(color = Color(0xFFFC8D03), fontWeight = FontWeight.Medium))
            Spacer(Modifier.fillMaxWidth().padding(vertical = 15.dp).height(0.8.dp).background(Color.LightGray))
            Row (Modifier.fillMaxWidth().padding(top = 10.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.Top) {
                Image(painter = painterResource(R.drawable.location_icon), " ", Modifier.padding(end = 15.dp).size(18.dp))
                Column {
                    Text(if (work == "Đã đến điểm lấy hàng") order.pickupAddress ?: "" else order.deliveryAddress ?: "",
                        style = TextStyle(fontWeight = FontWeight.Medium, fontSize = 17.sp)
                    )
                    if (work == "Xác nhận giao hàng") Text(order.note ?: "")
                }
            }
            Row (Modifier.fillMaxWidth().padding(top = 28.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.Top) {
                Image(painter = painterResource(R.drawable.user_icon), " ", Modifier.padding(end = 15.dp).size(18.dp))
                Column {
                    Text(
                        if (work == "Đã đến điểm lấy hàng") order.senderName
                            ?: "" else order.receiverName ?: "",
                        style = TextStyle(fontWeight = FontWeight.Medium), fontSize = 17.sp)
                    Text(
                        if (work == "Đến điểm lấy hàng") {
                            if (order.paymentRecipient == true) "Thu người gửi: 0đ"
                            else "Thu người gửi: ${order.totalAmount}"
                        } else {
                            if (order.paymentRecipient == false) "Thu người nhận: 0đ"
                            else "Thu người nhận: ${order.totalAmount}"
                        }, style = TextStyle(Color.Gray),
                        modifier = Modifier.padding(top = 5.dp)
                    )
                }
            }
            Spacer(Modifier.fillMaxWidth().padding(top = 15.dp).height(0.5.dp).background(Color.LightGray))
            Row (Modifier.fillMaxWidth().padding(top = 15.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically)
            {
                ActionInBottomSheet(R.drawable.phone_icon, "Gọi điện thoại")
                Spacer(Modifier.width(1.dp).height(22.dp).background(Color.LightGray))
                ActionInBottomSheet(R.drawable.more_icon, "Xem chi tiết")
            }
            LeadingBasicButton(work) { onClick }
        }
    }
}

//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//fun BottmSheetPreview() {
//    BottomSheetHasOrder("Đến điểm lấy hàng", "Đã đến điểm lấy hàng", order)
//}