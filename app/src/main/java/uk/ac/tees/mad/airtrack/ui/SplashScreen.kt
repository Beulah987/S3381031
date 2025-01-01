package uk.ac.tees.mad.airtrack.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import uk.ac.tees.mad.airtrack.R
import uk.ac.tees.mad.airtrack.ui.auth.AuthViewmodel

@Composable
fun SplashScreen(
    authViewmodel: AuthViewmodel,
    navController: NavController
) {

    val userExist by authViewmodel.userExist.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Image(
            painter = painterResource(id = R.drawable.air_quality),
            contentDescription = "App Logo",
            modifier = Modifier
                .align(Alignment.Center)
                .size(200.dp)
        )

        Text(
            text = "AirTracker",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp),
            color = Color(0xFF47A9F4)
        )
    }
    LaunchedEffect(Unit) {
        delay(2000)
        if (userExist){
            navController.navigate("home") {
                popUpTo("splash") { inclusive = true }
            }
        }else{
            navController.navigate("auth") {
                popUpTo("splash") { inclusive = true }
            }
        }
    }
}