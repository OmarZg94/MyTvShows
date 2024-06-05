package com.omarzg94.mytvshows.ui.view

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.omarzg94.mytvshows.R
import java.util.Calendar

@Composable
fun rememberDatePickerDialog(
    onDateSet: (year: Int, month: Int, day: Int) -> Unit,
    minDate: Long? = null,
    maxDate: Long? = null
): DatePickerDialog {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val datePickerDialog = remember {
        DatePickerDialog(
            context,
            R.style.DatePickerTheme,
            { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
                onDateSet(year, month, dayOfMonth)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).apply {
            minDate?.let { datePicker.minDate = it }
            maxDate?.let { datePicker.maxDate = it }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            datePickerDialog.setOnDismissListener(null)
        }
    }

    return datePickerDialog
}