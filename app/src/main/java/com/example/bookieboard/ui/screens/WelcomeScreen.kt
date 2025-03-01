package com.example.bookieboard.ui.screens

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.bookieboard.R
import com.example.bookieboard.ui.theme.BookieboardTheme

@Composable
fun AppDetails(
    @DrawableRes image: Int,
    @StringRes screenHeader: Int,
    @StringRes headerSubtitle: Int,
    modifier: Modifier = Modifier
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
                .padding(8.dp).
                clip(shape = MaterialTheme.shapes.medium)
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
        AppDetails(
            image = R.drawable.welcome_screen_image,
            screenHeader = R.string.bookieboard,
            headerSubtitle = R.string.welcome_subtitle
        )
    }
}