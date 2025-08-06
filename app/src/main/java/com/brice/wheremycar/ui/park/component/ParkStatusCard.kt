package com.brice.wheremycar.ui.park.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.brice.wheremycar.ui.park.model.ParkState


@Composable
fun ParkStatusCard(
    floor: Int,
    parkState: ParkState,
    onFloorClick: () -> Unit,
    onStateToggle: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 주차 층 정보 (클릭 가능)
            val floorText = if (floor == 0) "지상" else "지하 ${floor}층"
            Text(
                text = floorText,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onFloorClick) // 클릭 이벤트 연결
                    .padding(vertical = 8.dp)
            )

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp),
                thickness = DividerDefaults.Thickness,
                color = DividerDefaults.color
            )

            // 주차 상태 정보 (토글 스위치)
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = parkState.asParingStateString(),
                    style = MaterialTheme.typography.titleMedium
                )
                Switch(
                    checked = parkState == ParkState.TEMPORARILY_PARKED, // 임시주차일 때 ON
                    onCheckedChange = { onStateToggle() } // 토글 이벤트 연결
                )
            }
        }
    }
}