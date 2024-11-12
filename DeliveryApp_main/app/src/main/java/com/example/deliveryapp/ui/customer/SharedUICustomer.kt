package com.example.deliveryapp.ui.customer

import android.Manifest
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material3.DrawerState
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.example.deliveryapp.R
import com.example.deliveryapp.ui.authentication.LeadingBasicButton
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.LifecycleOwner
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.extension.compose.MapEffect
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState
import com.mapbox.maps.plugin.gestures.gestures
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorBearingChangedListener
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorPositionChangedListener
import com.mapbox.maps.plugin.locationcomponent.location
import com.mapbox.search.autocomplete.PlaceAutocomplete
import com.mapbox.common.location.LocationProvider
import com.mapbox.search.autocomplete.PlaceAutocompleteSuggestion
import com.mapbox.search.base.core.CoreApiType
import com.mapbox.search.base.location.defaultLocationProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import androidx.compose.ui.focus.onFocusChanged
import com.mapbox.geojson.BoundingBox
import com.mapbox.geojson.Point
import com.mapbox.search.autocomplete.PlaceAutocompleteOptions
import kotlin.random.Random

data class NavDrawerItem(
    val title: String,
    val selectedIcon: Int? = null,
    val unselectedIcon: Int? = null,
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
    ),
    NavDrawerItem(
        title = "Đăng xuất",
        route = "",
        group = "Đăng xuất"
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
fun TopBarWithNavigationDrawer(navController: NavController,
                               customer: String,
                               hasOrder: Boolean,
                               modalBottomSheet: @Composable () -> Unit? = {},
                               bottomBar: @Composable () -> Unit? = {},
                               content: @Composable (modifier: Modifier) -> Unit? = {}){
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
                Text(text = customer,
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

            Spacer(modifier = Modifier
                .height(1.dp)
                .fillMaxWidth()
                .background(color = Color.LightGray))

            navDrawerItems.forEachIndexed { index, item ->
                if(item.group == "Đăng xuất") {
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
            floatingActionButton = {
                FloatingActionButton(
                    containerColor = Color(0xFFFC8D03),
                    contentColor = Color.White,
                    onClick = { navController.navigate(AddOrderInfoScreenRoute()) }
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Add Order")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Tạo đơn hàng", color = Color.White)
                    }
                }
            }
        ) { innerPadding ->
            content(Modifier.padding(innerPadding))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(title: String? = null, navigationIcon: ImageVector, drawerState: DrawerState? = null, onClick: () -> Unit){
    val scope = rememberCoroutineScope()
    Column {
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
        Spacer(modifier = Modifier.fillMaxWidth().height(0.5.dp).background(color = Color.LightGray))
    }
}


@Composable
fun BottomBar(navController: NavController, selectedIndex: Int) {
    var selectedBottomBarIndex by rememberSaveable { mutableStateOf(selectedIndex) }

    BottomAppBar {
        itemBottoms.forEachIndexed { index, itemBottom ->
            NavigationBarItem(
                colors = NavigationBarItemDefaults.colors(indicatorColor = Color.Transparent),
                label = {
                    Text(
                        text = itemBottom.title,
                        style = TextStyle(fontWeight = if (selectedBottomBarIndex == index) FontWeight.Medium else FontWeight.Normal)
                    )
                },
                selected = selectedBottomBarIndex == index,
                icon = {
                    BadgedBox(badge = {
                        if (itemBottom.hasNews) {
                            Badge()
                        }
                    }) {
                        Icon(
                            imageVector = if (selectedBottomBarIndex == index) itemBottom.selectedIcon else itemBottom.unselectedIcon,
                            contentDescription = "",
                            modifier = Modifier.size(25.dp)
                        )
                    }
                },
                onClick = {
                    selectedBottomBarIndex = index
                    if(index == 0){
                        navController.navigate(CustomerHomePageScreenRoute())
                    }
                    else if(index == 1){
                        navController.navigate(CustomerActionScreenRoute())
                    }
                    itemBottom.hasNews = false
                },
            )
        }
    }
}

@Composable
fun CustomDrawerItem(item: NavDrawerItem, selected: Boolean, onClick: () -> Unit){
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
            (if (selected) item.selectedIcon else item.unselectedIcon)?.let { painterResource(id = it) }
                ?.let {
                    Icon(
                        painter = it,
                        contentDescription = item.title,
                        modifier = Modifier.size(20.dp)
                    )
                }
        },
        modifier = Modifier
            .padding(NavigationDrawerItemDefaults.ItemPadding)
    )
}

@Composable
fun MapScreen(context: Context) {
    val isPermissionGranted = remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions ->
            isPermissionGranted.value = permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false ||
                    permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false

            if (!isPermissionGranted.value) {
                Toast.makeText(context, "Vui lòng chỉnh sửa quyền trong cài đặt để sử dụng Maps", Toast.LENGTH_SHORT).show()
            }
        }
    )

    LaunchedEffect(Unit) {
        launcher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    if (isPermissionGranted.value) {
        val currentLocation = remember { mutableStateOf<Point?>(null) }
        val mapViewportState = rememberMapViewportState {
            setCameraOptions {
                zoom(14.0)
                center(currentLocation.value)
                pitch(0.0)
                bearing(0.0)
            }
        }

        MapboxMap(
            modifier = Modifier.fillMaxSize(),
            mapViewportState = mapViewportState,
        ) {
            MapEffect(key1 = Unit) { mapView ->
                val onIndicatorBearingChangedListener = OnIndicatorBearingChangedListener {
                    mapViewportState.setCameraOptions(CameraOptions.Builder().bearing(it).build())
                }

                val onIndicatorPositionChangedListener = OnIndicatorPositionChangedListener { location ->
                    mapViewportState.setCameraOptions(CameraOptions.Builder().center(location).build())
                    mapView.gestures.focalPoint = mapView.mapboxMap.pixelForCoordinate(location)

                    currentLocation.value = location
                }

                mapView.location.apply {
                    enabled = true
                    addOnIndicatorBearingChangedListener(onIndicatorBearingChangedListener)
                    addOnIndicatorPositionChangedListener(onIndicatorPositionChangedListener)
                }
            }
        }
    } else {
    }
}

//fun fetchPlaceAutocompleteSuggestions(
//    scope: CoroutineScope,
//    query: String,
//    onSuggestionsFetched: (List<String>) -> Unit
//) {
//
//    val placeAutocomplete = PlaceAutocomplete.create(
//        locationProvider = defaultLocationProvider(),
//        apiType = CoreApiType.SEARCH_BOX
//    )
//
//    scope.launch {
//        val response = placeAutocomplete.suggestions(query = query)
//        response.onValue { suggestions: List<PlaceAutocompleteSuggestion> ->
//            val suggestionNames = suggestions.map { it.name }.take(5)
//            onSuggestionsFetched(suggestionNames) // Gọi callback với danh sách gợi ý
//        }.onError { e ->
//            onSuggestionsFetched(emptyList())
//        }
//    }
//}

fun fetchPlaceAutocompleteSuggestions(
    scope: CoroutineScope,
    query: String,
    onSuggestionsFetched: (List<String>) -> Unit
) {
    val placeAutocomplete = PlaceAutocomplete.create(
        locationProvider = defaultLocationProvider(),
        apiType = CoreApiType.SEARCH_BOX
    )
    val northeastPoint = Point.fromLngLat(109.4603, 23.3891) // Điểm Đông Bắc
    val southwestPoint = Point.fromLngLat(102.1445, 8.3435) // Điểm Tây Nam
    val boundingBox = BoundingBox.fromPoints(northeastPoint, southwestPoint)


    scope.launch {
        val response = placeAutocomplete.suggestions(query = query)
        response.onValue { suggestions: List<PlaceAutocompleteSuggestion> ->
            val formattedAddresses = suggestions.mapNotNull { suggestion ->
                suggestion.formattedAddress
            }.take(5)
            onSuggestionsFetched(formattedAddresses)
        }.onError { e ->
            onSuggestionsFetched(emptyList())
        }
    }
}


@Composable
fun LoadingModal(isLoading: Boolean, onDismiss: () -> Unit) {
    if (isLoading) {
        Dialog(onDismissRequest = { }) { // Không cho phép đóng bằng cách chạm ra ngoài
            Surface(
                color = Color.White.copy(alpha = 0.8f), // Màu nền sáng hơn một chút
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(16.dp) // Thêm padding nếu cần
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp), // Thêm padding cho nội dung
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        LoadingIcon()
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Đang tìm tài xế...", fontSize = 20.sp)
                        LeadingBasicButton("Hủy") {
                            Log.d("ad", "dfjdsf")
                            onDismiss()
                            Log.d("av", "dfjdsf")

                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LoadingIcon() {
    // Tạo animation xoay cho biểu tượng
    val infiniteTransition = rememberInfiniteTransition()
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = LinearEasing)
        )
    )

    // Hiển thị biểu tượng với hiệu ứng xoay
    Image(
        painter = painterResource(id = R.drawable.loading_icon), // Thay đổi thành tài nguyên biểu tượng của bạn
        contentDescription = "Loading",
        modifier = Modifier
            .size(48.dp)
            .rotate(rotation) // Xoay biểu tượng
    )
}





