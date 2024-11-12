package com.example.deliveryapp.ui.customer

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.runtime.derivedStateOf
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
import com.example.deliveryapp.ui.authentication.LeadingBasicButton
import com.example.deliveryapp.ui.theme.DeliveryAppTheme
import kotlinx.coroutines.Delay
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.deliveryapp.R
import com.example.deliveryapp.data.models.Location
import com.example.deliveryapp.data.models.Order
import com.example.deliveryapp.data.repositories.CustomerRepository
import com.example.deliveryapp.data.repositories.OrderRepository
import com.example.deliveryapp.ui.authentication.Login
import com.example.deliveryapp.ui.authentication.textFiledAuthentication
import com.example.deliveryapp.viewmodels.CustomerViewModel
import com.example.deliveryapp.viewmodels.OrderViewModel
import com.google.gson.Gson
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.random.Random

fun AddOrderInfoScreenRoute(): String{
    return "addOrderInfo"
}

data class RegestrationState(
    var address: String = "",
    var name: String = "",
    var phoneNumber: String = "",
    var note: String = "" )
{
    fun isValid(): Boolean {
        return listOf(address, name, phoneNumber, note).all { it.isNotEmpty() }
    }
}

@Composable
fun CustomerAddOrderInformationScreen(navController: NavController, customerID: String){
    Scaffold(
        topBar = { TopBar(title = "Thêm thông tin đơn hàng", navigationIcon = Icons.Default.ArrowBack){
            navController.popBackStack()
        }
    }) {innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(innerPadding),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            AddOrderInfo(navController = navController, customerID = customerID)
        }
    }
}

@Composable
fun AddOrderInfo(navController: NavController, customerID: String){
    var formSenderAddInfo by remember { mutableStateOf(RegestrationState()) }
    var formReceiverAddInfo by remember { mutableStateOf(RegestrationState()) }
    var pickupCardIsExpanded by remember { mutableStateOf(true) }
    var deliveryCardIsExpanded by remember { mutableStateOf(true) }
    var filterChipMassIsSelect by remember { mutableStateOf(-1) }
    var filterChipCategoryIsSelect by remember { mutableStateOf(-1) }
    val scrollState = rememberScrollState()
    val customerRepository = CustomerRepository()
    val customerViewModel = CustomerViewModel(customerRepository)
    customerViewModel.getCustomerById(customerID)
    val productMassList = listOf("0 - 5kg", "5 - 10kg", "10 - 15kg", "15 - 20kg", "20 - 25kg", "25 - 30kg", "30 - 35kg")
    val productCategoryList = listOf("Thực phẩm", "Dễ vỡ", "Quần áo", "Khác")
    val existingStrings = mutableSetOf<String>()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.primary)
            .verticalScroll(scrollState) // Thêm khả năng cuộn cho Column
    ) {
        PersonInfoCard(
            R.drawable.box_out,
            "Người gửi",
            formSenderAddInfo,
            { formSenderAddInfo = it },
            pickupCardIsExpanded
        ) {
            pickupCardIsExpanded = !pickupCardIsExpanded
        }
        Spacer(modifier = Modifier.height(5.dp).fillMaxWidth().background(color = Color.LightGray))
        PersonInfoCard(
            R.drawable.box_in,
            "Người nhận",
            formReceiverAddInfo,
            { formReceiverAddInfo = it },
            deliveryCardIsExpanded
        ) {
            deliveryCardIsExpanded = !deliveryCardIsExpanded
        }
        Spacer(modifier = Modifier.height(5.dp).fillMaxWidth().background(color = Color.LightGray))
        Column(
            modifier = Modifier.fillMaxWidth().padding(12.dp)
        ) {
            Text(
                "Thông tin hàng hóa",
                style = TextStyle(fontWeight = FontWeight.Medium, fontSize = 16.sp),
                modifier = Modifier.padding(top = 10.dp)
            )
            Text(
                "Khối lượng",
                style = TextStyle(fontSize = 15.sp),
                modifier = Modifier.padding(top = 10.dp)
            )

            // Sử dụng Column để tạo lưới sản phẩm
            Column(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                val rows = productMassList.chunked(3) // Chia danh sách thành các hàng 3 cột
                for (row in rows) {
                    Row(modifier = Modifier.fillMaxWidth()
                        ) {
                        for (item in row) {
                            FilterChip(
                                modifier = Modifier
                                    .padding(horizontal = 5.dp)
                                    .weight(1f),
                                selected = filterChipMassIsSelect == productMassList.indexOf(item),
                                onClick = { filterChipMassIsSelect = productMassList.indexOf(item) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = Color(0xFFFC8D03),
                                    selectedLabelColor = Color.White
                                ),
                                label = {
                                    Text(
                                        item,
                                        modifier = Modifier.fillMaxWidth(),
                                        style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Normal),
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
                style = TextStyle(fontSize = 15.sp),
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
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = Color(0xFFFC8D03),
                                    selectedLabelColor = Color.White
                                ),
                                label = {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.Center,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(Icons.Default.Favorite, contentDescription = null)
                                        Text(item, modifier = Modifier.padding(start = 10.dp),
                                            style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Normal))
                                    }
                                }
                            )
                        }
                    }
                }
            }

            var orderNote by remember { mutableStateOf("") }

            Text(
                "Ghi chú đơn hàng",
                style = TextStyle(fontSize = 15.sp),
                modifier = Modifier.padding(top = 10.dp)
            )
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth().padding(top = 10.dp).height(48.dp),
                value = orderNote,
                onValueChange = { orderNote = it },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.tertiary,
                    cursorColor = MaterialTheme.colorScheme.onSecondary
                ),
                placeholder = {
                    Text("Thêm ghi chú đơn hàng", style = TextStyle(fontSize = 14.sp))
                }
            )

            val enablebtn by remember {
                derivedStateOf {
                    formSenderAddInfo.isValid() &&
                    formReceiverAddInfo.isValid() &&
                    filterChipMassIsSelect >= 0 &&
                    filterChipCategoryIsSelect >= 0
                }
            }
            LeadingBasicButton("Tiếp tục", enablebtn) {
                var order = Order(
                    orderIdShow = generateUniqueRandomString(existingStrings),
                    pickupAddress = formSenderAddInfo.address,
                    pickupLocation = Location(1.0, 1.0),
                    deliveryLocation = Location(1.1, 1.0),
                    deliveryAddress = formReceiverAddInfo.address,
                    customer = customerID,
                    driver = "",
                    status = "temporary",
                    timeStamp = System.currentTimeMillis().toString(),
                    voucher = "",
                    totalAmount = "",
                    paymentMethod = "",
                    weight = productMassList.get(filterChipMassIsSelect).toString(),
                    senderPhone = formSenderAddInfo.phoneNumber.toString(),
                    receiverPhone = formReceiverAddInfo.phoneNumber.toString(),
                    senderName = formSenderAddInfo.name.toString(),
                    receiverName = formReceiverAddInfo.name.toString(),
                    paymentRecipient = false,
                    note = orderNote
                )
                navController.navigate(CheckOrderScreenRoute(order))

            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonInfoCard(
    icon: Int,
    title: String,
    formInfo: RegestrationState,
    onValueChange: (RegestrationState) -> Unit,
    isExpanded: Boolean,
    onExpandToggle: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val suggestions = remember { mutableStateListOf<String>() } // Danh sách gợi ý

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
            modifier = Modifier
                .background(color = Color.White)
                .padding(16.dp)
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
                var dropmenuExpanded by remember { mutableStateOf(false) } // Trạng thái cho dropdown
                Column(modifier = Modifier.background(color = Color.White)) {
                    // Dropdown cho địa chỉ
                    ExposedDropdownMenuBox(
                        expanded = dropmenuExpanded,
                        onExpandedChange = { dropmenuExpanded = !dropmenuExpanded }
                    ) {
                        TextfieldAddPersonInfo(
                            value = formInfo.address,
                            placeholder = "Địa chỉ",
                            require = true,
                            trailingIcon = R.drawable.address,
                            modifier = Modifier
                                .menuAnchor(),
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                            onValueChange = { newAddress ->
                                onValueChange(formInfo.copy(address = newAddress))
                                fetchPlaceAutocompleteSuggestions(
                                    scope = coroutineScope,
                                    query = newAddress
                                ) { fetchedSuggestions ->
                                    suggestions.clear()
                                    suggestions.addAll(fetchedSuggestions)
                                }
                            }
                        )

                        if (suggestions.isNotEmpty()) {
                            ExposedDropdownMenu(
                                expanded = dropmenuExpanded,
                                onDismissRequest = { dropmenuExpanded = false },
                                modifier = Modifier.offset(x = 4.dp, y = 4.dp)
                            ) {
                                suggestions.forEach { suggestion ->
                                    DropdownMenuItem(
                                        text = { Text(text = suggestion) },
                                        onClick = {
                                            onValueChange(formInfo.copy(address = suggestion))
                                            dropmenuExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                    TextfieldAddPersonInfo(formInfo.name,
                        "Tên",
                        true,
                        R.drawable.notebook,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)) {
                        onValueChange(formInfo.copy(name = it))
                    }
                    TextfieldAddPersonInfo(formInfo.phoneNumber,
                        "Số điện thoại",
                        true,
                        R.drawable.phone,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done, keyboardType = KeyboardType.Number)) {
                        onValueChange(formInfo.copy(phoneNumber = it))
                    }
                    TextfieldAddPersonInfo(formInfo.note,
                        "Ghi chú",
                        false,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)) {
                        onValueChange(formInfo.copy(note = it))
                    }
                }
            }
        }
    }
}


@Composable
fun TextfieldAddPersonInfo(
    value: String,
    placeholder: String,
    require: Boolean,
    trailingIcon: Int? = null,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions,
    onValueChange: (String) -> Unit
) {
    val placeHolderCustom = buildAnnotatedString {
        append(placeholder)
        if (require) {
            withStyle(SpanStyle(color = Color.Red)) {
                append(" *")
            }
        }
    }

    OutlinedTextField(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 10.dp)
            .height(48.dp),
        value = value,
        textStyle = TextStyle(fontSize = 14.sp, color = MaterialTheme.colorScheme.onPrimary),
        onValueChange = {
            onValueChange(it)
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.tertiary,
            cursorColor = MaterialTheme.colorScheme.onSecondary
        ),
        placeholder = {
            Text(placeHolderCustom, style = TextStyle(fontSize = 14.sp))
        },
        trailingIcon = trailingIcon?.let {
            {
                Icon(
                    painterResource(it),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            }
        },
        keyboardOptions = keyboardOptions
    )
}

fun generateUniqueRandomString(existingStrings: MutableSet<String>): String {
    val characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
    var newString: String

    do {
        newString = (1..8)
            .map { characters[Random.nextInt(characters.length)] }
            .joinToString("")
    } while (existingStrings.contains(newString))

    existingStrings.add(newString) // Lưu chuỗi đã tạo vào tập hợp để tránh trùng
    return "GO"+newString
}





