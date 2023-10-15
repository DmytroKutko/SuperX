package com.heroes.superx.util

import android.content.Context
import com.heroes.superx.models.Hero
import com.jakewharton.byteunits.DecimalByteUnit
import com.jakewharton.disklrucache.DiskLruCache
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException
import java.lang.NullPointerException

data class AllHeroesCache(
    val nodes: List<Hero>,
)

data class MyTeamCache(
    val team: List<Hero>,
)

interface CacheInterface {
    suspend fun addAllHeroes(list: List<Hero>)
    suspend fun getAllHeroes(): List<Hero>?
    suspend fun addTeamMember(hero: Hero)
    suspend fun removeTeamMember(hero: Hero)
    suspend fun isInTeam(id: Int): Boolean
    suspend fun getAllTeamMembers(): List<Hero>?
    suspend fun isEmpty(): Boolean
}

const val CACHE_VERSION = 1
val MAX_DISK_CACHE_SIZE = DecimalByteUnit.MEGABYTES.toBytes(2)
const val ALL_HEROES_CACHE = "all_heroes_cache"
const val MY_TEAM_CACHE = "my_team_cache"

fun createPersistentCache(context: Context): CacheInterface {
    val cacheDir = File(context.cacheDir, "SuperXCache")
    if (!cacheDir.exists()) {
        cacheDir.mkdir()
    }

    try {
        val diskLruCache = DiskLruCache.open(cacheDir, CACHE_VERSION, 1, MAX_DISK_CACHE_SIZE)
        return PersistentCache(diskLruCache)
    } catch (e: IOException) {
        e.printStackTrace()
    }

    // fallback to memory cache
    return MemoryCache(mutableMapOf(), mutableMapOf())
}

class PersistentCache(
    private val cache: DiskLruCache,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : CacheInterface {

    private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    override suspend fun addAllHeroes(list: List<Hero>) = withContext(ioDispatcher) {
        val data = moshi.adapter(AllHeroesCache::class.java).toJson(AllHeroesCache(list))
        val editor = cache.edit(ALL_HEROES_CACHE)
        editor.set(0, data)
        editor.commit()
    }

    override suspend fun getAllHeroes(): List<Hero>? = withContext(ioDispatcher) {
        cache.get(ALL_HEROES_CACHE)?.let { text ->
            val data = text.getString(0)
            val adapter = moshi.adapter(AllHeroesCache::class.java)
            return@withContext adapter.fromJson(data)?.nodes
        } ?: run { null }
    }

    override suspend fun addTeamMember(hero: Hero) = withContext(ioDispatcher) {
        val data = getAllTeamMembers()?.toMutableList() ?: mutableListOf()
        data.add(hero)
        val myTeamJson = moshi.adapter(MyTeamCache::class.java).toJson(MyTeamCache(data))
        cache.edit(MY_TEAM_CACHE).set(0, myTeamJson)
    }

    override suspend fun removeTeamMember(hero: Hero) = withContext(ioDispatcher) {

    }

    override suspend fun isInTeam(id: Int): Boolean = withContext(ioDispatcher) {
        return@withContext false //TODO
    }

    override suspend fun getAllTeamMembers(): List<Hero>? = withContext(ioDispatcher) {
        try {
            val data = cache.get(MY_TEAM_CACHE).getString(0)
            return@withContext moshi.adapter(MyTeamCache::class.java).fromJson(data)?.team
        } catch (e: NullPointerException) {
            return@withContext listOf()
        }
    }

    override suspend fun isEmpty(): Boolean = withContext(ioDispatcher) {
        return@withContext try {
            getAllHeroes()!!.isEmpty()
        } catch (e: NullPointerException) {
            true
        }
    }
}

class MemoryCache(
    private val allHeroesMap: MutableMap<String, AllHeroesCache>,
    private val myTeamMap: MutableMap<String, MyTeamCache>,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : CacheInterface {

    override suspend fun addAllHeroes(list: List<Hero>) = withContext(ioDispatcher) {
        allHeroesMap[ALL_HEROES_CACHE] = AllHeroesCache(list)
    }

    override suspend fun getAllHeroes(): List<Hero>? = withContext(ioDispatcher) {
        return@withContext allHeroesMap[ALL_HEROES_CACHE]?.nodes
    }

    override suspend fun addTeamMember(hero: Hero) = withContext(ioDispatcher) {
        val team = getAllTeamMembers()?.toMutableList() ?: mutableListOf()
        team.add(hero)
        myTeamMap[MY_TEAM_CACHE] = MyTeamCache(team)
    }

    override suspend fun removeTeamMember(hero: Hero) = withContext(ioDispatcher) {

    }

    override suspend fun isInTeam(id: Int): Boolean = withContext(ioDispatcher) {
        return@withContext false //TODO
    }

    override suspend fun getAllTeamMembers(): List<Hero>? = withContext(ioDispatcher) {
        return@withContext myTeamMap[MY_TEAM_CACHE]?.team
    }

    override suspend fun isEmpty(): Boolean = withContext(ioDispatcher) {
        return@withContext try {
            getAllHeroes()?.isEmpty() ?: true
        } catch (e: NullPointerException) {
            true
        }
    }
}