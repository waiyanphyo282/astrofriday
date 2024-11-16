package com.waiyanphyo.astrofriday.domain.util

import java.time.Duration
import java.util.Locale

fun Duration.toFormattedString(): String {
    if (this.isZero) return "0 minutes"
    val hours = this.toHours()
    val minutes = this.minusHours(hours).toMinutes()
    return String.format(Locale.getDefault(), "%02d hours %02d minutes", hours, minutes)
}

fun Double.toHumanReadableDistance(): String {
    return "%.2f".format(this)
}