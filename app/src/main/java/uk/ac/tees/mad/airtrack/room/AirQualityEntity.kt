package uk.ac.tees.mad.airtrack.room

import androidx.room.Entity
import androidx.room.PrimaryKey



@Entity
data class AirQualityEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,

    val temp_c: Double,
    val wind_kph: Double,

    val co: Double,
    val gb_defra_index: Int,
    val no2: Double,
    val o3: Double,
    val pm10: Double,
    val pm2_5: Double,
    val so2: Double,
    val us_epa_index: Int
)
