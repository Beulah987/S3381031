package uk.ac.tees.mad.airtrack.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import uk.ac.tees.mad.airtrack.ui.mainapp.model.AirQuality
import uk.ac.tees.mad.airtrack.ui.theme.poppinsFamily

@Composable
fun DetailsScreen(
    airQuality: AirQuality,
    modifier: Modifier = Modifier
) {

    Column (
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){

        Text(
            text = "Air Pollutants",
            fontSize = 28.sp,
            fontFamily = poppinsFamily,
            fontWeight = FontWeight.Bold
        )

        Row(
            modifier = modifier
                .fillMaxWidth(0.9f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Text(
                text = "pm10",
                fontSize = 18.sp,
                fontFamily = poppinsFamily
            )
            Text(
                text = "${airQuality.pm10}",
                fontSize = 18.sp,
                fontFamily = poppinsFamily
            )
        }

        HorizontalDivider()

        Row(
            modifier = modifier
                .fillMaxWidth(0.9f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Text(
                text = "pm2.5",
                fontSize = 18.sp,
                fontFamily = poppinsFamily
            )
            Text(
                text = "${airQuality.pm2_5}",
                fontSize = 18.sp,
                fontFamily = poppinsFamily
            )
        }

        HorizontalDivider()

        Row(
            modifier = modifier
                .fillMaxWidth(0.9f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Text(
                text = "Carbon Monoxide",
                fontSize = 18.sp,
                fontFamily = poppinsFamily
            )
            Text(
                text = "${airQuality.co}",
                fontSize = 18.sp,
                fontFamily = poppinsFamily
            )
        }

        HorizontalDivider()

        Row(
            modifier = modifier
                .fillMaxWidth(0.9f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Text(
                text = "Nitrogen Dioxide",
                fontSize = 18.sp,
                fontFamily = poppinsFamily
            )
            Text(
                text = "${airQuality.no2}",
                fontSize = 18.sp,
                fontFamily = poppinsFamily
            )
        }

        HorizontalDivider()

        Row(
            modifier = modifier
                .fillMaxWidth(0.9f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Text(
                text = "Trioxygen",
                fontSize = 18.sp,
                fontFamily = poppinsFamily
            )
            Text(
                text = "${airQuality.o3}",
                fontSize = 18.sp,
                fontFamily = poppinsFamily
            )
        }

        HorizontalDivider()

        Row(
            modifier = modifier
                .fillMaxWidth(0.9f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Text(
                text = "Sulphur Dioxide",
                fontSize = 18.sp,
                fontFamily = poppinsFamily
            )
            Text(
                text = "${airQuality.so2}",
                fontSize = 18.sp,
                fontFamily = poppinsFamily
            )
        }

        HorizontalDivider()
    }

}