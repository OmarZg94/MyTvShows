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
    // Get the current context
    val context = LocalContext.current

    // Initialize the calendar instance
    val calendar = Calendar.getInstance()

    // Create and remember the DatePickerDialog
    val datePickerDialog = remember {
        DatePickerDialog(
            context,
            R.style.DatePickerTheme, // Apply custom theme
            { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
                // Callback when a date is selected
                onDateSet(year, month, dayOfMonth)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).apply {
            // Set minimum and maximum dates if provided
            minDate?.let { datePicker.minDate = it }
            maxDate?.let { datePicker.maxDate = it }
        }
    }

    // Handle the lifecycle of the DatePickerDialog
    DisposableEffect(Unit) {
        onDispose {
            // Clear the dismiss listener when the composable is disposed
            datePickerDialog.setOnDismissListener(null)
        }
    }

    // Return the created DatePickerDialog
    return datePickerDialog
}