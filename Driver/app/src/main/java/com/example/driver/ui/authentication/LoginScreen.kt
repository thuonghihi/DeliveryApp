package com.example.driver.ui.authentication

import android.app.Activity
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
//import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.driver.R
import com.example.driver.data.repositories.AuthRepository
import com.example.driver.viewmodel.AuthViewModel
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

fun LoginScreenRoute(numberPhone: String? = null): String{
    return if (numberPhone.isNullOrEmpty()) "login" else "login?numberPhone=$numberPhone"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController, numberPhone: String? = "", modifier: Modifier = Modifier){
    TopBarInAuthentication(content = {
        Login(numberPhone = numberPhone, modifier = modifier.padding(it), navController = navController)
    }) {
        navController.popBackStack()
//        graphViewModel.setGraph("")
    }
}


@Composable
fun Login(numberPhone: String? = null, modifier: Modifier = Modifier, navController: NavController){
    val repository = AuthRepository()
    val viewModel = AuthViewModel(repository)
    val phoneCheckState by viewModel.phoneCheckStatus.observeAsState()

    var phoneText by remember { mutableStateOf(if (numberPhone.isNullOrEmpty()) "" else numberPhone) }

    val phoneRegex = "^0\\d{9}$".toRegex()
    val enableButton by remember {
        derivedStateOf { phoneRegex.matches(phoneText)}
    }
    val phoneTextLength by remember {
        derivedStateOf { phoneText.length }
    }
    var showProgress by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .padding(20.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top
    ) {
        
        Text("Đăng nhập",
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp),
            textAlign = TextAlign.Center,
            style = TextStyle(fontSize = 27.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onPrimary)
        )

        var phoneFail by remember { mutableStateOf(false) }
        textFiledAuthentication(
            value = phoneText,
            onValueChange = { phoneText = it },
            label = "Số điện thoại",
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.onFocusChanged { focusState ->
                if(!focusState.isFocused && phoneTextLength < 10 && phoneTextLength > 0) phoneFail = true
                else phoneFail = false
                Log.d("phoneFail", phoneFail.toString())
            }
        )
        if (phoneFail ||
            phoneTextLength > 10 ||
            (phoneTextLength > 0 && phoneText[0] != '0')) {
            TextError(text = "Vui lòng nhập số điện thoại hợp lệ")
        }

        val scope = rememberCoroutineScope()
        val activity = LocalContext.current as Activity
        LeadingBasicButton(text = "Tiếp tục", enableButton) {
            val customPhone = "+84" + phoneText.substring(1)
            scope.launch {
                viewModel.checkPhoneNumber(phoneText) // Bây giờ là hàm suspend
                if (phoneCheckState?.first == true) { // Nếu kiểm tra thành công
                    navController.navigate(OTPScreenRoute(customPhone))
//                    viewModel.sendOTP(customPhone, activity)
                } else {
                    phoneCheckState?.second?.let {
                        Log.e("Error", it)
                    }
                }
                showProgress = false
            }

        }

        Text(text = "hoặc đăng nhập với",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = TextStyle(color = MaterialTheme.colorScheme.onSecondary))

        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            SocialLoginButton(R.drawable.ggicon, ""){
            }
            SocialLoginButton(R.drawable.fbicon, ""){
            }
        }

        val annotatedString = buildAnnotatedString {
            append("Bạn chưa có tài khoản? ")
            pushStringAnnotation(tag = "clickable", annotation = "clicked")
            withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.tertiary, fontWeight = FontWeight.Bold)) {
                append("Đăng ký") // Phần chữ có thể click
            }
            pop()
        }

        Spacer(modifier = Modifier.weight(1f))

        ClickableText(
            modifier = Modifier.fillMaxWidth(),
            style = TextStyle(
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onPrimary
            ),
            text = annotatedString,
            onClick = { offset ->
                annotatedString.getStringAnnotations(tag = "clickable", start = offset, end = offset)
                    .firstOrNull()?.let {
//                        navController.navigate(RegisterScreenRoute())
                    }
            }
        )
    }
}

