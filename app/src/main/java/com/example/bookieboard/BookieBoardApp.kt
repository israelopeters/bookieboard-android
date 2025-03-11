package com.example.bookieboard

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
import com.example.bookieboard.ui.screens.HomeScreen
import com.example.bookieboard.ui.screens.QuestionScreen
import com.example.bookieboard.ui.screens.SignInSuccessScreen
import com.example.bookieboard.ui.screens.WelcomeScreen
import kotlinx.coroutines.launch

// Enums for app screens
enum class BookieBoardScreen(@StringRes val title: Int) {
    Welcome(title = R.string.welcome),
    SignUp(title = R.string.sign_up),
    SignUpSuccess(title = R.string.sign_up_success),
    Home(title = R.string.home),
    Question(title = R.string.question)
}

@Composable
fun BookieBoardApp(
    userViewModel: UserViewModel,
    questionViewModel: QuestionViewModel,
    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = BookieBoardScreen.valueOf(
        backStackEntry?.destination?.route ?: BookieBoardScreen.Welcome.name
    )
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            val noTopBarScreens = listOf(
                BookieBoardScreen.Welcome.name,
                BookieBoardScreen.SignUpSuccess.name
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
            startDestination = BookieBoardScreen.Welcome.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = BookieBoardScreen.Welcome.name) {
                WelcomeScreen(
                    userViewModel = userViewModel,
                    onLoginClicked = {
                        userViewModel.getUser()
                        if (userViewModel.authenticatedUser.firstName.isNotEmpty()) {
                            scope.launch {
                                snackbarHostState.showSnackbar("Login successful!")
                            }
                            navController.navigate(BookieBoardScreen.SignUpSuccess.name)
                        } else {
                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    "Login error. Enter correct credentials."
                                )
                            }
                        }

                    },
                    onSignUpClicked = { }, // navigate to signup screen
                    modifier = modifier.fillMaxSize()
                )
            }
            composable(route = BookieBoardScreen.Home.name) {
                HomeScreen(
                    userViewModel,
                    questionViewModel,
                    onPlayClicked = {
                        navController.navigate(BookieBoardScreen.Question.name)
                    })
            }
            composable(route = BookieBoardScreen.SignUpSuccess.name) {
                SignInSuccessScreen(
                    userViewModel,
                    onContinueClicked = {
                        navController.navigate(BookieBoardScreen.Home.name)
                    }
                )
            }
            composable(route = BookieBoardScreen.Question.name) {
                QuestionScreen(questionViewModel)
            }
        }
    }
}
