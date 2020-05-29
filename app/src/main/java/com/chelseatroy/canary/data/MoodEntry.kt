package com.chelseatroy.canary.data

import android.util.Log
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MoodEntry(mood: Mood) {
    val mood = mood
    var notes: String = ""
    var loggedAt: Long = Calendar.getInstance().timeInMillis
    var recentPastimes = arrayListOf<Pastime>()

    constructor(
        mood: Mood,
        createdTimeInMillis: Long,
        notes: String,
        recentPastimes: String?
    ) : this(mood) {
        this.loggedAt = createdTimeInMillis
        this.notes = notes
        if(recentPastimes != null) {
            this.recentPastimes = hydrateFromDatabase(recentPastimes)
        } else {
            this.recentPastimes = ArrayList()
        }
        Log.i("ENTRY CONSTRUCTED!", this.toString())
    }

    companion object {
        @JvmStatic
        fun getFormattedLogTime(loggedAt: Long): String? {
            val DATE_FORMAT = "EEEE, dd MMM, yyyy, hh:mm a"
            val dateFormat = SimpleDateFormat(DATE_FORMAT)
            dateFormat.setTimeZone(TimeZone.getDefault())
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = loggedAt
            return dateFormat.format(calendar.getTime())
        }



        @JvmStatic
        fun formatForDatabase(pastimes: ArrayList<Pastime>): String {
            return pastimes.joinToString { it.toString() }
        }

        @JvmStatic
        fun hydrateFromDatabase(pastimes: String): ArrayList<Pastime> {
            if (pastimes.isNotEmpty()) {
                return ArrayList(
                    pastimes.split(", ")
                        .map { Pastime.valueOf(it) })
            } else {
                return ArrayList<Pastime>()
            }

        }

        fun formatForView(pastimes: ArrayList<Pastime>): String {
            val stringifiedPastimes = pastimes.joinToString { it.toString() }
            return if (stringifiedPastimes.isEmpty()) "None :(" else stringifiedPastimes
        }
    }

    override fun toString(): String {
        return "Mood Entry(" +
                "loggedAt: ${getFormattedLogTime(this.loggedAt)}, " +
                "mood: ${this.mood}, " +
                "notes: ${this.notes}, " +
                "pastimes: ${this.recentPastimes}"
    }
}

enum class Mood {
    UPSET, DOWN, NEUTRAL, COPING, ELATED
}

enum class Pastime {
    EXERCISE,
    MEDITATION,
    SOCIALIZING,
    HYDRATING,
    EATING,
    BIRDWATCHING,
    READING,
    TELEVISION,
    SLEEP,
    STRETCHING,
    CARING_FOR_OTHERS,
    JOURNALING
}