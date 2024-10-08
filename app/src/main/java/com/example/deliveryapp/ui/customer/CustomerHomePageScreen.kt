package com.example.deliveryapp.ui.customer

import android.content.Context
import android.location.Geocoder
import android.util.Log
import androidx.compose.foundation.background
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
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
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
import androidx.compose.foundation.layout.width
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.focus.FocusRequester.Companion.createRefs
import androidx.compose.ui.text.font.FontWeight
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import java.util.Locale

data class orderTracking(
    val id: String,
    val sender: String,
    val receiver: String,
    val expectedTime: String,
    var status: String
)

val orderTrackingItems = listOf(
    orderTracking("123", "Hà Nội", "Quảng Bình", "12/10", "Đã đến kho gửi")
)

@Composable
fun CustomerHomepageScreen(navController: NavController){
    CustomerHomepage(navController = navController)
}



@Preview(showSystemUi = true)
@Composable
fun previewCustomerHomepage(){
    CustomerHomepageScreen(navController = rememberNavController())
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerHomepage(navController: NavController){
    TopBarWithNavigationDrawer(navController = navController, bottomBar = { BottomBar() }) {modifier ->
   //     CustomerHomepageWhitoutOrder(modifier = modifier)
        CustomerHomepageItemWithOrder(orderTrackingItems.get(0), modifier = modifier)
    }

}

@Composable
fun CustomerHomepageWithOrder(modifier: Modifier = Modifier){

}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CustomerHomepageItemWithOrder(orderTrackingItem: orderTracking, modifier: Modifier) {
    Box(
        modifier = modifier
            .padding(20.dp)
            .fillMaxWidth()
    ) {
        ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
            // Define the components
            val (orderId, senderColumn, expectedDeliveryColumn, receiverColumn, statusColumn) = createRefs()
            val guideline = createGuidelineFromStart(0.5f) // Create a guideline in the middle

            // Show order ID
            Text(
                text = "Mã ĐH: ${orderTrackingItem.id}",
                style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Medium),
                    modifier = Modifier.constrainAs(orderId) {
                        top.linkTo(parent.top, margin = 8.dp)
                        start.linkTo(parent.start)
                        bottom.linkTo(senderColumn.top)
                        width = Dimension.fillToConstraints // Fill space until guideline
                    }
            )

            // Sender address column
            AddressColumn(
                iconResId = R.drawable.from_icon,
                title = "Địa chỉ gửi",
                content = orderTrackingItem.sender,
                modifier = Modifier.constrainAs(senderColumn) {
                    top.linkTo(orderId.bottom, margin = 8.dp)
                    start.linkTo(parent.start)
                    end.linkTo(guideline)
                    width = Dimension.fillToConstraints // Fill space until guideline
                }
            )

            // Expected delivery column
            AddressColumn(
                iconResId = R.drawable.to_icon,
                title = "Dự kiến giao thành công",
                content = orderTrackingItem.expectedTime,
                modifier = Modifier.constrainAs(expectedDeliveryColumn) {
                    top.linkTo(orderId.bottom, margin = 8.dp)
                    start.linkTo(guideline) // Start from guideline
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints // Fill space until guideline
                }
            )

            // Receiver address column
            AddressColumn(
                iconResId = R.drawable.to_icon,
                title = "Địa chỉ nhận",
                content = orderTrackingItem.receiver,
                modifier = Modifier.constrainAs(receiverColumn) {
                    top.linkTo(senderColumn.bottom, margin = 16.dp)
                    start.linkTo(parent.start)
                    end.linkTo(guideline)
                    width = Dimension.fillToConstraints // Fill space until guideline
                }
            )

            // Status column
            AddressColumn(
                iconResId = R.drawable.to_icon,
                title = "Trạng thái",
                content = orderTrackingItem.status,
                modifier = Modifier.constrainAs(statusColumn) {
                    top.linkTo(expectedDeliveryColumn.bottom, margin = 16.dp)
                    start.linkTo(guideline) // Start from guideline
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints // Fill space until guideline
                }
            )
        }
    }
}

@Composable
fun AddressColumn(
    modifier: Modifier = Modifier, // Ensure this parameter is included
    iconResId: Int,
    title: String,
    content: String
) {
    Column(modifier = modifier) {
        Row {
            Icon(painter = painterResource(iconResId), contentDescription = title)
            Spacer(modifier = Modifier.width(4.dp)) // Add spacing between icon and text
      //      Text(title, style = )
        }
        Text(content)
    }
}


@Composable
fun CustomerHomepageWhitoutOrder(modifier: Modifier){
    Box{
        MapScreen()
        AddAddress(modifier = modifier)
    }
}


@Composable
fun AddAddress(modifier: Modifier = Modifier){
    var receiptPointText by remember {
        mutableStateOf("")
    }
    var deliveryPointText by remember {
        mutableStateOf("")
    }
    Card(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        modifier = modifier.fillMaxWidth()
            .clip(shape = RoundedCornerShape(10.dp))
            .padding(20.dp)


    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            AddAddressItem(receiptPointText, R.drawable.from_icon, "Điểm lấy hàng") {
                receiptPointText = it
            }
            Spacer(modifier = Modifier.fillMaxWidth()
                .padding(vertical = 10.dp)
                .padding(start = 50.dp)
                .height(1.dp).
                background(color = Color.LightGray))
            AddAddressItem(deliveryPointText, R.drawable.to_icon, "Điểm nhận hàng") {
                deliveryPointText = it
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
fun AddAddressItem(text: String, icon: Int, label: String, onValueChange: (String) -> Unit){
    var isFocus by rememberSaveable {
        mutableStateOf(false)
    }

    var sender by remember {
        mutableStateOf("")
    }

    OutlinedTextField(value = text,
        onValueChange = onValueChange,
        label = {
            Text(label,
                style = TextStyle(fontSize = 15.sp)
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .onFocusChanged {
                isFocus = it.isFocused
            },
        textStyle = TextStyle(fontSize = 18.sp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent,
            errorBorderColor = Color.Transparent
        ),
        leadingIcon = {
            Icon(painter = painterResource(id = icon),
                contentDescription = "",
                modifier = Modifier.size(20.dp))
        },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done
        )
    )
}



