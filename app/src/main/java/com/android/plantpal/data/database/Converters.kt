package com.android.plantpal.data.database
import androidx.room.TypeConverter
import com.android.plantpal.data.remote.response.Plant
import com.android.plantpal.data.remote.response.User
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {

    private val gson = Gson()

    @TypeConverter
    fun fromPlant(plant: Plant?): String? {
        return gson.toJson(plant)
    }

    @TypeConverter
    fun toPlant(plantJson: String?): Plant? {
        val type = object : TypeToken<Plant>() {}.type
        return gson.fromJson(plantJson, type)
    }

    @TypeConverter
    fun fromUser(user: User?): String? {
        return gson.toJson(user)
    }

    @TypeConverter
    fun toUser(userJson: String?): User? {
        val type = object : TypeToken<User>() {}.type
        return gson.fromJson(userJson, type)
    }
}
