package com.example.bookieboard.ui.screens

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.util.Log
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.bookieboard.R
import com.example.bookieboard.data.ApiRepository
import com.example.bookieboard.service.AuthMode
import com.example.bookieboard.service.UserUiState
import com.example.bookieboard.service.UserViewModel
import com.example.bookieboard.ui.components.IndeterminateCircularIndicator
import com.example.bookieboard.ui.theme.BookieboardTheme
import io.ktor.client.HttpClient

private const val TAG: String = "BookieBoardActivity"

@Composable
fun WelcomeScreen(
    onContinueClicked: () -> Unit,
    onSignUpClicked: () -> Unit,
    modifier: Modifier = Modifier,
    userViewModel: UserViewModel
) {
    when (userViewModel.currentUser.authMode) {

        AuthMode.SIGNING_IN -> IndeterminateCircularIndicator()

        AuthMode.SIGNED_IN -> SignInSuccess(
            userState = userViewModel.currentUser,
            onContinueClicked = onContinueClicked
        )

        AuthMode.SIGNED_OUT ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
            ) {

                AppDetails()

                AppSignIn(
                    userViewModel,
                    onSignInClicked = {
                        Log.v(
                            TAG, "Welcome Screen - Before logging in: ${userViewModel.currentUser}"
                        )
                        Log.v(
                            TAG, "Welcome Screen - Before logging in: ${userViewModel.userEmail}"
                        )
                        userViewModel.getUser()
                        Log.v(
                            TAG,
                            "Welcome Screen - After logging in: ${userViewModel.currentUser}"
                        )
                    }
                )

                AppSignUp(onSignUpClicked = onSignUpClicked)
            }
    }
}


@Composable
fun AppDetails(
    modifier: Modifier = Modifier,
    @DrawableRes image: Int = R.drawable.welcome_screen_image,
    @StringRes screenHeader: Int = R.string.bookieboard,
    @StringRes headerSubtitle: Int = R.string.welcome_subtitle,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(top = 48.dp)
    ) {
        Text(
            text = stringResource(screenHeader),
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.paddingFromBaseline(
                top = 8.dp,
                bottom = 8.dp
            )
        )
        Text(
            text = stringResource(headerSubtitle),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Normal,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(8.dp)
        )
        Image(
            painter = painterResource(image),
            contentDescription = "App logo",
            alignment = Alignment.Center,
            modifier = Modifier
                .size(100.dp)
                .padding(8.dp)
                .clip(shape = MaterialTheme.shapes.medium)
        )
    }
}

@Composable
fun AppSignIn(
    userViewModel: UserViewModel,
    onSignInClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = modifier.padding(24.dp)
    ) {
        OutlinedTextField(
            value = userViewModel.userEmail,
            onValueChange = { userViewModel.updateEmail(it) },
            label = { Text(stringResource(R.string.email)) },
            modifier = Modifier
                .padding(8.dp)
        )
        OutlinedTextField(
            value = userViewModel.userPassword,
            onValueChange = { userViewModel.updatePassword(it) },
            label = { Text(stringResource(R.string.password)) },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .padding(8.dp)
        )
        Button(
            onClick = onSignInClicked,
            enabled = isFormValid(userViewModel.userEmail, userViewModel.userPassword),
            modifier = Modifier
                .padding(8.dp)
        ) {
            Text(stringResource(R.string.sign_in))
        }
    }
}

fun isFormValid(email: String?, password: String?): Boolean {
    return email?.isNotEmpty() == true && password?.isNotEmpty() == true
}

@Composable
fun AppSignUp(
    onSignUpClicked: () -> Unit,
    modifier: Modifier =  Modifier
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(top = 8.dp)
    ) {
        Text(
            text = stringResource(R.string.not_yet_registered),
            color = MaterialTheme.colorScheme.onBackground
        )

        TextButton (
            onClick = onSignUpClicked,
        ) {
            Text(stringResource(R.string.sign_up))
        }
    }
}

@Composable
fun SignInSuccess(
    onContinueClicked: () -> Unit,
    modifier: Modifier = Modifier,
    userState: UserUiState
) {
    Surface(modifier = modifier) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            Text(
                text = "Welcome, ${userState.firstName}!",
                style = MaterialTheme.typography.headlineLarge
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = "Ready to prove your bibliophilic prowess?",
                modifier = Modifier.padding(vertical = 8.dp)
            )
            OutlinedButton(
                onClick = onContinueClicked,
                modifier = Modifier.padding(8.dp)
            ) {
                Text("Continue")
            }
        }
    }
}

// Previews
@Preview(
    showBackground = true,
    showSystemUi = true,
    uiMode = UI_MODE_NIGHT_NO,
    name = "DefaultPreviewLight"
)
@Preview(
    showBackground = true,
    showSystemUi = true,
    uiMode = UI_MODE_NIGHT_YES,
    name = "DefaultPreviewDark"
)
@Composable
fun WelcomeScreenPreview() {
    BookieboardTheme {
        WelcomeScreen(
            userViewModel = hiltViewModel(),
            onContinueClicked = { },
            onSignUpClicked = { }
        )
    }
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
fun AppDetailsPreview() {
    BookieboardTheme {
        AppDetails()
    }
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
fun AppLoginPreview() {
    BookieboardTheme {
        AppSignIn(
            UserViewModel(ApiRepository(HttpClient())),
            onSignInClicked = { }
        )
    }
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
fun AppSignUpPreview() {
    BookieboardTheme {
        AppSignUp(
            onSignUpClicked = { }
        )
    }
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
fun SignInSuccessPreview() {
    BookieboardTheme {
        SignInSuccess(
            onContinueClicked = { },
            userState = UserUiState()
        )
    }
}