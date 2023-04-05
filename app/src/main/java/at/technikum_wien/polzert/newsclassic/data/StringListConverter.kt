package at.technikum_wien.polzert.newsclassic.data

import androidx.room.TypeConverter
import java.text.SimpleDateFormat
import java.util.*

private const val DELIMITER = "____________"

class StringListConverter {

    @TypeConverter
    fun convertListToString(input: Set<String>): String {
        return input.joinToString(DELIMITER)
    }

    @TypeConverter
    fun convertStringToList(input: String): Set<String> {
        return input.split(DELIMITER).toSet()
    }

    @TypeConverter
    fun convertDateToString(input: Date): String {
        return input.toString()
    }

    @TypeConverter
    fun convertStringToDate(input: String): Date? {
        val format = SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH)
        return try {
        return format.parse(input)?: Date()
        } catch (e: Exception) {
            Date()
        }
    }

}