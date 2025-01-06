package uk.ac.tees.mad.airtrack.ui.mainapp

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import androidx.room.util.appendPlaceholders
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import uk.ac.tees.mad.airtrack.room.AirQualityDatabase
import uk.ac.tees.mad.airtrack.room.AirQualityEntity
import uk.ac.tees.mad.airtrack.ui.mainapp.model.AirQualityInfo

class MainViewModel(
    application: Application
): AndroidViewModel(application) {

    private val qualityRoom = Room.databaseBuilder(
        application.applicationContext,
        AirQualityDatabase::class.java,
        "quality.db"
    ).build()

    private val qualitydb = qualityRoom.database()

    private val api = "65370aac83d8458981b180030243108"

    private val _airQuality = MutableStateFlow<AirQualityInfo?>(null)
    val airQuality = _airQuality.asStateFlow()

    private val _roomAirQuality = MutableStateFlow<AirQualityEntity?>(null)
    val roomAirQuality = _roomAirQuality.asStateFlow()

    fun getFromAPI(query: String){
        viewModelScope.launch {
            try{
                val airData = RetrofitInstance.api.getAirQuality(
                    api,
                    query,
                    "yes"
                )

                _airQuality.value = airData

                Log.i("The Data: ", "Air Quality is $airData for $query")

            }catch (e: Exception){

                getfromRoom()
                Log.i("Error", "Cannot fetch the air Quality from the api ${e.message} for $query")
            }
        }
    }

    fun getfromRoom() {
        viewModelScope.launch {
            val data = qualitydb.getAirQuality()
            _roomAirQuality.value = data
        }
    }

    fun addToRoom(data: AirQualityEntity){
        viewModelScope.launch {
            qualitydb.addAirQuality(data)
        }
    }
}