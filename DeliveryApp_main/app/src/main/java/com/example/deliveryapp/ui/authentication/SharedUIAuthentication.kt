package com.example.deliveryapp.ui.authentication

import android.graphics.Rect
import android.view.ViewTreeObserver
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.lang.Error

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarInAuthentication(
    content: @Composable (padding: PaddingValues) -> Unit,
    onBackClick: () -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null)
                    }
                },
                title = {
                    Text(text = "")
                }
            )
        }
    ) { innerPadding ->
        // Pass the padding from Scaffold to content
        content(innerPadding)
    }
}

@Composable
fun textFiledAuthentication(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    keyboardOptions: KeyboardOptions,
    trailingIcon: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    modifier: Modifier = Modifier
){
    OutlinedTextField(value = value,
        onValueChange = onValueChange,
        label = {
            Text(text = label)
        },
        textStyle = TextStyle(fontSize = 17.sp),
        keyboardOptions = keyboardOptions,
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedLabelColor = Color(0xFF3569CC),
            focusedBorderColor = Color(0xFF3569CC)
        ),
        trailingIcon = if(value.isNotEmpty()) trailingIcon else null,
        visualTransformation = visualTransformation
    )
}

@Composable
fun TextError(text: String){
    Text(text = text,
        color = Color.Red,
        style = TextStyle(fontSize = 14.sp),
        modifier = Modifier.padding(bottom = 10.dp))
}

@Composable
fun SocialLoginButton(iconResId: Int, contentDescription: String, onClick: () -> Unit) {
    Button(
        modifier = Modifier
            .padding(20.dp)
            .size(64.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White,
            contentColor = Color.Black
        ),
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        elevation = ButtonDefaults.buttonElevation(8.dp),
        contentPadding = PaddingValues(5.dp),
    ) {
        Image(
            painter = painterResource(id = iconResId),
            contentDescription = contentDescription,
            modifier = Modifier.fillMaxSize() // Kích thước icon
        )
    }
}

@Composable
fun LeadingBasicButton(
    text: String,
    enable: Boolean = true,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
){
    var isFocusButton by remember {
        mutableStateOf(false)
    }
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 20.dp)
            .onFocusChanged {
                isFocusButton = it.isFocused
            },
        enabled = enable,
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (!isFocusButton) Color(0xFFFC8D03) else Color(0xFFF57104)
        )
    ) {
        Text(
            text = text,
            style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.primary),
            modifier = Modifier.padding(vertical = 7.dp)
        )
    }
}

enum class KeyboardStatus {
    Opened,
    Closed
}

@Composable
fun keyboardAsState(initial: KeyboardStatus = KeyboardStatus.Closed): State<KeyboardStatus> {
    val keyboardState = remember { mutableStateOf(initial) }
    val view = LocalView.current
    DisposableEffect(view) {
        val onGlobalListener = ViewTreeObserver.OnGlobalLayoutListener {
            val rect = Rect()
            view.getWindowVisibleDisplayFrame(rect)
            val screenHeight = view.rootView.height
            val keypadHeight = screenHeight - rect.bottom
            keyboardState.value = if (keypadHeight > screenHeight * 0.15) {
                KeyboardStatus.Opened
            } else {
                KeyboardStatus.Closed
            }
        }
        view.viewTreeObserver.addOnGlobalLayoutListener(onGlobalListener)

        onDispose {
            view.viewTreeObserver.removeOnGlobalLayoutListener(onGlobalListener)
        }
    }

    return keyboardState
}
