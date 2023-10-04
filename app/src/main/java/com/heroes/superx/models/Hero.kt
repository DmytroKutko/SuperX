package com.heroes.superx.models

data class Hero(
    val appearance: Appearance,
    val biography: Biography,
    val connections: Connections,
    val id: Int,
    val images: Images,
    val name: String,
    val powerstats: Powerstats,
    val slug: String,
    val work: Work
)