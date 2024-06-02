package com.omarzg94.mytvshows.repository

import arrow.core.Either
import com.omarzg94.mytvshows.data.model.NetworkResult
import com.omarzg94.mytvshows.data.model.Show
import com.omarzg94.mytvshows.data.network.TvMazeService
import java.io.IOException
import javax.inject.Inject

class ShowRepository @Inject constructor(
    private val tvMazeService: TvMazeService
) {
    suspend fun getSchedule(): Either<NetworkResult.Error, NetworkResult.Success<List<Show>>> {
        return try {
            val response = tvMazeService.getSchedule()
            if (response.isSuccessful) {
                Either.Right(NetworkResult.Success(response.body()!!))
            } else {
                Either.Left(NetworkResult.HttpError(response.code()))
            }
        } catch (e: IOException) {
            Either.Left(NetworkResult.NetworkError)
        }
    }

    suspend fun getShowDetails(id: Int): Either<NetworkResult.Error, NetworkResult.Success<Show>> {
        return try {
            val response = tvMazeService.getShowDetails(id)
            if (response.isSuccessful) {
                Either.Right(NetworkResult.Success(response.body()!!))
            } else {
                Either.Left(NetworkResult.HttpError(response.code()))
            }
        } catch (e: IOException) {
            Either.Left(NetworkResult.NetworkError)
        }
    }
}