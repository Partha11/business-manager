package com.supernova.bkashmanager.util

import android.app.Activity
import android.os.Build
import android.util.DisplayMetrics
import android.view.WindowInsets
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.ISODateTimeFormat


class Utils {

    companion object {

        @JvmStatic
        fun getScreenWidth(activity: Activity?): Int {

            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {

                val windowMetrics = activity?.windowManager?.currentWindowMetrics
                val insets = windowMetrics?.windowInsets
                    ?.getInsetsIgnoringVisibility(WindowInsets.Type.systemBars())
                (windowMetrics?.bounds?.width() ?: 0) - (insets?.left ?: 0) - (insets?.right ?: 0)

            } else {

                val displayMetrics = DisplayMetrics()
                activity?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
                displayMetrics.widthPixels
            }
        }

        @JvmStatic
        fun getGreetings(): String {

            val greetings = "Good"
            return when (DateTime.now().hourOfDay) {

                in 7..12 -> "$greetings Morning,"
                in 12..15 -> "$greetings Noon,"
                in 15..18 -> "$greetings Afternoon,"
                in 18..20 -> "$greetings Evening,"
                else -> "$greetings Night,"
            }
        }

        @JvmStatic
        fun getCurrentDate(): String {

            val date = DateTime()
            val format = ISODateTimeFormat.date()

            return format.print(date)
        }

        @JvmStatic
        fun getLocalTimeFromServerTime(time: String): String {

            if (time == "") {

                return time
            }

            val dtfInp = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")
            val inDate = dtfInp.parseDateTime(time)
            val dtfOut = DateTimeFormat.forPattern("hh:mm a")

            return dtfOut.print(inDate)
        }

        @JvmStatic
        fun appendLeadingZero(number: Int): String {

            if (number in 0..9) {

                return "0$number"
            }

            return "$number"
        }
    }
}