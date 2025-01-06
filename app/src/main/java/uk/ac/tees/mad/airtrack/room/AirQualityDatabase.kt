package uk.ac.tees.mad.airtrack.room

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(
    entities = [AirQualityEntity::class],
    version = 1
)
abstract class AirQualityDatabase: RoomDatabase() {

    abstract fun database(): AirQualityDao
}