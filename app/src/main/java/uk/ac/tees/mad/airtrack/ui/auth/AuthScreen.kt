package uk.ac.tees.mad.airtrack.ui.auth

import android.Manifest
import android.content.Context
import android.location.Geocoder
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email

import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle

import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

import uk.ac.tees.mad.airtrack.R

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun AuthScreen(
    onLogin: (email: String, password: String) -> Unit,
    onSignup: (email: String, password: String, name: String, location: String, latitude: Double, longitude: Double) -> Unit
) {
    val (email, setEmail) = remember { mutableStateOf("") }
    val (password, setPassword) = remember { mutableStateOf("") }
    val (name, setName) = remember { mutableStateOf("") }
    val (location, setLocation) = remember { mutableStateOf("") }
    val (latitude, setLatitude) = remember { mutableStateOf(0.0) }
    val (longitude, setLongitude) = remember { mutableStateOf(0.0) }
    val (isLogin, setIsLogin) = remember { mutableStateOf(true) }

    val context = LocalContext.current
    val locationClient = LocationServices.getFusedLocationProviderClient(context)

    val locationPermission = rememberMultiplePermissionsState(
        listOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    )

    LaunchedEffect(key1 = Unit) {
        //Getting the current location
        try {
            locationPermission.launchMultiplePermissionRequest()
            getLocation(locationClient, context, setLatitude, setLongitude, setLocation)
        } catch (e: Exception) {
            Toast.makeText(context, "Error getting location", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF47A9F4))
    ) {
        Column(
            Modifier
                .wrapContentSize()
                .align(Alignment.TopCenter),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(top = 30.dp)
                    .clip(CircleShape)
                    .background(Color.White)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.air_quality),
                    contentDescription = "App Logo",
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(150.dp)
                        .padding(16.dp)
                )
            }
            Text(
                text = "AirTracker",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(24.dp),
                color = Color.White
            )
        }
        Column(
            modifier = Modifier
                .clip(shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)
                .align(Alignment.BottomCenter),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.size(30.dp))

            // Top bar with Login and Register buttons
            LoginAndRegisterButtons(isLogin, setIsLogin)

            Spacer(modifier = Modifier.size(36.dp))

            if (isLogin) {
                // Email and Password fields
                CustomTextField(
                    value = email,
                    onValueChange = setEmail,
                    label = "Email",
                    trailingIcon = {
                        Icon(imageVector = Icons.Default.Email, contentDescription = null)
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType. Email,
                        imeAction = ImeAction.Next
                    )
                )

                Spacer(modifier = Modifier.size(16.dp))

                CustomTextField(
                    value = password,
                    onValueChange = setPassword,
                    label = "Password",
                    trailingIcon = {
                        Icon(imageVector = Icons.Default.Lock, contentDescription = null)
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    visualTransformation = PasswordVisualTransformation()
                )

                Spacer(modifier = Modifier.size(56.dp))

                Button(
                    onClick = { onLogin(email, password) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF47A9F4)
                    )
                ) {
                    Text(text = "LOGIN")
                }
                Spacer(modifier = Modifier.size(56.dp))
            } else {
                // Name, Email, Password, and Location fields
                CustomTextField(
                    value = name,
                    onValueChange = setName,
                    label = "Name",
                    trailingIcon = {
                        Icon(imageVector = Icons.Default.Person, contentDescription = null)
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    )
                )

                Spacer(modifier = Modifier.size(16.dp))

                CustomTextField(
                    value = email,
                    onValueChange = setEmail,
                    label = "Email",
                    trailingIcon = {
                        Icon(imageVector = Icons.Default.Email, contentDescription = null)
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    )
                )

                Spacer(modifier = Modifier.size(16.dp))

                CustomTextField(
                    value = password,
                    onValueChange = setPassword,
                    label = "Password",
                    trailingIcon = {
                        Icon(imageVector = Icons.Default.Lock, contentDescription = null)
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Next
                    ),
                    visualTransformation = PasswordVisualTransformation()
                )

                Spacer(modifier = Modifier.size(16.dp))

                CustomTextField(
                    value = location,
                    onValueChange = setLocation,
                    label = "Location",
                    trailingIcon = {
                        IconButton(onClick = {
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
                        }) {
                            Icon(imageVector = Icons.Default.Place, contentDescription = null)
                        }
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done
                    )
                )

                Spacer(modifier = Modifier.size(24.dp))

                Button(
                    onClick = {
                        onSignup(
                            email,
                            password,
                            name,
                            location,
                            latitude,
                            longitude
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF47A9F4)
                    )
                ) {
                    Text(text = "SIGN UP")
                }
                Spacer(modifier = Modifier.size(30.dp))

            }

        }
    }
}

@Composable
fun LoginAndRegisterButtons(isLogin: Boolean, setIsLogin: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.extraLarge)
            .border(
                width = 1.dp,
                color = Color(0xFF47A9F4),
                shape = MaterialTheme.shapes.extraLarge
            ),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .clickable() {
                    setIsLogin(true)
                }
                .clip(MaterialTheme.shapes.extraLarge)
                .background(
                    if (isLogin) Color(0xFF47A9F4) else MaterialTheme.colorScheme.surface
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "LOGIN",
                style = TextStyle(
                    fontSize = 20.sp,
                    color = if (isLogin) Color.White else Color(0xFF47A9F4)
                ),
                modifier = Modifier.padding(16.dp)
            )
        }
        Box(
            modifier = Modifier
                .weight(1f)
                .clickable() {
                    setIsLogin(false)
                }
                .clip(MaterialTheme.shapes.extraLarge)
                .background(
                    if (!isLogin) Color(0xFF47A9F4) else MaterialTheme.colorScheme.surface
                ),
            contentAlignment = Alignment.Center

        ) {
            Text(
                text = "SIGN UP",
                style = TextStyle(
                    fontSize = 20.sp,
                    color = if (!isLogin) Color.White else Color(0xFF47A9F4)
                ),
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    maxLines: Int = 1
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        label = { Text(text = label) },
        shape = MaterialTheme.shapes.extraLarge,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0xFF47A9F4),
            unfocusedBorderColor = Color(0xFF47A9F4),
            cursorColor = Color(0xFF47A9F4),
            focusedTextColor = Color(0xFF47A9F4),
        ),
        keyboardOptions = keyboardOptions,
        visualTransformation = visualTransformation,
        maxLines = maxLines,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        singleLine = true
    )
}

suspend fun getLocation(
    locationClient: FusedLocationProviderClient,
    context: Context,
    setLatitude: (Double) -> Unit,
    setLongitude: (Double) -> Unit,
    setLocation: (String) -> Unit
) {
    val lastLocation = locationClient.lastLocation.await()
    if (lastLocation != null) {
        setLatitude(lastLocation.latitude)
        setLongitude(lastLocation.longitude)

        val geocoder = Geocoder(context)
        val addresses = geocoder.getFromLocation(
            lastLocation.latitude,
            lastLocation.longitude,
            1
        )
        if (addresses != null && addresses.isNotEmpty()) {
            setLocation("${addresses[0].subAdminArea}, ${addresses[0].locality}, ${addresses[0].adminArea}, ${addresses[0].countryName}")
        } else {
            Toast.makeText(context, "Error getting location", Toast.LENGTH_SHORT).show()
        }
    }
}