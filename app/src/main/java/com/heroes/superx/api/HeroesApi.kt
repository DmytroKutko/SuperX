package com.heroes.superx.api

import dagger.Component
import javax.inject.Singleton

@Singleton
@Component
interface HeroesApi {
    companion object {
        const val htmlJson = "https://cdn.jsdelivr.net/gh/akabab/superhero-api@0.3.0/api/all.json"
    }
}