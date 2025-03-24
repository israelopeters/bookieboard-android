package com.example.bookieboard

import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.bookieboard.service.QuestionViewModel
import com.example.bookieboard.service.UserViewModel
import com.example.bookieboard.ui.components.BookieBoardAppTopBar
import com.example.bookieboard.ui.screens.BookieBoardScreen
import com.example.bookieboard.ui.screens.HomeScreen
import com.example.bookieboard.ui.screens.QuestionScreen
import com.example.bookieboard.ui.screens.SignInSuccessScreen
import com.example.bookieboard.ui.screens.SignUpScreen
import com.example.bookieboard.ui.screens.SignUpSuccessScreen
import com.example.bookieboard.ui.screens.WelcomeScreen
import kotlinx.coroutines.launch

// Enums for app screens
enum class AppScreen(@StringRes val title: Int) {
    Welcome(title = R.string.welcome),
    SignUp(title = R.string.sign_up),
    SignInSuccess(title = R.string.sign_in_success),
    SignUpSuccess(title = R.string.sign_up_success),
    Home(title = R.string.home),
    Question(title = R.string.question),
    BookieBoard(title = R.string.bookieboard)
}

@Composable
fun BookieBoardApp(
    userViewModel: UserViewModel,
    questionViewModel: QuestionViewModel,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = AppScreen.valueOf(
        backStackEntry?.destination?.route ?: AppScreen.Welcome.name
    )
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            val noTopBarScreens = listOf(
                AppScreen.Welcome.name,
                AppScreen.SignUp.name,
                AppScreen.SignInSuccess.name,
                AppScreen.SignUpSuccess.name
            )
            if (!noTopBarScreens.contains(currentScreen.name)) {
                BookieBoardAppTopBar(
                    currentScreen = currentScreen,
                    canNavigate = navController.previousBackStackEntry != null,
                    navigateUp = { }
                )
            }
        },
        modifier = modifier

    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = AppScreen.Welcome.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = AppScreen.Welcome.name) {
                WelcomeScreen(
                    userViewModel = userViewModel,
                    onLoginClicked = {
                        Log.v("BookieBoardActivity",
                            "Before logging in: ${userViewModel.currentUser}"
                        )
                        Log.v("BookieBoardActivity",
                            "Before logging in: ${userViewModel.userEmail}"
                        )
                        userViewModel.getUser()
                        Log.v("BookieBoardActivity", "After logging in: ${userViewModel.currentUser}")
                        Log.v("BookieBoardActivity",
                            "Before logging in: ${userViewModel.userEmail}"
                        )
                        if (userViewModel.currentUser.isLoggedIn) {
                            navController.navigate(AppScreen.SignInSuccess.name)
                        } else {
                            Log.v("BookieBoardActivity", "At login failure: ${userViewModel.currentUser}")
                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    "Login error. Enter correct credentials."
                                )
                            }
                        }
                    },
                    onSignUpClicked = { navController.navigate(AppScreen.SignUp.name) },
                    modifier = modifier.fillMaxSize()
                )
            }
            composable(route = AppScreen.Home.name) {
                HomeScreen(
                    userViewModel,
                    questionViewModel,
                    onPlayClicked = {
                        navController.navigate(AppScreen.Question.name)
                    })
            }
            composable(route = AppScreen.SignInSuccess.name) {
                SignInSuccessScreen(
                    userViewModel,
                    onContinueClicked = {
                        navController.navigate(AppScreen.Home.name)
                    }
                )
            }
            composable(route = AppScreen.Question.name) {
                QuestionScreen(
                    onSubmitClicked = {
                        val currentPlayScore = questionViewModel.getCurrentPlayScore()
                        userViewModel.updateBookieBoardScore(currentPlayScore)
                        questionViewModel.resetCurrentQuestionIndex()
                        navController.navigate(AppScreen.BookieBoard.name)
                    },
                    onNextClicked = {
                        questionViewModel.updateCurrentQuestion()
                    },
                    questionViewModel = questionViewModel
                )
            }
            composable(route = AppScreen.BookieBoard.name) {
                BookieBoardScreen(
                    userViewModel,
                    questionViewModel,
                    onViewBookieBoardClicked = { },
                    onHomeClicked =  {
                        questionViewModel.resetCurrentPlayScore()
                        navController.navigate(AppScreen.Home.name)
                    }
                )
            }
            composable(route = AppScreen.SignUp.name) {
                SignUpScreen(
                    userViewModel,
                    onCreateAccountClicked = { navController.navigate(AppScreen.SignUpSuccess.name) },
                    onSignInClicked = { navController.navigate(AppScreen.Welcome.name) }
                )
            }
            composable(route = AppScreen.SignUpSuccess.name) {
                SignUpSuccessScreen(
                    onSignInClicked = { navController.navigate(AppScreen.Welcome.name) }
                )
            }
        }
    }
}
