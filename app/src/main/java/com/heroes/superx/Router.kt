package com.heroes.superx

import androidx.compose.animation.core.FloatSpringSpec
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.heroes.superx.view.HeroesList
import com.heroes.superx.view.MyTeam
import kotlinx.coroutines.launch


sealed class BottomNavItem(var title: String, var icon: Int, var screen_route: String) {
    data object HeroesList : BottomNavItem("Heroes List", R.drawable.baseline_list_24, "list_heroes")
    data object MyTeam : BottomNavItem("My Team", R.drawable.baseline_groups_3_24, "my_team")
}
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun NavigationGraph(navController: NavHostController, bottomBarState: MutableState<Boolean>) {
    val scope = rememberCoroutineScope()
    val modalSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        animationSpec = FloatSpringSpec(),
        skipHalfExpanded = false
    )

    LaunchedEffect(Unit) {
        snapshotFlow { modalSheetState.currentValue }
            .collect {
                bottomBarState.value = it == ModalBottomSheetValue.Hidden
            }
    }

    NavHost(navController = navController, startDestination = "list_heroes") {
        composable("list_heroes") {
            HeroesList(onHeroClick = {
                scope.launch {
                    modalSheetState.show()
                    bottomBarState.value = false
                }
            })
            BottomSheetDialog(modalSheetState = modalSheetState)
        }

        composable("my_team") { MyTeam() }
    }
}