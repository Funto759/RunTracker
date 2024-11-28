package com.example.runtracker.dao

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.room.TypeConverter
import java.io.ByteArrayOutputStream

class Converter {

    @TypeConverter
    fun fromBitmap(bmp: Bitmap) : ByteArray{
        val outPutStream = ByteArrayOutputStream()
        bmp.compress(Bitmap.CompressFormat.PNG, 100, outPutStream)
        return outPutStream.toByteArray()
    }

    @TypeConverter
    fun toBitmap(ba : ByteArray): Bitmap{
        return BitmapFactory.decodeByteArray(ba,0,ba.size)
    }
}