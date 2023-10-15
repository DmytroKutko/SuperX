package com.heroes.superx.ui.heroesList

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.heroes.superx.viewModels.MainViewModel
import com.heroes.superx.R
import com.heroes.superx.models.Appearance
import com.heroes.superx.models.Biography
import com.heroes.superx.models.Connections
import com.heroes.superx.models.Hero
import com.heroes.superx.models.Images
import com.heroes.superx.models.Powerstats
import com.heroes.superx.models.Work
import com.heroes.superx.ui.theme.SuperHeroesTheme
import kotlinx.coroutines.launch

const val GRID_COUNT = 3

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun HeroesList(
    onHeroClick: () -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    val list = remember { mutableStateOf<List<Hero>>(listOf()) }
    val viewModel: MainViewModel = hiltViewModel()

    coroutineScope.launch {
        viewModel.heroesFlow.collect {
            list.value = it
        }
    }

    LazyVerticalGrid(modifier = Modifier.padding(horizontal = 8.dp), columns = GridCells.Fixed(GRID_COUNT)) {
        list.value.forEach { hero ->
            item {
                HeroItem(
                    hero,
                    onHeroClick
                )
            }
        }
        for (i in 0..GRID_COUNT) { // Bottom space
            item {
                Spacer(modifier = Modifier.padding(bottom = 56.dp))
            }
        }
    }
}

@Composable
fun HeroItem(
    hero: Hero,
    onHeroClick: () -> Unit,
) {
    val viewModel: MainViewModel = viewModel()
    val coroutineScope = rememberCoroutineScope()
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                coroutineScope.launch {
                    viewModel.setHero(hero)
                    Log.d("chosenHero", "HeroItem: $hero")
                    onHeroClick()
                }
            }
            .padding(8.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        )
    ) {
        Box(contentAlignment = Alignment.BottomCenter) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Transparent),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                HeroImage(
                    imageUrl = hero.images.lg,
                    modifier = Modifier.fillMaxSize()
                )
                Text(
                    modifier = Modifier
                        .padding(start = 12.dp, end = 12.dp),
                    text = hero.name,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.W700,
                    maxLines = 1
                )
                Text(
                    modifier = Modifier
                        .padding(start = 12.dp, end = 12.dp, bottom = 4.dp),
                    text = hero.biography.fullName,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 13.sp
                )
            }
        }
    }
}

@Composable
fun HeroImage(imageUrl: String, modifier: Modifier = Modifier) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(imageUrl)
            .crossfade(true)
            .build(),
        contentDescription = "",
        contentScale = ContentScale.Fit,
        modifier = modifier,
        placeholder = painterResource(R.drawable.default_hero_image),
    )
}

@Preview(showBackground = true)
@Composable
fun HeroesListPreview() {
    SuperHeroesTheme {
        HeroesList(onHeroClick = {})
    }
}

@Preview
@Composable
fun HeroItemPreview() {
    HeroItem(
        hero = Hero(
            appearance = Appearance(
                eyeColor = "",
                gender = "",
                hairColor = "",
                height = listOf(),
                race = "",
                weight = listOf(),
            ),
            biography = Biography(
                aliases = listOf(),
                alignment = "",
                alterEgos = "",
                firstAppearance = "",
                fullName = "Bruce Banner",
                placeOfBirth = "",
                publisher = "",
            ),
            connections = Connections(
                groupAffiliation = "",
                relatives = "",
            ),
            id = 332,
            images = Images(
                lg = "https://cdn.jsdelivr.net/gh/akabab/superhero-api@0.3.0/api/images/lg/332-hulk.jpg",
                md = "",
                sm = "",
                xs = "",
            ),
            name = "Hulk",
            powerstats = Powerstats(
                combat = 0,
                durability = 0,
                intelligence = 0,
                power = 0,
                speed = 0,
                strength = 0
            ),
            slug = "",
            work = Work(
                base = "",
                occupation = "",
            )
        ),
        onHeroClick = {}
    )
}