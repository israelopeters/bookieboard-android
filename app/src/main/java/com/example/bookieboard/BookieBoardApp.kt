package com.example.bookieboard

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.bookieboard.service.QuestionViewModel
import com.example.bookieboard.service.UserViewModel
import com.example.bookieboard.ui.components.AppAlertDialog
import com.example.bookieboard.ui.components.BookieBoardAppTopBar
import com.example.bookieboard.ui.components.IndeterminateCircularIndicator
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
    modifier: Modifier = Modifier,
    questionViewModel: QuestionViewModel = hiltViewModel(),
    userViewModel: UserViewModel = hiltViewModel(),
    navController: NavHostController = rememberNavController()
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = AppScreen.valueOf(
        backStackEntry?.destination?.route ?: AppScreen.Welcome.name
    )
    var openSignOutAlertDialog by remember { mutableStateOf(false) }

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
                    navigateUp = {  },
                    onSignOutClicked = {
                        openSignOutAlertDialog = true
                    }
                )
            }
        },
        modifier = modifier

    ) { innerPadding ->

        when {
            openSignOutAlertDialog -> {
                AppAlertDialog(
                    onDismissRequest = { openSignOutAlertDialog = false },
                    onConfirmation = {
                        openSignOutAlertDialog = false
                        userViewModel.signOut()
                        navController.navigate(AppScreen.Welcome.name)
                    },
                    dialogTitle = stringResource(R.string.sign_out_confirmation),
                    dialogText = stringResource(R.string.sign_out_confirmation_text),
                    icon = Icons.Filled.Info,
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }

        NavHost(
            navController = navController,
            startDestination = AppScreen.Welcome.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = AppScreen.Welcome.name) {
                WelcomeScreen(
                    userViewModel = userViewModel,
                    onContinueClicked = { navController.navigate(AppScreen.Home.name) },
                    onSignUpClicked = { navController.navigate(AppScreen.SignUp.name) },
                    modifier = modifier.fillMaxSize()
                )
            }
            composable(route = AppScreen.Home.name) {
                HomeScreen(
                    userViewModel = userViewModel,
                    questionViewModel = questionViewModel,
                    onPlayClicked = { navController.navigate(AppScreen.Question.name) })
            }
            composable(route = AppScreen.Question.name) {
                QuestionScreen(
                    userViewModel = userViewModel,
                    questionViewModel = questionViewModel,
                    onSubmitClicked = { navController.navigate(AppScreen.BookieBoard.name) },
                    onNextClicked = { questionViewModel.updateCurrentQuestion() },
                )
            }
            composable(route = AppScreen.BookieBoard.name) {
                BookieBoardScreen(
                    userViewModel = userViewModel,
                    questionViewModel = questionViewModel,
                    onViewBookieBoardClicked = { },
                    onHomeClicked =  {
                        questionViewModel.resetCurrentPlayScore()
                        navController.navigate(AppScreen.Home.name)
                    }
                )
            }
            composable(route = AppScreen.SignUp.name) {
                SignUpScreen(
                    userViewModel = userViewModel,
                    onSignInClicked = {
                        userViewModel.clearAddedUser()
                        navController.navigate(AppScreen.Welcome.name)
                    }
                )
            }
        }
    }
}
