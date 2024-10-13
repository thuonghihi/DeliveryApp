package com.example.deliveryapp.ui.customer

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import android.widget.ImageButton
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import com.example.deliveryapp.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
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
fun TopBarWithNavigationDrawer(navController: NavController, customerID: String, modalBottomSheet: @Composable () -> Unit? = {}, bottomBar: @Composable () -> Unit? = {}, content: @Composable (modifier: Modifier) -> Unit?){
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    var selectedItemIndex by rememberSaveable {
        mutableStateOf(-1)
    }
    val scope = rememberCoroutineScope()
    BackHandler(enabled = drawerState.isOpen) {
        scope.launch {
            drawerState.close()
        }
    }
    ModalNavigationDrawer(
        drawerContent = {
        ModalDrawerSheet(
            modifier = Modifier
                .fillMaxWidth(0.85f)
        ) {
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
                Text(text = "Chào $customerID",
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
            topBar = { TopBar(
                navigationIcon = Icons.Default.Menu,
                drawerState = drawerState) { }
            },
            bottomBar = { bottomBar() },
        ) { innerPadding ->
            content(modifier = Modifier.padding(innerPadding))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(title: String? = null, navigationIcon: ImageVector, drawerState: DrawerState? = null, onClick: () -> Unit){
    val scope = rememberCoroutineScope()
    TopAppBar(
        colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimary
        ),
        title = {
            if (title != null) {
                Text(text = title, style = TextStyle(fontSize = 19.sp))
            }
        },
        navigationIcon = {
            IconButton(onClick = {
//                scope.launch {
//                    drawerState.open()
//                }
                drawerState?.let {
                    // Nếu drawerState khác null, mở Drawer
                    scope.launch {
                        drawerState.open()
                    }
                } ?: run {
                    // Nếu không có drawerState, thực hiện onClick() thông thường
                    onClick()
                }
            }) {
                Icon(navigationIcon, contentDescription = "")
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

@Composable
fun MapScreen(modifier: Modifier = Modifier) {
    var currentLocation by remember { mutableStateOf<LatLng?>(null) }
    val defaultLocation = LatLng(20.0, 105.0)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition(defaultLocation, 14f, 0f, 0f) // Default zoom level
    }
    val context = LocalContext.current
    val fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

    val locationPermissionRequest = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            getCurrentLocation(fusedLocationClient, cameraPositionState) { location ->
                currentLocation = location
            }
        } else {
            Log.e("MapScreen", "Location permission not granted")
        }
    }

    LaunchedEffect(Unit) {
        when {
            ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED -> {
                getCurrentLocation(fusedLocationClient, cameraPositionState) { location ->
                    currentLocation = location
                }
            }
            else -> {
                locationPermissionRequest.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
    }

    Column(modifier = modifier) {
        // Map
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState
        ) {
            // Show marker at current location if available
            currentLocation?.let {
                Marker(
                    state = MarkerState(position = it),
                    title = "You are here",
                    snippet = "Current location"
                )
            }
        }


    }
}

// Function to get current location
@SuppressLint("MissingPermission")
fun getCurrentLocation(
    fusedLocationClient: FusedLocationProviderClient,
    cameraPositionState: CameraPositionState,
    onLocationReceived: (LatLng) -> Unit
){
    try {
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            location?.let {
                val latLng = LatLng(it.latitude, it.longitude)
                onLocationReceived(latLng)
                cameraPositionState.move(CameraUpdateFactory.newCameraPosition(CameraPosition(latLng, 15f, 0f, 0f)))
                Log.d("MapScreen", "Current location: $latLng")
                latLng
            } ?: Log.e("MapScreen", "Current location not found")
        }
    } catch (e: SecurityException) {
        Log.e("MapScreen", "Unable to get location: ${e.message}")
    }
}


@Composable
fun CustomerAddPointScreen(placeHolder: String, iconResiD: Int, modifier: Modifier, value: String, onValueChange: (String) -> Unit){
    Box(modifier = modifier) {
        MapScreen(modifier = modifier)
        Surface(
            shadowElevation = 10.dp,
            shape = MaterialTheme.shapes.small, // Hình dạng cho Surface
            modifier = Modifier.fillMaxWidth().padding(10.dp) // Chiếm hết chiều rộng
        ) {
            TextField(
                value = value,
                onValueChange = onValueChange,
                leadingIcon = {
                    Image(painter = painterResource(iconResiD), "", Modifier.size(25.dp))
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF3569CC),
                    unfocusedLabelColor = Color.LightGray
                ),
                placeholder = {
                    Text(placeHolder)
                },
                textStyle = TextStyle(fontSize = 14.sp),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}



