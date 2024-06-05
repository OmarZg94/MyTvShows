package com.omarzg94.mytvshows.ui.view

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
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import coil.compose.rememberAsyncImagePainter
import com.omarzg94.mytvshows.R
import com.omarzg94.mytvshows.data.model.Episode
import com.omarzg94.mytvshows.ui.theme.screenBackground
import com.omarzg94.mytvshows.ui.theme.secondaryColor
import com.omarzg94.mytvshows.utils.Constants.LargePadding
import com.omarzg94.mytvshows.utils.Constants.NormalPadding
import com.omarzg94.mytvshows.utils.Constants.NormalPlusPadding
import com.omarzg94.mytvshows.utils.Constants.SmallPadding

@Composable
fun ShowDetailContent(episode: Episode) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = screenBackground)
    ) {
        ShowImageHeader(imageUrl = episode.show.image?.original, showName = episode.show.name)
        Spacer(modifier = Modifier.height(SmallPadding))
        ShowSummary(summary = episode.show.summary)
        Spacer(modifier = Modifier.height(NormalPadding))
        ShowGenres(genres = episode.show.genres)
        Spacer(modifier = Modifier.height(NormalPadding))
        ShowAiringTime(episode = episode)
    }
}

@Composable
fun ShowImageHeader(imageUrl: String?, showName: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(LargePadding)
    ) {
        imageUrl?.let {
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
                modifier = Modifier.padding(NormalPadding),
                text = showName,
                style = MaterialTheme.typography.headlineLarge,
                color = Color.White,
            )
        }
    }
}

@Composable
fun ShowSummary(summary: String?) {
    if (!summary.isNullOrEmpty()) {
        AndroidView(
            modifier = Modifier.padding(horizontal = NormalPadding),
            factory = { context ->
                TextView(context).apply {
                    text = HtmlCompat.fromHtml(summary, HtmlCompat.FROM_HTML_MODE_COMPACT).trim()
                    setTextColor(ContextCompat.getColor(context, R.color.header_color))
                    justificationMode = android.graphics.text.LineBreaker.JUSTIFICATION_MODE_INTER_WORD
                }
            }
        )
    } else {
        Text(
            modifier = Modifier.padding(horizontal = NormalPadding),
            text = stringResource(id = R.string.without_summary),
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White,
            textAlign = TextAlign.Justify
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ShowGenres(genres: List<String>?) {
    if (!genres.isNullOrEmpty()) {
        Text(
            modifier = Modifier.padding(horizontal = NormalPadding),
            text = stringResource(id = R.string.genres),
            style = MaterialTheme.typography.titleMedium,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(SmallPadding))
        FlowRow(
            modifier = Modifier.padding(horizontal = NormalPadding),
            horizontalArrangement = Arrangement.spacedBy(SmallPadding),
            verticalArrangement = Arrangement.Top
        ) {
            genres.forEach { genre ->
                SuggestionChip(
                    enabled = false,
                    onClick = { /* Do nothing */ },
                    label = { Text(text = genre) },
                )
            }
        }
    }
}

@Composable
fun ShowAiringTime(episode: Episode) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = NormalPadding, end = NormalPadding, bottom = NormalPlusPadding)
    ) {
        Text(
            text = stringResource(id = R.string.airing_time),
            style = MaterialTheme.typography.titleMedium,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(SmallPadding))
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
            color = secondaryColor
        )
    }
}