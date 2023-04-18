package com.example.trucksload.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.trucksload.util.Constants.DATABASE_TABLE_USER
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Entity(tableName = DATABASE_TABLE_USER)
@Parcelize
class TruckLoadEntity(
    val uid: String,
    val firstName: String,
    val lastName: String
): Parcelable {
    @IgnoredOnParcel
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}