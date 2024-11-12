package com.example.deliveryapp.ui.authentication

import android.util.Log
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.deliveryapp.R
import com.example.deliveryapp.data.repositories.AuthRepository
import com.example.deliveryapp.viewmodels.AuthViewModel
import com.example.deliveryapp.viewmodels.GraphViewModel

fun LoginScreenRoute(numberPhone: String? = null): String{
    return if (numberPhone.isNullOrEmpty()) "login" else "login?numberPhone=$numberPhone"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController, numberPhone: String? = "", modifier: Modifier = Modifier, graphViewModel: GraphViewModel){
    TopBarInAuthentication(content = {
        Login(numberPhone = numberPhone, modifier = modifier.padding(it), navController = navController, graphViewModel = graphViewModel)
    }) {
        navController.popBackStack()
        graphViewModel.setGraph("")
    }
}


@Composable
fun Login(numberPhone: String? = null, modifier: Modifier = Modifier, navController: NavController, graphViewModel: GraphViewModel){
    val repository = AuthRepository()
    val viewModel = AuthViewModel(repository)
    val loginStatus by viewModel.loginStatus.observeAsState()

    var passwordText by remember { mutableStateOf("") }
    var phoneText by remember { mutableStateOf(if (numberPhone.isNullOrEmpty()) "" else numberPhone) }
    var showPassword by remember { mutableStateOf(false) }
    val keyboardState = keyboardAsState(KeyboardStatus.Closed)

    val phoneRegex = "^0\\d{9}$".toRegex()
    val enableButton by remember {
        derivedStateOf { passwordText.length > 0 && phoneRegex.matches(phoneText)}
    }
    val phoneTextLength by remember {
        derivedStateOf { phoneText.length }
    }

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

        textFiledAuthentication(
            value = passwordText,
            onValueChange = { passwordText = it },
            label = "Mật khẩu",
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            trailingIcon = {
                IconButton(onClick = {
                    showPassword = !showPassword
                }) {
                    Icon(painter = if(!showPassword) painterResource(R.drawable.visibility) else painterResource(R.drawable.visibilityoff),
                        "",
                        modifier = Modifier.size(23.dp))
                }
            },
            visualTransformation = if(showPassword) VisualTransformation.None else PasswordVisualTransformation()
        )

        Row (modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically){
            Row (horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically){
                var isCheck by remember {
                    mutableStateOf(false)
                }
                Checkbox(checked = isCheck, onCheckedChange = { isCheck = it })
                Text(text = "Nhớ tài khoản",
                    style = TextStyle(fontSize = 16.sp, color = MaterialTheme.colorScheme.onPrimary))
            }

            Text("Quên mật khẩu?",
                modifier = Modifier
                    .padding(end = 15.dp)
                    .clickable { navController.navigate(ForgotPasswordScreenRoute()) },
                style = TextStyle(fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.tertiary),
                )
        }

        LeadingBasicButton(text = "Đăng nhập", enableButton) {
            viewModel.login(phoneText, passwordText)
            loginStatus?.let { (success, customerId) ->
                if (success) {
                    graphViewModel.setGraph(customerId.toString())
                }
                else {
                    Log.d("Đăng nhập", "thất bại")
                }
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
                        navController.navigate(RegisterScreenRoute())
                    }
            }
        )
    }
}

