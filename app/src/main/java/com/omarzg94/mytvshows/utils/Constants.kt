package com.omarzg94.mytvshows.utils

import androidx.compose.ui.unit.dp

object Constants {
    const val EMPTY = ""
    const val YEAR_MONTH_DAY_FORMAT = "yyyy-MM-dd"
    const val HOUR_MINUTE_FORMAT = "HH:mm"

    const val NETWORK_ERROR = "Network error, please try again later."
    const val UNKNOWN_ERROR = "Unknown Error"

    /* HTTP status error messages */
    const val NO_CONTENT = "No Content"
    const val BAD_REQUEST = "Bad Request"
    const val UNAUTHORIZED = "Unauthorized"
    const val PAYMENT_REQUIRED = "Payment Required"
    const val FORBIDDEN = "Forbidden"
    const val NOT_FOUND = "Not Found"
    const val METHOD_NOT_ALLOWED = "Method Not Allowed"
    const val REQUEST_TIMEOUT = "Request Timeout"
    const val INTERNAL_SERVER_ERROR = "Internal Server Error"
    const val SERVICE_UNAVAILABLE = "Service Unavailable"

    /* Paddings */
    val SmallPadding = 8.dp
    val NormalPadding = 16.dp
    val NormalPlusPadding = 24.dp
    val MediumPlusPadding = 70.dp
    val LargePadding = 200.dp
}