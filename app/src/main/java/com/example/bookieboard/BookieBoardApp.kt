package com.example.bookieboard

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.bookieboard.service.UserViewModel
import com.example.bookieboard.ui.screens.HomeScreen
import com.example.bookieboard.ui.screens.WelcomeScreen
import com.example.bookieboard.ui.theme.BookieboardTheme
import kotlinx.coroutines.launch

// Enums for app screens
enum class BookieBoardScreen(@StringRes val title: Int) {
    Welcome(R.string.bookieboard),
    SignUp(R.string.sign_up),
    Home(R.string.home)
}

@Composable
fun BookieBoardApp(
    userViewModel: UserViewModel,
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
                BookieBoardScreen.Welcome.name
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
                        if (userViewModel.authenticatedUser.email.isNotEmpty()) {
                            navController.navigate(BookieBoardScreen.Home)
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
                HomeScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookieBoardAppTopBar(
    currentScreen: BookieBoardScreen,
    canNavigate: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = stringResource(currentScreen.title),
                style = MaterialTheme.typography.titleLarge
            )
        },
        navigationIcon = {
            if (canNavigate) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.arrow_back)
                    )
                }
            }
        },
        colors = topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onSurface
        ),
        modifier = modifier.padding(16.dp).fillMaxWidth()
    )
}

@Preview(
    showBackground = true,
    uiMode = UI_MODE_NIGHT_NO,
    name = "DefaultPreviewLight"
)
@Preview(
    showBackground = true,
    uiMode = UI_MODE_NIGHT_YES,
    name = "DefaultPreviewDark"
)
@Composable
fun ReviewsAppTopBarPreview() {
    BookieboardTheme {
         BookieBoardAppTopBar(
            currentScreen = BookieBoardScreen.SignUp,
            canNavigate = true,
            navigateUp = { },
        )
    }
}