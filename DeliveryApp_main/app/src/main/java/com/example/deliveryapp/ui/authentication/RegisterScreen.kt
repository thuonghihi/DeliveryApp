package com.example.deliveryapp.ui.authentication

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.RegisterReceiverFlags
import androidx.core.content.ContextCompat.getDataDir
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.deliveryapp.R

fun RegisterScreenRoute(): String{
    return "register"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(navController: NavController, modifier: Modifier = Modifier){
    TopBarInAuthentication(content = {
        Register(navController = navController, modifier = modifier.padding(it))
    }) {
        navController.popBackStack()
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewRegister(){
    Surface(modifier = Modifier.fillMaxSize()) {
        RegisterScreen(navController = rememberNavController())
    }
}

@Composable
fun Register(navController: NavController, modifier: Modifier = Modifier){
    data class RegestrationState(
        var name: String = "",
        var email: String = "",
        var phoneNumber: String = "",
        var password: String = "",
        var confirmPassword: String = ""
    ){
        fun isValid(): Boolean {
            return listOf(name, email, phoneNumber, password, confirmPassword).all { it.isNotEmpty() }
        }
    }
    var form by remember { mutableStateOf(RegestrationState()) }

    var showPassword by remember {
        mutableStateOf(false)
    }

    var showConfirmPassword by remember {
        mutableStateOf(false)
    }

    val phoneRegex = "^0\\d{9}$".toRegex()

    val emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}\$".toRegex()

    var isFocusEmail by remember {
        mutableStateOf(false)
    }

    val phoneNumberLength by remember {
        derivedStateOf { form.phoneNumber.length }
    }

    val isNotEmailValid = !emailRegex.matches(form.email) && form.email.length > 0 && isFocusEmail == false
    val isNotPhoneValid = (phoneNumberLength == 10 && !phoneRegex.matches(form.phoneNumber)) || phoneNumberLength > 10 || (phoneNumberLength > 0 && form.phoneNumber.get(0) != '0')
    val isNotValidConfirmPassWord = (form.confirmPassword.length == form.password.length && !form.confirmPassword.equals(form.password)) || form.confirmPassword.length > form.password.length

    val enableButton by remember {
        derivedStateOf { form.isValid() && !isNotPhoneValid && !isNotEmailValid && !isNotValidConfirmPassWord}
    }

    Column(modifier = modifier
        .padding(bottom = 20.dp, start = 20.dp, end = 20.dp)
        .fillMaxWidth(),
        verticalArrangement = Arrangement.Top) {
        Column(modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Top) {
            Text("Đăng ký",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp),
                textAlign = TextAlign.Center,
                style = TextStyle(fontSize = 27.sp,
                    fontWeight = FontWeight.ExtraBold)
            )

            Text(
                text = "Điền đầy đủ thông tin để hoàn tất việc đăng ký",
                modifier = Modifier
                    .padding(bottom = 10.dp)
                    .fillMaxWidth(),
                style = TextStyle(fontSize = 15.sp, textAlign = TextAlign.Center, color = Color.Gray)
            )
        }

        Column(
            modifier = Modifier
                .imePadding()
                .verticalScroll(rememberScrollState())
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Top
        ) {
            textFiledAuthentication(value = form.name,
                onValueChange = { form = form.copy(name = it) },
                label = "Tên",
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next
                )
            )
            textFiledAuthentication(value = form.email,
                onValueChange = { form = form.copy(email =  it)},
                label = "Email",
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier.onFocusChanged {
                    isFocusEmail = it.isFocused
                    Log.d("focus", isFocusEmail.toString())
                }
            )

            if(isNotEmailValid){
                TextError(text = "Vui lòng nhập email hợp lệ")
            }
            
            textFiledAuthentication(value = form.phoneNumber,
                onValueChange = { form = form.copy(phoneNumber =  it) },
                label = "Số điện thoại",
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                )
            )
            if(isNotPhoneValid){
                TextError(text = "Vui lòng nhập số điện thoại hợp lệ")
            }
            textFiledAuthentication(value = form.password,
                onValueChange = { form = form.copy(password =  it) },
                label = "Mật khẩu",
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Next
                ),
                visualTransformation = if(showPassword) VisualTransformation.None else PasswordVisualTransformation()
            )
            textFiledAuthentication(value = form.confirmPassword,
                onValueChange = { form = form.copy(confirmPassword =  it) },
                label = "Nhập lại mật khẩu",
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                trailingIcon = {
                    IconButton(onClick = {
                        showConfirmPassword = !showConfirmPassword
                    }) {
                        Icon(painter = if(!showConfirmPassword) painterResource(R.drawable.visibility) else painterResource(
                            R.drawable.visibilityoff),
                            "",
                            modifier = Modifier.size(23.dp))
                    }
                },
                visualTransformation = if(showConfirmPassword) VisualTransformation.None else PasswordVisualTransformation()
            )
            if(isNotValidConfirmPassWord){
                TextError(text = "Mật khẩu không khớp, vui lòng kiểm tra lại")
            }

            LeadingBasicButton(text = "Đăng ký", enableButton) {
                navController.navigate(LoginScreenRoute(form.phoneNumber))
            }
        }
        val annotatedString = buildAnnotatedString {
            append("Bạn đã có tài khoản? ")
            pushStringAnnotation(tag = "clickable", annotation = "clicked")
            withStyle(style = SpanStyle(color = Color.Blue, fontWeight = FontWeight.Bold)){
                append("Đăng nhập") // Phần chữ có thể click
            }
            pop()
        }

        Spacer(modifier = Modifier.weight(1f))

        ClickableText(
            modifier = Modifier.fillMaxWidth(),
            style = TextStyle(
                textAlign = TextAlign.Center,
                fontSize = 16.sp
            ),
            text = annotatedString,
            onClick = { offset ->
                // Kiểm tra nếu click vào phần chữ có thể click
                annotatedString.getStringAnnotations(tag = "clickable", start = offset, end = offset)
                    .firstOrNull()?.let {
                        navController.popBackStack()
                    }
            }
        )
    }
}


