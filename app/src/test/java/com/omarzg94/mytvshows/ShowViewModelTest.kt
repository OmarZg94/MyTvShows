package com.omarzg94.mytvshows

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import arrow.core.Either
import com.omarzg94.mytvshows.data.model.Episode
import com.omarzg94.mytvshows.data.model.HttpErrorStatus
import com.omarzg94.mytvshows.data.model.Image
import com.omarzg94.mytvshows.data.model.NetworkResult
import com.omarzg94.mytvshows.data.model.Rating
import com.omarzg94.mytvshows.data.model.Schedule
import com.omarzg94.mytvshows.data.model.Show
import com.omarzg94.mytvshows.data.model.ShowNetwork
import com.omarzg94.mytvshows.data.model.UiState
import com.omarzg94.mytvshows.repository.ShowRepository
import com.omarzg94.mytvshows.ui.viewmodel.ShowViewModel
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@ExperimentalCoroutinesApi
class ShowViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    @Mock
    private lateinit var repository: ShowRepository

    private lateinit var viewModel: ShowViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        viewModel = ShowViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `fetchSchedule returns success`() = runTest {
        val date = SimpleDateFormat(
            "yyyy-MM-dd",
            Locale.getDefault()
        ).format(Calendar.getInstance().time)
        val episodes = listOf(
            Episode(id = 1, name = "Episode 1", season = 1, number = 1, airtime = "20:00", runtime = 60, show = mockShow(), airdate = "2023-07-01"),
            Episode(id = 2, name = "Episode 2", season = 1, number = 2, airtime = "21:00", runtime = 60, show = mockShow(), airdate = "2023-05-08")
        )
        whenever(repository.getSchedule(date)).thenReturn(Either.Right(NetworkResult.Success(episodes)))

        viewModel.fetchSchedule(date)
        testDispatcher.scheduler.advanceUntilIdle()

        val result = viewModel.schedule.first()
        assertEquals(UiState.Success(episodes), result)
    }

    @Test
    fun `fetchSchedule returns error`() = runTest {
        val date = SimpleDateFormat(
            "yyyy-MM-dd",
            Locale.getDefault()
        ).format(Calendar.getInstance().time)
        whenever(repository.getSchedule(date)).thenReturn(Either.Left(NetworkResult.HttpError(400)))

        viewModel.fetchSchedule(date)
        testDispatcher.scheduler.advanceUntilIdle()

        val result = viewModel.schedule.first()
        assertEquals(UiState.Error(HttpErrorStatus.getMessageFromCode(400)), result)
    }

    private fun mockShow() = Show(
        id = 1,
        name = "Sample Show",
        image = Image(medium = "https://via.placeholder.com/150", original = "https://via.placeholder.com/300"),
        network = ShowNetwork(id = 1, name = "Sample Network", officialSite = "https://www.sample.com"),
        summary = "This is a sample show summary.",
        genres = arrayListOf("Drama", "Comedy"),
        schedule = Schedule(days = listOf("Monday"), time = "20:00"),
        ended = null,
        premiered = null,
        rating = Rating(4.5F)
    )
}