package com.omarzg94.mytvshows

import android.content.Context
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import androidx.test.platform.app.InstrumentationRegistry
import com.omarzg94.mytvshows.ui.theme.MyTvShowsTheme
import com.omarzg94.mytvshows.ui.view.SearchBar
import com.omarzg94.mytvshows.utils.Constants.SEARCH_BAR_TT
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class SearchBarTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val context: Context = InstrumentationRegistry.getInstrumentation().targetContext

    @Test
    fun searchBarDisplaysPlaceholderText() {
        composeTestRule.setContent {
            SearchBar(query = "", onQueryChanged = { })
        }
        composeTestRule
            .onNodeWithText(context.getString(R.string.schedule_search_bar_hint))
            .assertExists()
    }

    @Test
    fun searchBarUpdatesQueryText() {
        val query = "sample query"
        composeTestRule.setContent {
            SearchBar(query = query, onQueryChanged = {})
        }

        composeTestRule
            .onNodeWithText(query)
            .assertExists()
    }

    @Test
    fun searchBarLeadingIconExists() {
        composeTestRule.setContent {
            SearchBar(query = "", onQueryChanged = {})
        }

        composeTestRule
            .onNodeWithContentDescription(context.getString(R.string.schedule_search_bar_hint))
            .assertExists()
    }

    @Test
    fun searchBarOnValueChanged() {
        var newQuery = ""
        composeTestRule.setContent {
            MyTvShowsTheme {
                SearchBar(query = "", onQueryChanged = { newQuery = it })
            }
        }

        val inputText = "New Search Query"
        composeTestRule
            .onNodeWithTag(SEARCH_BAR_TT)
            .performTextInput(inputText)

        assert(newQuery == inputText)
    }
}