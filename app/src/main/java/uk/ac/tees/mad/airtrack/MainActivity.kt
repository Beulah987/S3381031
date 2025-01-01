package uk.ac.tees.mad.airtrack

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import uk.ac.tees.mad.airtrack.ui.ProfileScreen
import uk.ac.tees.mad.airtrack.ui.auth.AuthScreen
import uk.ac.tees.mad.airtrack.ui.auth.AuthViewmodel
import uk.ac.tees.mad.airtrack.ui.theme.AirTrackTheme
import uk.ac.tees.mad.airtrack.ui.SplashScreen


class MainActivity : ComponentActivity() {

    private val authViewModel by viewModels<AuthViewmodel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AirTrackTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val navController = rememberNavController()
                    val authViewmodel: AuthViewmodel = viewModel()

                    NavHost(
                        navController = navController,
                        startDestination = "splash",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("splash") {
                            SplashScreen(authViewmodel = authViewmodel, navController = navController)
                        }
                        composable("auth") {
                            AuthScreen(
                                onLogin = { email, password ->
                                    authViewmodel.login(
                                        email,
                                        password,
                                        onSuccess = {
                                            navController.navigate("home") {
                                                popUpTo("auth") { inclusive = true }
                                            }
                                        },
                                        onFailure = { e ->
                                            Toast.makeText(
                                                this@MainActivity,
                                                "Login failed: ${e.message}",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    )
                                },
                                onSignup = { email, password, name, location, latitude, longitude ->
                                    authViewmodel.register(
                                        email,
                                        password,
                                        name,
                                        location,
                                        latitude,
                                        longitude,
                                        onSuccess = {
                                            navController.navigate("home") {
                                                popUpTo("auth") { inclusive = true }
                                            }

                                        },
                                        onFailure = { e ->
                                            Toast.makeText(
                                                this@MainActivity,
                                                "Signup failed: ${e.message}",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    )
                                }
                            )
                        }
                        composable("home") {
                            ProfileScreen(authViewmodel = authViewmodel, navController = navController)
                        }
                    }
                }
            }
        }
    }
}