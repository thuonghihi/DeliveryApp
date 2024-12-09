package com.example.deliveryapp.ui.authentication

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.deliveryapp.R

@Composable
fun WelcomeScreen(navController: NavController) {
    Column(modifier = Modifier
        .fillMaxSize()
        .background(color = MaterialTheme.colorScheme.primary)
        .padding(20.dp),
        verticalArrangement = Arrangement.Center) {

        Image(
            painterResource(id = R.drawable.shipper_welcome), contentDescription = "",
            modifier = Modifier
                .aspectRatio(1.5f)
        )

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 50.dp),
            text = "Chào mừng đến với GO!",

            style = TextStyle(
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = 22.sp
            )
        )

        Text(
            text = "\"Giao hàng nhanh chóng, an toàn và tin cậy\ncùng chúng tôi kết nối mọi miền đất nước\"",
            lineHeight = 20.sp,
            maxLines = 2,
            modifier = Modifier
                .padding(top = 20.dp)
                .fillMaxWidth(),
            style = TextStyle(fontSize = 16.sp, textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.onSecondary)
        )

        LeadingBasicButton("Bắt đầu", true) {
            navController.navigate(LoginScreenRoute())
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewWelcome(){
    Surface(modifier = Modifier.fillMaxSize()) {
        WelcomeScreen(navController = rememberNavController())
    }
}
