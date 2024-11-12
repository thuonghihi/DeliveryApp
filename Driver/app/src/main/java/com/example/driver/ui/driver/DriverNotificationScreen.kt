package com.example.driver.ui.driver

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Badge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

fun DriverNotificationScreenRoute(): String{
    return "notification"
}

data class Notification(
    val time: String,
    val date: String,
    val title: String,
    val content: String,
    var status: String
)

val notificationLists = mutableStateListOf(
    Notification("15:30", "29/10/2024", "Go gửi bạn", "Bạn đã hoàn thành đơn thứ 10, nhận thưởng ngay thôi", "unread"),
    Notification("15:30", "29/10/2024", "Go gửi bạn", "Bạn đã hoàn thành đơn thứ 10, nhận thưởng ngay thôi", "unread"),
    Notification("15:30", "29/10/2024", "Go gửi bạn", "Bạn đã hoàn thành đơn thứ 10, nhận thưởng ngay thôi", "readed"),
)

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun preview(){
    DriverNotificationScreen("1")
}
@Composable
fun DriverNotificationScreen(diverId: String) {
    Column {
        Text("Thông báo",
            style = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.SemiBold),
            modifier = Modifier.fillMaxWidth().padding(20.dp)
        )
        Spacer(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color.LightGray))
        LazyColumn {
            itemsIndexed(notificationLists) { index, item ->
                NotificationItem(item) {
                    notificationLists[index] = item.copy(status = "readed")
                }
            }
        }
    }
}

@Composable
fun NotificationItem(notification: Notification, onClick: () -> Unit){
    Row (modifier = Modifier.clickable { onClick() }
        .fillMaxWidth()
        .background(if(notification.status == "unread") Color(0xFFFFFBDE) else Color.Unspecified)
        .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ){
        Column(Modifier.weight(1f)) {
            Row(
                Modifier.padding(bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    notification.time,
                    style = TextStyle(color = Color.Gray)
                )
                Spacer(
                    modifier = Modifier.padding(horizontal = 10.dp).height(13.dp).width(1.dp)
                        .background(Color.Gray)
                )
                Text(
                    notification.date,
                    style = TextStyle(color = Color.Gray)
                )
            }
            Text(
                notification.title.uppercase(),
                style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Medium),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                notification.content,
                style = TextStyle(fontSize = 15.sp, color = Color.Gray),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(top = 5.dp)
            )
        }
        if(notification.status == "unread"){
            Badge(
                containerColor = Color.Red,
                modifier = Modifier.size(8.dp)
            )
        }
    }
    Spacer(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color.LightGray))
}