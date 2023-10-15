package com.heroes.superx.repository

import com.heroes.superx.api.HeroesApi
import com.heroes.superx.models.Hero
import com.heroes.superx.util.CacheInterface
import com.heroes.superx.util.parseJsonToData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import javax.inject.Inject

interface HeroesListRepo {
    suspend fun getHeroesList(): List<Hero>
}

class HeroesListRepository @Inject constructor(
    private val cache: CacheInterface,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : HeroesListRepo {

    override suspend fun getHeroesList(): List<Hero> = withContext(ioDispatcher) {
        return@withContext if (cache.isEmpty().not())
            cache.getAllHeroes()!!
        else {
            val result = Jsoup.connect(HeroesApi.htmlJson).ignoreContentType(true).execute().body()
            val response = parseJsonToData(result)
            cache.addAllHeroes(response)
            response
        }
    }

}