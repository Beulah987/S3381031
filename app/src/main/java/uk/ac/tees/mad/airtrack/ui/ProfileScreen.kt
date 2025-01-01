package uk.ac.tees.mad.airtrack.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import uk.ac.tees.mad.airtrack.R
import uk.ac.tees.mad.airtrack.ui.auth.AuthViewmodel
import uk.ac.tees.mad.airtrack.ui.theme.poppinsFamily


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ProfileScreen(
    authViewmodel: AuthViewmodel,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {

    val currentUser by authViewmodel.currentUser.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF47A9F4)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = modifier.weight(2F))
        Text(
            text = "Profile Screen",
            fontSize = 28.sp,
            fontFamily = poppinsFamily,
            fontWeight = FontWeight.ExtraBold,
            color = Color.Black
        )

        Spacer(modifier = modifier.weight(1F))

        GlideImage(
            modifier = modifier
                .height(250.dp)
                .aspectRatio(1f, matchHeightConstraintsFirst = true)
                .border(
                    width = 1.dp,
                    color = Color.Black,
                    shape = CircleShape
                    )
                .padding(1.dp)
                .clip(CircleShape),
            model = currentUser?.profileUrl,
            failure = placeholder(R.drawable.avatar),
            contentDescription = "Profile Picture"
        )

        Spacer(modifier = modifier.weight(1F))

        Text(
            text = "Account Details",
            fontSize = 20.sp,
            fontFamily = poppinsFamily,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Spacer(modifier = modifier.weight(1F))

        Text(
            text = "Name: ${currentUser?.name}",
            fontFamily = poppinsFamily,
            fontSize = 18.sp,
            color = Color.Black
        )

        HorizontalDivider(modifier = modifier
            .fillMaxWidth(0.85f)
            .padding(vertical = 10.dp)
        )

        Text(
            text = "Email: ${currentUser?.email}",
            fontFamily = poppinsFamily,
            fontSize = 18.sp,
            color = Color.Black
        )

        HorizontalDivider(modifier = modifier
            .fillMaxWidth(0.85f)
            .padding(vertical = 10.dp)
        )

        Text(
            text = "Location: ${currentUser?.location}",
            fontFamily = poppinsFamily,
            fontSize = 18.sp,
            color = Color.Black
        )

        HorizontalDivider(modifier = modifier
            .fillMaxWidth(0.85f)
            .padding(vertical = 10.dp)
        )

        Spacer(modifier = modifier.weight(1F))

        Button(
            modifier = modifier
                .fillMaxWidth(0.8f),
            onClick = {
                authViewmodel.logOut()
                navController.navigate("auth") {
                    popUpTo("home") { inclusive = true }
                }
            }
        ) {
            Text(
                text = "Log Out!!",
                fontFamily = poppinsFamily,
                fontSize = 18.sp,
                color = Color.Black
            )
        }

        Spacer(modifier = modifier.weight(10F))

    }
}
