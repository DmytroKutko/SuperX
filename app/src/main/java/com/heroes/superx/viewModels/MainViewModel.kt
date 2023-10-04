package com.heroes.superx.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.heroes.superx.models.Hero
import com.heroes.superx.repository.HeroesListRepo
import com.heroes.superx.util.SharedPreferenceUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repo: HeroesListRepo,
    private val preferenceUtil: SharedPreferenceUtil,
) : ViewModel() {

    val heroesFlow: MutableStateFlow<List<Hero>> = MutableStateFlow(listOf())

    private val currentHero: MutableStateFlow<Hero?> = MutableStateFlow(null)
    val mCurrentHero: StateFlow<Hero?> = currentHero


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
        }
    }

    fun addTeamMember(id: Int) {
        viewModelScope.launch {
            preferenceUtil.addTeamMember(id.toString())
        }
    }

    fun removeTeamMember(id: Int) {
        viewModelScope.launch {
            preferenceUtil.removeTeamMember(id.toString())
        }
    }

    fun isInTeam(id: Int): Boolean = preferenceUtil.isInTeam(id.toString())
}