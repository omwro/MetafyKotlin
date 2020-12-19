package nl.omererdem.metafy.model

import kotlin.math.floor

private const val HOUR_FROM_MILLISECONDS = 3600000.0
private const val MINUTE_FROM_MILLISECONDS = 60000.0
private const val SECOND_FROM_MILLISECONDS = 1000.0

class SongDuration(
    private val hour: Int,
    private val minute: Int,
    private val second: Int,
    private val millisecond: Int
) {
    fun getTotalMilliseconds(): Int {
        var ms = millisecond
        ms += (second * SECOND_FROM_MILLISECONDS).toInt()
        ms += (minute * MINUTE_FROM_MILLISECONDS).toInt()
        ms += (hour * HOUR_FROM_MILLISECONDS).toInt()
        return ms
    }

    companion object {
        fun createFromMilliseconds(milliseconds: Int): SongDuration {
            var remainingMilliseconds = milliseconds
            val hours = floor(remainingMilliseconds / HOUR_FROM_MILLISECONDS).toInt()
            remainingMilliseconds -= (hours * HOUR_FROM_MILLISECONDS).toInt()
            val minutes = floor(remainingMilliseconds / MINUTE_FROM_MILLISECONDS).toInt()
            remainingMilliseconds -= (minutes * MINUTE_FROM_MILLISECONDS).toInt()
            val seconds = floor(remainingMilliseconds / SECOND_FROM_MILLISECONDS).toInt()
            remainingMilliseconds -= (seconds * SECOND_FROM_MILLISECONDS).toInt()
            return SongDuration(hours, minutes, seconds, remainingMilliseconds)
        }
    }

    fun shortString(): String {
        var string = ""
        if (this.hour > 0) {
            string += "${this.hour}h"
        }
        if (this.minute > 0) {
            string += "${this.minute}m"
        }
        if (this.second > 0) {
            string += "${this.second}s"
        }
        return string
    }

    fun longString(): String {
        var string = ""
        if (this.hour > 0) {
            string += "${this.hour} hours"
        }
        if (string.isNotBlank()) {
            string += ", "
        }
        if (this.minute > 0) {
            string += "${this.minute} minutes"
        }
        if (string.isNotBlank()) {
            string += ", "
        }
        if (this.second > 0) {
            string += "${this.second} seconds"
        }
        return string
    }

    override fun toString(): String {
        return "SongDuration(hour=$hour, minute=$minute, second=$second, millisecond=$millisecond)"
    }
}