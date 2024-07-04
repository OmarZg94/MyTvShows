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
import androidx.compose.ui.platform.testTag
import coil.compose.rememberAsyncImagePainter
import com.omarzg94.mytvshows.data.model.Episode
import com.omarzg94.mytvshows.ui.theme.secondaryColor
import com.omarzg94.mytvshows.utils.Constants.EPISODE_IMAGE_TT
import com.omarzg94.mytvshows.utils.Constants.HOUR_MINUTE_FORMAT
import com.omarzg94.mytvshows.utils.Constants.MediumPlusPadding
import com.omarzg94.mytvshows.utils.Constants.SmallPadding
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun ShowItem(episode: Episode, onShowSelected: (Episode) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onShowSelected(episode) }
            .padding(SmallPadding),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Row(
            modifier = Modifier.padding(SmallPadding),
            verticalAlignment = Alignment.CenterVertically
        ) {
            EpisodeImage(imageUrl = episode.show.image?.medium)
            Spacer(modifier = Modifier.width(SmallPadding))
            EpisodeDetails(episode = episode)
        }
    }
}

@Composable
fun EpisodeImage(imageUrl: String?) {
    imageUrl?.let {
        Image(
            painter = rememberAsyncImagePainter(model = it),
            contentDescription = null,
            modifier = Modifier
                .size(MediumPlusPadding)
                .testTag(EPISODE_IMAGE_TT)
        )
    }
}

@Composable
fun EpisodeDetails(episode: Episode) {
    Column {
        Text(
            text = episode.show.name,
            style = MaterialTheme.typography.titleSmall.copy(color = Color.White)
        )
        val formatter = DateTimeFormatter.ofPattern(HOUR_MINUTE_FORMAT)
        val startTime = LocalTime.parse(episode.airtime, formatter)
        val endTime = startTime.plusMinutes(episode.runtime?.toLong() ?: 0)
        Text(
            text = "${startTime.format(formatter)} - ${endTime.format(formatter)}",
            style = MaterialTheme.typography.bodyMedium.copy(color = secondaryColor)
        )
    }
}
