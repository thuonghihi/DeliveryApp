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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
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
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.text.font.FontWeight
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.maps.android.compose.CameraPositionState
import kotlinx.coroutines.launch
import java.util.Locale

fun CustomerHomePageScreenRoute(customerID: String): String{
    return "customerHomepage/$customerID"
}

data class orderTracking(
    val id: String,
    val sender: String,
    val receiver: String,
    val expectedTime: String,
    var status: String
)

val orderTrackingItems = listOf(
    orderTracking("123", "Hà Nội", "Quảng Bình", "12/10", "Đã đến kho gửi"),
    orderTracking("1234", "Hà Nội", "Quảng Bình", "12/10", "Đã đến kho gửi")
)

@Composable
fun CustomerHomepageScreen(navController: NavController, customerID: String){
    CustomerHomepage(navController = navController, customerID = customerID)
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun previewCustomerHomepage(){
    CustomerHomepageScreen(navController = rememberNavController(), customerID = "12")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerHomepage(navController: NavController, customerID: String){
    var hasOrderTracking by remember {
        mutableStateOf(true)
    }
    TopBarWithNavigationDrawer(navController = navController, customerID = customerID, bottomBar = { BottomBar() }) { modifier ->
        if(hasOrderTracking) CustomerHomepageWithOrder(navController = navController, customerID = customerID, modifier = modifier)
        else CustomerHomepageWhitoutOrder(navController = navController, modifier = modifier)
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerHomepageWithOrder(navController: NavController, customerID: String, modifier: Modifier = Modifier) {
    var isBottomSheetVisible by remember { mutableStateOf(false) }
    var selectedItem by remember { mutableStateOf<orderTracking?>(null) } // Thêm biến để giữ đối tượng được chọn
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    Column(
        verticalArrangement = Arrangement.Top,
        modifier = modifier.fillMaxSize()
    ) {
        AddAddress(navController = navController, modifier = Modifier)
        LazyColumn(
            modifier = Modifier.fillMaxWidth().padding(20.dp).weight(1f),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(orderTrackingItems) { item ->
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
fun CustomerHomepageItemWithOrder(orderTrackingItem: orderTracking, modifier: Modifier = Modifier, onClick: () -> Unit) {
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
                text = "Mã ĐH: ${orderTrackingItem.id}",
                style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Medium),
                modifier = Modifier.padding(vertical = 15.dp)
            )
        }

        Divider(color = Color.LightGray, thickness = 0.3.dp)

        // Row chứa địa chỉ gửi và dự kiến giao
        Row(modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp)) {
            OrderTrackingItemColumn(
                iconResId = R.drawable.box_out,
                title = "Địa chỉ gửi",
                content = orderTrackingItem.sender,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(16.dp))
            OrderTrackingItemColumn(
                iconResId = R.drawable.date_icon,
                title = "Dự kiến giao",
                content = orderTrackingItem.expectedTime,
                modifier = Modifier.weight(1f)
            )
        }

        // Row chứa địa chỉ nhận và trạng thái
        Row(modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp)) {
            OrderTrackingItemColumn(
                iconResId = R.drawable.box_in,
                title = "Địa chỉ nhận",
                content = orderTrackingItem.receiver,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(16.dp))
            OrderTrackingItemColumn(
                iconResId = R.drawable.status_icon,
                title = "Trạng thái",
                content = orderTrackingItem.status,
                modifier = Modifier.weight(1f).padding(bottom = 5.dp)
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
                color = Color.LightGray
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
        MapScreen(modifier = modifier)
        AddAddress(navController = navController, modifier = modifier)
    }
}


@Composable
fun AddAddress(navController: NavController, modifier: Modifier = Modifier, senderPointText: String? = "", receiverPointText: String? = ""){
    Card(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        modifier = modifier.fillMaxWidth()
            .clip(shape = RoundedCornerShape(10.dp))
            .padding(10.dp)


    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            AddAddressItem(if(senderPointText.isNullOrEmpty()) "" else senderPointText, R.drawable.from_icon, "Điểm lấy hàng") {
                Log.d("send", "takeee")
                navController.navigate(AddSenderPointScreenRoute())
            }

            Spacer(modifier = Modifier.fillMaxWidth()
                .padding(start = 50.dp, top = 5.dp)
                .height(1.dp)
                .background(color = Color.LightGray))
            AddAddressItem(if(receiverPointText.isNullOrEmpty()) "" else receiverPointText, R.drawable.to_icon, "Điểm nhận hàng") {
                navController.navigate(AddReceiverPointScreenRoute() )
            }
        }
    }
}


fun getAddressFromLocation(
    fusedLocationClient: FusedLocationProviderClient,
    cameraPositionState: CameraPositionState,
    context: Context,
    onAddressReceived: (String?) -> Unit
) {
    getCurrentLocation(fusedLocationClient, cameraPositionState) { latLng ->
        val geocoder = Geocoder(context, Locale.getDefault())
        try {
            val addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
            val address = addresses?.firstOrNull()?.getAddressLine(0)
            onAddressReceived(address)
        } catch (e: Exception) {
            Log.e("MapScreen", "Unable to get address: ${e.message}")
            onAddressReceived(null)
        }
    }
}

@Composable
fun AddAddressItem(text: String, icon: Int, placeHolder: String, onClick: () -> Unit){
    TextField(value = text,
        readOnly = true,
        onValueChange = {},
        textStyle = TextStyle(fontSize = 18.sp),
        modifier = Modifier.fillMaxWidth()
            .clickable {
                onClick()
            }
            .padding(top = 5.dp),
        colors = OutlinedTextFieldDefaults.colors(
            disabledTextColor = Color.Black,
            disabledBorderColor = Color.Transparent,
            disabledPlaceholderColor = Color.Gray
        ),
        placeholder = {
            Text(placeHolder)
        },
        enabled = false,
        leadingIcon = {
            Image(painter = painterResource(id = icon),
                contentDescription = "",
                modifier = Modifier.size(20.dp))
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderTrackingDetailSheet(bottomSheetState: SheetState, customerID: String, orderTrackingItem: orderTracking, hideSheet: () -> Unit){
    val scope = rememberCoroutineScope()
    ModalBottomSheet(
        sheetState = bottomSheetState,
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
fun BottomSheetContent(orderTrackingItem: orderTracking, customerID: String){
    Column(
        modifier = Modifier.fillMaxSize().padding(14.dp)
    ) {
        Text("Theo dõi đơn hàng",
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium)
        Text(
            text = "Mã ĐH: ${orderTrackingItem.id}",
            style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Medium),
            modifier = Modifier.padding(vertical = 15.dp)
        )

        Column(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(30.dp)).weight(1f).padding(20.dp)) {
            MapScreen(modifier = Modifier.fillMaxWidth().fillMaxSize())
        }
        Row(horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 20.dp)) {
            OrderTrackingDetailItemColumn(
                iconResId = R.drawable.box_out,
                title = "Địa chỉ gửi",
                content = orderTrackingItem.sender
            )
            //    Spacer(modifier = Modifier.weight(1f))
            OrderTrackingDetailItemColumn(
                iconResId = R.drawable.box_in,
                title = "Địa chỉ nhận",
                content = orderTrackingItem.receiver
            )
        }
        Spacer(modifier = Modifier.fillMaxWidth().height(1.dp).background(color = Color.LightGray))
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            OrderTrackingDetailItemColumn(
                title = "Khách hàng",
                content = customerID
            )
            OrderTrackingDetailItemColumn(
                title = "Trọng lượng",
                content = "1.5kg"
            )
            OrderTrackingDetailItemColumn(
                title = "Chi phí",
                content = "123"
            )
        }
        Spacer(modifier = Modifier.fillMaxWidth().height(1.dp).background(color = Color.LightGray))

        Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Image(painter = painterResource(R.drawable.shipper_welcome), "",
                    modifier = Modifier.size(35.dp))
                Column {
                    Text("Duan ml",
                        style = TextStyle(fontSize = 18.sp))
                    Text("Tài xế",
                        style = TextStyle(fontSize = 14.sp,
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
            Spacer(modifier = Modifier.width(4.dp)) // Add spacing between icon and text
            Text(title, style = TextStyle(
                fontSize = 13.sp,
                color = Color.LightGray
            ), modifier = Modifier.padding(start = 10.dp, bottom = 5.dp))
            Text(content, style = TextStyle(
                fontSize = 16.sp,
            ), modifier = Modifier.padding(start = 10.dp))
        }
    }
}






