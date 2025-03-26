package com.example.bookieboard.ui.screens

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import com.example.bookieboard.R
import com.example.bookieboard.data.ApiRepository
import com.example.bookieboard.service.UserViewModel
import com.example.bookieboard.ui.theme.BookieboardTheme
import io.ktor.client.HttpClient

@Composable
fun WelcomeScreen(
    userViewModel: UserViewModel,
    onLoginClicked: () -> Unit,
    onSignUpClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
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
            onLoginClicked = onLoginClicked
        )

        AppSignUp(onSignUpClicked = onSignUpClicked)
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
    onLoginClicked: () -> Unit,
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
            onClick = onLoginClicked,
            modifier = Modifier
                .padding(8.dp)
        ) {
            Text(stringResource(R.string.sign_in))
        }
    }
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
            UserViewModel(ApiRepository(HttpClient())),
            onLoginClicked = { },
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
            onLoginClicked = { }
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