package com.example.bookieboard.ui.screens

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.bookieboard.data.ApiRepository
import com.example.bookieboard.service.UserViewModel
import com.example.bookieboard.ui.theme.BookieboardTheme
import io.ktor.client.HttpClient

@Composable
fun SignInSuccessScreen(
    userViewModel: UserViewModel,
    onContinueClicked: () -> Unit,
    modifier: Modifier = Modifier
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
                text = "Welcome, ${userViewModel.authenticatedUser.firstName}!",
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
fun SignInSuccessScreenPreview() {
    BookieboardTheme {
        SignInSuccessScreen(
            UserViewModel(ApiRepository((HttpClient()))),
            onContinueClicked = { }
        )
    }
}