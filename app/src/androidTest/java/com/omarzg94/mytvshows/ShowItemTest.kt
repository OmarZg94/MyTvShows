package com.omarzg94.mytvshows

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.omarzg94.mytvshows.data.model.Episode
import com.omarzg94.mytvshows.data.model.Image
import com.omarzg94.mytvshows.data.model.Rating
import com.omarzg94.mytvshows.data.model.Schedule
import com.omarzg94.mytvshows.data.model.Show
import com.omarzg94.mytvshows.data.model.ShowNetwork
import com.omarzg94.mytvshows.ui.theme.MyTvShowsTheme
import com.omarzg94.mytvshows.ui.view.ShowItem
import com.omarzg94.mytvshows.utils.Constants.EPISODE_IMAGE_TT
import com.omarzg94.mytvshows.utils.Constants.HOUR_MINUTE_FORMAT
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@RunWith(AndroidJUnit4::class)
@SmallTest
class ShowItemTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val formatter = DateTimeFormatter.ofPattern(HOUR_MINUTE_FORMAT)

    @Test
    fun showItemDisplaysShowName() {
        val episode = mockEpisode(showName = "Sample Show")
        composeTestRule.setContent {
            MyTvShowsTheme {
                ShowItem(episode = episode, onShowSelected = {})
            }
        }

        composeTestRule
            .onNodeWithText("Sample Show")
            .assertExists()
    }

    @Test
    fun showItemDisplaysAirtime() {
        val startTime = LocalTime.parse("20:00", formatter)
        val endTime = startTime.plusMinutes(60)
        val episode = mockEpisode(airtime = "20:00", runtime = 60)
        composeTestRule.setContent {
            MyTvShowsTheme {
                ShowItem(episode = episode, onShowSelected = {})
            }
        }

        composeTestRule
            .onNodeWithText("${startTime.format(formatter)} - ${endTime.format(formatter)}")
            .assertExists()
    }

    @Test
    fun showItemDisplaysImage() {
        val episode = mockEpisode(imageUrl = "https://via.placeholder.com/150")
        composeTestRule.setContent {
            MyTvShowsTheme {
                ShowItem(episode = episode, onShowSelected = {})
            }
        }

        composeTestRule
            .onNodeWithTag(EPISODE_IMAGE_TT, useUnmergedTree = true)
            .assertExists()
    }

    @Test
    fun showItemHandlesClick() {
        val episode = mockEpisode(showName = "Sample Show")
        var clickedEpisode: Episode? = null
        composeTestRule.setContent {
            MyTvShowsTheme {
                ShowItem(episode = episode, onShowSelected = { clickedEpisode = it })
            }
        }

        composeTestRule
            .onNodeWithText("Sample Show")
            .performClick()

        assert(clickedEpisode == episode)
    }

    @Test
    fun showItemWithoutImage() {
        val episode = mockEpisode(imageUrl = null)
        composeTestRule.setContent {
            MyTvShowsTheme {
                ShowItem(episode = episode, onShowSelected = {})
            }
        }

        composeTestRule
            .onNodeWithTag(EPISODE_IMAGE_TT)
            .assertDoesNotExist()
    }

    @Test
    fun showItemWithDifferentAirtimeFormat() {
        val startTime = LocalTime.parse("21:00", formatter)
        val endTime = startTime.plusMinutes(30)
        val episode = mockEpisode(airtime = "21:00", runtime = 30)
        composeTestRule.setContent {
            MyTvShowsTheme {
                ShowItem(episode = episode, onShowSelected = {})
            }
        }

        composeTestRule
            .onNodeWithText("${startTime.format(formatter)} - ${endTime.format(formatter)}")
            .assertExists()
    }


    private fun mockEpisode(
        showName: String = "Sample Show",
        airtime: String = "20:00",
        runtime: Int = 60,
        imageUrl: String? = "https://via.placeholder.com/150",
        airDate: String = ""
    ): Episode {
        return Episode(
            id = 1,
            name = showName,
            season = 1,
            number = 1,
            airtime = airtime,
            runtime = runtime,
            airdate = airDate,
            show = Show(
                id = 1,
                name = showName,
                image = if (imageUrl != null) Image(
                    medium = imageUrl,
                    original = imageUrl
                ) else null,
                network = ShowNetwork(
                    id = 1,
                    name = "Sample Network",
                    officialSite = "Sample Official Site"
                ),
                summary = "This is a sample show summary.",
                genres = arrayListOf("Drama", "Comedy"),
                schedule = Schedule(days = listOf("Monday"), time = airtime),
                ended = "",
                premiered = "",
                rating = Rating(4.7F)
            )
        )
    }
}