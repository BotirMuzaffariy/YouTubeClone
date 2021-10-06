package uz.lycr.lesson26mvvmyoutubeapi.utils

import java.util.*

object Functions{

    fun sortDate(str: String): String {
        val calendar = Calendar.getInstance()
        val curYear = calendar.get(Calendar.YEAR)
        val curMonth = calendar.get(Calendar.MONTH) + 1
        val curDay = calendar.get(Calendar.DAY_OF_MONTH)
        val curMinute = calendar.get(Calendar.MINUTE)
        val curHour = calendar.get(Calendar.HOUR_OF_DAY)

        val year = str.substring(0, 4).toInt()
        val month = str.substring(5, 7).toInt()
        val day = str.substring(8, 10).toInt()
        val hour = str.substring(11, 13).toInt()
        val minute = str.substring(14, 16).toInt()

        if (curYear == year) {
            if (curMonth == month) {
                if (curDay == day) {
                    if (curHour == hour) {
                        if (curMinute == minute) {
                            return "1 minute ago"
                        } else {
                            val i = curMinute - minute
                            return if (i == 1) "1 minute ago" else "$i minute ago"
                        }
                    } else {
                        val i = curHour - hour
                        return if (i == 1) "1 hour ago" else "$i hours ago"
                    }
                } else {
                    val i = curDay - day
                    return when {
                        i == 1 -> "1 day ago"
                        i >= 7 -> "1 week ago"
                        i >= 14 -> "2 weeks ago"
                        i >= 21 -> "3 weeks ago"
                        i >= 28 -> "4 weeks ago"
                        else -> "$i days ago"
                    }
                }
            } else {
                val i = curMonth - month
                return if (i == 1) "1 month ago" else "$i months ago"
            }
        } else {
            val i = curYear - year
            return if (i == 1) "1 year ago" else "$i years ago"
        }
    }

    fun sortViewCount(str: String): String {
        if (str.length <= 3) {
            return "$str"
        } else if (str.length == 4 && str.substring(1, 2) == "0") {
            return "${str.substring(0, 1)}K"
        } else if (str.length == 4 && str.substring(1, 2) != "0") {
            return "${str.substring(0, 1)}.${str.substring(1, 2)}K"
        } else if (str.length == 5 && str.substring(2, 3) == "0") {
            return "${str.substring(0, 2)}K"
        } else if (str.length == 5 && str.substring(2, 3) != "0") {
            return "${str.substring(0, 2)}.${str.substring(2, 3)}K"
        } else if (str.length == 6 && str.substring(3, 4) == "0") {
            return "${str.substring(0, 3)}K"
        } else if (str.length == 6 && str.substring(3, 4) != "0") {
            return "${str.substring(0, 3)}.${str.substring(3, 4)}K"
        } else if (str.length == 7 && str.substring(1, 2) == "0") {
            return "${str.substring(0, 1)}M"
        } else if (str.length == 7 && str.substring(1, 2) != "0") {
            return "${str.substring(0, 1)}.${str.substring(1, 2)}M"
        } else if (str.length == 8 && str.substring(2, 3) == "0") {
            return "${str.substring(0, 2)}M"
        } else if (str.length == 8 && str.substring(2, 3) != "0") {
            return "${str.substring(0, 2)}.${str.substring(2, 3)}M"
        } else if (str.length == 9 && str.substring(3, 4) == "0") {
            return "${str.substring(0, 3)}M"
        } else if (str.length == 9 && str.substring(3, 4) != "0") {
            return "${str.substring(0, 3)}.${str.substring(3, 4)}M"
        } else if (str.length == 10) {
            return "${str.substring(0, 1)}B"
        } else if (str.length == 11) {
            return "${str.substring(0, 2)}B"
        }
        return str
    }

}