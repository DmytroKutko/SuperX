package com.heroes.superx.di

import android.content.Context
import com.heroes.superx.repository.HeroesListRepo
import com.heroes.superx.repository.HeroesListRepository
import com.heroes.superx.util.CacheInterface
import com.heroes.superx.util.createPersistentCache
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HeroesListModule {

    @Singleton
    @Provides
    fun provideCache(@ApplicationContext context: Context): CacheInterface = createPersistentCache(context)

    @Singleton
    @Provides
    fun provideRepository(cacheInterface: CacheInterface): HeroesListRepo = HeroesListRepository(cacheInterface)
}