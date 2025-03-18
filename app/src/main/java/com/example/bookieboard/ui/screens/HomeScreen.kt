package com.example.bookieboard.ui.screens

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.bookieboard.R
import com.example.bookieboard.data.ApiRepository
import com.example.bookieboard.model.DifficultyLevel
import com.example.bookieboard.service.QuestionViewModel
import com.example.bookieboard.service.UserViewModel
import com.example.bookieboard.ui.theme.BookieboardTheme
import io.ktor.client.HttpClient

@Composable
fun HomeScreen(
    userViewModel: UserViewModel,
    questionViewModel: QuestionViewModel,
    onPlayClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        StatusSection(userViewModel)

        HorizontalDivider(
            modifier = Modifier.padding(32.dp)
        )

        DifficultySelection(questionViewModel, onPlayClicked)
    }
}

@Composable
fun StatusSection(
    userViewModel: UserViewModel,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ){
        Text(
            text = "Hi ${userViewModel.authenticatedUser.firstName}! " +
                    "This is your",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Normal,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(8.dp)
        )
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
        Text(
            text = "Your current rank: ${userViewModel.authenticatedUser.bookieRank}",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Normal,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(8.dp)
        )
    }
}

@Composable
fun DifficultySelection(
    questionViewModel: QuestionViewModel,
    onPlayClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = "Where would you like to start from?",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Normal,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(horizontal = 32.dp)
        )

        // Block below copied and slightly modified from Google's official Android documentation
        val radioOptions = listOf("Let's take it easy", "Bring it on!", "Book lords only")
        val (selectedOption, onOptionSelected) = remember { mutableStateOf(radioOptions[0]) }
        // Note that Modifier.selectableGroup() is essential to ensure correct accessibility behavior
        Column(
            modifier.selectableGroup().padding(horizontal = 16.dp)
        ) {
            radioOptions.forEach { text ->
                Row(
                    Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .selectable(
                            selected = (text == selectedOption),
                            onClick = { onOptionSelected(text) },
                            role = Role.RadioButton
                        )
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Only make EASY questions available for now
                    var enabledState by remember { mutableStateOf(true) }
                    if (selectedOption != "Let's take it easy") {
                        enabledState = false
                    } else {
                        enabledState = true
                    }

                    RadioButton(
                        selected = (text == selectedOption),
                        enabled = enabledState,
                        onClick = null // null recommended for accessibility with screen readers
                    )
                    Text(
                        text = text,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
            }
        }

        Button(
            onClick = {
                when (selectedOption) {
                    radioOptions[0] -> questionViewModel.setDifficultyLevel(DifficultyLevel.EASY)
                    radioOptions[1] -> questionViewModel.setDifficultyLevel(DifficultyLevel.MEDIUM)
                    radioOptions[2] -> questionViewModel.setDifficultyLevel(DifficultyLevel.HARD)
                }
                questionViewModel.getQuestions()
                onPlayClicked()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 72.dp, vertical = 16.dp)
        ) {
            Text("Play")
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
fun HomeScreenPreview() {
    BookieboardTheme {
        HomeScreen(
            UserViewModel(ApiRepository(HttpClient())),
            QuestionViewModel(ApiRepository(HttpClient())),
            onPlayClicked = { }
        )
    }
}