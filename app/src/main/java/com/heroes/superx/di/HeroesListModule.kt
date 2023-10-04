package com.heroes.superx.di

import com.heroes.superx.repository.HeroesListRepo
import com.heroes.superx.repository.HeroesListRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HeroesListModule {


    @Singleton
    @Provides
    fun provideRepository(): HeroesListRepo = HeroesListRepository()
}