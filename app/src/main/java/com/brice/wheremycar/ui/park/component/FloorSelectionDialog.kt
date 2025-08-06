package com.brice.wheremycar.ui.park.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun FloorSelectionDialog(
    onDismissRequest: () -> Unit,
    onFloorSelected: (Int) -> Unit
) {
    val floors = listOf("지상", "지하 1층", "지하 2층", "지하 3층", "지하 4층", "지하 5층")

    Dialog(onDismissRequest = onDismissRequest) {
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("주차 층을 선택하세요", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(16.dp))
                floors.forEachIndexed { index, floorName ->
                    Text(
                        text = floorName,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onFloorSelected(index) } // 선택 시 index(0~5) 전달
                            .padding(vertical = 12.dp)
                    )
                }
            }
        }
    }
}