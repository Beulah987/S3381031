package uk.ac.tees.mad.airtrack.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import uk.ac.tees.mad.airtrack.R
import uk.ac.tees.mad.airtrack.ui.theme.AirTrackTheme
import uk.ac.tees.mad.airtrack.ui.theme.poppinsFamily


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = modifier.height(75.dp))
        Text(
            text = "Profile Screen",
            fontSize = 20.sp,
            fontFamily = poppinsFamily
        )

        GlideImage(
            modifier = modifier
                .height(300.dp)
                .aspectRatio(1f, matchHeightConstraintsFirst = true)
                .border(
                    width = 1.dp,
                    color = Color.Black,
                    shape = CircleShape
                    )
                .padding(1.dp)
                .clip(CircleShape),
            model = "",
            failure = placeholder(R.drawable.avatar),
            contentDescription = "Profile Picture"
        )

    }
}

@PreviewLightDark
@Composable
fun ProfilePreview(){
    AirTrackTheme {
        ProfileScreen()
    }
}