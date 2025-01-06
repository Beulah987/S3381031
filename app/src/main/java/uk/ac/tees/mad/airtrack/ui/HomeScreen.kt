package uk.ac.tees.mad.airtrack.ui

import android.Manifest
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.location.LocationServices
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import uk.ac.tees.mad.airtrack.room.AirQualityEntity
import uk.ac.tees.mad.airtrack.ui.auth.getLocation
import uk.ac.tees.mad.airtrack.ui.mainapp.MainViewModel
import uk.ac.tees.mad.airtrack.ui.mainapp.model.AirQuality
import uk.ac.tees.mad.airtrack.ui.theme.poppinsFamily

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HomeScreen(
    navController: NavHostController,
    mainViewModel: MainViewModel,
    modifier: Modifier = Modifier
) {

    val (location, setLocation) = remember { mutableStateOf("") }
    val (latitude, setLatitude) = remember { mutableStateOf(0.0) }
    val (longitude, setLongitude) = remember { mutableStateOf(0.0) }
    val context = LocalContext.current
    val locationClient = LocationServices.getFusedLocationProviderClient(context)

    val locationPermission = rememberMultiplePermissionsState(
        listOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    )

    val airQuality by mainViewModel.airQuality.collectAsState()
    val roomAirQuality by mainViewModel.roomAirQuality.collectAsState()

    var cityToSearch by remember { mutableStateOf("") }
    mainViewModel.getFromAPI(location)
    if (airQuality != null){
        mainViewModel.addToRoom(AirQualityEntity(
            id = 1,
            temp_c = airQuality!!.current.temp_c,
            wind_kph = airQuality!!.current.wind_kph,
            co = airQuality!!.current.air_quality.co,
            gb_defra_index = airQuality!!.current.air_quality.gb_defra_index,
            no2 = airQuality!!.current.air_quality.no2,
            o3 = airQuality!!.current.air_quality.o3,
            pm10 = airQuality!!.current.air_quality.pm10,
            pm2_5 = airQuality!!.current.air_quality.pm2_5,
            so2 = airQuality!!.current.air_quality.so2,
            us_epa_index = airQuality!!.current.air_quality.us_epa_index
        ))
    }

    LaunchedEffect(Unit) {
        locationPermission.launchMultiplePermissionRequest()
        CoroutineScope(Dispatchers.IO).launch {
            getLocation(
                locationClient,
                context,
                setLatitude,
                setLongitude,
                setLocation
            )
        }

    }

    Column(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ){
        Spacer(modifier.height(75.dp))

        OutlinedTextField(
            modifier = modifier
                .fillMaxWidth(0.9f),
            shape = RoundedCornerShape(15.dp),
            value = cityToSearch,
            onValueChange = {
                cityToSearch = it
            },
            label = {
                Text(
                    text = "Enter full name of city",
                    fontSize = 16.sp,
                    fontFamily = poppinsFamily
                )
            }
        )

        Spacer(modifier.height(15.dp))

        Button(
            modifier = modifier
                .fillMaxWidth(0.8f),
            border = BorderStroke(width = 2.dp, color = Color.Black),
            shape = RoundedCornerShape(15.dp),
            onClick = {
                mainViewModel.getFromAPI(cityToSearch)
                setLocation(cityToSearch)
            }
        ) {
            Text(
                text = "Search",
                fontSize = 16.sp,
                fontFamily = poppinsFamily
            )
        }

        Spacer(modifier.height(15.dp))

        Button(
            modifier = modifier
                .fillMaxWidth(0.8f),
            border = BorderStroke(width = 2.dp, color = Color.Black),
            shape = RoundedCornerShape(15.dp),
            onClick = {
                mainViewModel.getFromAPI(location)
            }
        ) {
            Text(
                text = "Refresh!!",
                fontSize = 16.sp,
                fontFamily = poppinsFamily
            )
        }

        Spacer(modifier.height(50.dp))

        Card (
            modifier = modifier
                .fillMaxWidth(0.9f),
            border = BorderStroke(width = 2.dp, color = Color.Black)
        ){
            if (cityToSearch.isNotEmpty()){
                Column (
                    modifier = modifier.padding(horizontal = 10.dp, vertical = 10.dp)
                        .clickable {
                            val airQualityJson = Gson().toJson(airQuality?.current?.air_quality)
                            navController.navigate("details_screen/$airQualityJson")
                        }
                ){
                    Text(
                        text = location,
                        fontSize = 20.sp,
                        fontFamily = poppinsFamily,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier.height(20.dp))

                    Text(
                        text = "Air Quality pm10: ${airQuality?.current?.air_quality?.pm10}",
                        fontSize = 18.sp,
                        fontFamily = poppinsFamily
                    )

                    Spacer(modifier.height(20.dp))

                    Text(
                        text = "Temperature: ${airQuality?.current?.temp_c} °C",
                        fontSize = 18.sp,
                        fontFamily = poppinsFamily
                    )

                    Spacer(modifier.height(20.dp))

                    Text(
                        text = "Humidity: ${airQuality?.current?.wind_kph} km/h",
                        fontSize = 18.sp,
                        fontFamily = poppinsFamily
                    )
                }
            }else{
                Column (
                    modifier = modifier
                        .padding(horizontal = 10.dp, vertical = 10.dp)
                        .clickable {
                            val passingData = AirQuality(
                                co = roomAirQuality!!.co,
                                gb_defra_index = roomAirQuality!!.gb_defra_index,
                                no2 = roomAirQuality!!.no2,
                                o3 = roomAirQuality!!.o3,
                                pm10 = roomAirQuality!!.pm10,
                                pm2_5 = roomAirQuality!!.pm2_5,
                                so2 = roomAirQuality!!.so2,
                                us_epa_index = roomAirQuality!!.us_epa_index
                            )
                            val airQualityJson = Gson().toJson(passingData)
                            navController.navigate("details_screen/$airQualityJson")
                        }
                ){
                    Text(
                        text = location,
                        fontSize = 20.sp,
                        fontFamily = poppinsFamily,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier.height(20.dp))

                    Text(
                        text = "Air Quality pm10: ${roomAirQuality?.pm10}",
                        fontSize = 18.sp,
                        fontFamily = poppinsFamily
                    )

                    Spacer(modifier.height(20.dp))

                    Text(
                        text = "Temperature: ${roomAirQuality?.temp_c} °C",
                        fontSize = 18.sp,
                        fontFamily = poppinsFamily
                    )

                    Spacer(modifier.height(20.dp))

                    Text(
                        text = "Wind Speed: ${roomAirQuality?.wind_kph} km/h",
                        fontSize = 18.sp,
                        fontFamily = poppinsFamily
                    )
                }
            }
        }

        Spacer(modifier.height(20.dp))

        Button(
            onClick = {
                navController.navigate("profile_screen")
            }
        ) {
            Text(
                text = "Profile Screen",
                fontSize = 18.sp,
                fontFamily = poppinsFamily
            )
        }
    }
}