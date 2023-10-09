package com.example.netplix.utils

class Constants {
    companion object {
        const val IS_POP: String = "IS_POP"
        const val BASE_URL = "https://api.themoviedb.org/3/"
        const val Links_BASE_URL = "https://api.themoviedb.org/3/"
        const val IMAGES_BASE = "https://image.tmdb.org/t/p/w500"
        const val POSTER_URL = "POSTER_URL"
        const val MOVIE_ID = "MOVIE_ID"
        const val TV_ID = "TV_ID"
        const val IS_MOVE = "IS_MOVE"
        const val NETPLIX_DB = "NETPLIX_DB"
        const val NETPLIX_DATABASE = "NETPLIX_DATABASE"
        const val NETPLIX_USERS = "NETPLIX_USERS"
        const val MOVIES_LIST = "MOVIES_LIST"
        const val TV_LIST = "TV_LIST"
        const val PASSWORD_REGEX = "^(?=.*[A-Z])(?=.*[!@#\$%^&*])[a-zA-Z0-9!@#\$%^&*]{8,}\$"
        const val _8CHAR_PASSWORD_REGEX = "^[a-zA-Z0-9!@#\$%^&*]{8,}\$"
        const val PHONE_REGEX = "^(010|011|015|012)\\d{8}\$"
        const val FEMALE_AVATAR = "gs://netplix-a2240.appspot.com/female_avatar.jpg"
        const val MALE_AVATAR = "gs://netplix-a2240.appspot.com/male_avatar.jpg"
    }
}