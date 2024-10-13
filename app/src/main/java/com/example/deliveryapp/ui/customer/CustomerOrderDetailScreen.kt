package com.example.deliveryapp.ui.customer//package com.example.deliveryapp.ui.customer

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
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.deliveryapp.R

//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun OrderTrackingDetail(hideSheet: () -> Unit){
//    val sheetState = rememberModalBottomSheetState()
//    val scope = rememberCoroutineScope()
//    ModalBottomSheet(
//        sheetState = sheetState,
//        onDismissRequest = {
//            scope.launch {
//                sheetState.hide()
//            }.invokeOnCompletion {
//                hideSheet()
//            }
//        }
//    ) {
//
//    }
//}

@Composable
fun CustomerOrderTrackingDetailScreen(navController: NavController){
    BottomSheetContent(orderTrackingItems.get(0), "thuong")
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewBottomSheet(){
    BottomSheetContent(orderTrackingItems.get(0), "thuong")
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
//        MapScreen(modifier = Modifier.fillMaxWidth().height(350.dp).clip(RoundedCornerShape(10.dp)).padding(20.dp))
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
//            Spacer(modifier = Modifier.weight(1f))
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