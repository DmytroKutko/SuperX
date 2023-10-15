package com.heroes.superx.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.heroes.superx.models.Hero
import com.heroes.superx.util.CacheInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyTeamViewModel @Inject constructor(
    private val cache: CacheInterface
) : ViewModel() {

    private val myTeam: MutableStateFlow<Set<Hero>> = MutableStateFlow(setOf())
    val mMyTeam: StateFlow<Set<Hero>> = myTeam

    fun getCurrentTeamMembers() {
        viewModelScope.launch(Dispatchers.IO) {
            val members = cache.getAllTeamMembers()
            myTeam.value = members?.toSet() ?: setOf()
        }
    }
}