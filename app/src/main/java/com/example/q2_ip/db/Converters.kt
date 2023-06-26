package com.example.q2_ip.db

import androidx.room.TypeConverter
import com.example.q2_ip.model.Source

class Converters {

    @TypeConverter
    fun fromSource(source: Source):String{
        return source.name
    }

    @TypeConverter
    fun toSource(name:String): Source {
        return Source(name,name)
    }
}