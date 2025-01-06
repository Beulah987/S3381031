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
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.gson.Gson
import uk.ac.tees.mad.airtrack.ui.DetailsScreen
import uk.ac.tees.mad.airtrack.ui.EditProfileScreen
import uk.ac.tees.mad.airtrack.ui.HomeScreen
import uk.ac.tees.mad.airtrack.ui.ProfileScreen
import uk.ac.tees.mad.airtrack.ui.auth.AuthScreen
import uk.ac.tees.mad.airtrack.ui.auth.AuthViewmodel
import uk.ac.tees.mad.airtrack.ui.theme.AirTrackTheme
import uk.ac.tees.mad.airtrack.ui.SplashScreen
import uk.ac.tees.mad.airtrack.ui.mainapp.MainViewModel
import uk.ac.tees.mad.airtrack.ui.mainapp.model.AirQuality


class MainActivity : ComponentActivity() {

    private val mainViewModel by viewModels<MainViewModel>()

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
                            HomeScreen(navController, mainViewModel)
                        }
                        composable("profile_screen"){
                            ProfileScreen(authViewmodel = authViewmodel, navController = navController)
                        }
                        composable("edit_profile") {
                            EditProfileScreen(navController, authViewmodel)
                        }
                        composable("details_screen/{airQuality}",
                            arguments = listOf(
                                navArgument("airQuality"){
                                    type= NavType.StringType
                                }
                            )
                        ) {backStack->
                            val airQualityJson = backStack.arguments?.getString("airQuality")
                            val airQuality = Gson().fromJson(airQualityJson, AirQuality::class.java)

                            DetailsScreen(airQuality)
                        }
                    }
                }
            }
        }
    }
}