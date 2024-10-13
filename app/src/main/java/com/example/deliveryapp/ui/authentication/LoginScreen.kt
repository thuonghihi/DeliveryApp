package com.example.deliveryapp.ui.authentication

import android.graphics.drawable.Icon
import android.util.Log
import android.widget.ImageButton
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
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
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.deliveryapp.R
import com.example.deliveryapp.ui.customer.CustomerHomePageScreenRoute

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
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewLogin(){
    Surface(modifier = Modifier.fillMaxSize()) {
        LoginScreen(navController = rememberNavController())
    }
}

//}

@Composable
fun Login(numberPhone: String? = null, modifier: Modifier = Modifier, navController: NavController){
    Column(
        modifier = modifier
            .padding(20.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top
    ) {
        var phoneNumberText by remember {
            mutableStateOf("")
        }
        var passwordText by remember {
            mutableStateOf("")
        }
        var showPassword by remember {
            mutableStateOf(false)
        }

        val phoneRegex = "^0\\d{9}$".toRegex()

        val enableButton by remember {
            derivedStateOf { passwordText.length > 0 && phoneRegex.matches(phoneNumberText)}
        }
        
        Text("Đăng nhập",
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp),
            textAlign = TextAlign.Center,
            style = TextStyle(fontSize = 27.sp,
                fontWeight = FontWeight.ExtraBold)
        )

        numberPhone?.let {
            phoneNumberText = numberPhone
        }
        textFiledAuthentication(
            value = phoneNumberText,
            onValueChange = { phoneNumberText = it },
            label = "Số điện thoại",
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            )
        )

        val phoneTextLength by remember {
            derivedStateOf { phoneNumberText.length }
        }
        if((phoneTextLength == 10 && !phoneRegex.matches(phoneNumberText)) || phoneTextLength > 10 || (phoneTextLength > 0 && phoneNumberText.get(0) != '0')){
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
                    //  modifier = Modifier.padding(start = 2.dp),
                    style = TextStyle(fontSize = 16.sp))
            }

            Text("Quên mật khẩu?",
                modifier = Modifier
                    .padding(end = 15.dp)
                    .clickable { navController.navigate(ForgotPasswordScreenRoute()) },
                style = TextStyle(fontSize = 16.sp,
                    color = Color(0xFF3569CC)),
                )
        }

        LeadingBasicButton(text = "Đăng nhập", enableButton) {
            Log.d("login", "hehe")
//            navController.navigate(CustomerHomePageScreenRoute("12"))
        }

        Text(text = "hoặc đăng nhập với",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = TextStyle(color = Color.LightGray))

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
            withStyle(style = SpanStyle(color = Color.Blue, fontWeight = FontWeight.Bold)) {
                append("Đăng ký") // Phần chữ có thể click
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
                        navController.navigate(RegisterScreenRoute())
                    }
            }
        )
    }
}

