package com.example.bookieboard

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.example.bookieboard.ui.screens.SignUpScreen
import com.example.bookieboard.ui.screens.WelcomeScreen

// Enums for app screens
enum class AppScreen(@StringRes val title: Int) {
    Welcome(title = R.string.welcome),
    SignUp(title = R.string.sign_up),
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

    Scaffold(
        topBar = {
            val noTopBarScreens = listOf(
                AppScreen.Welcome.name,
                AppScreen.SignUp.name,
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
                    onContinueClicked = { navController.navigate(AppScreen.Home.name) },
                    onSignUpClicked = { navController.navigate(AppScreen.SignUp.name) },
                    modifier = modifier.fillMaxSize()
                )
            }
            composable(route = AppScreen.Home.name) {
                HomeScreen(
                    onPlayClicked = {
                        navController.navigate(AppScreen.Question.name)
                    })
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
                    onViewBookieBoardClicked = { },
                    onHomeClicked =  {
                        questionViewModel.resetCurrentPlayScore()
                        navController.navigate(AppScreen.Home.name)
                    }
                )
            }
            composable(route = AppScreen.SignUp.name) {
                SignUpScreen(
                    onSignInClicked = { navController.navigate(AppScreen.Welcome.name) }
                )
            }
        }
    }
}
