package com.omarzg94.mytvshows.data.model

import com.omarzg94.mytvshows.utils.Constants.BAD_REQUEST
import com.omarzg94.mytvshows.utils.Constants.FORBIDDEN
import com.omarzg94.mytvshows.utils.Constants.NETWORK_ERROR
import com.omarzg94.mytvshows.utils.Constants.NOT_FOUND
import com.omarzg94.mytvshows.utils.Constants.NO_CONTENT
import com.omarzg94.mytvshows.utils.Constants.PAYMENT_REQUIRED
import com.omarzg94.mytvshows.utils.Constants.UNAUTHORIZED
import com.omarzg94.mytvshows.utils.Constants.UNKNOWN_ERROR

sealed class NetworkResult<out T> {
    data class Success<out T>(val data: T) : NetworkResult<T>()

    /* Error */
    sealed class Error(val message: String?) : NetworkResult<Nothing>()
    data class HttpError(val code: Int) : Error(HttpErrorStatus.getMessageFromCode(code))
    data object NetworkError : Error(NETWORK_ERROR)
}

internal enum class HttpErrorStatus(val code: Int, val message: String) {
    DEFAULT(0, UNKNOWN_ERROR),
    HTTP_204(204, NO_CONTENT),
    HTTP_400(400, BAD_REQUEST),
    HTTP_401(401, UNAUTHORIZED),
    HTTP_402(402, PAYMENT_REQUIRED),
    HTTP_403(403, FORBIDDEN),
    HTTP_404(404, NOT_FOUND);

    companion object {
        fun getMessageFromCode(code: Int): String {
            return when (code) {
                HTTP_204.code -> HTTP_204.message
                HTTP_400.code -> HTTP_400.message
                HTTP_401.code -> HTTP_401.message
                HTTP_402.code -> HTTP_402.message
                HTTP_403.code -> HTTP_403.message
                HTTP_404.code -> HTTP_404.message
                else -> DEFAULT.message
            }
        }
    }
}