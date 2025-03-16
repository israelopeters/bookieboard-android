package com.example.bookieboard.ui.screens

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.bookieboard.R
import com.example.bookieboard.data.ApiRepository
import com.example.bookieboard.service.QuestionViewModel
import com.example.bookieboard.service.UserViewModel
import com.example.bookieboard.ui.theme.BookieboardTheme
import io.ktor.client.HttpClient

@Composable
fun BookieBoardScreen(
    userViewModel: UserViewModel,
    questionViewModel: QuestionViewModel,
    onViewBookieBoardClicked: () -> Unit,
    onHomeClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        ScoreStatus(userViewModel, questionViewModel)

        Spacer(Modifier.height(8.dp))

        OutlinedButton(
            onClick = onViewBookieBoardClicked,
            modifier = Modifier.padding(8.dp)
        ) {
            Text("See Full BookieBoard")
        }

        Spacer(Modifier.height(8.dp))

        TextButton (
            onClick = onHomeClicked,
        ) {
            Text(stringResource(R.string.home))
        }
    }

}

@Composable
fun ScoreStatus(
    userViewModel: UserViewModel,
    questionViewModel: QuestionViewModel,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ){
        Text(
            text = stringResource(R.string.bookieboard),
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.paddingFromBaseline(
                top = 8.dp,
                bottom = 8.dp
            )
        )

        Spacer(Modifier.height(8.dp))

        ElevatedCard(
            modifier = Modifier.padding(24.dp)
        ) {
            Text(
                text = "Player: ${userViewModel.authenticatedUser.firstName} " +
                        " ${userViewModel.authenticatedUser.lastName}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(8.dp)
            )
            Text(
                text = "Current score: ${questionViewModel.getCurrentPlayScore()}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(8.dp)
            )
            Text(
                text = "All-time score: ${userViewModel.authenticatedUser.bookieScore}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(8.dp)
            )
            Text(
                text = "Rank: ${userViewModel.authenticatedUser.bookieRank}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(8.dp)
            )
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
fun BookieBoardScreenPreview() {
    BookieboardTheme {
        BookieBoardScreen(
            UserViewModel(ApiRepository(HttpClient())),
            QuestionViewModel(ApiRepository(HttpClient())),
            { },
            { }
        )
    }
}