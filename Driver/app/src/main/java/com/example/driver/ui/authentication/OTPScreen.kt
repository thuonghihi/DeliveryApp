package com.example.driver.ui.authentication

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.OnTrimMemoryProvider
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.driver.data.repositories.AuthRepository
import com.example.driver.ui.driver.DriverHomepageScreenRoute
import com.example.driver.viewmodel.AuthViewModel
import com.example.driver.viewmodel.GraphViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlin.math.sign

fun OTPScreenRoute(phoneNumber: String): String{
    return "otpScreen/$phoneNumber"
}

@Composable
fun OtpScreen(navController: NavController, phoneNumber: String, graphViewModel: GraphViewModel, modifier: Modifier = Modifier){
    TopBarInAuthentication(content = {
        Otp(navController = navController, phoneNumber = phoneNumber, graphViewModel = graphViewModel, modifier = modifier.padding(it))
    }) {
        navController.popBackStack()
    }
}

@Composable
fun Otp(navController: NavController, phoneNumber: String, graphViewModel: GraphViewModel, modifier: Modifier = Modifier){
    val repository = AuthRepository()
    val viewModel = AuthViewModel(repository)
//    val phoneCheckState by viewModel.phoneCheckStatus.observeAsState()
    var otpValue by remember {
        mutableStateOf("")
    }

    val isEnableButton by remember {
        derivedStateOf {
            otpValue.length == 6
        }
    }

    Column(
        modifier = modifier
            .padding(horizontal = 20.dp)
            .fillMaxWidth()
    ) {

        Text("Nhập mã xác thực",
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 3.dp),
            style = TextStyle(fontSize = 27.sp,
                fontWeight = FontWeight.ExtraBold)
        )

        Text(
            text = "Mã xác thực sẽ được gửi đến $phoneNumber.\nVui lòng kiểm tra tin nhắn và cung cấp mã xác thực",
            modifier = Modifier
                .padding(bottom = 5.dp)
                .fillMaxWidth(),
            style = TextStyle(fontSize = 14.sp, color = Color.Gray)
        )

        Spacer(modifier = Modifier.height(24.dp))

        val typography = MaterialTheme.typography.copy(
            displaySmall = TextStyle(
                fontSize = 16.sp
            )
        )

        val currentColor = MaterialTheme.colorScheme.copy(primary = Color(0xFF3569CC), secondary = Color.LightGray)

        MaterialTheme(
            colorScheme = currentColor,
            typography = typography
        ) {
            OtpField(otpLength = 6, onOtpChanged = { otp ->
                otpValue = otp
            })
        }

        val scope = rememberCoroutineScope()
        LeadingBasicButton(text = "Đăng nhập", isEnableButton) {
//            scope.launch {
//                val signInStatus = viewModel.verifyOTP(otpValue)
//                if(signInStatus.first){
//                    navController.navigate(DriverHomepageScreenRoute())
//                }
//                else{
//                    Log.d("otpsai", "qsdfgh")
//                }
//            }
            navController.navigate(DriverHomepageScreenRoute())
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun OtpField(
    otpLength: Int,
    onOtpChanged: (String) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    // 0998779
    var otpValue by remember {
        mutableStateOf("")
    }

    var entered by remember {
        mutableStateOf(false)
    }

    val keyboardState = keyboardAsState(KeyboardStatus.Closed)

    val isShowWarning by remember(keyboardState) {
        derivedStateOf {
            if (keyboardState.value == KeyboardStatus.Closed && entered) {
                if (otpValue.length != otpLength) {
                    return@derivedStateOf true
                }
            }
            false
        }
    }

    BasicTextField(
        value = otpValue, onValueChange = { value ->
            if (value.length <= otpLength) {
                otpValue = value
                onOtpChanged(otpValue)
                if(value.length == 0) entered = false
                else entered = true
            }
        },
        decorationBox = {
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                repeat(otpLength) { index ->
                    val char = when {
                        index >= otpValue.length -> ""
                        else -> otpValue[index].toString()
                    }

                    val isFocus = if(keyboardState.value == KeyboardStatus.Closed) false else index == otpValue.length
                    OtpCell(
                        char = char,
                        isFocus = isFocus,
                        isShowWarning = isShowWarning,
                        modifier = Modifier.weight(
                            1f
                        )
                    )
                }
            }
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Number
        ),
        keyboardActions = KeyboardActions(onDone = {
            keyboardController?.hide()
        }
        )
    )

}

@Composable
fun OtpCell(
    char: String,
    isFocus: Boolean,
    isShowWarning: Boolean,
    modifier: Modifier = Modifier
) {

    val borderColor = if (isShowWarning) {
        MaterialTheme.colorScheme.error
    } else if (isFocus) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.secondary
    }

    Surface(
        modifier = modifier
            .aspectRatio(1f)
            .border(width = 2.dp, color = borderColor, shape = MaterialTheme.shapes.small)
    ) {
        Text(
            text = char,
            style = MaterialTheme.typography.displaySmall.copy(
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            ),
            modifier = Modifier.wrapContentSize(align = Alignment.Center)
        )
    }
}


@Preview(showBackground = true)
@Composable
fun OtpInputFieldPreview() {
    MaterialTheme {
        Box(modifier = Modifier.fillMaxSize()) {
//            OtpScreen(navController = rememberNavController())
        }
    }
}
