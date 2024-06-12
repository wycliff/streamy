package com.movies.streamy.utils

import timber.log.Timber
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class DateTimeUtils {

    companion object {

        @Throws(ParseException::class)
        private fun convertStringToDate(dateString: String): Date {
            val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            formatter.timeZone = TimeZone.getTimeZone("UTC")
            return formatter.parse(dateString)
        }

        fun dateToString(dateString: String): String? {
            return try {
                val date = convertStringToDate(dateString)
                val dateFormat = DateFormat.getDateTimeInstance(
                    DateFormat.MEDIUM, DateFormat.SHORT, Locale.getDefault()
                )
                dateFormat.format(date)
            } catch (e: ParseException) {
                Timber.w(e)
                ""
            }
        }

        @Throws(ParseException::class)
        fun formatStringToDate(dateString: String?): Date? {
            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            simpleDateFormat.timeZone = TimeZone.getTimeZone("UTC")
            return simpleDateFormat.parse(dateString)
        }

        fun getCurrentDate(): String {
            val formatter = SimpleDateFormat("yyyy-MM-dd")
            val date = Date()
            return formatter.format(date)
        }

        fun getDayOfWeek(date: Date?): String {
            val sdf = SimpleDateFormat("EEEE")
            val d = date
            return sdf.format(d)
        }


        fun getTime(date: Date?): Int {
            val cal = Calendar.getInstance()
            if (date != null) {
                cal.time = date
            }
            return cal.get(Calendar.HOUR_OF_DAY)
        }

        fun formatPositionDateTime(date: Date?): String {
            val dateTimeFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            dateTimeFormat.timeZone = TimeZone.getTimeZone("UTC")
            return dateTimeFormat.format(date)
        }

        fun dateToUserReadableString(dateString: String): String? {
            return try {
                val date = formatStringToDate(dateString)
                val dateFormat = DateFormat.getDateTimeInstance(
                    DateFormat.MEDIUM, DateFormat.SHORT, Locale.getDefault()
                )
                dateFormat.format(date)
            } catch (e: ParseException) {
                Timber.w(e)
                ""
            }
        }

        @Throws(ParseException::class)
        fun formatLStringToDate(dateString: String?): Date? {
            val simpleDateFormat =
                SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            //simpleDateFormat.timeZone = TimeZone.getTimeZone("UTC")
            return simpleDateFormat.parse(dateString)
        }
    }
}