package com.portugal1576.marsrover.presentation.customElements

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.portugal1576.marsrover.R
import com.portugal1576.marsrover.domain.model.Character
import com.portugal1576.marsrover.ui.theme.PortugalGreenFlag
import com.portugal1576.marsrover.ui.theme.PortugalRedWine

@Composable
fun ContainerItem(
    character: Character,
    onDetailedDescriptionClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    showFavoriteIcon: Boolean = true
) {
    val font = FontFamily(Font(R.font.inter_regular, FontWeight.Normal))

    Card(
        border = BorderStroke(width = 2.dp, PortugalRedWine),
        colors = CardDefaults.cardColors(containerColor = PortugalGreenFlag),
        shape = RoundedCornerShape(size = 12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp, horizontal = 12.dp)
            .clickable { onDetailedDescriptionClick() }
    ) {
        Row(
            modifier = Modifier
                .padding(start = 8.dp, end = 16.dp, bottom = 8.dp, top = 8.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Card(
                border = BorderStroke(width = 2.dp, PortugalRedWine),
                colors = CardDefaults.cardColors(containerColor = PortugalGreenFlag),
                shape = RoundedCornerShape(size = 12.dp),
                modifier = Modifier.size(110.dp)
            ) {
                AsyncImage(
                    model = character.image,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    contentScale = ContentScale.Crop
                )
            }

            Column(
                modifier = Modifier
                    .padding(start = 6.dp)
                    .weight(1f)
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = character.name.ifBlank { "No name available" },
                    color = Color.White,
                    fontFamily = font,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 8.dp)
                )

                Text(
                    text = character.status.ifBlank { "Unknown status" },
                    fontSize = 14.sp,
                    color = Color.White,
                    fontFamily = FontFamily(Font(R.font.robo_thin_font, FontWeight.Normal)),
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp, end = 8.dp)
                )
            }

            if (showFavoriteIcon) {
                Image(
                    painter = painterResource(id = R.drawable.ic_favorite),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(if (character.isFavorite) Color.Red else Color.White),
                    modifier = Modifier.clickable { onFavoriteClick() }
                )
            }
        }
    }
}

@Preview(showBackground = true, heightDp = 220)
@Composable
private fun ContainerItemPreview() {
    ContainerItem(
        character = Character(
            id = 1,
            name = "Rick Sanchez",
            image = "https://rickandmortyapi.com/api/character/avatar/1.jpeg",
            status = "Alive",
            species = "Human",
            gender = "Male",
            originName = "Earth (C-137)",
            locationName = "Citadel of Ricks",
            episodeUrls = emptyList(),
            isFavorite = false
        ),
        onDetailedDescriptionClick = {},
        onFavoriteClick = {},
        showFavoriteIcon = true
    )
}
