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
import javax.inject.Inject

@HiltViewModel
class MyTeamViewModel @Inject constructor(
    private val repo: HeroesListRepo,
    private val preferences: SharedPreferenceUtil,
) : ViewModel() {

    private val myTeam: MutableStateFlow<Set<Hero>> = MutableStateFlow(setOf())
    val mMyTeam: StateFlow<Set<Hero>> = myTeam

    fun getCurrentTeamMembers() {
        viewModelScope.launch(Dispatchers.IO) {
            val members = preferences.getTeamMembers()
            myTeam.emit(repo.getHeroesList().filter { hero ->
                members.find { it == hero.id.toString() } != null
            }.toSet())
        }
    }
}