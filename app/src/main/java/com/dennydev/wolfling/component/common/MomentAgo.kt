package com.dennydev.wolfling.component.common

import org.ocpsoft.prettytime.PrettyTime
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

fun MomentAgo(date: String): String{
    val prettyTime = PrettyTime(Locale.getDefault())
    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    sdf.setTimeZone(TimeZone.getTimeZone("GMT"))
    val time: Long = sdf.parse(date)?.getTime() ?: Date().time
    return prettyTime.format(Date(time));
}