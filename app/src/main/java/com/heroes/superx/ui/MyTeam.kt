package com.heroes.superx.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.heroes.superx.R
import com.heroes.superx.models.Hero
import com.heroes.superx.viewModels.MyTeamViewModel
import kotlinx.coroutines.launch

private const val TEAM_GRID_COUNT = 2

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun MyTeam() {
    val scope = rememberCoroutineScope()
    val viewModel: MyTeamViewModel = hiltViewModel()

    val myTeam = remember { mutableStateOf<Set<Hero>?>(null) }

    scope.launch {
        with(viewModel) {
            getCurrentTeamMembers()
            mMyTeam.collect {
                myTeam.value = it
            }
        }
    }

    myTeam.value?.let { team ->
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            content = {
                item {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "YOUR TEAM",
                        textAlign = TextAlign.Center
                    )
                }
                item {
                    YourSquad(team)
                }
                item {
                    TeamStats(team)
                }
            }
        )
    }
}

@Composable
fun TeamStats(team: Set<Hero>) {
    var strength = 0
    team.forEach { strength += it.powerstats.strength }
    Text(text = "Strength $strength")
}

@Composable
fun YourSquad(team: Set<Hero>) {
    LazyVerticalGrid(
        modifier = Modifier.padding(horizontal = 8.dp),
        columns = GridCells.Fixed(TEAM_GRID_COUNT),
        horizontalArrangement = Arrangement.Center
    )
    {
        team.forEach {
            item {
                HeroItem(hero = it)
            }
        }

        for (i in 0..<(4 - team.size)) {
            item {
                BlankHero()
            }
        }
    }
}

@Composable
fun BlankHero() {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    Box(
        modifier = Modifier
            .height(120.dp)
            .width(screenWidth / 4),
        contentAlignment = Alignment.BottomCenter
    ) {
        Column(
            modifier = Modifier
                .background(Color.Transparent),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier
                    .height(100.dp),
                shape = RoundedCornerShape(6.dp)
            ) {
                Image(
                    painterResource(R.drawable.default_hero_image),
                    contentDescription = "",
                    contentScale = ContentScale.Fit,
                )
            }

            Text(
                text = "Add Member",
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.W600,
                maxLines = 1,
                fontSize = 12.sp
            )
        }
    }
}

@Composable
fun HeroItem(hero: Hero) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    Box(
        modifier = Modifier
            .height(120.dp)
            .width(screenWidth / 4),
        contentAlignment = Alignment.BottomCenter
    ) {
        Column(
            modifier = Modifier
                .background(Color.Transparent),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier
                    .height(100.dp),
                shape = RoundedCornerShape(6.dp)
            ) {
                HeroImage(
                    imageUrl = hero.images.lg,
                )
            }

            Text(
                text = hero.name,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.W600,
                maxLines = 1,
                fontSize = 12.sp,
            )
        }
    }
}