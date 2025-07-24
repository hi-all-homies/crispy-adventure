package ru.stanise.animebrowsing.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.NearbyError
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import ru.stanise.animebrowsing.dto.Character
import ru.stanise.animebrowsing.dto.CharacterData
import ru.stanise.animebrowsing.ui.model.CharacterModel
import ru.stanise.animebrowsing.ui.model.SingleCharacterUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterList(
    characters: List<CharacterData>,
    characterModel: CharacterModel,
    modifier: Modifier = Modifier
){
    if (characters.isNotEmpty()) {
        val sheetState = rememberModalBottomSheetState()
        val scope = rememberCoroutineScope()
        var showBottomSheet by remember { mutableStateOf(false) }

        HorizontalDivider(modifier = modifier.padding(vertical = 8.dp))

        Row {
            Text(text = "Characters:", style = MaterialTheme.typography.bodyLarge)
        }

        LazyRow(
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            contentPadding = PaddingValues(horizontal = 4.dp)
        ) {
            items(items = characters, key = { it.character.id }) {
                CharacterItem(it.character, modifier){ id ->
                    showBottomSheet = true
                    characterModel.getCharacterById(id)
                }
            }
        }

        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { showBottomSheet = false },
                sheetState = sheetState
            ) {
                SingleCharacter(
                    characterModel = characterModel,
                    onClose = {
                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                            if (!sheetState.isVisible) {
                                showBottomSheet = false
                            }
                        }
                    },
                    modifier = modifier
                )
            }
        }
    }
}


@Composable
fun CharacterItem(
    character: Character,
    modifier: Modifier = Modifier,
    onClick: (Int) -> Unit
) {
    Column(
        modifier = modifier
            .clickable(onClick = { onClick(character.id) })
            .width(120.dp)
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AnimePoster(
            character.images.webp.imageUrl,
            modifier = modifier
                .size(100.dp)
                .clip(CircleShape)
        )

        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = character.name,
            style = MaterialTheme.typography.bodySmall,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}


@Composable
fun SingleCharacter(
    characterModel: CharacterModel,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
){
    val singleCharState by characterModel.singleCharState.collectAsState()

    Column(
        modifier = modifier.fillMaxWidth()
            .padding(bottom = 12.dp, start = 20.dp, end = 20.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
            IconButton(onClick = onClose) {
                Icon(Icons.Default.Close, contentDescription = "Close")
            }
        }

        when(singleCharState){
            is SingleCharacterUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            is SingleCharacterUiState.Success -> {
                val single = (singleCharState as SingleCharacterUiState.Success).single
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = modifier.fillMaxWidth()
                ) {
                    Text(text = single.name, style = MaterialTheme.typography.headlineSmall)
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = modifier.fillMaxWidth()
                ) {
                    AnimePoster(
                        single.images.webp.imageUrl,
                        modifier = modifier
                            .height(250.dp)
                            .aspectRatio(2f / 3f)
                            .clip(MaterialTheme.shapes.medium)
                    )
                }

                single.about?.let {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.verticalScroll(rememberScrollState())
                    )
                }
            }
            else -> {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = modifier.fillMaxWidth()
                ) {
                    Text(text = "Couldn't get character bio", style = MaterialTheme.typography.bodyLarge)
                    Spacer(modifier.width(16.dp))
                    IconButton(onClick = onClose) {
                        Icon(
                            imageVector = Icons.Default.NearbyError,
                            contentDescription = "close icon",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }
    }
}