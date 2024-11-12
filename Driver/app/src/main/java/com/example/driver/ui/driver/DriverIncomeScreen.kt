package com.example.driver.ui.driver

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.driver.data.models.Driver

fun DriverIncomeScreenRoute(): String{
    return "incomeScreen"
}

//@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DriverIncomeScreen(driverId: String){
    Column {
        Text("Thu nhập",
            style = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.SemiBold),
            modifier = Modifier.fillMaxWidth().padding(20.dp)
        )
        Spacer(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color.LightGray))
        TabsIncomeScreen(driverId)
    }
}

@Composable
fun TabsIncomeScreen(driverId: String) {
    val tabs = listOf("Ngày", "Tuần", "Tháng") // Tên cho mỗi tab
    var selectedTabIndex by remember { mutableStateOf(0) } // Trạng thái tab được chọn

    Column {
        // TabRow tạo thanh chứa các Tab
        TabRow(
            selectedTabIndex = selectedTabIndex,
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                    color = Color(0xFFC94F4F)
                )
            }
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = { Text(title,
                        style = TextStyle(color = MaterialTheme.colorScheme.onPrimary)) }
                )
            }
        }

        // Nội dung cho từng tab
        when (selectedTabIndex) {
            0 -> TabContent("Nội dung cho Tab 1")
            1 -> TabContent("Nội dung cho Tab 2")
            2 -> TabContent("Nội dung cho Tab 3")
        }
    }
}

@Composable
fun TabContent(content: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = content)
    }
}


