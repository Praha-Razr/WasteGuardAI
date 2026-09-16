package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.data.model.UserRole
import com.example.ui.components.WasteGuardBottomNavBar
import com.example.ui.components.WasteGuardTopBar
import com.example.ui.screens.*
import com.example.ui.theme.WasteGuardTheme
import com.example.ui.viewmodel.MainViewModel
import com.example.ui.viewmodel.Screen

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            WasteGuardTheme {
                WasteGuardApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun WasteGuardApp(viewModel: MainViewModel) {
    val currentUser by viewModel.currentUser.collectAsState()
    val currentLanguage by viewModel.currentLanguage.collectAsState()
    val currentScreen by viewModel.currentScreen.collectAsState()
    val selectedIncidentId by viewModel.selectedIncidentId.collectAsState()
    val capturedSampleTag by viewModel.capturedSampleTag.collectAsState()
    val capturedPhotoBitmap by viewModel.capturedPhotoBitmap.collectAsState()
    val aiAnalysisResult by viewModel.aiAnalysisResult.collectAsState()
    val isAnalyzing by viewModel.isAnalyzing.collectAsState()

    val incidents by viewModel.incidents.collectAsState()
    val hotspots by viewModel.hotspots.collectAsState()
    val teams by viewModel.teams.collectAsState()

    val showBars = currentScreen != Screen.SPLASH &&
            currentScreen != Screen.REGISTRATION &&
            currentScreen != Screen.LOGIN

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            if (showBars) {
                WasteGuardTopBar(
                    currentLanguage = currentLanguage,
                    onLanguageToggle = { viewModel.toggleLanguage() }
                )
            }
        },
        bottomBar = {
            if (showBars) {
                WasteGuardBottomNavBar(
                    currentRole = currentUser.role,
                    currentScreen = currentScreen,
                    currentLanguage = currentLanguage,
                    onNavigate = { viewModel.navigateTo(it) }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Crossfade(targetState = currentScreen, label = "ScreenTransition") { screen ->
                when (screen) {
                    Screen.SPLASH -> SplashScreen(
                        currentLanguage = currentLanguage,
                        onLanguageToggle = { viewModel.toggleLanguage() },
                        onNavigate = { viewModel.navigateTo(it) },
                        onDemoCitizenLogin = { viewModel.loginAsCitizen() },
                        onDemoAuthorityLogin = { viewModel.loginAsAuthority() }
                    )

                    Screen.REGISTRATION -> RegistrationScreen(
                        currentLanguage = currentLanguage,
                        onNavigate = { viewModel.navigateTo(it) },
                        onRegister = { name, email, phone, dist, ward ->
                            viewModel.registerCitizen(name, email, phone, dist, ward)
                        }
                    )

                    Screen.LOGIN -> LoginScreen(
                        currentLanguage = currentLanguage,
                        onNavigate = { viewModel.navigateTo(it) },
                        onDemoCitizenLogin = { viewModel.loginAsCitizen() },
                        onDemoAuthorityLogin = { viewModel.loginAsAuthority() }
                    )

                    Screen.CITIZEN_HOME -> CitizenHomeScreen(
                        user = currentUser,
                        incidents = incidents,
                        hotspots = hotspots,
                        currentLanguage = currentLanguage,
                        onNavigate = { viewModel.navigateTo(it) },
                        onSelectIncident = {
                            viewModel.selectIncident(it)
                        }
                    )

                    Screen.CITIZEN_REPORT -> ReportWasteScreen(
                        currentLanguage = currentLanguage,
                        selectedSampleTag = capturedSampleTag,
                        capturedPhotoBitmap = capturedPhotoBitmap,
                        onSelectSamplePhoto = { tag, bitmap ->
                            viewModel.selectSamplePhoto(tag, bitmap)
                        },
                        onNavigate = { viewModel.navigateTo(it) },
                        onRunAiAnalysis = { lat, lng, desc ->
                            viewModel.runAiAnalysis(lat, lng, desc)
                        }
                    )

                    Screen.CITIZEN_AI_ANALYSIS -> AiAnalysisScreen(
                        isAnalyzing = isAnalyzing,
                        analysisResult = aiAnalysisResult,
                        currentLanguage = currentLanguage,
                        onNavigate = { viewModel.navigateTo(it) },
                        onSubmitReport = { desc ->
                            viewModel.submitNewIncident(desc)
                        }
                    )

                    Screen.CITIZEN_MY_REPORTS -> CitizenMyReportsScreen(
                        incidents = incidents,
                        selectedIncidentId = selectedIncidentId,
                        currentLanguage = currentLanguage,
                        onSelectIncident = { viewModel.selectIncident(it) }
                    )

                    Screen.CITIZEN_MAP -> CitizenMapScreen(
                        incidents = incidents,
                        hotspots = hotspots,
                        currentLanguage = currentLanguage,
                        onSelectIncident = { viewModel.selectIncident(it) }
                    )

                    Screen.CITIZEN_PROFILE -> CitizenProfileScreen(
                        user = currentUser,
                        currentLanguage = currentLanguage,
                        onLanguageToggle = { viewModel.toggleLanguage() },
                        onLogout = { viewModel.logout() }
                    )

                    Screen.AUTHORITY_DASHBOARD -> AuthorityDashboardScreen(
                        incidents = incidents,
                        currentLanguage = currentLanguage,
                        onNavigate = { viewModel.navigateTo(it) },
                        onSelectIncident = { viewModel.selectIncident(it) }
                    )

                    Screen.AUTHORITY_HEATMAP -> AuthorityHeatmapScreen(
                        incidents = incidents,
                        hotspots = hotspots,
                        currentLanguage = currentLanguage,
                        onSelectIncident = { viewModel.selectIncident(it) }
                    )

                    Screen.AUTHORITY_HOTSPOTS -> AuthorityHotspotsScreen(
                        hotspots = hotspots,
                        currentLanguage = currentLanguage,
                        onNavigate = { viewModel.navigateTo(it) }
                    )

                    Screen.AUTHORITY_INCIDENTS -> AuthorityIncidentListScreen(
                        incidents = incidents,
                        currentLanguage = currentLanguage,
                        onSelectIncident = { viewModel.selectIncident(it) }
                    )

                    Screen.AUTHORITY_INCIDENT_DETAIL -> AuthorityIncidentDetailScreen(
                        incident = incidents.firstOrNull { it.id == selectedIncidentId } ?: incidents.firstOrNull(),
                        teams = teams,
                        currentLanguage = currentLanguage,
                        onNavigate = { viewModel.navigateTo(it) },
                        onAssignTeam = { incId, teamId, teamName ->
                            viewModel.assignTeam(incId, teamId, teamName)
                        },
                        onVerifyCleanup = { incId ->
                            viewModel.verifyCleanup(incId)
                        }
                    )

                    Screen.AUTHORITY_INSIGHTS -> AuthorityInsightsScreen(
                        currentLanguage = currentLanguage
                    )
                }
            }
        }
    }
}
