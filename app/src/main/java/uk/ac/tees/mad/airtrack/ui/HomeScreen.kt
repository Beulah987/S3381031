package uk.ac.tees.mad.airtrack.ui

import android.Manifest
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import uk.ac.tees.mad.airtrack.ui.auth.getLocation
import uk.ac.tees.mad.airtrack.ui.theme.poppinsFamily

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HomeScreen(
    navController: NavHostController,
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
        Text(
            text = location,
            fontSize = 20.sp,
            fontFamily = poppinsFamily,
            fontWeight = FontWeight.Bold
        )

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