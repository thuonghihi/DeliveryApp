package com.example.driver.ui.driver

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
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
import com.example.driver.R
import com.example.driver.data.models.Driver
import com.example.driver.data.repositories.DriverRepository
import com.example.driver.ui.authentication.Login
import com.example.driver.viewmodel.DriverViewModel
import com.mapbox.maps.extension.style.expressions.dsl.generated.mod

//@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DriverProfileScreen(driverId: String) {
    val driverRepository = DriverRepository()
    val driverViewModel = DriverViewModel(driverRepository)
    LaunchedEffect (Unit) {
        driverViewModel.getDriverById(driverId)
    }
    val driver by driverViewModel.driver.observeAsState()
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(painterResource(id = R.drawable.shipper_welcome),
            contentDescription = "",
            modifier = Modifier
                .fillMaxWidth()
                .size(100.dp)
                .clip(shape = CircleShape))

        Spacer(modifier = Modifier.height(5.dp))
        Text(text = driver?.name?.let {
                if (it.isNotEmpty()) it else ""
            } ?: "",
            style = TextStyle(fontSize = 20.sp,
                fontWeight = FontWeight.Medium),
            modifier = Modifier.padding(top = 5.dp, bottom = 7.dp)
        )
        Row (
            modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ){
            Image(painterResource(R.drawable.star_icon), "", modifier = Modifier.size(20.dp))
            Text(text = driver?.rating?.let {
                    if (it.toString().isNotEmpty()) it.toString() else ""
                } ?: "",
                style = TextStyle(fontSize = 16.sp,
                    fontWeight = FontWeight.Medium))
        }
        Spacer(modifier = Modifier.fillMaxWidth().height(8.dp).background(Color.LightGray))
        ProfileItem("Thông tin tài khoản"){}
        Spacer(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color.LightGray))
        ProfileItem("Lịch sử đơn hàng"){}
        Spacer(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color.LightGray))
        ProfileItem("Đánh giá từ khách hàng"){}
        Spacer(modifier = Modifier.fillMaxWidth().height(8.dp).background(Color.LightGray))
        Text(
            text = "Đăng xuất",
            modifier = Modifier.fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 20.dp),
            style = TextStyle(fontSize = 17.sp, color = Color(0xFFD64D5B))
        )
        Box(
            modifier = Modifier.fillMaxSize().background(Color.LightGray)
        )
    }

}

@Composable
fun ProfileItem(title: String, onClick: () -> Unit){
    Row (modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween) {
        Text(
            text = title,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 20.dp),
            style = TextStyle(fontSize = 17.sp)
        )
        Image(
            painter = painterResource(R.drawable.direction_icon),
            "",
            modifier = Modifier.size(30.dp).padding(end = 13.dp)
        )
    }
}

