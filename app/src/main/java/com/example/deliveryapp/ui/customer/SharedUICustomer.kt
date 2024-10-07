package com.example.deliveryapp.ui.customer

import android.util.Log
import android.widget.ImageButton
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.deliveryapp.R
import kotlinx.coroutines.launch

data class NavDrawerItem(
    val title: String,
    val selectedIcon: Int,
    val unselectedIcon: Int,
    val route: String,
    val group: String
)

data class NavigationBottomItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    var hasNews: Boolean
)

val navDrawerItems = listOf(
    NavDrawerItem(
        title = "Trang chủ",
        selectedIcon = R.drawable.home_filled,
        unselectedIcon = R.drawable.home_outlined,
        route = "customerHomepage",
        group = "Trang chủ"
    ),
    NavDrawerItem(
        title = "Thông tin tài khoản",
        selectedIcon = R.drawable.account_filled,
        unselectedIcon = R.drawable.account_outlined,
        route = "",
        group = "Cài đặt"
    ),
    NavDrawerItem(
        title = "Chọn ngôn ngữ",
        selectedIcon = R.drawable.globe_filled,
        unselectedIcon = R.drawable.globe_outlined,
        route = "",
        group = "Cài đặt"
    ),
    NavDrawerItem(
        title = "Cập nhật mật khẩu",
        selectedIcon = R.drawable.lock_filled,
        unselectedIcon = R.drawable.lock_outlined,
        route = "otpScreen",
        group = "Cài đặt"
    ),NavDrawerItem(
        title = "Hỗ trợ khách hàng",
        selectedIcon = R.drawable.call_filled,
        unselectedIcon = R.drawable.call_outlined,
        route = "",
        group = "Chính sách"
    ),
    NavDrawerItem(
        title = "Điều khoản sử dụng",
        selectedIcon = R.drawable.note_filled,
        unselectedIcon = R.drawable.note_outlined,
        route = "",
        group = "Chính sách"
    )

)

val itemBottoms = listOf(
    NavigationBottomItem(
        title = "Trang chủ",
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home,
        hasNews = false
    ),
    NavigationBottomItem(
        title = "Hoạt động",
        selectedIcon = Icons.Filled.DateRange,
        unselectedIcon = Icons.Outlined.DateRange,
        hasNews = true
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarWithNavigationDrawer(navController: NavController, bottomBar: @Composable () -> Unit? = {}, content: @Composable () -> Unit?){
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    var selectedItemIndex by rememberSaveable {
        mutableStateOf(-1)
    }
    val scope = rememberCoroutineScope()
    ModalNavigationDrawer(drawerContent = {
        ModalDrawerSheet {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(painterResource(id = R.drawable.shipper_welcome),
                    contentDescription = "",
                    modifier = Modifier
                        .clickable {

                        }
                        .fillMaxWidth()
                        .size(120.dp)
                        .clip(shape = CircleShape))

                Spacer(modifier = Modifier.height(10.dp))
                Text(text = "Chào Thương",
                    style = TextStyle(fontSize = 20.sp,
                        fontWeight = FontWeight.Medium))
            }

            Spacer(modifier = Modifier
                .height(1.dp)
                .fillMaxWidth()
                .background(color = Color.LightGray))

            navDrawerItems.forEachIndexed { index, item ->
                if(item.group == "Trang chủ") {
                    CustomDrawerItem(item = item, selected = index == selectedItemIndex) {
                        selectedItemIndex = index
                        scope.launch {
                            drawerState.close()
                        }
                    }
                }
            }

            Spacer(modifier = Modifier
                .height(1.dp)
                .fillMaxWidth()
                .background(color = Color.LightGray))

            Text("Cài đặt",
                modifier = Modifier.padding(start = 10.dp, top = 20.dp),
                style = TextStyle(fontWeight = FontWeight.Bold,
                    fontSize = 16.sp)
            )
            navDrawerItems.forEachIndexed { index, item ->
                if(item.group == "Cài đặt") {
                    CustomDrawerItem(item = item, selected = index == selectedItemIndex) {
                        selectedItemIndex = index
                        scope.launch {
                            drawerState.close()
                        }
                    }
                }
            }

            Spacer(modifier = Modifier
                .height(1.dp)
                .fillMaxWidth()
                .background(color = Color.LightGray))

            Text("Chính sách",
                modifier = Modifier.padding(start = 10.dp, top = 20.dp),
                style = TextStyle(fontWeight = FontWeight.Bold,
                    fontSize = 16.sp)
            )
            navDrawerItems.forEachIndexed { index, item ->
                if(item.group == "Chính sách") {
                    CustomDrawerItem(item = item, selected = index == selectedItemIndex) {
                        selectedItemIndex = index
                        scope.launch {
                            drawerState.close()
                        }
                    }
                }
            }
        }
    },
        drawerState = drawerState) {
        Scaffold (
            topBar = {
                TopAppBar(
                    colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary,
                        navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                        actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    title = { Text(text = "") },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch {
                                drawerState.open()
                            }
                        }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    },
                    actions = {
                        IconButton(onClick = { /* TODO: Thêm hành động cho menu */ },
                            modifier = Modifier
                                .size(80.dp)
                                .padding(end = 5.dp)) {
                            BadgedBox(badge = {
                                Badge{
                                    Text(text = "12")
                                }
                            }) {
                                Icon(
                                    Icons.Default.Notifications, contentDescription = "",
                                    modifier = Modifier.size(27.dp))
                            }
                        }
                    }
                )
            },
            bottomBar = { bottomBar() },
        ) { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)){
            }
        }

    }
}

@Composable
fun BottomBar() {
    var selectedBottomBarIndex by rememberSaveable {
        mutableStateOf(0)
    }
    BottomAppBar(
    ) {
        itemBottoms.forEachIndexed { index, itemBottoms ->
            NavigationBarItem(
                colors = NavigationBarItemDefaults.colors(indicatorColor = Color.Transparent),
                label = {
                    Text(
                        text = itemBottoms.title,
                        style = TextStyle(fontWeight = if (selectedBottomBarIndex == index) FontWeight.Medium else FontWeight.Normal)
                    )
                },
                selected = selectedBottomBarIndex == index,
                icon = {
                    BadgedBox(badge = {
                        if (itemBottoms.hasNews) {
                            Badge()
                        }
                    }) {
                        Icon(
                            imageVector = if (selectedBottomBarIndex == index) itemBottoms.selectedIcon else itemBottoms.unselectedIcon,
                            contentDescription = "",
                            modifier = Modifier.size(30.dp)
                        )
                    }
                },
                onClick = {
                    selectedBottomBarIndex = index
                    itemBottoms.hasNews = false
                },
            )
        }
    }
}

@Composable
fun CustomDrawerItem(item: NavDrawerItem, selected: Boolean,  onClick: () -> Unit){
    NavigationDrawerItem(
        shape = RoundedCornerShape(10.dp),
        label = {
            Text(text = item.title)
        },
        selected = selected,
        onClick = {
            onClick()
        },
        icon = {
            Icon(
                painter = painterResource(id = if (selected) item.selectedIcon else item.unselectedIcon),
                contentDescription = item.title,
                modifier = Modifier.size(20.dp)
            )
        },
        modifier = Modifier
            .padding(NavigationDrawerItemDefaults.ItemPadding)
    )
}