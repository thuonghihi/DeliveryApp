package com.example.deliveryapp.ui.customer

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.fastForEachIndexed
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.wear.compose.material.Chip
import com.example.deliveryapp.R
import com.example.deliveryapp.ui.authentication.LeadingBasicButton
import com.example.deliveryapp.ui.theme.DeliveryAppTheme
import kotlinx.coroutines.Delay
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp


fun AddOrderInfoScreenRoute(senderPoint: String? = ""): String{
    return ""
}

data class RegestrationState(
    var address: String = "",
    var name: String = "",
    var phoneNumber: String = "",
    var note: String = ""
)

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun previewCustomerAddOrderInfo(){
    CustomerAddOrderInformationScreen(navController = rememberNavController())
}

@Composable
fun CustomerAddOrderInformationScreen(navController: NavController){
    DeliveryAppTheme {
        Scaffold(
            topBar = { TopBar(title = "Thêm thông tin đơn hàng", navigationIcon = Icons.Default.ArrowBack){
                navController.popBackStack()
            } }
        ) {innerPadding ->
            Column(
                modifier = Modifier.fillMaxSize().padding(innerPadding).background(color = Color(0xFFF7F7F7)),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                AddOrderInfo()
            }
        }
    }
}

@Composable
fun AddOrderInfo(){
    var formSenderAddInfo by remember { mutableStateOf(RegestrationState()) }
    var formReceiverAddInfo by remember { mutableStateOf(RegestrationState()) }
    var senderCardIsExpanded by remember { mutableStateOf(false) }
    var receiverCardIsExpanded by remember { mutableStateOf(true) }
    var filterChipMassIsSelect by remember { mutableStateOf(-1) }
    var filterChipCategoryIsSelect by remember { mutableStateOf(-1) }
    val scrollState = rememberScrollState()
    val productMassList = listOf("0 - 5kg", "5 - 10kg", "10 - 15kg", "15 - 20kg", "20 - 25kg", "25 - 30kg", "30 - 35kg")
    val productCategoryList = listOf("Thực phẩm", "Dễ vỡ", "Quần áo", "Khác")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
            .verticalScroll(scrollState) // Thêm khả năng cuộn cho Column
    ) {
        PersonInfoCard(
            R.drawable.box_out,
            "Người gửi",
            formSenderAddInfo,
            { formSenderAddInfo = it },
            senderCardIsExpanded
        ) {
            senderCardIsExpanded = !senderCardIsExpanded
        }
        Spacer(modifier = Modifier.height(5.dp))
        PersonInfoCard(
            R.drawable.box_in,
            "Người nhận",
            formReceiverAddInfo,
            { formReceiverAddInfo = it },
            receiverCardIsExpanded
        ) {
            receiverCardIsExpanded = !receiverCardIsExpanded
        }
        Text(
            "Thông tin hàng hóa",
            style = TextStyle(fontWeight = FontWeight.Medium, fontSize = 16.sp),
            modifier = Modifier.padding(top = 10.dp)
        )
        Text(
            "Khối lượng",
            style = TextStyle(fontSize = 16.sp),
            modifier = Modifier.padding(top = 10.dp)
        )

        // Sử dụng Column để tạo lưới sản phẩm
        Column(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
            val rows = productMassList.chunked(3) // Chia danh sách thành các hàng 3 cột
            for (row in rows) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    for (item in row) {
                        FilterChip(
                            modifier = Modifier
                                .padding(horizontal = 5.dp)
                                .weight(1f), // Sử dụng weight để chia đều không gian
                            selected = filterChipMassIsSelect == productMassList.indexOf(item),
                            onClick = { filterChipMassIsSelect = productMassList.indexOf(item) },
                            label = {
                                Text(
                                    item,
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Center
                                )
                            }
                        )
                    }
                }
            }
        }

        Text(
            "Loại hàng hóa",
            style = TextStyle(fontSize = 16.sp),
            modifier = Modifier.padding(top = 10.dp)
        )

        // Sử dụng Column để tạo lưới loại hàng hóa
        Column(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
            val rowsCategory = productCategoryList.chunked(2) // Chia danh sách thành các hàng 2 cột
            for (row in rowsCategory) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    for (item in row) {
                        FilterChip(
                            modifier = Modifier
                                .padding(horizontal = 5.dp)
                                .weight(1f), // Sử dụng weight để chia đều không gian
                            selected = filterChipCategoryIsSelect == productCategoryList.indexOf(item),
                            onClick = { filterChipCategoryIsSelect = productCategoryList.indexOf(item) },
                            label = {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Default.Favorite, contentDescription = null)
                                    Text(item, modifier = Modifier.padding(start = 10.dp))
                                }
                            }
                        )
                    }
                }
            }
        }

        LeadingBasicButton("Tiếp tục") { }
    }
}


@Composable
fun PersonInfoCard(
    icon: Int,
    title: String,
    formInfo: RegestrationState,
    onValueChange: (RegestrationState) -> Unit,
    isExpanded: Boolean,
    onExpandToggle: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onExpandToggle() },
        colors = CardDefaults.cardColors(
            containerColor = Color.LightGray,
            contentColor = Color.Black
        )
    ) {
        Column(
            modifier = Modifier.background(color = Color.White).padding(16.dp)
        ) {
            // Tiêu đề
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(painter = painterResource(icon), "", modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(text = title, style = TextStyle(fontWeight = FontWeight.Medium))
                }
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    painterResource(id = if (isExpanded) R.drawable.expendless else R.drawable.expendmore),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            }
            if (isExpanded) {
                Column(modifier = Modifier.background(color = Color.White)) {
                    TextfieldAddPersonInfo(formInfo.address,
                        { onValueChange(formInfo.copy(address = it)) },
                        "Địa chỉ",
                        true,
                        R.drawable.pen
                    )
                    TextfieldAddPersonInfo(formInfo.name,
                        { onValueChange(formInfo.copy(name = it)) },
                        "Tên",
                        true,
                        R.drawable.notebook
                    )
                    TextfieldAddPersonInfo(formInfo.phoneNumber,
                        { onValueChange(formInfo.copy(phoneNumber = it)) },
                        "Số điện thoại",
                        true,
                        R.drawable.phone
                    )
                    TextfieldAddPersonInfo(formInfo.note,
                        { onValueChange(formInfo.copy(note = it)) },
                        "Ghi chú",
                        false
                    )
                }
            }
        }
    }
}



@Composable
fun TextfieldAddPersonInfo(value: String, onValueChange: (String) -> Unit, placeHolder: String, require: Boolean, trailingIcon: Int? = null){
    val placeHolderCustom = buildAnnotatedString {
        append(placeHolder)
        if (require) {
            withStyle(SpanStyle(color = Color.Red)) {
                append(" *")
            }
        }
    }
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth().padding(top = 10.dp).height(48.dp),
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(placeHolderCustom, style = TextStyle(fontSize = 14.sp))
        },
        trailingIcon = {
            if (trailingIcon != null) {
                Icon(painterResource(id = trailingIcon), contentDescription = "", modifier = Modifier.size(20.dp))
            }
        }
    )
}
