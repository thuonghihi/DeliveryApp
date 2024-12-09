package com.example.driver.ui.driver

import android.Manifest
import android.content.Context
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.driver.R
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.extension.compose.MapEffect
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState
import com.mapbox.maps.plugin.gestures.gestures
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorBearingChangedListener
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorPositionChangedListener
import com.mapbox.maps.plugin.locationcomponent.location

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

@Composable
fun ActionInBottomSheet(imageRes: Int, note: String){
    Column (horizontalAlignment = Alignment.CenterHorizontally){
        Image(painter = painterResource(imageRes), "", Modifier.size(20.dp).padding(bottom = 5.dp))
        Text(note, style = TextStyle(fontSize = 13.sp, color = Color.DarkGray))
    }
}

@Preview
@Composable
fun ActionInBottomSheetPreview() {
    ActionInBottomSheet(R.drawable.home_outlined, "Giao hàng")
}