package com.heroes.superx.repository

import com.heroes.superx.api.HeroesApi
import com.heroes.superx.models.Hero
import com.heroes.superx.util.parseJsonToData
import org.jsoup.Jsoup
import javax.inject.Inject


interface HeroesListRepo {
    suspend fun getHeroesList(): List<Hero>
}

class HeroesListRepository @Inject constructor(): HeroesListRepo {

    override suspend fun getHeroesList(): List<Hero> {
        val result = Jsoup.connect(HeroesApi.htmlJson).ignoreContentType(true).execute().body()
        return parseJsonToData(result)
    }

}