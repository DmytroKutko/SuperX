package com.heroes.superx.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.heroes.superx.viewModels.MainViewModel
import com.heroes.superx.models.Appearance
import com.heroes.superx.models.Hero
import com.heroes.superx.models.Powerstats
import com.heroes.superx.models.Work
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun HeroProfile() {
    val coroutineScope = rememberCoroutineScope()
    val viewModel: MainViewModel = hiltViewModel()
    val hero = remember { mutableStateOf<Hero?>(null) }
    val isInTeam = remember { mutableStateOf(false) }
    val buttonAddRemove = remember { mutableStateOf("Add to your team") }

    coroutineScope.launch {
        viewModel.mCurrentHero.collect {
            hero.value = it
        }
    }

    coroutineScope.launch {
        viewModel.mIsInTeam.collect { isTeamMember ->
            isInTeam.value = isTeamMember
            if (isTeamMember)
                buttonAddRemove.value = "Remove"
            else
                buttonAddRemove.value = "Add to your team"
        }
    }

    hero.value?.let {

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                Header(hero = it)
            }
            item {
                Column(
                    modifier = Modifier.padding(start = 8.dp, top = 8.dp, end = 8.dp),
                ) {
                    Divider(modifier = Modifier.padding(top = 8.dp))

                    PowerStats(powerstats = it.powerstats)

                    Appearance(appearance = it.appearance)

                    Work(work = it.work)


                    Actions(buttonAddRemove.value) {
                        if (isInTeam.value) {
//                            buttonAddRemove.value = "Remove"
                            viewModel.removeTeamMember(it)
                        } else {
//                            buttonAddRemove.value = "Add to your team"
                            viewModel.addTeamMember(it)
                        }
                    }

                    Spacer(modifier = Modifier.height(100.dp))
                }
            }
        }
    }
}

@Composable
fun Header(hero: Hero) {
    Row {
        Card(
            modifier = Modifier
                .padding(start = 8.dp, end = 8.dp)
                .width(150.dp),
            shape = RoundedCornerShape(6.dp),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 6.dp
            )
        ) {
            HeroImage(
                imageUrl = hero.images.lg
            )
        }
        Column(
            modifier = Modifier.padding(
                start = 8.dp, top = 16.dp, end = 8.dp
            )
        ) {
            Text(text = "Name:")
            Text(
                modifier = Modifier.padding(bottom = 4.dp, start = 8.dp),
                text = hero.name,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
            )

            if (hero.biography.fullName.isNotEmpty()) {
                Text(
                    modifier = Modifier.padding(bottom = 4.dp), text = "Full name:"
                )
                Text(
                    modifier = Modifier.padding(bottom = 4.dp, start = 8.dp),
                    text = hero.biography.fullName,
                )
            }

            if (hero.biography.placeOfBirth.isNotEmpty() && hero.biography.placeOfBirth != "-") {
                Text(text = "Place of birth:")
                Text(
                    modifier = Modifier.padding(bottom = 4.dp, start = 8.dp),
                    text = hero.biography.placeOfBirth,
                )
            }
        }
    }
}

@Composable
fun PowerStats(powerstats: Powerstats) {
    Text(
        modifier = Modifier
            .padding(top = 4.dp),
        text = "Powerstats:"
    )
    Text(
        modifier = Modifier.padding(start = 16.dp),
        text = "Intelligence: ${powerstats.intelligence}"
    )
    Text(
        modifier = Modifier.padding(start = 16.dp),
        text = "Strength: ${powerstats.strength}"
    )
    Text(
        modifier = Modifier.padding(start = 16.dp),
        text = "Speed: ${powerstats.speed}"
    )
    Text(
        modifier = Modifier.padding(start = 16.dp),
        text = "Durability: ${powerstats.durability}"
    )
    Text(
        modifier = Modifier.padding(start = 16.dp),
        text = "Power: ${powerstats.power}"
    )
    Text(
        modifier = Modifier.padding(start = 16.dp),
        text = "Combat: ${powerstats.combat}"
    )
}

@Composable
fun Appearance(appearance: Appearance) {
    Text(
        modifier = Modifier.padding(top = 8.dp),
        text = "Appearance:"
    )

    Text(
        modifier = Modifier.padding(start = 16.dp),
        text = "Gender: ${appearance.gender}"
    )

    if (appearance.race.toString() != "null") {
        Text(
            modifier = Modifier.padding(start = 16.dp),
            text = "Race: ${appearance.race}"
        )
    }

    if (appearance.eyeColor != "-") {
        Text(
            modifier = Modifier.padding(start = 16.dp),
            text = "Eye color: ${appearance.eyeColor}"
        )
    }
}

@Composable
fun Work(work: Work) {
    if (work.occupation != "-" || work.base != "-") {
        Text(
            modifier = Modifier.padding(top = 8.dp),
            text = "Work:"
        )
    }

    if (work.occupation != "-") {
        Column {
            Text(
                modifier = Modifier.padding(start = 16.dp),
                text = "Occupation:"
            )
            work.occupation.split(";", ",").forEach {
                if (it.isNotEmpty())
                    Text(
                        modifier = Modifier.padding(start = 24.dp),
                        text = "- ${it.replace(",", "").trim()}"
                    )
            }
        }
    }

    if (work.base != "-") {
        Column {
            Text(
                modifier = Modifier.padding(start = 16.dp),
                text = "Base:"
            )
            work.base.split(";").forEach {
                if (it.isNotEmpty())
                    Text(
                        modifier = Modifier.padding(start = 24.dp),
                        text = "- ${it.replace(";", "").trim()}"
                    )
            }
        }
    }
}

@Composable
fun Actions(text: String, onClick: () -> Unit) {
    Button(onClick = {
        onClick()
    }) {
        Text(text = text)
    }
}