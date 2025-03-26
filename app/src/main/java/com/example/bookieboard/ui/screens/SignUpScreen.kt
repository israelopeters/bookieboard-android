package com.example.bookieboard.ui.screens

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.bookieboard.R
import com.example.bookieboard.data.ApiRepository
import com.example.bookieboard.model.UserCreation
import com.example.bookieboard.service.UserUiState
import com.example.bookieboard.service.UserViewModel
import com.example.bookieboard.ui.theme.BookieboardTheme
import io.ktor.client.HttpClient
import kotlinx.coroutines.launch

@Composable
fun SignUpScreen(
    userViewModel: UserViewModel,
    onCreateAccountClicked: () -> Unit,
    onSignInClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var firstName: String by rememberSaveable { mutableStateOf("") }
    var lastName: String by rememberSaveable { mutableStateOf("") }
    var email: String by rememberSaveable { mutableStateOf("") }
    var password: String by rememberSaveable { mutableStateOf("") }
    var addedUser: UserUiState by remember { mutableStateOf(UserUiState()) }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Surface(
        modifier = modifier
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            Text(
                text = stringResource(R.string.create_your_account),
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.paddingFromBaseline(
                    top = 8.dp,
                    bottom = 8.dp
                )
            )
            HorizontalDivider(
                modifier = Modifier.padding(16.dp)
            )
            OutlinedTextField(
                value = firstName,
                onValueChange = { firstName = it },
                label = { Text(stringResource(R.string.first_name)) },
                modifier = Modifier
                    .padding(8.dp)
            )
            OutlinedTextField(
                value = lastName,
                onValueChange = { lastName = it },
                label = { Text(stringResource(R.string.last_name)) },
                modifier = Modifier
                    .padding(8.dp)
            )
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text(stringResource(R.string.email)) },
                modifier = Modifier
                    .padding(8.dp)
            )
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text(stringResource(R.string.password)) },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier
                    .padding(8.dp)
            )
            Button(
                onClick = {
                    Log.v(
                        "BookieBoard Activity",
                        "Added user before network connection --- $addedUser"
                    )
                    addedUser = userViewModel.addNewUser(
                        UserCreation(
                            firstName = firstName,
                            lastName = lastName,
                            email = email,
                            password = password
                        )
                    )
                    Log.v(
                        "BookieBoard Activity",
                        "Added user after network connection --- $addedUser"
                    )
                    if (addedUser.firstName.isNotEmpty()) {
                        onCreateAccountClicked()
                    } else {
                        Log.v(
                            "BookieBoard Activity",
                            "Failed to add user!"
                        )
                        scope.launch {
                            snackbarHostState.showSnackbar(
                                "Signup error. Try again."
                            )
                        }
                    }
                },
                modifier = Modifier
                    .padding(8.dp)
            ) {
                Text(stringResource(R.string.create_account))
            }
            Text(
                text = stringResource(R.string.already_have_an_account),
                modifier = Modifier.padding(top = 24.dp),
                color = MaterialTheme.colorScheme.onBackground
            )
            TextButton (
                onClick = onSignInClicked
            ) {
                Text(stringResource(R.string.sign_in))
            }
        }
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
fun SignUpScreenPreview() {
    BookieboardTheme {
        SignUpScreen(
            userViewModel = UserViewModel(ApiRepository(HttpClient())),
            onCreateAccountClicked = { },
            onSignInClicked = { }
        )
    }
}