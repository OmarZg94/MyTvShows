package com.omarzg94.mytvshows.ui.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.omarzg94.mytvshows.data.model.Episode
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun ShowItem(episode: Episode, onShowSelected: (Episode) -> Unit) {
    with(episode) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onShowSelected(episode) },
            colors = CardDefaults.cardColors(containerColor = Color.Transparent)
        ) {
            Row(
                modifier = Modifier.padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                show.image?.medium?.let {
                    Image(
                        painter = rememberAsyncImagePainter(model = it),
                        contentDescription = null,
                        modifier = Modifier
                            .size(70.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = show.name,
                        style = MaterialTheme.typography.titleSmall.copy(color = Color.White)
                    )
                    val formatter = DateTimeFormatter.ofPattern("HH:mm")
                    val startTime = LocalTime.parse(episode.airtime, formatter)
                    val endTime = startTime.plusMinutes(episode.runtime!!.toLong())
                    Text(
                        text = "${startTime.format(formatter)} - ${endTime.format(formatter)}",
                        style = MaterialTheme.typography.bodyMedium.copy(color = Color(0xFF9494C7))
                    )
                }
            }
        }
    }
}