package com.heroes.superx.util

import android.content.Context
import android.preference.PreferenceManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

const val TEAM_MEMBERS = "TEAM_MEMBERS"

@Singleton
class SharedPreferenceUtil @Inject constructor(@ApplicationContext context: Context) {
    private val prefs = PreferenceManager.getDefaultSharedPreferences(context)

    fun getTeamMembers(): MutableSet<String> {
        return prefs.getStringSet(TEAM_MEMBERS, mutableSetOf<String>())!!
    }

    fun addTeamMember(id: String) {
        val members = getTeamMembers()
        if (members.size < 4) {
            members.add(id)
            prefs.edit().putStringSet(TEAM_MEMBERS, members).apply()
        }
    }

    fun removeTeamMember(id: String) {
        val members = getTeamMembers()
        members.remove(id)
        prefs.edit().putStringSet(TEAM_MEMBERS, members).apply()
    }

    fun isInTeam(id: String): Boolean {
        return getTeamMembers().isNotEmpty() && getTeamMembers().contains(id)
    }
}