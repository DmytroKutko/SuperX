package com.heroes.superx.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.heroes.superx.models.Hero
import com.heroes.superx.repository.HeroesListRepo
import com.heroes.superx.util.CacheInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repo: HeroesListRepo,
    private val cache: CacheInterface
) : ViewModel() {

    val heroesFlow: MutableStateFlow<List<Hero>> = MutableStateFlow(listOf())

    private val currentHero: MutableStateFlow<Hero?> = MutableStateFlow(null)
    val mCurrentHero: StateFlow<Hero?> = currentHero

    private val isInTeam: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val mIsInTeam: StateFlow<Boolean> = isInTeam


    init {
        getHeroesList()
    }

    private fun getHeroesList() {
        viewModelScope.launch(Dispatchers.IO) {
            heroesFlow.value = repo.getHeroesList()
        }
    }

    fun setHero(hero: Hero) {
        viewModelScope.launch {
            currentHero.emit(hero)
            isInTeam(hero.id)
        }
    }

    fun addTeamMember(hero: Hero) {
        viewModelScope.launch {
            cache.addTeamMember(hero)
        }
    }

    fun removeTeamMember(id: Int) {
        viewModelScope.launch {
//            preferenceUtil.removeTeamMember(id.toString())
        }
    }

    private fun isInTeam(id: Int) {
        viewModelScope.launch {
            isInTeam.value = cache.isInTeam(id)
        }
    }
}