package uk.ac.tees.mad.airtrack.room

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert


@Dao
interface AirQualityDao {

    @Upsert
    suspend fun addAirQuality(data: AirQualityEntity)

    @Query("SELECT * FROM airqualityentity WHERE id=1")
    suspend fun getAirQuality(): AirQualityEntity
}