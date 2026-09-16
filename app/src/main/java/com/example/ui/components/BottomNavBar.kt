package com.example.ui.components

import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.data.model.UserRole
import com.example.localization.AppLanguage
import com.example.localization.StringsDictionary
import com.example.ui.theme.GreenPrimary
import com.example.ui.theme.SlateNavy
import com.example.ui.viewmodel.Screen

data class NavItem(
    val screen: Screen,
    val titleKey: String,
    val activeIcon: ImageVector,
    val inactiveIcon: ImageVector
)

@Composable
fun WasteGuardBottomNavBar(
    currentRole: UserRole,
    currentScreen: Screen,
    currentLanguage: AppLanguage,
    onNavigate: (Screen) -> Unit
) {
    val items = if (currentRole == UserRole.CITIZEN) {
        listOf(
            NavItem(Screen.CITIZEN_HOME, "home", Icons.Filled.Home, Icons.Outlined.Home),
            NavItem(Screen.CITIZEN_REPORT, "report", Icons.Filled.PhotoCamera, Icons.Outlined.PhotoCamera),
            NavItem(Screen.CITIZEN_MAP, "map", Icons.Filled.Map, Icons.Outlined.Map),
            NavItem(Screen.CITIZEN_MY_REPORTS, "my_reports", Icons.Filled.Assignment, Icons.Outlined.Assignment),
            NavItem(Screen.CITIZEN_PROFILE, "profile", Icons.Filled.Person, Icons.Outlined.Person)
        )
    } else {
        listOf(
            NavItem(Screen.AUTHORITY_DASHBOARD, "dashboard", Icons.Filled.Dashboard, Icons.Outlined.Dashboard),
            NavItem(Screen.AUTHORITY_INCIDENTS, "incidents", Icons.Filled.Warning, Icons.Outlined.Warning),
            NavItem(Screen.AUTHORITY_HEATMAP, "heatmap", Icons.Filled.Map, Icons.Outlined.Map),
            NavItem(Screen.AUTHORITY_INSIGHTS, "insights", Icons.Filled.Analytics, Icons.Outlined.Analytics),
            NavItem(Screen.CITIZEN_PROFILE, "profile", Icons.Filled.Person, Icons.Outlined.Person)
        )
    }

    NavigationBar(
        modifier = Modifier.navigationBarsPadding(),
        containerColor = Color.White,
        tonalElevation = 8.dp
    ) {
        items.forEach { item ->
            val isSelected = currentScreen == item.screen
            NavigationBarItem(
                selected = isSelected,
                onClick = { onNavigate(item.screen) },
                icon = {
                    Icon(
                        imageVector = if (isSelected) item.activeIcon else item.inactiveIcon,
                        contentDescription = StringsDictionary.get(item.titleKey, currentLanguage)
                    )
                },
                label = {
                    Text(
                        text = StringsDictionary.get(item.titleKey, currentLanguage),
                        style = MaterialTheme.typography.labelSmall
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = GreenPrimary,
                    selectedTextColor = GreenPrimary,
                    indicatorColor = GreenPrimary.copy(alpha = 0.15f),
                    unselectedIconColor = SlateNavy.copy(alpha = 0.6f),
                    unselectedTextColor = SlateNavy.copy(alpha = 0.6f)
                )
            )
        }
    }
}
