package com.omarzg94.mytvshows.ui.view

import android.graphics.text.LineBreaker.JUSTIFICATION_MODE_INTER_WORD
import android.view.ViewGroup.LayoutParams
import android.widget.LinearLayout
import android.widget.TextView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import coil.compose.rememberAsyncImagePainter
import com.omarzg94.mytvshows.R
import com.omarzg94.mytvshows.data.model.Episode

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ShowDetailContent(episode: Episode) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        ) {
            episode.show.image?.original?.let {
                Image(
                    painter = rememberAsyncImagePainter(model = it),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            } ?: Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Gray)
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomStart)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black
                            )
                        )
                    )
            ) {
                Text(
                    modifier = Modifier.padding(16.dp),
                    text = episode.show.name,
                    style = MaterialTheme.typography.headlineLarge,
                    color = Color.White,
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        if (!episode.show.summary.isNullOrEmpty()) {
            AndroidView(
                modifier = Modifier.padding(horizontal = 16.dp),
                factory = { context ->
                    TextView(context).apply {
                        text =
                            HtmlCompat.fromHtml(
                                episode.show.summary,
                                HtmlCompat.FROM_HTML_MODE_COMPACT
                            ).trim()
                        setTextColor(ContextCompat.getColor(context, R.color.header_color))
                        justificationMode = JUSTIFICATION_MODE_INTER_WORD
                    }
                })
        } else {
            Text(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = stringResource(id = R.string.without_summary),
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White,
                textAlign = TextAlign.Justify
            )
        }
        if (!episode.show.genres.isNullOrEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = stringResource(id = R.string.genres),
                style = MaterialTheme.typography.titleMedium,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(8.dp))
            FlowRow(
                modifier = Modifier.padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.Top
            ) {
                episode.show.genres.forEach { genre ->
                    SuggestionChip(
                        enabled = false,
                        onClick = { /* Do nothing */ },
                        label = { Text(text = genre) },
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = stringResource(id = R.string.airing_time),
            style = MaterialTheme.typography.titleMedium,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(8.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, bottom = 24.dp)
        ) {
            Text(
                text = "${episode.show.schedule.days.joinToString(", ")} at ${episode.show.schedule.time}",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White
            )
            Text(
                text = stringResource(
                    id = R.string.season_episode_format,
                    episode.season,
                    episode.number
                ),
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF9494C7)
            )
        }
    }
}