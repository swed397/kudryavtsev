package tinkoff.fintech.lab.ui.list.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tinkoff.fintech.lab.R
import tinkoff.fintech.lab.domain.model.FilmType
import tinkoff.fintech.lab.ui.list.ListEvent
import tinkoff.fintech.lab.ui.list.ListState

@Composable
fun TopBar(state: ListState, onEvent: (ListEvent) -> Unit) {
    var isSearching by remember { mutableStateOf(false) }

    if (isSearching) {
        SearchText(onClickIcon = { isSearching = isSearching.not() }, onEvent)
    } else {
        ScreenTitle(
            screenTitle = if (state.filmType == FilmType.POPULAR) stringResource(id = R.string.tab_name_popular) else stringResource(
                id = R.string.tab_name_favorites
            ),
            onClickIcon = {
                isSearching = isSearching.not()
            }
        )
    }
}

@Composable
private fun ScreenTitle(screenTitle: String, onClickIcon: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp)
            .background(Color.White)
    ) {
        Text(
            screenTitle,
            fontSize = 25.sp,
            color = Color.Black,
            fontWeight = FontWeight.Bold
        )
        Icon(
            painter = painterResource(id = R.drawable.search_icon),
            contentDescription = "search_icon",
            tint = colorResource(id = R.color.app_blue),
            modifier = Modifier
                .padding(end = 16.dp)
                .clickable { onClickIcon.invoke() }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchText(onClickIcon: () -> Unit, onEvent: (ListEvent) -> Unit) {
    var searchText by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .background(Color.White)
            .fillMaxWidth()
            .padding(start = 16.dp)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.baseline_arrow_back_24),
            contentDescription = "back arrow search",
            tint = colorResource(id = R.color.app_blue),
            modifier = Modifier
                .size(24.dp)
                .clickable { onClickIcon.invoke() }
        )

        OutlinedTextField(
            value = searchText,
            onValueChange = { searchText = it },
            placeholder = {
                Text(
                    stringResource(id = R.string.search),
                    color = Color.Gray,
                    maxLines = 1
                )
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent
            ),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    onEvent.invoke(ListEvent.FilterByText(searchText))
                    keyboardController?.hide()
                }
            ),
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun ComponentPreviewSearchText() {
    SearchText(onClickIcon = {}, onEvent = {})
}

@Composable
@Preview(showBackground = true)
private fun ComponentPreviewScreenTitle() {
    ScreenTitle(screenTitle = "Полуярное") {
    }
}