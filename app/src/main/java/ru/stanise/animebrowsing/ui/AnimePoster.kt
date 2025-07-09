package ru.stanise.animebrowsing.ui


import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import ru.stanise.animebrowsing.R

@Composable
fun AnimePoster(url: String?, modifier: Modifier = Modifier) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(url)
            .crossfade(true)
            .build(),
        contentDescription = "anime poster",
        contentScale = ContentScale.Crop,
        modifier = modifier,
        placeholder = painterResource(R.drawable.placeholder),
        error = painterResource(R.drawable.error)
    )
}
