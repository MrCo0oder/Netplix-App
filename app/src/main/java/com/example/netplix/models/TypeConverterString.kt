package com.example.netplix.models

import androidx.room.TypeConverter

object TypeConverterString {
    @TypeConverter
    @JvmStatic
    fun toListOfStrings(flatStringList: String): List<String> {
        return flatStringList.split(",")
    }

    @TypeConverter
    @JvmStatic
    fun fromListOfStrings(listOfString: List<String>): String {
        return listOfString.joinToString(",")
    }
}
