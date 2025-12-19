package com.example.travenor_customerapp.core.navigation

import android.telecom.Call
import android.widget.Toast
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.travenor_customerapp.presentation.Details.DetailScreen
import com.example.travenor_customerapp.presentation.Details.viewmodel.DetailViewModel
import com.example.travenor_customerapp.presentation.Home.HomeScreen
import com.example.travenor_customerapp.presentation.Home.viewmodel.HomeViewModel
import com.example.travenor_customerapp.presentation.Intro.OnboardingScreen
import com.example.travenor_customerapp.presentation.Login.LoginScreen
import com.example.travenor_customerapp.presentation.Login.viewModel.LoginViewModel
import com.example.travenor_customerapp.presentation.Intro.SplashScreen
import com.google.firebase.auth.FirebaseAuth


@Composable
fun AppNavigation(navController: NavHostController,padding: PaddingValues){

    val loginVM: LoginViewModel = hiltViewModel<LoginViewModel>()
    val homeVM: HomeViewModel= hiltViewModel<HomeViewModel>()
    val detailVM : DetailViewModel = hiltViewModel()
    val uiState by loginVM.loginUiState.collectAsStateWithLifecycle()
    val destination by homeVM.destination.collectAsStateWithLifecycle()
    val context = LocalContext.current

    NavHost(
        navController = navController,
        startDestination = Screens.Splash.route
    ){
        composable(Screens.Splash.route){
            SplashScreen(onNavigateHome = { hasSeen ->

                if(hasSeen){

                    if (loginVM.isLoggedIn()){

                        navController.navigate(Screens.Home.route){
                            popUpTo(Screens.Splash.route){inclusive = true}

                        }

                    }else{

                        navController.navigate(Screens.Login.route){
                            popUpTo(Screens.Splash.route){inclusive = true}
                        }
                    }

                }else{

                    navController.navigate(Screens.Intro.route){
                        popUpTo(Screens.Splash.route){inclusive = true}
                    }
                }
            } )
        }

        composable(Screens.Intro.route){
            OnboardingScreen(paddingValues = padding,
                onNavigateHome = {
                    navController.navigate(Screens.Login.route){
                        popUpTo(Screens.Intro.route){inclusive = true}
                    }
                })
        }

        composable(Screens.Login.route){

            LaunchedEffect(uiState.authError) {
                uiState.authError?.let { msg ->
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                    loginVM.onAuthMessageShown()
                }
            }

            LaunchedEffect(uiState.isSuccessLogin) {


                if (uiState.isSuccessLogin) {
                    navController.navigate(Screens.Home.route) {
                        popUpTo(Screens.Login.route) { inclusive = true }
                    }
                }
            }

            LaunchedEffect(uiState.emailError) {

            }

            LoginScreen(
                state = uiState,
                email = uiState.email,
                onEmailChange = { newEmail ->
                    loginVM.onEmailChange(newEmail)
                },
                onSignIn = {
                    loginVM.onSignInClicked()
                },
                emailError = uiState.emailError,
                isSignInEnabled = uiState.isSignInEnabled,
                password = uiState.password,
                passwordError = uiState.passwordError,
                onPasswordChange = { newPass ->
                    loginVM.onPasswordChange(newPass)
                }
            )
        }

        composable(Screens.Home.route){

            HomeScreen(padding = padding,
                viewModel = homeVM,
                onDestinationClick = { destinationId ->
                    homeVM.getDestination(destinationId)

                    navController.navigate(Screens.Detail.route)
                },
                onLoggedOut = {
                    navController.navigate(Screens.Login.route){
                        popUpTo(Screens.Home.route){
                            inclusive=true
                        }
                    }
                })
        }

        composable(Screens.Detail.route){

            DetailScreen(
                vm=detailVM,
                destination = destination,
                paddingValues= padding,
                onBack={
                    navController.popBackStack()
                },
                onBookNow={
                    detailVM.book(cityId = destination.id,FirebaseAuth.getInstance().currentUser!!.uid)
                },
                customerId = FirebaseAuth.getInstance().currentUser!!.uid)
        }

    }

}

sealed class Screens(val route: String){
    object Splash: Screens("splash")
    object Home: Screens("home")
    object Intro: Screens("intro")
    object  Login : Screens("login")
    object Detail :Screens("detail")
}