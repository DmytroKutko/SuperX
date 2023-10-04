package com.heroes.superx.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.heroes.superx.BottomNavItem
import com.heroes.superx.ui.theme.LightGray40


@Composable
fun BottomNavigationMenu(
    navController: NavController,
    bottomBarState: MutableState<Boolean>,
) {
    val items = listOf(
        BottomNavItem.HeroesList,
        BottomNavItem.MyTeam,
    )

    AnimatedVisibility(
        visible = bottomBarState.value,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it }),
        content = {
            Card(
                modifier = Modifier,
                shape = RoundedCornerShape(
                    topEnd = 6.dp,
                    topStart = 6.dp,
                    bottomEnd = 0.dp,
                    bottomStart = 0.dp
                ),
            ) {
                BottomNavigation(
                    backgroundColor = Color.LightGray,
                    contentColor = Color.Black
                ) {
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentRoute = navBackStackEntry?.destination?.route

                    items.forEach { item ->
                        val selected = currentRoute == item.screen_route
                        BottomNavigationItem(
                            icon = {
                                Card(
                                    colors = CardDefaults.cardColors(
                                        containerColor = if (selected) LightGray40 else Color.Transparent,
                                    ),
                                    shape = RoundedCornerShape(16.dp)
                                ) {
                                    Icon(
                                        painterResource(id = item.icon),
                                        contentDescription = item.title,
                                        modifier = Modifier
                                            .padding(horizontal = 8.dp),
                                        tint = Color.Black,
                                    )
                                }
                            },
                            label = {
                                Text(
                                    text = item.title,
                                    fontSize = 9.sp,
                                    color = Color.Black
                                )
                            },
                            selectedContentColor = Color.Black,
                            unselectedContentColor = Color.Black.copy(0.4f),
                            alwaysShowLabel = true,
                            selected = selected,
                            onClick = {
                                navController.navigate(item.screen_route) {
                                    navController.graph.startDestinationRoute?.let { screen_route ->
                                        popUpTo(screen_route) {
                                            saveState = true
                                        }
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                        )
                    }
                }
            }
        }
    )
}
