package com.example.deliveryapp.ui.authentication

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

fun ForgotPasswordScreenRoute():String{
    return "forgotPassword"
}

@Composable
fun ForgotPasswordScreen(navController: NavController, modifier: Modifier = Modifier){
    TopBarInAuthentication(
        content = {
            ForgotPassword(navController = navController, modifier = modifier.padding(it))
        }
    ){
        navController.popBackStack()
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewForgotPW(){
    ForgotPasswordScreen(navController = rememberNavController())
}

@Composable
fun ForgotPassword(navController: NavController, modifier: Modifier = Modifier){
    var phoneText by rememberSaveable {
        mutableStateOf("")
    }

    val phoneRegex = "^0\\d{9}$".toRegex()

    val enableButton by remember {
        derivedStateOf { phoneRegex.matches(phoneText) }
    }

    val phoneTextLength by remember {
        derivedStateOf { phoneText.length }
    }

    Column(modifier = modifier
        .padding(20.dp),
        verticalArrangement = Arrangement.Top) {
        Text("Nhập số điện thoại",
            modifier = Modifier
                .fillMaxWidth(),
            style = TextStyle(fontSize = 27.sp,
                fontWeight = FontWeight.ExtraBold)
        )
        Text(
            text = "Điền đầy đủ thông tin để hoàn tất việc đăng ký",
            modifier = Modifier
                .padding(vertical = 10.dp)
                .fillMaxWidth(),
            style = TextStyle(fontSize = 15.sp, color = MaterialTheme.colorScheme.onSecondary)
        )

        textFiledAuthentication(value = phoneText,
            onValueChange = { phoneText = it },
            label = "Số điện thoại",
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            )
        )

        if((phoneTextLength == 10 && !enableButton) || phoneTextLength > 10 || (phoneTextLength > 0 && phoneText.get(0) != '0')){
            TextError(text = "Vui lòng nhập số điện thoại hợp lệ")
        }

        LeadingBasicButton(text = "Tiếp tục", enableButton) {
            phoneText?.let {
                navController.navigate(OTPScreenRoute())
            }
        }
    }
}


